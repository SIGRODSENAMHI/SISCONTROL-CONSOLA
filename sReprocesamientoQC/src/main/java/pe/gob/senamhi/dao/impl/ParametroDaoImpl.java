package pe.gob.senamhi.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.ParametroBean;
import pe.gob.senamhi.dao.ParametroDao;
import pe.gob.senamhi.database.AccesoDB;

public class ParametroDaoImpl extends AccesoDB implements ParametroDao{
	
	private static final Logger LOGGER = Logger.getLogger(ParametroDaoImpl.class);
	String sql = null;
	
	public ParametroBean obtParametros(String nameTest, String nameVar) {
		ParametroBean parametroBean = null;
		sql = "SELECT * FROM\r\n" + 
				"(SELECT T1.NOM_PARAMETRO,T1.N_DATO\r\n" + 
				"FROM SISCTRLCAL.PARAMETRO T1\r\n" + 
				"INNER JOIN SISCTRLCAL.VARIABLE_TEST T2 ON T1.VARIABLE_TEST_ID=T2.ID\r\n" + 
				"INNER JOIN SISCTRLCAL.TEST T3 ON T3.ID=T2.TEST_ID\r\n" + 
				"INNER JOIN SISCTRLCAL.DADTBP_VARIABLE_QC T4 ON T4.ID=T2.DADTBP_VARIABLE_QC_ID\r\n" + 
				"INNER JOIN SISCTRLCAL.DADTBP_VARIABLE T5 ON T4.ID=T5.V_COD_VAR\r\n" + 
				"WHERE T3.NAME_FUNCION=? AND T5.V_NOM_VAR=?\r\n" + 
				"ORDER BY NOM_PARAMETRO\r\n" + 
				")PIVOT\r\n" + 
				"(\r\n" + 
				"SUM(N_DATO)\r\n" + 
				"FOR NOM_PARAMETRO IN ('P1' AS P01,'P2' AS P02,'P3' AS P03,'P4' AS P04,'P5' AS P05,'P6' AS P06,'P7' AS P07,'P8' AS P08,'P9' AS P09,'P10' AS P10)\r\n" + 
				")";
		try {
			getConnectionDS();
			PreparedStatement ps = ds.prepareStatement(sql);
			ps.setString(1, nameTest);
			ps.setString(2, nameVar);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				parametroBean = new ParametroBean();
				parametroBean.setParam1(rs.getInt("P01"));
				parametroBean.setParam2(rs.getInt("P02"));
				parametroBean.setParam3(rs.getInt("P03"));
				parametroBean.setParam4(rs.getInt("P04"));
				parametroBean.setParam5(rs.getInt("P05"));
				parametroBean.setParam6(rs.getInt("P06"));
				parametroBean.setParam7(rs.getInt("P07"));
				parametroBean.setParam8(rs.getInt("P08"));
				parametroBean.setParam9(rs.getInt("P09"));
				parametroBean.setParam10(rs.getInt("P10"));
				
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			parametroBean = null;
			LOGGER.error("Error ParametroDaoImpl.obtParametros " + e.getMessage());
			e.printStackTrace();
		} finally{
			cerrarConexionDS();
		}
		return parametroBean;
	}

}
