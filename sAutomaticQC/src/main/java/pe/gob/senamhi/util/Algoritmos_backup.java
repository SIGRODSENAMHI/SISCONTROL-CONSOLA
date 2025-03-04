package pe.gob.senamhi.util;


import java.util.ArrayList;
import java.util.List;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.FlagBean;
import pe.gob.senamhi.bean.ParametroBean;
import pe.gob.senamhi.bean.UmbralBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.ParametroDao;
import pe.gob.senamhi.dao.UmbralDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.dao.impl.ParametroDaoImpl;
import pe.gob.senamhi.dao.impl.UmbralDaoImpl;

public class Algoritmos_backup {
	final String fBueno = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.bueno");
	final String fMalo = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.malo");
	final String fMaloEsp = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.maloesp");
	private String flagP="";
	private String codflag = "";
	private String codFlagT = "";
	private Double Linf1 = 0.00;
	private Double Linf2 = 0.00;
	private Double Lsup1 = 0.00;
	private Double Lsup2 = 0.00;
	
	private UmbralDao umbral = new UmbralDaoImpl();
	private DatosDao datosDao = new DatosDaoImpl();
	private ParametroDao params = new ParametroDaoImpl();
	private DatosBean datosBean = null;
	private ParametroBean parametroBean = null;
	private UmbralBean umbralBean = null;
	private FlagBean flagBean = null;
	private List<String> flag = new ArrayList<String>();
	
	private String cadena = "";
	private Integer num = 0;
	private String flagReg = "";
	
