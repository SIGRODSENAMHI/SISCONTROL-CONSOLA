package pe.gob.senamhi.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.EstacionesBean;
import pe.gob.senamhi.bean.FlagBean;
import pe.gob.senamhi.bean.FunctionBean;
import pe.gob.senamhi.bean.ParamObtBean;
import pe.gob.senamhi.bean.UmbralBean;
import pe.gob.senamhi.bean.VariableBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.FunctionDao;
import pe.gob.senamhi.dao.QCDataDao;
import pe.gob.senamhi.dao.UmbralDao;
import pe.gob.senamhi.dao.VariableDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.dao.impl.FunctionDaoImpl;
import pe.gob.senamhi.dao.impl.QCDataDaoImpl;
import pe.gob.senamhi.dao.impl.UmbralDaoImpl;
import pe.gob.senamhi.dao.impl.VariableDaoImpl;
import pe.gob.senamhi.quartzjob.QCAutomaticJob;
import pe.gob.senamhi.util.Algoritmos;
import pe.gob.senamhi.util.EnviaEmailThread;
import pe.gob.senamhi.util.FechaHoraUtil;
import pe.gob.senamhi.util.PropiedadesUtil;
import pe.gob.senamhi.util.Util;

public class ControlCalidadThread extends Thread{
	final String appname = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String correoDestino = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");
	final String fBueno = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.bueno");
	final String fMalo = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.malo");
	final String fnd = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.sindato");
	private DatosDao dato = new DatosDaoImpl();
	private UmbralDao umbD = new UmbralDaoImpl();
	private VariableDao varbl = new VariableDaoImpl();
	private Algoritmos ecu = new Algoritmos();
	private QCDataDao regData = new QCDataDaoImpl();
	private FunctionDao nameFunt = new FunctionDaoImpl();
	private List<String> myArray = new ArrayList<String>();
	private List<FunctionBean> totalFunctionsL = null;
	private List<FunctionBean> totalFunctions = null;
	private List<VariableBean> datosvar = null;
	private FlagBean flagBean = null;
	private UmbralBean umbralBean = null;
	private ParamObtBean paramObtBean = null;
	private DatosBean datosBean = null;
	
	private static final Logger LOGGER = Logger.getLogger(QCAutomaticJob.class);
	
	private List<EstacionesBean> listaEsta;
	private int pIni;
	private int pFin;
	private String fecHora = "";
	
	private String idusu = "";
	private String flag;
	private String cod;
	private String codEsta="";
	private String flagTemp;
	
	long millisI = System.currentTimeMillis();
	java.sql.Date dateI = new java.sql.Date(millisI);
	String fechaActual = FechaHoraUtil.obtFechaStringSinhora(dateI);
	String horActual = FechaHoraUtil.obtHoraStringSinfecha(dateI);
	private List<String> arrayHilosVar = new ArrayList<String>();
	String ip = Util.capturarIP();
	private Integer tvar;
	private Integer tFunt;
	private Integer tFuntL;
	private String vfec = "";
	private String vhor = "";
	private String codflag = "";
	private String codFlagGen = "";
	private String nomvar = "";
	private String nameFunction = "";
	private int cant = 0;
	private int cont = 0;
//	private String mes = Util.obtenerMes();
	
	private int intVar = 0;
	private int pIniV = 0;
	private int pFinV = 0;
	
	private int CANTIDAD_PROCESOS_VAR = Integer.parseInt(PropiedadesUtil.obtenerPropiedad("configuracion", "cantidadHilosProcesarVariable"));
	private ProcesoVariableThread[] hilosProcesoVariable = new ProcesoVariableThread[CANTIDAD_PROCESOS_VAR];

//	public ControlCalidadThread(List<EstacionesBean> listaEsta, Integer pIni, Integer pFin, String fecHora) {
//		this.listaEsta = listaEsta;
//		this.pIni = pIni;
//		this.pFin = pFin;
//		this.fecHora = fecHora;
//	}
	
	@Override
	public void run() {
		try {
			
//			for (int i = pIni; i < pFin; i++) {
//				codEsta = listaEsta.get(i).getCodesta();
//				LOGGER.info(this.getName() + " PROCESANDO LA ESTACION:  " + codEsta + " Num: "+ i + " Fecha y Hora: " + fecHora);
//			}

					paramObtBean = dato.obtEstTrabo(ip, appname, this.getName());
					if (paramObtBean != null) {
						
						codEsta = paramObtBean.getVcodesta();
						vfec = paramObtBean.getFechareg(); //FECHA
						vhor = paramObtBean.getHorareg(); //HORA
						
						datosvar = varbl.listarVariables(codEsta);
						tvar = datosvar.size();
						
//						Inicio Nuevo proceso con hilos
						
						intVar = (int) Math.floor(tvar/CANTIDAD_PROCESOS_VAR);
						pIniV=0;
						
						for (int j = 0; j < CANTIDAD_PROCESOS_VAR; j++) {
							
							pFinV = pIniV + intVar;
							
							if (j == (CANTIDAD_PROCESOS_VAR-1) ) {
								pFinV = tvar;
							}
							
							hilosProcesoVariable[j] = new ProcesoVariableThread(datosvar, codEsta, vfec, vhor, pIniV, pFinV);
							hilosProcesoVariable[j].setName("HILO: " + (j + 1));
							hilosProcesoVariable[j].start();
							Thread.sleep(800);
							pIniV = pFinV;
							
						}
						
//						Fin Nuevo proceso con hilos

//						Inicio de validacion por hilos
						int procesosTerminados = -1;
						int t = 0;
						while (procesosTerminados != 0) {
							
							for (int k = 0; k < CANTIDAD_PROCESOS_VAR; k++) {
								if (hilosProcesoVariable[k] != null) {
									if (!hilosProcesoVariable[k].isAlive()) {
										
										String nombreHilo = hilosProcesoVariable[k].getName();
										
										if (arrayHilosVar.size() == 0) {
											arrayHilosVar.add(nombreHilo);
											t = 1;
										}else {
											for (int s = 0; s < arrayHilosVar.size(); s++) { 
												
												if (nombreHilo.equals(arrayHilosVar.get(s))) {
													t = 1;
													break;
												} else {
													t = 0;
												}
												
											}
											if (t != 1) {
												arrayHilosVar.add(nombreHilo);
											}
											
										}
										
									}

								}
							}
							
							
							if (arrayHilosVar.size() == CANTIDAD_PROCESOS_VAR) {
								procesosTerminados++;
//								LOGGER.info("Proceso culminados de variables=" + arrayHilosVar.size());
								LOGGER.info(arrayHilosVar.size());
								if (dato.regDatoTrabajada(codEsta, vfec, vhor)) {
									LOGGER.info("Se actualizo a estado 2 correctamente");
									limpiar();
								} else {
									LOGGER.error("No se pudo actualizar a estado 2");
									new EnviaEmailThread("Cantidades diferentes: " + arrayHilosVar.size() + " " + codEsta,correoDestino).start();
									limpiar();
								}
								
							} 
							
						}
//						Fin de validacion por hilos
						

					} else {
//						String error = "No existen registros disponibles" + " <br>" + appname + "<br>";
//						new EnviaEmailThread(error,correoDestino).start();
						LOGGER.info("YA SE PROCESO LA ESTACIÓN: " + codEsta);
					}
					
				
			
			
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			String error = e.getMessage()+"<br>"+codEsta+"<br>";
			new EnviaEmailThread(error,correoDestino).start();

		}
	}
	
	public void limpiar() {
		
		tFunt = 0;
		tFuntL = 0;
		cont = 0;
		
	}
	
}
