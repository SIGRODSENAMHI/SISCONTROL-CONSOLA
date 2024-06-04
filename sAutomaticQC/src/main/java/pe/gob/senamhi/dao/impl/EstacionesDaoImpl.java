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
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public List<EstacionesBean> listarEstaciones(String fecHora, String grupoEsta) throws Exception {
		List<EstacionesBean> InformeEsta = new ArrayList<EstacionesBean>();
		LOGGER.info("Inicio de consulta CABECERA ESTACIONES: ");
		try {
			abrirConexionDS();
			ds.setAutoCommit(false);
			String sql = "SELECT ROWNUM AS FILA,T1.V_COD_ESTA\r\n" + 
					"FROM SISCTRLCAL.SEMAP_ESTA T1 \r\n" + 
					"INNER JOIN SISCTRLCAL.ESTA_VARIABLE T2 ON T1.V_COD_ESTA=T2.V_COD_ESTA\r\n" + 
					"LEFT JOIN \r\n" + 
					"(SELECT * FROM SISCTRLCAL.EATBP_DATA_QCA S1 \r\n" + 
					"WHERE TO_DATE(TO_CHAR(S1.D_FECHA_REG,'DD/MM/YYYY')||' '||S1.V_HORA_REG,'DD/MM/YYYY HH24:MI:SS')=TO_DATE('"+fecHora+"','DD/MM/YYYY HH24:MI:SS')-1/24) T3 ON T2.V_COD_ESTA=T3.V_COD_ESTA\r\n" + 
					"WHERE T1.V_SUB_ESTA='A' AND T1.V_COD_COND='F' AND (T3.ESTADO_A='0' OR T3.ESTADO_A IS NULL) AND T2.V_GRUPO_PROCESO='"+grupoEsta+"'"; // 
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
			LOGGER.error("Error EstacionesDaoImpl.listarEstaciones " + e.getMessage());
		} finally {
			stmt.close();
			rs.close();
			cerrarConexionDS();
		}
		return InformeEsta;
	}
	
}
