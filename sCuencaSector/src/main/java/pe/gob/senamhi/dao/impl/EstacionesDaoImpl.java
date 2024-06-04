package pe.gob.senamhi.dao.impl;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.EstacionesBean;
import pe.gob.senamhi.dao.EstacionesDao;
import pe.gob.senamhi.database.AccesoDB;

public class EstacionesDaoImpl extends AccesoDB implements EstacionesDao{

	private static final Logger LOGGER = Logger.getLogger(EstacionesDaoImpl.class);
	
	public List<EstacionesBean> listaEstaciones() throws Exception {
		List<EstacionesBean> InformeEsta = new ArrayList<EstacionesBean>();
		LOGGER.info("Inicio de consulta CABECERA ESTACIONES: ");
		Statement stmt = null;
		ResultSet rs = null;
		try {
			abrirConexionDS();
			ds.setAutoCommit(false);
			String sql = "SELECT ROWNUM AS FILA,V_COD_ESTA FROM ESTA_VARIABLE";
			stmt = ds.createStatement();
			stmt.setFetchSize(1000);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				EstacionesBean objResultadoGestion = new EstacionesBean();
				objResultadoGestion.setFila(rs.getInt("FILA"));
				objResultadoGestion.setCodesta(rs.getString("V_COD_ESTA"));
				InformeEsta.add(objResultadoGestion);
			}
			LOGGER.info("Fin de consulta CABECERA ESTACIONES: ");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error EstacionesDaoImpl.listaEstaciones " + e.getMessage());
		} finally {
			cerrarConexionDS();
			stmt.close();
			rs.close();
			
		}
		return InformeEsta;
	}

}
