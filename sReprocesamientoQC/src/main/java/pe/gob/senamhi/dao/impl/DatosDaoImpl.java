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

	public ParamObtBean obtEstTrabo(String ip, String nameApp, String nameHilo) {
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_REPROCESAMIENTO_QC.SP_OBT_EST_DE_TRABAJO(?,?,?,?,?,?)}";

			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, ip);
			cstmt.setString(2, nameApp);
			cstmt.setString(3, nameHilo);
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();

			paramObtBean = new ParamObtBean();
			paramObtBean.setVcodesta(cstmt.getObject(4).toString());
			paramObtBean.setFechareg(cstmt.getObject(5).toString());
			paramObtBean.setHorareg(cstmt.getObject(6).toString());

			cstmt.close();
			rs.close();

		} catch (Exception e) {
			paramObtBean = null;
			LOGGER.error("Error DatosDaoImpl.obtEstTrabo " + e.getMessage() );
		} finally {

			cerrarConexionDS();
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
			
			cstmt.close();
			rs.close();
			
		} catch (Exception e) {
			datosBean = null;
			LOGGER.error("Error DatosDaoImpl.obtDatosVar " + e.getMessage());
		} finally {
			cerrarConexionDS();
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
			cstmt.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.regDatoTrabajada " + e.getMessage());
			procesoOk = false;
		} finally {
			cerrarConexionDS();
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
			
//			LOGGER.info("Paso 1: "+cstmt.getObject(3).toString()+cstmt.getObject(4).toString());
			
			cstmt.close();
			rs.close();

		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.obtEstadoEst " + e.getMessage() + " COD_ESTA: " + codesta + fecHora);
		} finally {

			cerrarConexionDS();
		}
		return validarDatoBean;
	}

	public DatosBean listaDatosHorarios(String nvar, String codesta, String fechaHora, Integer horAtr) {
		
		try {
			abrirConexionDS();
			
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
			stmt.close();
			rs.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.listaDatosHorarios" + e.getMessage());
		} finally {
			cerrarConexionDS();
		}
		
		return datosBean;
	}

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
			
			cstmt.close();
			rs.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.calculoDesviacion" + e.getMessage());
		} finally {
			cerrarConexionDS();
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
			
			cstmt.close();
			rs.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.listaDatosCuartiles" + e.getMessage());
		} finally {
			cerrarConexionDS();
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
			
			cstmt.close();
			rs.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.calculoMedia" + e.getMessage());
		} finally {
			cerrarConexionDS();
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
			
			cstmt.close();
			rs.close();
		} catch (Exception e) {
			flagBean = null;
			LOGGER.error("Error DatosDaoImpl.obtCodigoFlag" + e.getMessage() + nameFuncion + nomvar + numRegla);
		} finally {
			cerrarConexionDS();
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
			
			cstmt.close();
			rs.close();
		} catch (Exception e) {
			flagBean = null;
			LOGGER.error("Error DatosDaoImpl.obtCodigoFlagGeneral" + e.getMessage());
		} finally {
			cerrarConexionDS();
		}
		
		return flagBean;
	}
	
	public int regDisponibles() throws Exception{
		int total = 0;
		try {
			abrirConexionDS();
			String sql = "SELECT count(*) as cantidadReg FROM SISCTRLCAL.EATBP_DATA_QCA WHERE ESTADO_R='5' AND ESTADO_A='5'";
			ResultSet rs = ds.createStatement().executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt("cantidadReg");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error DatosDaoImpl.opDisp " + e.getMessage());
		} finally {
			cerrarConexionDS();
		}
		return total;
	}
	
	public DatosBean sum24Horas(String nvar, String codesta, String fechaHora, Integer horAtr) {
		
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FUNCIONES_QC.CAL_SUMA24HORAS_REPROC(?,?,?,?,?,?)}";
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
			
			cstmt.close();
			rs.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.sum24Horas" + e.getMessage());
		} finally {
			cerrarConexionDS();
		}
		
		return datosBean;
	}
	
	public int validarEstado(String appIp, String nameApp) throws Exception{
		int cant = 0;
		try {
			abrirConexionDS();
			String sql = "SELECT COUNT(*) AS CANT_REG FROM SISCTRLCAL.EATBP_DATA_QCA WHERE ESTADO_A='1' AND ESTADO_R='1' AND APP_IP='"+appIp+"' AND NOMBRE_APP='"+nameApp+"'";
			ResultSet rs = ds.createStatement().executeQuery(sql);
			while (rs.next()) {
				cant = rs.getInt("CANT_REG");
				
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error DatosDaoImpl.validarEstado " + e.getMessage());
		} finally {
			cerrarConexionDS();
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
			cstmt.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.errorExterno " + e.getMessage());
			procesoOk = false;
		} finally {
			cerrarConexionDS();
		}
		return procesoOk;
	}
	
//	Validaciones para capturar datos de las ultimas 24 horas con estado 0
	public DatosFHBean obtFHMinMax() {
		
		try {
			abrirConexionDS();
			
			consulta = "SELECT \r\n" + 
					"CASE WHEN C1.F_MINIMA IS NULL THEN 'NE' ELSE C1.F_MINIMA END AS FECHA_MINIMA,\r\n" + 
					"CASE WHEN C1.F_MAXIMA IS NULL THEN 'NE' ELSE C1.F_MAXIMA END AS FECHA_MAXIMA\r\n" + 
					"FROM (SELECT \r\n" + 
					"TO_CHAR(MIN(TO_DATE(TO_CHAR(T1.D_FECHA_REG,'DD/MM/YYYY')||' '||T1.V_HORA_REG,'DD/MM/YYYY HH24:MI:SS')),'YYYYMMDDHH24MISS') AS F_MINIMA,\r\n" + 
					"TO_CHAR(MAX(TO_DATE(TO_CHAR(T1.D_FECHA_REG,'DD/MM/YYYY')||' '||T1.V_HORA_REG,'DD/MM/YYYY HH24:MI:SS')+1/24),'YYYYMMDDHH24MISS') AS F_MAXIMA \r\n" + 
					"FROM SISCTRLCAL.EATBP_DATA_QCA T1\r\n" + 
					"INNER JOIN SISCTRLCAL.ESTA_VARIABLE T2 ON T1.V_COD_ESTA=T2.V_COD_ESTA \r\n" + 
					"WHERE T1.ESTADO_A='5' AND T1.ESTADO_R='5') C1";
			
			stmt = ds.createStatement();
			stmt.setFetchSize(1000);
			rs = stmt.executeQuery(consulta);
			while (rs.next()) {
				datosFHBean = new DatosFHBean();
				datosFHBean.setFecMin(rs.getString("FECHA_MINIMA"));
				datosFHBean.setFecMax(rs.getString("FECHA_MAXIMA"));
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.obtFHMinMax" + e.getMessage());
		} finally {
			cerrarConexionDS();
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
			
			cstmt.close();
			rs.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.obtFHProcesar" + e.getMessage());
		} finally {
			cerrarConexionDS();
		}
		
		return datosFHBean;
	}

}