	public String LimitesDuros(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		Double sum24H = 0.00;
		String flagB = "";
		
		umbralBean= umbral.obtenerUmbrales("LD", variable, cod_est, "NULL", "NULL", "NULL");
		
		if (umbralBean != null) {
			if (variable.equals("N_LLUVIA")) {
				
				Linf1 = Util.convertToDouble(umbralBean.getValmin1());
				Lsup1 = Util.convertToDouble(umbralBean.getValmax1());
				
				Linf2 = Util.convertToDouble(umbralBean.getValmin2());
				Lsup2 = Util.convertToDouble(umbralBean.getValmax2());
				
			} else {
				
				Linf1 = Util.convertToDouble(umbralBean.getValmin1());
				Lsup1 = Util.convertToDouble(umbralBean.getValmax1());
				
			}
			
		}
		
		if (variable.equals("N_LLUVIA")) {
			
			String fechaHora = fecha+" "+hora;
			datosBean = datosDao.sum24Horas(variable, cod_est, fechaHora, 23);
			sum24H = datosBean.getSum24h();
			
			if ( (dato >= Linf1) && (dato <= Lsup1) ) {
				flagB = fBueno;
			} else {
				flag.add(fMalo);
			}
			
			if ( (sum24H >= Linf2) && (sum24H <= Lsup2) ) {
				flagB = fBueno;
				
			} else {
				flag.add(fMaloEsp);
//				flagBean = datosDao.obtCodigoFlag("LimitesDuros", variable, "R2");
//				codflag = flagBean.getCodFlag();
//				flag.add("M"+codflag);
			}
			
		} else {
			if (dato >= Linf1 && dato <= Lsup1) {
				flagB = fBueno;
			} else {
				flag.add(fMalo);
			}
		}
		
		if (flagB.equals(fBueno)) {
			if (flag.size() == 0) {
				flag.add(flagB);
			}
		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	public String LimitesNacional(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		Double sum24H = 0.00;
		String flagB = "";
		String flagDB1 = "";
		String flagDB2 = "";
		
		umbralBean= umbral.obtenerUmbrales("LNN", variable, cod_est, "NULL", "NULL", "NULL");
		
		
		
		if (umbralBean != null) {
			if (variable.equals("N_LLUVIA")) {
				
				Linf1 = Util.convertToDouble(umbralBean.getValmin1());
				Lsup1 = Util.convertToDouble(umbralBean.getValmax1());
				
				Linf2 = Util.convertToDouble(umbralBean.getValmin2());
				Lsup2 = Util.convertToDouble(umbralBean.getValmax2());
				
			} else {
				
				Linf1 = Util.convertToDouble(umbralBean.getValmin1());
				Lsup1 = Util.convertToDouble(umbralBean.getValmax1());
				
			}
			
		}
		
		if (variable.equals("N_LLUVIA")) {
			
			String fechaHora = fecha+" "+hora;
			datosBean = datosDao.sum24Horas(variable, cod_est, fechaHora, 23);
			sum24H = datosBean.getSum24h();
			
			flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R1");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
			
			if ( (dato >= Linf1) && (dato <= Lsup1) ) {
//				flagB = fBueno;
				flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R3");
				codFlagT = flagBean.getCodFlag();
				
				flagDB1 = codFlagT;
				flag.add("D"+codFlagT);
				
			} else {
				flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R4");
				codFlagT = flagBean.getCodFlag();
				
				flagDB2 = codFlagT;
				flag.add("D"+codFlagT);
			}
			
			flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R2");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
			
			if ( (sum24H >= Linf2) && (sum24H <= Lsup2) ) {
//				flagB = fBueno;
				flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R3");
				codFlagT = flagBean.getCodFlag();
				
				if (flagDB1.equals("")) {
					flag.add("D"+codFlagT);
				}
				
			} else {
				flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R4");
				codFlagT = flagBean.getCodFlag();
				
				if (flagDB2.equals("")) {
					flag.add("D"+codFlagT);
				}
			}
			
		} else {
			flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R1");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
			
			if ( (dato >= Linf1) && (dato <= Lsup1) ) {
//				flag.add(fBueno);
				flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R2");
				codFlagT = flagBean.getCodFlag();
				
				flag.add("D"+codFlagT);
			} else {
				flagBean = datosDao.obtCodigoFlag("LimitesNacional", variable, "R3");
				codFlagT = flagBean.getCodFlag();
				
				flag.add("D"+codFlagT);
			}
			
		}
		
//		if (flagB.equals(fBueno)) {
//			if (flag.size() == 0) {
//				flag.add(flagB);
//			}
//		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	public String LimitesRegional(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		String mes = Util.obtenerMes();
//		capturar el codigo de sector para pasarlo a la funcion obtenerUmbrales();
		umbralBean= umbral.obtenerUmbrales("LNR", variable, cod_est, mes, "MENSUAL HORARIO", hora);
		
		flagBean = datosDao.obtCodigoFlag("LimitesRegional", variable, "R1");
		codflag = flagBean.getCodFlag();
		
		if (umbralBean != null) {
			Linf1 = Util.convertToDouble(umbralBean.getValmin1()); // SUMAR LA ALTURA
			Lsup1 = Util.convertToDouble(umbralBean.getValmax1()); // SUMAR LA ALTURA
		}
		
		if (dato >= Linf1 && dato <= Lsup1) {
			flag.add(fBueno);
		} else {
			flag.add("D"+codflag);
		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	public String LimitesEstacion(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		String mes = Util.obtenerMes();
		
		
		umbralBean= umbral.obtenerUmbrales("LNE", variable, cod_est, mes, "MENSUAL HORARIO", hora);
		
		if (umbralBean != null) {
			Linf1 = Util.convertToDouble(umbralBean.getValmin1());
			Linf2 = Util.convertToDouble(umbralBean.getValmin2());
			Lsup1 = Util.convertToDouble(umbralBean.getValmax1());
			Lsup2 = Util.convertToDouble(umbralBean.getValmax2());
		}
		
		if (dato >= Linf1 && dato <= Lsup1) {
			flag.add(fBueno);
		} else {
			
			if ( (dato > Lsup1 && dato <= Lsup2) || (dato >= Linf2 && dato < Linf1 ) ) {
				
				if ( (dato > Lsup1 && dato <= Lsup2) ) {
					
					flagBean = datosDao.obtCodigoFlag("LimitesEstacion", variable, "R1");
					codflag = flagBean.getCodFlag();
					
					flag.add("D"+codflag);
				} 
				
				if ( (dato >= Linf2 && dato < Linf1 ) ) {
					
					flagBean = datosDao.obtCodigoFlag("LimitesEstacion", variable, "R2");
					codflag = flagBean.getCodFlag();
					
					flag.add("D"+codflag);
				}
				
			} else {
				
				if ( (dato > Lsup2) ) {
					flagBean = datosDao.obtCodigoFlag("LimitesEstacion", variable, "R3");
					codflag = flagBean.getCodFlag();
					
					flag.add("D"+codflag);
				}
				
				if ( (dato < Linf2) ) {
					flagBean = datosDao.obtCodigoFlag("LimitesEstacion", variable, "R4");
					codflag = flagBean.getCodFlag();
					
					flag.add("D"+codflag);
				}
			}
		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	
//	ALGORITMOS PARA EL CALCULO DE TEST DE CONSISTENCIA TEMPORAL DE PASO
	public String ConsistenciaTemporalPaso(String variable, String fecha, String hora, String cod_est, Double dato) {
		if (variable.equals("N_HUMEDAD")) {
			flagP = ConsistenciaTemporalPaso_N_HUMEDAD(variable,fecha,hora,cod_est,dato);
		}
		if (variable.equals("N_AIRTEMP")) {
			flagP = ConsistenciaTemporalPaso_N_AIRTEMP(variable,fecha,hora,cod_est,dato);
		}
		if (variable.equals("N_NIVELAGUA")) {
			flagP = ConsistenciaTemporalPaso_N_NIVELAGUA(variable,fecha,hora,cod_est,dato);
		}
		
		return flagP;
	}
	public String ConsistenciaTemporalPaso_N_HUMEDAD(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		Double dath1 = 0.00;
		Integer pmt1 = 0;
		Double dif = 0.00;
		String fechaHora = fecha+" "+hora;
		
		parametroBean = params.obtParametros("ConsistenciaTemporalPaso", variable);
		pmt1 = parametroBean.getParam1();
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est, fechaHora,1);
		if (datosBean != null) {
			dath1 = datosBean.getDath();
		} else {
			dath1 = null;
		}
		if (dath1 == null) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R1");
			codflag = flagBean.getCodFlag();
			
			flag.add("D"+codflag);
		} else {
			
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R1");
			codflag = flagBean.getCodFlag();
			dif = Math.abs(dato - dath1);
			
			if ( dif < pmt1 ) {
				flag.add(fBueno);
			} else {
				flag.add("D"+codflag);
			}
		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	public String ConsistenciaTemporalPaso_N_AIRTEMP(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		
		String flagB = "";
		
		Double dath1=0.00;Double dath2=0.00;Double dath3=0.00;Double dath6=0.00;Double dath12=0.00;
		
		Integer pmt1 = 0; Integer pmt2 = 0; Integer pmt3 = 0; Integer pmt4 = 0; Integer pmt5 = 0;
		
		Double dif1 = 0.00; Double dif2 = 0.00; Double dif3 = 0.00; Double dif4 = 0.00; Double dif5 = 0.00;
		
		String fechaHora = fecha+" "+hora;
		
		parametroBean = params.obtParametros("ConsistenciaTemporalPaso", variable);
		pmt1 = parametroBean.getParam1();
		pmt2 = parametroBean.getParam2();
		pmt3 = parametroBean.getParam3();
		pmt4 = parametroBean.getParam4();
		pmt5 = parametroBean.getParam5();
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,1);
		if (datosBean != null) {
			dath1 = datosBean.getDath();
		} else {
			dath1 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,2);
		if (datosBean != null) {
			dath2 = datosBean.getDath();
		} else {
			dath2 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,3);
		if (datosBean != null) {
			dath3 = datosBean.getDath();
		} else {
			dath3 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,6);
		if (datosBean != null) {
			dath6 = datosBean.getDath();
		} else {
			dath6 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,12);
		if (datosBean != null) {
			dath12 = datosBean.getDath();
		} else {
			dath12 = null;
		}
		
		if (dath1 == null) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R1");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R1");
			codflag = flagBean.getCodFlag();
			dif1 = Math.abs(dato - dath1);
			if (dif1 <= pmt1) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if (dath2 == null) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R2");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
			
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R2");
			codflag = flagBean.getCodFlag();
			dif2 = Math.abs(dato - dath2);
			if (dif2 <= pmt2) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
				
			}
		}
		
		if (dath3 == null) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R3");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
			
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R3");
			codflag = flagBean.getCodFlag();
			dif3 = Math.abs(dato - dath3);
			if (dif3 <= pmt3) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if (dath6 == null) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R4");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R4");
			codflag = flagBean.getCodFlag();
			dif4 = Math.abs(dato - dath6);
			if (dif4 <= pmt4) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if (dath12 == null) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R5");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R5");
			codflag = flagBean.getCodFlag();
			dif5 = Math.abs(dato - dath12);
			if (dif5 <= pmt5) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if (flagB.equals(fBueno)) {
			if (flag.size() == 0) {
				flag.add(flagB);
			}
		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	public String ConsistenciaTemporalPaso_N_NIVELAGUA(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		String flagB = "";
		
		Double dath1=0.00;
		Double dif1 = 0.00;
		Double desv = 0.00;
		Double q1 = 0.00;
		Double q3 = 0.00;
		Double ric = 0.00;
		Double vlef = 0.00;
		Double vright = 0.00;
		String fechaHora = fecha+" "+hora;
		
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,1);
		if (datosBean != null) {
			dath1 = datosBean.getDath();
		} else {
			dath1 = null;
		}
		datosBean = datosDao.calculoDesviacion(variable, cod_est,fechaHora,23);
		if (datosBean != null) {
			desv = datosBean.getDesv();
		} else {
			desv = null;
		}
		datosBean = datosDao.listaDatosCuartiles(variable, cod_est,fechaHora,23);
		if (datosBean != null) {
			q1 = datosBean.getQ1();
			q3 = datosBean.getQ3();
			ric = datosBean.getRic();
		} else {
			q1 = null;q3 = null;ric = null;
		}
		
		if (dath1 == null) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R1");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R1");
			codflag = flagBean.getCodFlag();
			dif1 = Math.abs(dato - dath1);
			if (dif1 < 3*desv) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if ( (desv == null) || (q1 == null) || (q3 == null) || (ric == null) ) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R2");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPaso", variable, "R2");
			codflag = flagBean.getCodFlag();
			vlef = (q1)-(1.5*ric);
			vright = (q3)+(1.5*ric);
			
