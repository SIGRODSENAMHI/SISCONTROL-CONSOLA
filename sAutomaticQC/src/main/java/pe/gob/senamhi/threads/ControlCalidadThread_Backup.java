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

public class ControlCalidadThread_Backup extends Thread{
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
	private FlagBean flagBean = null;
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
	private Integer tvar;
	private Integer tFunt;
	private Integer tFuntL;
	private String codEsta = "";
	private String fecHora = "";
	private String fecha = "";
	private String hora = "";
	private String vfec = "";
	private String vhor = "";
	private String codflag = "";
	private String codFlagGen = "";
	private String nomvar = "";
	private String nameFunction = "";
	private int cant = 0;
	private String mes = Util.obtenerMes();
	
	private int cont = 0;
	
	public ControlCalidadThread_Backup(List<EstacionesBean> listaEsta, Integer pIni, Integer pFin, String fecHora) {
		this.listaEsta = listaEsta;
		this.pIni = pIni;
		this.pFin = pFin;
		this.fecHora = fecHora;
	}

	@Override
	public void run() {
		try {
			
//			List<EstacionesBean> listaInforme = est.listarEstaciones();
//			System.out.println("S: "+listaEsta);
			for (int i = pIni; i < pFin; i++) {
				codEsta = listaEsta.get(i).getCodesta();
				LOGGER.info(this.getName() + " PROCESANDO LA ESTACION:  " + codEsta + " Num: "+ i + " Fecha y Hora: " + fecHora);
//				hora = horActual.substring(0, 2) + ":00:00";
//				fecHora = fechaActual + " " + hora;
				String datos[] = fecHora.split(" ");
				fecha = datos[0];
				hora = datos[1];
//				hora = "16:00:00";
//				fechaActual = "21/08/2019";
//				
//				fecHora = fechaActual + " " + hora;
				
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
					
//					new EnviaEmailThread(error,correoDestino).start();
					
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
									
//									cod = ecu.LimitesNacional(nomvar, vfec, vhor, codEsta, datosBean.getDato1());
									
									totalFunctionsL = nameFunt.obtNameFunction(nomvar,1); //Consulta que esta amarrado con el tipo_test_id - 1 Referencia a los test de limites
									tFuntL = totalFunctionsL.size();
//									System.out.println(tFunt);
									if (tFuntL == 2) {
										myArray.add(evalNacional(nomvar, vfec, vhor, codEsta, datosBean.getDato1()));
									}
									if ( (tFuntL == 3) || (tFuntL == 4) ) {
										
//										cod = ecu.LimitesNacional(nomvar, vfec, vhor, codEsta, datosBean.getDato1());
//										myArray.add(cod);
										myArray.add(evalNacional(nomvar, vfec, vhor, codEsta, datosBean.getDato1()));
										
										flagTemp = evalRegEsta(nomvar, vfec, vhor, codEsta, datosBean.getDato1());
										
										if (!flagTemp.equals("NE")) {
											
											myArray.add(flagTemp);
											
										}
									}
//									if (tFuntL == 3) {
//										myArray.add(evalRegional(nomvar, vfec, vhor, codEsta, datosBean.getDato1()));
//									}
//									if (tFuntL == 4) {
//										myArray.add(evalEstacion(nomvar, vfec, vhor, codEsta, datosBean.getDato1()));
//									}

//									TEST DE CONSISTENCIA
									totalFunctions = nameFunt.obtNameFunction(nomvar,2); //Consulta que esta amarrado con el tipo_test_id - 2 Referencia a los test de consistencia
									tFunt = totalFunctions.size();
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
									totalFunctionsL.clear();
									totalFunctions.clear();
								}
								
							}

//							Registrar los datos con control de calidad automatico
							String cadena = myArray.toString();
							Integer num = cadena.length();
							String flagReg = cadena.substring(1, (num-1));
//							System.out.println("T: "+flagReg);
							if (regData.registrar(idusu, appname, this.getName(), ip, "", codEsta, nomvar, datosBean.getDato1(), "", flagReg,vfec,vhor)) {
								LOGGER.info("Registrado correctamente: " + cont);
								LOGGER.info("Cadena: " + flagReg + " " + nomvar);
								cont = cont+1; 
								myArray.clear();
							}else {
								LOGGER.error("Error: " + codEsta + " " + datosBean.getDato1() + " " + nomvar);
							}
						}
						
						
//						Actualizar estado de las estaciones trabajadas
						if (cont == tvar) {
							LOGGER.info(cont);
							if (dato.regDatoTrabajada(codEsta, vfec, vhor)) {
								LOGGER.info("Se actualizo a estado 2 correctamente");
								cont = 0;
							} else {
								LOGGER.error("No se pudo actualizar a estado 2");
							}
						} else {
							LOGGER.error(cont + " diferente a " + tvar);
							new EnviaEmailThread("Cantidades diferentes: " + datosvar + " " + codEsta,correoDestino).start();
						}
						datosvar.clear();	

					} else {
						LOGGER.info("YA SE PROCESO LA ESTACIÓN: " + codEsta);
					}
					
					
				}
				
				
			}
			
			
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			LOGGER.error("Error: " + e.getMessage());
			String error = e.getMessage()+"<br>"+codEsta+"<br>";
			new EnviaEmailThread(error,correoDestino).start();
