package pe.gob.senamhi.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import pe.gob.senamhi.bean.FrecuenciaBean;
import pe.gob.senamhi.dao.FrecuenciaDao;
import pe.gob.senamhi.database.AccesoDB;

public class FrecuenciaDaoImpl extends AccesoDB implements FrecuenciaDao{

	private static final Logger LOGGER = Logger.getLogger(FrecuenciaDaoImpl.class);
	private CallableStatement cstmt = null;
	private ResultSet rs = null;
	
	public FrecuenciaBean validarFrecuencia(String fecha, String hora, String codigo) {
		FrecuenciaBean frecuenciaBean = null;
		String consulta = "";
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_FRECUENCIA.SP_ACTUALIZAR_EST_QUARTZ(?,?,?,?,?)}";

			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, fecha);
			cstmt.setString(2, hora);
			cstmt.setString(3, codigo);
			cstmt.setString(4, "SISCONTROL");
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			rs = cstmt.executeQuery();

			frecuenciaBean = new FrecuenciaBean();
			frecuenciaBean.setMensaje(cstmt.getObject(5).toString());

		} catch (Exception e) {
			frecuenciaBean = null;
			LOGGER.error("Error FrecuenciaDaoImpl.validarFrecuencia " + e.getMessage() );
		} finally {
			try {
				cstmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error FrecuenciaDaoImpl.validarFrecuencia " + e2.getMessage() );
			}
			
		}
		return frecuenciaBean;
	}

}
