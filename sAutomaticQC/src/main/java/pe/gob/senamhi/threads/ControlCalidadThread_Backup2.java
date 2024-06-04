package pe.gob.senamhi.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.EstacionesBean;
import pe.gob.senamhi.bean.FunctionBean;
import pe.gob.senamhi.bean.ParamObtBean;
import pe.gob.senamhi.bean.UmbralBean;
import pe.gob.senamhi.bean.ValidarDatoBean;
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

public class ControlCalidadThread_Backup2 extends Thread{
	final String appname = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String correoDestino = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");
	final String fBueno = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.bueno");
	final String fMalo = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.malo");
	final String fnd = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.sindato");
	private DatosDao dato = new DatosDaoImpl();
	private VariableDao varbl = new VariableDaoImpl();
	private Algoritmos ecu = new Algoritmos();
	private QCDataDao regData = new QCDataDaoImpl();
	private FunctionDao nameFunt = new FunctionDaoImpl();
	private UmbralDao umbD = new UmbralDaoImpl();
	private List<String> myArray = new ArrayList<String>();
	private List<VariableBean> datosvar = null;
	private List<FunctionBean> totalFunctionsL = null;
	private List<FunctionBean> totalFunctions = null;
	private DatosBean datosBean = null;
	private ParamObtBean paramObtBean = null;
	private ValidarDatoBean validarDatoBean = null;
	private UmbralBean umbralBean = null;
	
	private static final Logger LOGGER = Logger.getLogger(QCAutomaticJob.class);
	
	private List<EstacionesBean> listaEsta;
	private int pIni;
	private int pFin;
	private String idusu = "";
	private String flag;
	private String flagTemp;
	private String cod;
	
	long millisI = System.currentTimeMillis();
	java.sql.Date dateI = new java.sql.Date(millisI);
	String fechaActual = FechaHoraUtil.obtFechaStringSinhora(dateI);
	String horActual = FechaHoraUtil.obtHoraStringSinfecha(dateI);
	String ip = Util.capturarIP();
	private Integer tvar = 0;
	private Integer tFunt = 0;
	private Integer tFuntL = 0;
	private String codEsta = "";
	private String fecHora = "";
	private String fecha = "";
	private String hora = "";
	private String vfec = "";
	private String vhor = "";
	private String nomvar = "";
	private String nameFunction = "";
	private int cant = 0;
	private String vLflag = "";
	private String mes = Util.obtenerMes();
	
	private int cont = 0;
	
	public ControlCalidadThread_Backup2(List<EstacionesBean> listaEsta, Integer pIni, Integer pFin, String fecHora) {
		this.listaEsta = listaEsta;
		this.pIni = pIni;
		this.pFin = pFin;
		this.fecHora = fecHora;
	}

