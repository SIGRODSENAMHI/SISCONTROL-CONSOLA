package pe.gob.senamhi.dao.impl;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.FunctionBean;
import pe.gob.senamhi.dao.FunctionDao;
import pe.gob.senamhi.database.AccesoDB;

public class FunctionDaoImpl extends AccesoDB implements FunctionDao{

	private static final Logger LOGGER = Logger.getLogger(FunctionDaoImpl.class);
	
	public List<FunctionBean> obtNameFunction(String nvar, Integer opcion) throws Exception {
		List<FunctionBean> listaFunction = new ArrayList<FunctionBean>();
		Statement stmt = null;
		ResultSet rs = null;
		String consulta = "";
		
		try {
			
			abrirConexionDS();
			ds.setAutoCommit(false);
			consulta = "SELECT ROWNUM AS FILA, T2.NAME_FUNCION AS NAME_FUNCTION,T2.COD_TEST\r\n" + 
					"FROM SISCTRLCAL.VARIABLE_TEST T1\r\n" + 
					"INNER JOIN SISCTRLCAL.TEST T2 ON T1.TEST_ID=T2.ID\r\n" + 
					"WHERE TIPO_TEST_ID='"+opcion+"' AND T1.DADTBP_VARIABLE_QC_ID=(SELECT V_COD_VAR FROM SISCTRLCAL.DADTBP_VARIABLE WHERE V_NOM_VAR='"+nvar+"') AND T1.ESTADO='1'";
			
			stmt = ds.createStatement();
			stmt.setFetchSize(1000);
			rs = stmt.executeQuery(consulta);
			while (rs.next()) {
				FunctionBean functionBean = new FunctionBean();
				functionBean.setFila(rs.getInt("FILA"));
				functionBean.setNameFunction(rs.getString("NAME_FUNCTION"));
				functionBean.setCodigo(rs.getString("COD_TEST"));
				listaFunction.add(functionBean);
			}
			rs.close();
			
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.obtDatosVar " + e.getMessage());
		} finally {
			cerrarConexionDS();
			stmt.close();
			rs.close();
		}
		return listaFunction;
	}

}
