package pe.gob.senamhi.quartzjob;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import pe.gob.senamhi.bean.DatosFHBean;
import pe.gob.senamhi.bean.EstacionesBean;
import pe.gob.senamhi.bean.FrecuenciaBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.EstacionesDao;
import pe.gob.senamhi.dao.FrecuenciaDao;
import pe.gob.senamhi.dao.FunctionDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.dao.impl.EstacionesDaoImpl;
import pe.gob.senamhi.dao.impl.FrecuenciaDaoImpl;
import pe.gob.senamhi.dao.impl.FunctionDaoImpl;
import pe.gob.senamhi.enums.QuartzJobEnum;
import pe.gob.senamhi.model.QuartzModel;
import pe.gob.senamhi.threads.ControlCalidadThread;
import pe.gob.senamhi.util.EnviaEmailThread;
import pe.gob.senamhi.util.PropiedadesUtil;
import pe.gob.senamhi.util.Util;

public class QCAutomaticJob implements Job {
	final String APP_NAME = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String COD_QUARTZ = PropiedadesUtil.obtenerPropiedad("configuracion", "codigo.proceso.atumatic.qc");
	final String CORREO_DESTINO = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");
	final String GRUPO_ESTA = PropiedadesUtil.obtenerPropiedad("configuracion", "codigo.grupo.estaciones");
	
	private static final Logger LOGGER = Logger.getLogger(QCAutomaticJob.class);
	private EstacionesDao est = new EstacionesDaoImpl();
	private FrecuenciaDao frec = new FrecuenciaDaoImpl();
	private DatosDao datosDao = new DatosDaoImpl();
	private FunctionDao functionDao = new FunctionDaoImpl();
	private FrecuenciaBean frecBean = null;
	private DatosFHBean datosFHBean = null;
	long millisI = System.currentTimeMillis();
	java.sql.Date dateI = new java.sql.Date(millisI);
	
//	private String fechaActual = FechaHoraUtil.obtFechaStringSinhora(dateI);
//	private String horActual = FechaHoraUtil.obtHoraStringSinfecha(dateI);
	
	private String fechaActual = "";
	private String horActual = "";
	
	private List<String> arrayHilos = new ArrayList<String>();
	private List<EstacionesBean> listaEsta = null;
	private String fecHora = "";
	private String hora = "";
	private Integer totDatos;
	private Integer pIni;
	private Integer pFin;
	private Integer intv;
	private String ip = "";
	
	private String FIni = "";
	private String FFin = "";
	private String Fec_Pasar = "";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("INICIO QUARTZ");
		ip = Util.capturarIP();
		
		fechaActual = functionDao.obtFechaStringBD();
		horActual = functionDao.obtHoraStringBD();
		
//		fechaActual = "17/07/2021";
//		horActual = "23:00:00";
		