			if ( (dato > vlef) && (dato < vright) ) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if (flagB.equals(fBueno)) {
			if (flag.size() == 0) {
				flag.add(flagB);
			}
		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
//	ALGORITMO PARA EL CALCULO DE LOS TEST DE CONSISTENCIA TEMPORAL DE PERSISTENCIA
	public String ConsistenciaTemporalPersistencia(String variable, String fecha, String hora, String cod_est, Double dato) {
		if (variable.equals("N_HUMEDAD")) {
			flagP = ConsistenciaTemporalPersistencia_N_HUMEDAD(variable,fecha,hora,cod_est,dato);
		}
		if (variable.equals("N_AIRTEMP")) {
			flagP = ConsistenciaTemporalPersistencia_N_AIRTEMP(variable,fecha,hora,cod_est,dato);
		}
		
		return flagP;
	}
	
	public String ConsistenciaTemporalPersistencia_N_HUMEDAD(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		String flagB = "";
		
		Double dath1=0.00; Double dath2=0.00; Double dath3=0.00;
		Double desv=0.00;
		Double media=0.00;
		Double vleft=0.00;
		Double vright=0.00;
		Integer pmt1=0;
		String fechaHora = fecha+" "+hora;
		
		parametroBean = params.obtParametros("ConsistenciaTemporalPersistencia", variable);
		pmt1 = parametroBean.getParam1();
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,1);
		if (datosBean != null) {
			dath1 = datosBean.getDath();
		} else {
			dath1 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,2);
		if (datosBean != null) {
			dath2 = datosBean.getDath();
		} else {
			dath2 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,3);
		if (datosBean != null) {
			dath3 = datosBean.getDath();
		} else {
			dath3 = null;
		}
		
		datosBean = datosDao.calculoDesviacion(variable, cod_est,fechaHora,4);
		if (datosBean != null) {
			desv = datosBean.getDesv();
		} else {
			desv = null;
		}
		
		datosBean = datosDao.calculoMedia(variable, cod_est,fechaHora,4);
		if (datosBean != null) {
			media = datosBean.getMedia();
		} else {
			media = null;
		}
		
		if (dato < pmt1) {
			flag.add(fBueno);
		} else {
			if ( (dath1 == null) || (dath2 == null) || (dath3 == null) ) {
				flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R1");
				codflag = flagBean.getCodFlag();
				flag.add("D"+codflag);
			} else {
				flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R1");
				codflag = flagBean.getCodFlag();
				if ( (dato != dath1) && (dath1 != dath2) && (dath2 != dath3) ) {
					flagB = fBueno;
				} else {
					flag.add("D"+codflag);
				}
			}
			
			if ( (desv == null) || (media == null) ) {
				flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R2");
				codflag = flagBean.getCodFlag();
				flag.add("D"+codflag);
			} else {
				flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R2");
				codflag = flagBean.getCodFlag();
				vleft=(media)-(3*desv);
				vright=(media)+(3*desv);
				
				if ( (dato <= vright) && (dato >= vleft) ) {
					flagB = fBueno;
				} else {
					flag.add("D"+codflag);
				}
			}
		}
		
		if (flagB.equals(fBueno)) {
			if (flag.size() == 0) {
				flag.add(flagB);
			}
		} 
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	public String ConsistenciaTemporalPersistencia_N_AIRTEMP(String variable, String fecha, String hora, String cod_est, Double dato) {
		
		String flagB = "";
		
		Double dath1=0.00; Double dath2=0.00;Double dath3=0.00;
		Double media=0.00;
		Double desv=0.00;
		Double vleft = (media)-(3*desv);
		Double vright = (media)+(3*desv);
		String fechaHora = fecha+" "+hora;
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,1);
		if (datosBean != null) {
			dath1 = datosBean.getDath();
		} else {
			dath1 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,2);
		if (datosBean != null) {
			dath2 = datosBean.getDath();
		} else {
			dath2 = null;
		}
		
		datosBean = datosDao.listaDatosHorarios(variable, cod_est,fechaHora,3);
		if (datosBean != null) {
			dath3 = datosBean.getDath();
		} else {
			dath3 = null;
		}
		
		datosBean = datosDao.calculoDesviacion(variable, cod_est,fechaHora,4);
		if (datosBean != null) {
			desv = datosBean.getDesv();
		} else {
			desv = null;
		}
		
		datosBean = datosDao.calculoMedia(variable, cod_est,fechaHora,4);
		if (datosBean != null) {
			media = datosBean.getMedia();
		} else {
			media = null;
		}
		
		if ( (dath1 == null) || (dath2 == null) || (dath3 == null) ) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R1");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R1");
			codflag = flagBean.getCodFlag();
			if ( (dato != dath1) && (dath1 != dath2) && (dath2 != dath3) ) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if ( (desv == null) || (media == null) ) {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R2");
			codflag = flagBean.getCodFlag();
			flag.add("D"+codflag);
		} else {
			flagBean = datosDao.obtCodigoFlag("ConsistenciaTemporalPersistencia", variable, "R2");
			codflag = flagBean.getCodFlag();
			vleft = (media)-(3*desv);
			vright = (media)+(3*desv);
			
			if ( (dato <= vright) && (dato >= vleft) ) {
				flagB = fBueno;
			} else {
				flag.add("D"+codflag);
			}
		}
		
		if (flagB.equals(fBueno)) {
			if (flag.size() == 0) {
				flag.add(flagB);
			}
		}
		
		cadena = flag.toString();
		num = cadena.length();
		flagReg = cadena.substring(1, (num-1));
		flag.clear();
		
		return flagReg;
	}
	
	
}
