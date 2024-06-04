package pe.gob.senamhi.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.FunctionBean;
import pe.gob.senamhi.bean.UmbralBean;
import pe.gob.senamhi.bean.VariableBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.FunctionDao;
import pe.gob.senamhi.dao.QCDataDao;
import pe.gob.senamhi.dao.UmbralDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.dao.impl.FunctionDaoImpl;
import pe.gob.senamhi.dao.impl.QCDataDaoImpl;
import pe.gob.senamhi.dao.impl.UmbralDaoImpl;
import pe.gob.senamhi.util.Algoritmos;
import pe.gob.senamhi.util.EnviaEmailThread;
import pe.gob.senamhi.util.FechaHoraUtil;
import pe.gob.senamhi.util.PropiedadesUtil;
import pe.gob.senamhi.util.Util;

public class ProcesosVariableThread extends Thread{

	final String appname = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String correoDestino = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");
	final String fBueno = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.bueno");
	final String fMalo1 = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.malo");
	final String fMalo2 = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.maloesp");
	final String fnd = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.sindato");
	private DatosDao dato = new DatosDaoImpl();
	private Algoritmos ecu = new Algoritmos();
	private QCDataDao regData = new QCDataDaoImpl();
	private FunctionDao nameFunt = new FunctionDaoImpl();
	private UmbralDao umbD = new UmbralDaoImpl();
	private List<String> myArray = new ArrayList<String>();
	private List<FunctionBean> totalFunctionsL = null;
	private List<FunctionBean> totalFunctions = null;
	private DatosBean datosBean = null;
	private UmbralBean umbralBean = null;
	
	private static final Logger LOGGER = Logger.getLogger(ProcesosVariableThread.class);
	
	private List<VariableBean> listaVar;
	private int pIniV;
	private int pFinV;
	private String idusu = "";
	
	private String flag;
	private String flagTemp;
	private String cod;
	
	long millisI = System.currentTimeMillis();
	java.sql.Date dateI = new java.sql.Date(millisI);
	String fechaActual = FechaHoraUtil.obtFechaStringSinhora(dateI);
	String horActual = FechaHoraUtil.obtHoraStringSinfecha(dateI);
	String ip = Util.capturarIP();
//	String ip = "10.10.20.100";
	private Integer tFunt = 0;
	private Integer tFuntL = 0;
	private String codEsta = "";
	private String vfec = "";
	private String vhor = "";
	private String nomvar = "", nomVarDin = "";
	private String nameFunction = "";
	private String cadenaMalos = "";
	private int cant = 0;
	private int validarArray = 0;
	private String vLflag = "";
	
	private String[] valor;
	private Integer mesCap;
	private Integer ms;
	private String mes;
	
	private String mesNormal;
	
	private String cadena = "";
	private Integer num = 0;
	private String flagReg = "";
	
	public ProcesosVariableThread(List<VariableBean> listaVar, String codEsta, String vfec, String vhor, Integer pIniV, Integer pFinV) {
		this.listaVar = listaVar;
		this.codEsta = codEsta;
		this.vfec = vfec;
		this.vhor = vhor;
		this.pIniV = pIniV;
		this.pFinV = pFinV;
	}
	
	@Override
	public void run() {
		mesNormal = Util.obtenerMes(); //Con esta opcion cuando cambia de mes las ceros horas sigue tomando el mes anterior, por eso no se utiliza
		
		valor = vfec.split("/");
		mesCap = Integer.parseInt(valor[1]);
		ms = mesCap-1;
		mes = Util.obtenerMesxFecha(ms);  //Toma el mes correcto, funcion que se utiliza para realizar los calculos
		
		try {
			
			for (int j = pIniV; j < pFinV; j++) {
				
				nomVarDin = listaVar.get(j).getNomvar();
				
				if (nomVarDin.equals("N_NIVELMEDIO") || nomVarDin.equals("N_NIV_INST_00")) {
					nomvar = "N_NIVELAGUA";
				} else {
					nomvar = nomVarDin;
				}
				
//				LOGGER.info(this.getName() + " PROCESANDO LA VARIABLE:  " + nomvar + " Num: "+ j + " ESTACION: " + codEsta);
				
				datosBean = dato.obtDatosVar(nomVarDin, codEsta, vfec, vhor);
				
				if (datosBean.getDato1() == -999) {
					myArray.add(fnd);
				} else {
					
//					TEST DE LIMITES
					cod = ecu.LimitesDuros(nomvar, vfec, vhor, codEsta,datosBean.getDato1());
					
					if (!cod.equals(fBueno)) {
						validarArray = Util.contarArrayOfString(cod);
						if (validarArray == 2) {
							cadenaMalos = fMalo1 + ", " + fMalo2;
						} else {
							cadenaMalos = cod;
						}
					} 
					
					
					
					if (cod.equals(cadenaMalos)) {
						myArray.add(cod);
					} else {
						
						totalFunctionsL = nameFunt.obtNameFunction(nomvar,1); //Consulta que esta amarrado con el tipo_test_id - 1 Referencia a los test de limites
						tFuntL = totalFunctionsL.size();
//						System.out.println(totalFunctionsL.size());
						if ( (tFuntL == 2) || (tFuntL == 3) || (tFuntL == 4) ) {
							
							flagTemp = evalRegEstaNacional(nomvar, vfec, vhor, codEsta, datosBean.getDato1());
							
							if (!flagTemp.equals("NE")) {
								
								myArray.add(flagTemp);
								
							}
						}

//						TEST DE CONSISTENCIA
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

//				Registrar los datos con control de calidad automatico
				cadena = myArray.toString();
				num = cadena.length();
				flagReg = cadena.substring(1, (num-1));
				
				if (regData.registrar(idusu, appname, this.getName(), ip, "", codEsta, nomvar, datosBean.getDato1(), "", flagReg,vfec,vhor)) {
//					LOGGER.info("Registrado correctamente: " + cont);
					LOGGER.info("Cadena: " + flagReg + " Variable: " + nomvar + " Codigo: " + codEsta + " Dato: " + datosBean.getDato1() + " FH: " + vfec + vhor + " Mes: " + mes + " Mes Normal: " + mesNormal);
//					cont = cont+1; 
					myArray.clear();
				}else {
//					limpiar();
					LOGGER.error("Error: " + codEsta + " " + datosBean.getDato1() + " " + nomvar);
				}
			}
			
		} catch (Exception e) {
			limpiar();
			totalFunctionsL.clear();
			totalFunctions.clear();
			myArray.clear();
			System.out.println("Error " + e.getMessage());
			LOGGER.error("Error: " + e.getMessage());
			String error = "Error en ProcesoVariableThread: " + e.getMessage()+"<br>"+codEsta+"<br>";
			new EnviaEmailThread(error,correoDestino).start();
		}
		
		
	}
	
	public String evalRegEstaNacional(String nomvar1, String vfec1, String vhor1, String codEsta1, Double datoEst) {
		
//		LOGGER.info("Valor de Cantidad: " + cant);
		mes = Util.obtenerMes();
		try {
			
			valor = vfec1.split("/");
			mesCap = Integer.parseInt(valor[1]);
			ms = mesCap-1;
			mes = Util.obtenerMesxFecha(ms);
			
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
		
		tFunt = 0;
		tFuntL = 0;
		
	}
	
}
