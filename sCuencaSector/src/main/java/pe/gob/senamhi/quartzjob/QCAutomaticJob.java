package pe.gob.senamhi.quartzjob;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import pe.gob.senamhi.bean.EstacionesBean;
import pe.gob.senamhi.dao.EstacionesDao;
import pe.gob.senamhi.dao.impl.EstacionesDaoImpl;
import pe.gob.senamhi.enums.QuartzJobEnum;
import pe.gob.senamhi.model.QuartzModel;
import pe.gob.senamhi.threads.ControlCalidadThread;
import pe.gob.senamhi.util.PropiedadesUtil;

public class QCAutomaticJob implements Job {
	final String appname = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String codQuartz = PropiedadesUtil.obtenerPropiedad("configuracion", "codigo.proceso.atumatic.qc");
	final String listaServ = PropiedadesUtil.obtenerPropiedad("configuracion", "name.lista.servicios");
	
	private static final Logger LOGGER = Logger.getLogger(QCAutomaticJob.class);
	
	private EstacionesDao est = new EstacionesDaoImpl();
	private List<String> arrayHilos = new ArrayList<String>();
	
	private Integer totDatos;
	private Integer pIni;
	private Integer pFin;
	private Integer intv;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("INICIO QUARTZ");
		QuartzModel quartzModel = new QuartzModel();
		if (quartzModel.isJobDisponible(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo())) {
			quartzModel.bloquearJob(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo());
			
			int CANTIDAD_PROCESOS = Integer.parseInt(PropiedadesUtil.obtenerPropiedad("configuracion", "cantidadHilosProcesar"));
			ControlCalidadThread[] hilosControlCalidad = new ControlCalidadThread[CANTIDAD_PROCESOS];
			
			try {
				
				List<EstacionesBean> listaEsta = est.listaEstaciones();
				
//				String[] arrOfStr = listaServ.split(","); 
//				for (String a : arrOfStr) {
//			           myArray.add(a);
//				}

				totDatos = listaEsta.size();
				
				intv = (int) Math.floor(totDatos/CANTIDAD_PROCESOS);
				pIni=0;
				
				LOGGER.info("JOB DISPONIBLE ");
				
				for (int i = 0; i < CANTIDAD_PROCESOS; i++) {
					
					pFin = pIni+intv;
					
					if (i == (CANTIDAD_PROCESOS-1)) {
						pFin = totDatos;
					}
					
					LOGGER.info("Procesar de lista incial... HILO[" + (i + 1) + "] ");
					hilosControlCalidad[i] = new ControlCalidadThread(listaEsta,pIni,pFin);
					hilosControlCalidad[i].setName("HILO: " + (i + 1));
					hilosControlCalidad[i].start();
					Thread.sleep(1000);
					pIni = pFin;
				}
				
				int procesosTerminados = -1;
				int j = 0;
				while (procesosTerminados != 0) {
					
					for (int i = 0; i < CANTIDAD_PROCESOS; i++) {
						if (hilosControlCalidad[i] != null) {
							if (!hilosControlCalidad[i].isAlive()) {
								
								String nombreHilo = hilosControlCalidad[i].getName();
								
								if (arrayHilos.size() == 0) {
									arrayHilos.add(nombreHilo);
									j = 1;
								}else {
									for (int k = 0; k < arrayHilos.size(); k++) { 
										
										if (nombreHilo.equals(arrayHilos.get(k))) {
											j = 1;
											break;
										} else {
											j = 0;
										}
										
									}
									if (j != 1) {
										arrayHilos.add(nombreHilo);
									}
									
								}
								
							}

						}
					}
					
//					LOGGER.info("NRO ARRAY: "+arrayHilos.size());
//					LOGGER.info("VALOR ARRAY: "+arrayHilos);
					
					if (arrayHilos.size() == CANTIDAD_PROCESOS) {
						procesosTerminados++;
						LOGGER.info("Proceso culminados=" + arrayHilos.size());
						LOGGER.info("Cuencas y Sectores Actualizados correctamente");
					}
					
				}
				arrayHilos.clear();
				
				
			} catch (Exception e) {
				
				System.out.println("Error QCAutomaticJob.execute " + e.getMessage());
				e.printStackTrace();
				LOGGER.error("Error QCAutomaticJob.execute " + e.getMessage(), e);
				String error = "Error QCAutomaticJob.execute " + e.getMessage() + "<br>";
				for (StackTraceElement el : e.getStackTrace()) {
					error += el.toString() + "<br>" + appname + "<br>";
				}
				// Enviamos los correos correspondientes a las personas interesadas
				//new EnviaEmailThread(error).start();
				System.out.println(error);
				
				// Interrumpimos los procesos actuales
				for (ControlCalidadThread hilo : hilosControlCalidad) {
					if (hilo != null) {
						if (hilo.isAlive()) {
							try {
								LOGGER.error("Interrumpir Hilo " + hilo.getName());
								hilo.interrupt();
								hilo.join();
							} catch (InterruptedException e2) {
								LOGGER.error("Error Interrumpiendo Hilo " + hilo.getName(), e2);
							}
						}
					}
				}
				
			} finally {
				quartzModel.liberarJob(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo());
				
			}
			
		} else {
			LOGGER.info("JOB OCUPADO");
			
		}

	}

}