		QuartzModel quartzModel = new QuartzModel();
		if (quartzModel.isJobDisponible(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo())) {
			quartzModel.bloquearJob(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo());
			
			LOGGER.info("JOB DISPONIBLE ");
						
			int CANTIDAD_PROCESOS = Integer.parseInt(PropiedadesUtil.obtenerPropiedad("configuracion", "cantidadHilosProcesar"));
			ControlCalidadThread[] hilosControlCalidad = new ControlCalidadThread[CANTIDAD_PROCESOS];
			
//			hilosControlCalidad[0].isAlive();
			
			try {
				
				hora = horActual.substring(0, 2) + ":00:00";
				fecHora = fechaActual + " " + hora;
				
				int valEstado = datosDao.validarEstado(ip, APP_NAME);

				if (valEstado != 0) {
					if (datosDao.errorExterno(ip, APP_NAME)) {
						LOGGER.info("Actualizacion de Registros a estado 0");
					}
				}
				
				datosFHBean = datosDao.obtFHMinMax(fecHora);
				
				if (datosFHBean != null) {
				
					FIni = datosFHBean.getFecMin();
					FFin = datosFHBean.getFecMax();
					
//					Inicio del while
					while (!FIni.equals(FFin)) {
						
						datosFHBean = datosDao.obtFHProcesar(FIni);
						FIni = datosFHBean.getFecIncrem();
						Fec_Pasar = datosFHBean.getFecHora();
						EstacionesBean obj = new EstacionesBean();
						obj.setFila(1);
						obj.setCodesta("212900");
						listaEsta = Arrays.asList(obj);
//						listaEsta = est.listarEstaciones(Fec_Pasar,GRUPO_ESTA);
	
						totDatos = listaEsta.size();
						
						if (totDatos != 0) {
							
							intv = (int) Math.floor(totDatos/CANTIDAD_PROCESOS);
							pIni=0;
	
							for (int i = 0; i < CANTIDAD_PROCESOS; i++) {
								
								pFin = pIni+intv;
								
								if (i == (CANTIDAD_PROCESOS-1) ) {
									pFin = totDatos;
								}
								
								LOGGER.info("Procesar de lista incial... HILO[" + (i + 1) + "] ");
								hilosControlCalidad[i] = new ControlCalidadThread(listaEsta,pIni,pFin,Fec_Pasar);
								hilosControlCalidad[i].setName("HILO: " + (i + 1));
								hilosControlCalidad[i].start();
								Thread.sleep(1000); //4700
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
								
								
								if (arrayHilos.size() == CANTIDAD_PROCESOS) {
									procesosTerminados++;
									LOGGER.info("Proceso culminados=" + arrayHilos.size());
								}
								
							}
							arrayHilos.clear();
	
							
						} else {
							LOGGER.info("NO HAY REGISTRO QUE PROCESAR ...");
							String error = "No existen registros disponibles" + " <br>" + APP_NAME + "<br>";
							new EnviaEmailThread(error,CORREO_DESTINO).start();
						}
						
					}
					
//					Fin del while
				} else {
					LOGGER.info("NO SE ENCONTRO NINGUNA ESTACION QUE PROCESAR, JOB LIBERADO");
					quartzModel.liberarJob(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo());
				}
				
			} catch (Exception e) {
				
				System.out.println("Error QCAutomaticJob.execute " + e.getMessage());
				e.printStackTrace();
				LOGGER.error("Error QCAutomaticJob.execute " + e.getMessage(), e);
				String error = "Error QCAutomaticJob.execute " + e.getMessage() + "<br>";
				for (StackTraceElement el : e.getStackTrace()) {
					error += el.toString() + "<br>" + APP_NAME + "<br>";
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
//				estado = "INACTIVO";
				LOGGER.info("El JOB se liberara la siguiente Hora");
//				quartzModel.liberarJob(QuartzJobEnum.QC_AUTOMATIC_JOB.codigo());
//				frecBean = frec.validarFrecuencia(fechaActual, horActual,COD_QUARTZ);  
//				String valdat = frecBean.getMensaje();
//				LOGGER.info("ESTADO DE QUARTZ: "+valdat);
//				if (valdat.equals("1")) {
//					LOGGER.info("JOB DISPONIBLE - ESPERANDO PARA INICIAR ...");
//				} else {
//					LOGGER.info("JOB OCUPADO");
//				}
			}
			
		} else {
//			LOGGER.info("JOB OCUPADO");
//			hilosControlCalidad[i].isAlive();
			frecBean = frec.validarFrecuencia(fechaActual, horActual,COD_QUARTZ);  
			String valdat = frecBean.getMensaje();
			LOGGER.info("ESTADO DE QUARTZ: "+valdat);
			if (valdat.equals("1")) {
				LOGGER.info("JOB DISPONIBLE - ESPERANDO PARA INICIAR ...");
			} else {
				LOGGER.info("JOB OCUPADO");
			}
//			if (estado.equals("ACTIVO")) {
//				LOGGER.info("ESTADO DE QUARTZ: 0");
//				LOGGER.info("JOB OCUPADO");
//			} else {
//				frecBean = frec.validarFrecuencia(fechaActual, horActual,COD_QUARTZ);  
//				String valdat = frecBean.getMensaje();
//				LOGGER.info("ESTADO DE QUARTZ: "+valdat);
//				if (valdat.equals("1")) {
//					LOGGER.info("JOB DISPONIBLE - ESPERANDO PARA INICIAR ...");
//				} else {
//					LOGGER.info("JOB OCUPADO");
//				}
//			}
			
		}
	}

}