//			e.printStackTrace();
		}
	}
	
	public String evalNacional(String nomvar1,String vfec,String vhor, String codEsta,Double datoEst) {
		
		String vLflag = "";
		
		cod = ecu.LimitesNacional(nomvar1, vfec, vhor, codEsta, datoEst);
		vLflag = cod;
		
		return vLflag;
	}
	
	public String evalRegEsta(String nomvar1, String vfec1, String vhor1, String codEsta1, Double datoEst) {
		String vLflag = "";
		LOGGER.info("Valor a Evaluar: " + datoEst);
		try {
			
//			validar si existe umbral a nivel de estación
			umbralBean = umbD.validarExistencia("LNE",nomvar1,codEsta1,mes,vhor1);
			cant = umbralBean.getCantidad();
			
			if ( cant > 0 ) {
				
				cod = ecu.LimitesEstacion(nomvar1, vfec1, vhor1, codEsta1, datoEst);
				vLflag = cod;
				
			} else {
				
//				validar si existe umbral a nivel de sector
				umbralBean = umbD.validarExistencia("LNR",nomvar1,codEsta1,mes,vhor1);
				cant = umbralBean.getCantidad();
				
				if ( cant > 0 ) {
					
					cod = ecu.LimitesRegional(nomvar1, vfec1, vhor1, codEsta1, datoEst);
					vLflag = cod;
					
				} else {
					
					vLflag = "NE";
					
				}
				
			}
			
		} catch (Exception e) {
			LOGGER.error("Error en ControlCalidadThread.evalRegEsta " + e.getMessage());
		}
		
		return vLflag;
	}
	
	public String evalRegional(String nomvar1,String vfec,String vhor, String codEsta,Double datoEst) {
		
		String vLflag = "";
		
		flagBean = dato.obtCodigoFlag("LimitesRegional", nomvar1, "R1");
		codflag = flagBean.getCodFlag();
		
		cod = ecu.LimitesNacional(nomvar1, vfec, vhor, codEsta, datoEst);
		if (cod != fBueno) {
			vLflag = cod+",D"+codflag;
		} else {
			cod = ecu.LimitesRegional(nomvar1, vfec, vhor, codEsta, datoEst);
			vLflag = cod;
		}
		
		return vLflag;
	}
	
	public String evalEstacion(String nomvar1,String vfec,String vhor, String codEsta,Double datoEst) {
		
		String vLflag = "";
		
		flagBean = dato.obtCodigoFlag("LimitesRegional", nomvar1, "R1");
		codflag = flagBean.getCodFlag();
		
		flagBean = dato.obtCodigoFlagGeneral("LimitesEstacion", nomvar1);
		codFlagGen = flagBean.getCodFlagEsta();
		
		cod = ecu.LimitesNacional(nomvar1, vfec, vhor, codEsta, datoEst);
		if (cod != fBueno) {
			vLflag = cod+",D"+codflag+","+codFlagGen;
		} else {
			cod = ecu.LimitesRegional(nomvar1, vfec, vhor, codEsta, datoEst);
			if (cod != fBueno) {
				vLflag = "D"+codflag+","+codFlagGen;
			} else {
				cod = ecu.LimitesEstacion(nomvar1, vfec, vhor, codEsta, datoEst);
				vLflag = cod;
			}
		}
		
		return vLflag;
	}
	
}
