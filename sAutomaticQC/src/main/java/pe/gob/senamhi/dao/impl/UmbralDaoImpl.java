package pe.gob.senamhi.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import pe.gob.senamhi.bean.UmbralBean;
import pe.gob.senamhi.dao.UmbralDao;
import pe.gob.senamhi.database.AccesoDB;

public class UmbralDaoImpl extends AccesoDB implements UmbralDao{

	private static final Logger LOGGER = Logger.getLogger(UmbralDaoImpl.class);
	private CallableStatement cstmt = null;
	private ResultSet rs = null;
	private String valmi1, valmi2, valma1, valma2;
	String[] datos = new String[4];
	
	public UmbralBean obtenerUmbrales(String codtest, String varbl, String codest, String detPer, String nomPer, String detHor) {
		UmbralBean umbralBean = null;
		
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
			rs = cstmt.executeQuery();
			
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
			
			
			
		} catch (Exception e) {
			LOGGER.error("Error UmbralDaoImpl.obtenerUmbrales " + e.getMessage());
			LOGGER.error("Error de Estacion: " + codest + detHor + detPer + nomPer);
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error UmbralDaoImpl.obtenerUmbrales " + e2.getMessage());
			}
			
			
		}
		return umbralBean;
	}
	
	public UmbralBean validarExistencia(String codtest, String varbl, String codest, String detPer, String detHora) throws SQLException {
		UmbralBean umbralBean = null;
		String consulta = "";
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_OBTENER_UMBRALES.SP_VALIDAREXIST(?,?,?,?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codtest);
			cstmt.setString(2, varbl);
			cstmt.setString(3, codest);
			cstmt.setString(4, detPer);
			cstmt.setString(5, detHora);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
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
