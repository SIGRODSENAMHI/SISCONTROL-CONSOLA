package pe.gob.senamhi.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.DatosFHBean;
import pe.gob.senamhi.bean.FlagBean;
import pe.gob.senamhi.bean.ParamObtBean;
import pe.gob.senamhi.bean.ValidarDatoBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.database.AccesoDB;
import pe.gob.senamhi.util.PropiedadesUtil;
import pe.gob.senamhi.util.Util;


public class DatosDaoImpl extends AccesoDB implements DatosDao {
	
	private static final Logger LOGGER = Logger.getLogger(DatosDaoImpl.class);
	final String fMalo = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.malo");
	final String fND = PropiedadesUtil.obtenerPropiedad("configuracion", "name.flag.sindato");
	private ParamObtBean paramObtBean = null;
	private DatosBean datosBean = null;
	private ValidarDatoBean validarDatoBean = null;
	private FlagBean flagBean = null;
	private DatosFHBean datosFHBean = null;
	private CallableStatement cstmt = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String consulta = "";
	private boolean procesoOk;
	
	public ParamObtBean obtEstTrabo(String codesta, String ip, String nameApp, String nameHilo, String fecHora) {

		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_QUALITY_CONTROL.SP_OBT_EST_DE_TRABAJO(?,?,?,?,?,?,?)}";

			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codesta);
			cstmt.setString(2, ip);
			cstmt.setString(3, nameApp);
			cstmt.setString(4, nameHilo);
			cstmt.setString(5, fecHora);
			cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();

			paramObtBean = new ParamObtBean();
			paramObtBean.setFechareg(cstmt.getObject(6).toString());
			paramObtBean.setHorareg(cstmt.getObject(7).toString());

			

		} catch (Exception e) {
			paramObtBean = null;
			LOGGER.error("Error DatosDaoImpl.obtEstTrabo " + e.getMessage() + " COD_ESTA: " + codesta +" - "+ fecHora);
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.obtEstTrabo " + e2.getMessage() + " COD_ESTA: " + codesta +" - "+ fecHora);
			}
			
		}
		return paramObtBean;
	}

	public DatosBean obtDatosVar(String nvar, String codesta, String fecha, String hora) {

		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.OBT_DATOS_VARIABLE(?,?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codesta);
			cstmt.setString(2, fecha);
			cstmt.setString(3, hora);
			cstmt.setString(4, nvar);
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();
			
			datosBean = new DatosBean();
			datosBean.setDato1(Util.convertToDouble(cstmt.getObject(4).toString()));
			
			
			
		} catch (Exception e) {
			datosBean = null;
			LOGGER.error("Error DatosDaoImpl.obtDatosVar " + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.obtDatosVar " + e2.getMessage());
			}
			
		}
		return datosBean;
	}

	public boolean regDatoTrabajada(String codesta, String fecha, String hora) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_QUALITY_CONTROL.SP_REG_EST_TRABAJADA(?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codesta);
			cstmt.setString(2, fecha);
			cstmt.setString(3, hora);
			cstmt.executeQuery();
			procesoOk = true;
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.regDatoTrabajada " + e.getMessage());
			procesoOk = false;
		} finally {
			try {
				cstmt.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.regDatoTrabajada " + e2.getMessage());
			}
			
		}
		return procesoOk;
	}

	public ValidarDatoBean obtEstadoEst(String codesta, String fecHora) {

		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_QUALITY_CONTROL.SP_VALIDAR_DATOS(?,?,?,?)}";

			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codesta);
			cstmt.setString(2, fecHora);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();

			validarDatoBean = new ValidarDatoBean();
			validarDatoBean.setCodesta(cstmt.getObject(3).toString());
			validarDatoBean.setValdato(cstmt.getObject(4).toString());
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.obtEstadoEst " + e.getMessage() + " COD_ESTA: " + codesta + fecHora);
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.obtEstadoEst " + e2.getMessage() + " COD_ESTA: " + codesta + fecHora);
			}
			
		}
		return validarDatoBean;
	}

	public DatosBean listaDatosHorarios(String nvar, String codesta, String fechaHora, Integer horAtr) {
		
		try {
			abrirConexionDS();
			
			/*consulta = "SELECT T1.V_COD_ESTA,T1.FECHA,T1."+nvar+"  AS DATOH FROM\r\n" + 
					"(SELECT V_COD_ESTA,TO_CHAR(TO_DATE(D_FECHA_REG||' '||V_HORA_REG,'DD/MM/YY HH24:MI:SS'),'DD/MM/YYYY HH24:MI:SS') AS FECHA,"+nvar+" \r\n" + 
					"FROM SISCTRLCAL.EATBP_DATA \r\n" + 
					"WHERE V_COD_ESTA='"+codesta+"' AND D_FECHA_REG=TO_CHAR(TO_DATE('"+fechaHora+"','DD/MM/YYYY HH24:MI:SS')-"+horAtr+"/24,'DD/MM/YYYY')) T1 \r\n" + 
					"WHERE TO_DATE(T1.FECHA,'DD/MM/YYYY HH24:MI:SS') = TO_DATE('"+fechaHora+"','DD/MM/YYYY HH24:MI:SS')-"+horAtr+"/24";*/
			
			consulta = "SELECT T1.V_COD_ESTA,T1.N_DATA AS DATOH FROM \r\n" + 
					"(SELECT A1.V_COD_ESTA,CASE WHEN A2.CODIGO_NEW = '"+fMalo+"' OR A2.CODIGO_NEW = '"+fND+"' THEN -999 ELSE A1.N_DATA END AS N_DATA,A1.D_FECHA_REG,A1.V_HORA_REG\r\n" + 
					"FROM SISCTRLCAL.QC_DATA A1\r\n" + 
					"INNER JOIN SISCTRLCAL.FLAG A2 ON A1.ID=A2.QC_DATA_ID\r\n" + 
					"WHERE A1.V_COD_ESTA='"+codesta+"' AND A1.D_FECHA_REG=TO_CHAR(TO_DATE('"+fechaHora+"','DD/MM/YYYY HH24:MI:SS')-"+horAtr+"/24,'DD/MM/YYYY') \r\n" + 
					"AND A1.V_COD_VAR=(SELECT V_COD_VAR FROM SISCTRLCAL.DADTBP_VARIABLE WHERE V_NOM_VAR='"+nvar+"')) T1  \r\n" + 
					"WHERE TO_DATE(T1.D_FECHA_REG||' '||T1.V_HORA_REG,'DD/MM/YYYY HH24:MI:SS') = TO_DATE('"+fechaHora+"','DD/MM/YYYY HH24:MI:SS')-"+horAtr+"/24";
			
			stmt = ds.createStatement();
			stmt.setFetchSize(1000);
			rs = stmt.executeQuery(consulta);
			while (rs.next()) {
				datosBean = new DatosBean();
				datosBean.setCodesta(rs.getString("V_COD_ESTA"));
				datosBean.setDath(rs.getDouble("DATOH"));
			}
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.listaDatosHorarios" + e.getMessage());
			datosBean = null;
		} finally {
			try {
				stmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.listaDatosHorarios" + e2.getMessage());
			}
			
		}
		
		return datosBean;
	}
	
