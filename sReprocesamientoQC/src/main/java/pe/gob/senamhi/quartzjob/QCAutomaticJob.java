package pe.gob.senamhi.quartzjob;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.enums.QuartzJobEnum;
import pe.gob.senamhi.model.QuartzModel;
import pe.gob.senamhi.threads.ControlCalidadThread;
import pe.gob.senamhi.util.PropiedadesUtil;
import pe.gob.senamhi.util.Util;

public class QCAutomaticJob implements Job {
	final String appname = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String codQuartz = PropiedadesUtil.obtenerPropiedad("configuracion", "codigo.proceso.atumatic.qc");
	final String correoDestino = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");
	private static final Logger LOGGER = Logger.getLogger(QCAutomaticJob.class);

	private DatosDao datdao = new DatosDaoImpl();
	private String ip = "";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("INICIO QUARTZ");
		ip = Util.capturarIP();
		QuartzModel quartzModel = new QuartzModel();
		if (quartzModel.isJobDisponible(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo())) {
			quartzModel.bloquearJob(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo());
			
			int CANTIDAD_PROCESOS = Integer.parseInt(PropiedadesUtil.obtenerPropiedad("configuracion", "cantidadHilosProcesar"));
			ControlCalidadThread[] hilosControlCalidad = new ControlCalidadThread[CANTIDAD_PROCESOS];
			
			try {
				
				int valEstado = datdao.validarEstado(ip, appname);

				if (valEstado != 0) {
					if (datdao.errorExterno(ip, appname)) {
						LOGGER.info("Actualizacion de Registros a estado 0");
					}
				}
				
					
//					INICIO
					int regDisponibles = datdao.regDisponibles();
					LOGGER.info(regDisponibles);
					if (regDisponibles == 0) {
						CANTIDAD_PROCESOS = 1;
					} else {
						if (regDisponibles < CANTIDAD_PROCESOS) {
							CANTIDAD_PROCESOS = regDisponibles;
						}
					}
					

					LOGGER.info("JOB DISPONIBLE ");
					
					if (regDisponibles != 0) {
//						contador = 1;
						for (int i = 0; i < CANTIDAD_PROCESOS; i++) {
							
							LOGGER.info("Procesar de lista incial... HILO[" + (i + 1) + "] ");
							hilosControlCalidad[i] = new ControlCalidadThread();
							hilosControlCalidad[i].setName("HILO: " + (i + 1));
							hilosControlCalidad[i].start();
							Thread.sleep(1000);
						}
						
						int procesosTerminados = -1;
						while (procesosTerminados != 0) {
							procesosTerminados = 0;

							int regDisponibles1 = datdao.regDisponibles();

							for (int i = 0; i < CANTIDAD_PROCESOS; i++) {
								if (hilosControlCalidad[i] != null) {
									if (!hilosControlCalidad[i].isAlive()) {
										LOGGER.info("Proceso de hilo culminado, limpiar hilo[" + i + "]="
												+ hilosControlCalidad[i].getName());
										hilosControlCalidad[i] = null;
										LOGGER.info("While Procesar en un hilo nuevo [" + i + "]... ");

										if (regDisponibles1 != 0) {

											hilosControlCalidad[i] = new ControlCalidadThread();
											hilosControlCalidad[i].setName("HILO: " + (i + 1));
											hilosControlCalidad[i].start();
											procesosTerminados++;
											Thread.sleep(1000);
										}

									}

								}
							}

							for (ControlCalidadThread hilo : hilosControlCalidad) {
								if (hilo != null) {
									if (hilo.isAlive()) {
										procesosTerminados++;
									}
								}
							}
							Thread.sleep(1000);
						}
						
					} else {
						LOGGER.info("NO HAY REGISTRO QUE PROCESAR ...");
						String error = "No existen registros disponibles" + " <br>" + appname + "<br>";
//						new EnviaEmailThread(error,correoDestino).start();
//						LOGGER.info("CORREO ENVIADO A " + correoDestino);
						
					}
//					FIN
				
				
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
