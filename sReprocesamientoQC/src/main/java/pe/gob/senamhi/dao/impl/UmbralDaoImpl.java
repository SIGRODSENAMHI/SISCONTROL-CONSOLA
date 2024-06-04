package pe.gob.senamhi.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import pe.gob.senamhi.bean.UmbralBean;
import pe.gob.senamhi.dao.UmbralDao;
import pe.gob.senamhi.database.AccesoDB;

public class UmbralDaoImpl extends AccesoDB implements UmbralDao{

	private static final Logger LOGGER = Logger.getLogger(UmbralDaoImpl.class);
	private String valmi1, valmi2, valma1, valma2;
	String[] datos = new String[4];
	ResultSet rs = null;
	
	public UmbralBean obtenerUmbrales(String codtest, String varbl, String codest, String detPer, String nomPer, String detHor) {
		UmbralBean umbralBean = null;
		CallableStatement cstmt;
		ResultSet resultado = null;
		String consulta = "";
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_OBTENER_UMBRALES.SP_LISTARLIM(?,?,?,?,?,?,?,?,?,?,?)}";

			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codtest);
			cstmt.setString(2, varbl);
			cstmt.setString(3, codest);
			cstmt.setString(4, detPer);
			cstmt.setString(5, nomPer);
			cstmt.setString(6, detHor);
			cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
//			cstmt.setFetchSize(1000);
			resultado = cstmt.executeQuery();
			
//			LOGGER.info(resultado);
//			LOGGER.info("V_MIN: "+cstmt.getObject(4));
//			LOGGER.info("V_MAX: "+ cstmt.getObject(8));
			if (cstmt.getObject(7) == null) { valmi1 = "SD"; } else { valmi1 = cstmt.getObject(7).toString(); }
			if (cstmt.getObject(8) == null) { valma1 = "SD"; } else { valma1 = cstmt.getObject(8).toString(); }
			if (cstmt.getObject(9) == null) { valmi2 = "SD"; } else { valmi2 = cstmt.getObject(9).toString(); }
			if (cstmt.getObject(10) == null) { valma2 = "SD"; } else { valma2 = cstmt.getObject(10).toString(); }
			
			umbralBean = new UmbralBean();
			umbralBean.setValmin1(valmi1);
			umbralBean.setValmax1(valma1);
			umbralBean.setValmin2(valmi2);
			umbralBean.setValmax2(valma2);
			umbralBean.setEstado(cstmt.getObject(11).toString());
			
			cstmt.close();
			resultado.close();
			
		} catch (Exception e) {
			LOGGER.error("Error UmbralDaoImpl.obtenerUmbrales " + e.getMessage());
		} finally {
			cerrarConexionDS();
			
		}
		return umbralBean;
	}
	
	public UmbralBean validarExistencia(String codtest, String varbl, String codest, String detPer, String detHora) throws SQLException {
		UmbralBean umbralBean = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String consulta = "";
		try {
			abrirConexionDS();
//			consulta = "SELECT COUNT(*) AS CANTIDAD FROM UMBRAL T1 "
//					+ "INNER JOIN SISCTRLCAL.SEMAP_ESTA T2 ON T1.ESTACION_ID=T2.V_COD_ESTA "
//					+ "INNER JOIN SISCTRLCAL.VARIABLE_TEST T3 ON T1.VARIABLE_TEST_ID=T3.ID "
//					+ "INNER JOIN SISCTRLCAL.DADTBP_VARIABLE T4 ON T3.DADTBP_VARIABLE_QC_ID=T4.V_COD_VAR "
//					+ "INNER JOIN SISCTRLCAL.TEST T5 ON T5.ID=T3.TEST_ID "
//					+ "WHERE T2.V_COD_ESTA='"+codest+"' AND T4.V_NOM_VAR='"+varbl+"' AND T5.COD_TEST='"+codtest+"'";
//			
//			stmt = ds.createStatement();
//			stmt.setFetchSize(1000);
//			rs = stmt.executeQuery(consulta);
//			while (rs.next()) {
//				umbralBean = new UmbralBean();
//				umbralBean.setCantidad(rs.getInt("CANTIDAD"));
//			}
			
			consulta = "{call SISCTRLCAL.PKG_OBTENER_UMBRALES.SP_VALIDAREXIST(?,?,?,?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codtest);
			cstmt.setString(2, varbl);
			cstmt.setString(3, codest);
			cstmt.setString(4, detPer);
			cstmt.setString(5, detHora);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
//			cstmt.setFetchSize(1000);
			rs = cstmt.executeQuery();
			
			umbralBean = new UmbralBean();
			umbralBean.setCantidad(Integer.parseInt(cstmt.getObject(6).toString()));
			
		} catch (Exception e) {
			LOGGER.error("Error UmbralDaoImpl.validarExistencia " + e.getMessage());
		} finally {
			cerrarConexionDS();
			cstmt.close();
			rs.close();
		}
		return umbralBean;
	}
}