//	Procedure implementado correctamente
//	public DatosBean listaDatosHorarios(String nvar, String codesta, String fechaHora, Integer horAtr) {
//		
//		try {
//			abrirConexionDS();
//			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.OBT_DATOS_HORARIOS(?,?,?,?,?,?)}";
//
//			cstmt = ds.prepareCall(consulta);
//			cstmt.setString(1, nvar);
//			cstmt.setString(2, codesta);
//			cstmt.setString(3, fechaHora);
//			cstmt.setInt(4, horAtr);
//			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
//			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
//			rs = cstmt.executeQuery();
//			
//			datosBean = new DatosBean();
//			datosBean.setCodesta(cstmt.getObject(5).toString());
//			datosBean.setDath(Util.convertToDouble(cstmt.getObject(6).toString()));
//			
//			cstmt.close();
//			rs.close();
//		} catch (Exception e) {
//			LOGGER.error("Error DatosDaoImpl.listaDatosHorarios" + e.getMessage() + " " + nvar + " " + codesta + " " + fechaHora + " " + horAtr);
//			datosBean = null;
//		} finally {
//			cerrarConexionDS();
//		}
//		
//		return datosBean;
//	}

	public DatosBean calculoDesviacion(String nvar, String codesta, String fechaHora, Integer horAtr) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.CAL_DESVIACION_ESTANDAR(?,?,?,?,?,?)}";

			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, nvar);
			cstmt.setString(2, codesta);
			cstmt.setString(3, fechaHora);
			cstmt.setInt(4, horAtr);
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
			rs = cstmt.executeQuery();
			
			datosBean = new DatosBean();
			datosBean.setCodesta(cstmt.getObject(5).toString());
			datosBean.setDesv(Util.convertToDouble(cstmt.getObject(6).toString()));
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.calculoDesviacion" + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.calculoDesviacion" + e2.getMessage());
			}
			
		}
		
		return datosBean;
	}

	public DatosBean listaDatosCuartiles(String nvar, String codesta, String fechaHora, Integer horAtr) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.CAL_QUARTILES(?,?,?,?,?,?,?,?,?)}";

			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, nvar);
			cstmt.setString(2, codesta);
			cstmt.setString(3, fechaHora);
			cstmt.setInt(4, horAtr);
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
			cstmt.registerOutParameter(7, OracleTypes.NUMBER);
			cstmt.registerOutParameter(8, OracleTypes.NUMBER);
			cstmt.registerOutParameter(9, OracleTypes.NUMBER);
			rs = cstmt.executeQuery();
			
			datosBean = new DatosBean();
			datosBean.setCodesta(cstmt.getObject(5).toString());
			datosBean.setQ1(Util.convertToDouble(cstmt.getObject(6).toString()));
			datosBean.setQ2(Util.convertToDouble(cstmt.getObject(7).toString()));
			datosBean.setQ3(Util.convertToDouble(cstmt.getObject(8).toString()));
			datosBean.setRic(Util.convertToDouble(cstmt.getObject(9).toString()));
			
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.listaDatosCuartiles" + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.listaDatosCuartiles" + e2.getMessage());
			}
			
		}
		
		return datosBean;
	}

	public DatosBean calculoMedia(String nvar, String codesta, String fechaHora, Integer horAtr) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.CAL_MEDIA_ARIMETICA(?,?,?,?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, nvar);
			cstmt.setString(2, codesta);
			cstmt.setString(3, fechaHora);
			cstmt.setInt(4, horAtr);
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
			rs = cstmt.executeQuery();
			
			datosBean = new DatosBean();
			datosBean.setCodesta(cstmt.getObject(5).toString());
			datosBean.setMedia(Util.convertToDouble(cstmt.getObject(6).toString()));
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.calculoMedia" + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.calculoMedia" + e2.getMessage());
			}
			
		}
		
		return datosBean;
	}

	public FlagBean obtCodigoFlag(String nameFuncion, String nomvar, String numRegla) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.OBT_CODIGO_FLAG(?,?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, nameFuncion);
			cstmt.setString(2, nomvar);
			cstmt.setString(3, numRegla);
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();
			
			flagBean = new FlagBean();
			flagBean.setCodFlag(cstmt.getObject(4).toString());
			
			
		} catch (Exception e) {
			flagBean = null;
			LOGGER.error("Error DatosDaoImpl.obtCodigoFlag" + e.getMessage() + nameFuncion + nomvar + numRegla);
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.obtCodigoFlag" + e2.getMessage() + nameFuncion + nomvar + numRegla);
			}
			
		}
		
		return flagBean;
	}
	
	public FlagBean obtCodigoFlagGeneral(String nameFuncion, String nomvar) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.OBT_FLAG_GENERAL(?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, nameFuncion);
			cstmt.setString(2, nomvar);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();
			
			flagBean = new FlagBean();
			flagBean.setCodFlagEsta(cstmt.getObject(3).toString());
			
			
		} catch (Exception e) {
			flagBean = null;
			LOGGER.error("Error DatosDaoImpl.obtCodigoFlagGeneral" + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.obtCodigoFlagGeneral" + e2.getMessage());
			}
			
		}
		
		return flagBean;
	}

	public DatosBean sum24Horas(String nvar, String codesta, String fechaHora, Integer horAtr) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.CAL_SUMA24HORAS(?,?,?,?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, nvar);
			cstmt.setString(2, codesta);
			cstmt.setString(3, fechaHora);
			cstmt.setInt(4, horAtr);
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
			rs = cstmt.executeQuery();
			
			datosBean = new DatosBean();
			datosBean.setCodesta(cstmt.getObject(5).toString());
			datosBean.setSum24h(Util.convertToDouble(cstmt.getObject(6).toString()));
			
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.sum24Horas" + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.sum24Horas" + e2.getMessage());
			}
			
		}
		
		return datosBean;
	}
	
	
	public int validarEstado(String appIp, String nameApp) throws Exception{ 
		int cant = 0;
		try {
			abrirConexionDS();
			consulta = "SELECT COUNT(*) AS CANT_REG FROM SISCTRLCAL.EATBP_DATA_QCA WHERE ESTADO_A='1' AND ESTADO_R='1' AND APP_IP='"+appIp+"' AND NOMBRE_APP='"+nameApp+"' ";
			rs = ds.createStatement().executeQuery(consulta);
			while (rs.next()) {
				cant = rs.getInt("CANT_REG");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error DatosDaoImpl.validarEstado " + e.getMessage());
		} finally {
			try {
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.validarEstado " + e2.getMessage());
			}
			
		}
		return cant;
	}
	
	public boolean errorExterno(String appIP, String appName) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_QUALITY_CONTROL.SP_REPURERAR_DE_ERROR(?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, appIP);
			cstmt.setString(2, appName);
			cstmt.executeQuery();
			procesoOk = true;
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.errorExterno " + e.getMessage());
			procesoOk = false;
		} finally {
			try {
				cstmt.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.errorExterno " + e2.getMessage());
			}
			
		}
		return procesoOk;
	}

//	Validaciones para capturar datos de las ultimas 24 horas con estado 0
	public DatosFHBean obtFHMinMax(String fechaHora) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.OBT_FECH_MINMAX(?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, fechaHora);
			cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();
			
			datosFHBean = new DatosFHBean();
			datosFHBean.setFecMin(cstmt.getObject(2).toString());
			datosFHBean.setFecMax(cstmt.getObject(3).toString());
			
			
		} catch (Exception e) {
			datosFHBean = null;
			LOGGER.error("Error DatosDaoImpl.obtFHMinMax" + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.obtFHMinMax" + e2.getMessage());
			}
		}
		
		return datosFHBean;
	}

	public DatosFHBean obtFHProcesar(String fecNumber) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.OBT_FECH_PROCESAR(?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, fecNumber);
			cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();
			
			datosFHBean = new DatosFHBean();
			datosFHBean.setFecIncrem(cstmt.getObject(2).toString());
			datosFHBean.setFecHora(cstmt.getObject(3).toString());
			
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.obtFHProcesar" + e.getMessage());
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error DatosDaoImpl.obtFHProcesar" + e2.getMessage());
			}
			
		}
		
		return datosFHBean;
	}

}