	@Override
	public void run() {
		try {
			
			for (int i = pIni; i < pFin; i++) {
				codEsta = listaEsta.get(i).getCodesta();
				LOGGER.info(this.getName() + " PROCESANDO LA ESTACION:  " + codEsta + " Num: "+ i + " Fecha y Hora: " + fecHora);

				String datos[] = fecHora.split(" ");
				fecha = datos[0];
				hora = datos[1];
				
//				Registro de Estaciones que no cuentan con datos
				validarDatoBean = dato.obtEstadoEst(codEsta, fecHora);
				if (validarDatoBean.getValdato().equals("ND")) {
					String error="";
					LOGGER.info("No existen datos para la estacion " + codEsta + validarDatoBean.getValdato() + " / " + validarDatoBean.getCodesta());
					String cadena1 = "No existen datos para la estacion: " + codEsta + " <br>" + appname + "<br>";
				
					if (regData.completarDatos(appname, this.getName(), ip, codEsta,fecha,hora)) {
						error = cadena1+"Registrado correctamente!<br>";
						LOGGER.info("Registrado correctamente!");
					}else {
						error = cadena1+"Registro sin exito!<br>";
						LOGGER.error("Registro sin exito!: " + codEsta);
						new EnviaEmailThread(error,correoDestino).start();
					}
					
//				Registro de Estaciones que cuentan con datos
				} else {
					paramObtBean = dato.obtEstTrabo(codEsta, ip, appname, this.getName(),fecHora);
					if (paramObtBean != null) {
						
						vfec = paramObtBean.getFechareg(); //FECHA DEL DIA
						vhor = paramObtBean.getHorareg(); //UNA HORA ANTES DE LA ACTUAL
						
						datosvar = varbl.listarVariables(codEsta);
						tvar = datosvar.size();
						for (int j = 0; j < tvar; j++) {
							
							
							nomvar = datosvar.get(j).getNomvar();
							datosBean = dato.obtDatosVar(nomvar, codEsta, vfec, vhor);
							
							if (datosBean.getDato1() == -999) {
								myArray.add(fnd);
							} else {
								
//								TEST DE LIMITES
								cod = ecu.LimitesDuros(nomvar, vfec, vhor, codEsta,datosBean.getDato1());
								if (cod.equals(fMalo)) {
									myArray.add(cod);
								} else {
									
									totalFunctionsL = nameFunt.obtNameFunction(nomvar,1); //Consulta que esta amarrado con el tipo_test_id - 1 Referencia a los test de limites
									tFuntL = totalFunctionsL.size();

									if ( (tFuntL == 2) || (tFuntL == 3) || (tFuntL == 4) ) {
										
										flagTemp = evalRegEstaNacional(nomvar, vfec, vhor, codEsta, datosBean.getDato1());
										
										if (!flagTemp.equals("NE")) {
											
											myArray.add(flagTemp);
											
										}
									}

//									TEST DE CONSISTENCIA
									totalFunctions = nameFunt.obtNameFunction(nomvar,2); //Consulta que esta amarrado con el tipo_test_id - 2 Referencia a los test de consistencia
									tFunt = totalFunctions.size();
									if (tFunt != 0) {
										for (int k = 0; k < tFunt; k++) {
											nameFunction = totalFunctions.get(k).getNameFunction();
											
											if (nameFunction.equals("ConsistenciaTemporalPaso")) {
												cod = ecu.ConsistenciaTemporalPaso(nomvar, vfec, vhor, codEsta, datosBean.getDato1());
												flag = cod;
												if (flag != fBueno) {
													myArray.add(flag);
												}
												
											}
											if (nameFunction.equals("ConsistenciaTemporalPersistencia")) {
												cod = ecu.ConsistenciaTemporalPersistencia(nomvar, vfec, vhor, codEsta, datosBean.getDato1());
												flag = cod;
												if (flag != fBueno) {
													myArray.add(flag);
												}
												
											}
										}
									}
									
									totalFunctionsL.clear();
									totalFunctions.clear();
								}
								
							}

//							Registrar los datos con control de calidad automatico
							String cadena = myArray.toString();
							Integer num = cadena.length();
							String flagReg = cadena.substring(1, (num-1));
							
							if (regData.registrar(idusu, appname, this.getName(), ip, "", codEsta, nomvar, datosBean.getDato1(), "", flagReg,vfec,vhor)) {
								LOGGER.info("Registrado correctamente: " + cont);
								LOGGER.info("Cadena: " + flagReg + " " + nomvar);
								cont = cont+1; 
								myArray.clear();
							}else {
//								limpiar();
								LOGGER.error("Error: " + codEsta + " " + datosBean.getDato1() + " " + nomvar);
							}
						}
						
						
//						Actualizar estado de las estaciones trabajadas
						if (cont == tvar) {
							LOGGER.info(cont);
							if (dato.regDatoTrabajada(codEsta, vfec, vhor)) {
								LOGGER.info("Se actualizo a estado 2 correctamente");
								limpiar();
							} else {
								LOGGER.error("No se pudo actualizar a estado 2");
								limpiar();
							}
						} else {
							LOGGER.error(cont + " diferente a " + tvar);
							new EnviaEmailThread("Cantidades diferentes: " + datosvar + " " + codEsta,correoDestino).start();
							limpiar();
						}
						datosvar.clear();	

					} else {
						LOGGER.info("YA SE PROCESO LA ESTACIÓN: " + codEsta);
					}
					
					
				}
				
				
			}
			
			
		} catch (Exception e) {
			limpiar();
			totalFunctionsL.clear();
			totalFunctions.clear();
			myArray.clear();
			System.out.println("Error " + e.getMessage());
			LOGGER.error("Error: " + e.getMessage());
			String error = "Error en ControlCalidadThread: " + e.getMessage()+"<br>"+codEsta+"<br>";
			new EnviaEmailThread(error,correoDestino).start();
		}
	}
	
	public String evalRegEstaNacional(String nomvar1, String vfec1, String vhor1, String codEsta1, Double datoEst) {
		
//		LOGGER.info("Valor de Cantidad: " + cant);
		try {
			
//			validar si existe umbral a nivel de estación
			umbralBean = umbD.validarExistencia("LNE",nomvar1,codEsta1,mes,vhor1);
			cant = umbralBean.getCantidad();
			
			if ( cant > 0 ) {
				
				cod = ecu.LimitesEstacion(nomvar1, vfec1, vhor1, codEsta1, datoEst);
				vLflag = cod;
				
//				LOGGER.info( "Estacion >> " + nomvar1 + ": " + vLflag);
				
			} else {
				
//				validar si existe umbral a nivel de sector
				umbralBean = umbD.validarExistencia("LNR",nomvar1,codEsta1,mes,vhor1);
				cant = umbralBean.getCantidad();
				
				if ( cant > 0 ) {
					
					cod = ecu.LimitesRegional(nomvar1, vfec1, vhor1, codEsta1, datoEst);
					vLflag = cod;
					
//					LOGGER.info( "Regional >> " + nomvar1 + ": " + vLflag);
					
				} else {
					
//					validar si existe umbral a nivel de nacional
					umbralBean = umbD.validarExistencia("LNN",nomvar1,codEsta1,mes,vhor1);
					cant = umbralBean.getCantidad();
					
					if ( cant > 0 ) {
						
						cod = ecu.LimitesNacional(nomvar1, vfec1, vhor1, codEsta1, datoEst);
						vLflag = cod;
						
//						LOGGER.info( "Nacional >> " + nomvar1 + ": " + vLflag);
						
					} else {
						
						vLflag = "NE";
						
					}
					
				}
				
			}
			cant = 0;
			
		} catch (Exception e) {
			limpiar();
			totalFunctionsL.clear();
			totalFunctions.clear();
			myArray.clear();
			cant = 0;
			LOGGER.error("Error en ControlCalidadThread.evalRegEsta " + e.getMessage());
		}
		
		return vLflag;
	}
	
	public void limpiar() {
		
//		totalFunctionsL.clear();
//		totalFunctions.clear();
//		myArray.clear();
		tFunt = 0;
		tFuntL = 0;
		cont = 0;
		
	}
	
}
