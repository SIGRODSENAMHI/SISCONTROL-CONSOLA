package pe.gob.senamhi.dao.impl;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.ListaVarBean;
import pe.gob.senamhi.bean.VariableBean;
import pe.gob.senamhi.dao.VariableDao;
import pe.gob.senamhi.database.AccesoDB;
import pe.gob.senamhi.util.ValidarVariableUtil;

public class VariableDaoImpl extends AccesoDB implements VariableDao{

	private static final Logger LOGGER = Logger.getLogger(VariableDaoImpl.class);
	private Statement stmt = null;
	private ResultSet rs = null;

	public List<VariableBean> listarVariables(String codEsta) throws Exception {
		List<VariableBean> InformeVar = new ArrayList<VariableBean>();
		LOGGER.info("Inicio de consulta VARIABLES");

		String varDinamica;

		try {
			abrirConexionDS();
			ds.setAutoCommit(false);

			varDinamica = ValidarVariableUtil.obtenerNombreVariable(codEsta);

			String sql = "SELECT ROWNUM AS FILA,T2.V_COD_VAR,T2.V_DESC_VAR,T2.V_ABREV_VAR,\r\n" +
					"CASE WHEN T2.V_NOM_VAR <> 'N_NIVELAGUA' THEN T2.V_NOM_VAR ELSE '" + varDinamica + "' END V_NOM_VAR\r\n" +
					"FROM SISCTRLCAL.DADTBP_VARIABLE_QC T1\r\n" +
					"INNER JOIN SISCTRLCAL.DADTBP_VARIABLE T2 ON T1.ID=T2.V_COD_VAR\r\n" +
					"WHERE T1.ID_VAR_GLOBAL IS NULL";

			stmt = ds.createStatement();
			stmt.setFetchSize(100);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				VariableBean objResultadoGestion = new VariableBean();
				objResultadoGestion.setFila(rs.getInt("FILA"));
				objResultadoGestion.setIdvar(rs.getInt("V_COD_VAR"));
				objResultadoGestion.setNomvar(rs.getString("V_NOM_VAR"));
				objResultadoGestion.setDesvar(rs.getString("V_DESC_VAR"));
				objResultadoGestion.setAbrevar(rs.getString("V_ABREV_VAR"));
				InformeVar.add(objResultadoGestion);
			}
			LOGGER.info("Fin de consulta VARIABLES");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error VariableDaoImpl.listarVariables " + e.getMessage());
		} finally {
			cerrarConexionDS();
			stmt.close();
			rs.close();

		}
		return InformeVar;
	}

	public ListaVarBean listaVarAsociados(String variable) {
		ListaVarBean listaVarBean = null;
		String consulta = "";
		try {
			abrirConexionDS();
			consulta = "SELECT '"+variable+"'||','||S1.LISTA_VAR AS LISTA FROM\r\n" +
					"(SELECT LISTAGG(T1.V_NOM_VAR, ',') WITHIN GROUP (ORDER BY T1.V_NOM_VAR) over (partition by T2.ID_VAR_GLOBAL) LISTA_VAR\r\n" +
					"FROM SISCTRLCAL.DADTBP_VARIABLE T1 \r\n" +
					"INNER JOIN SISCTRLCAL.DADTBP_VARIABLE_QC T2 ON T1.V_COD_VAR=T2.ID \r\n" +
					"WHERE T2.ID_VAR_GLOBAL=(SELECT V_COD_VAR FROM SISCTRLCAL.DADTBP_VARIABLE WHERE V_NOM_VAR='"+variable+"')\r\n" +
					"ORDER BY T1.V_NOM_VAR,T2.ID_VAR_GLOBAL) S1\r\n" +
					"GROUP BY LISTA_VAR";

			stmt = ds.createStatement();
			stmt.setFetchSize(1000);
			rs = stmt.executeQuery(consulta);
			while (rs.next()) {
				listaVarBean = new ListaVarBean();
				listaVarBean.setLista(rs.getString("LISTA"));
			}

		} catch (Exception e) {
			LOGGER.error("Error ListaVarBean.listaVarAsociados " + e.getMessage());
		} finally {
			try {
				stmt.close();
				rs.close();
				cerrarConexionDS();
			} catch (Exception e2) {
				LOGGER.error("Error ListaVarBean.listaVarAsociados " + e2.getMessage());
			}

		}
		return listaVarBean;
	}



}
