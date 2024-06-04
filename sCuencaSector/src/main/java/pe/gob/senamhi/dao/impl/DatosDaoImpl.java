package pe.gob.senamhi.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.database.AccesoDB;

public class DatosDaoImpl extends AccesoDB implements DatosDao{

	private static final Logger LOGGER = Logger.getLogger(DatosDaoImpl.class);
	
	public DatosBean datosEstacion(String codesta) throws Exception {
		DatosBean infoEstaciones = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			abrirConexionDS();
			ds.setAutoCommit(false);
			String sql = "SELECT T1.V_COD_ESTA,T2.N_LAT_SIG,T2.N_LON_SIG FROM ESTA_VARIABLE T1\r\n" + 
					"INNER JOIN SEMAP_ESTA T2 ON T1.V_COD_ESTA=T2.V_COD_ESTA AND T1.V_COD_ESTA='"+codesta+"'";
			stmt = ds.createStatement();
			stmt.setFetchSize(1000);
			rs = stmt.executeQuery(sql);
			infoEstaciones = new DatosBean();
			while (rs.next()) {
				infoEstaciones.setCodesta(rs.getString("V_COD_ESTA"));
				infoEstaciones.setLatsig(rs.getDouble("N_LAT_SIG"));
				infoEstaciones.setLonsig(rs.getDouble("N_LON_SIG"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error DatosDaoImpl.datosEstacion " + e.getMessage());
		} finally {
			cerrarConexionDS();
			stmt.close();
			rs.close();
			
		}
		return infoEstaciones;
	}

	public boolean registrarDatos(String codEsta, String codCuenca, String nomCuenca, String secClimt,
			String codClimt) {
		
		boolean procesoOk;
		CallableStatement cstmt;
		String consulta = "";
		try {
			abrirConexionDS();
			consulta = "{call SISCTRLCAL.PKG_CUENCA_SECTORES.SP_ACTUZALIZAR(?,?,?,?,?)}";
			cstmt = ds.prepareCall(consulta);
			cstmt.setString(1, codEsta);
			cstmt.setString(2, codCuenca);
			cstmt.setString(3, nomCuenca);
			cstmt.setString(4, codClimt);
			cstmt.setString(5, secClimt);
			cstmt.executeQuery();
			procesoOk = true;
			cstmt.close();
		} catch (Exception e) {
			LOGGER.error("Error DatosDaoImpl.registrarDatos " + e.getMessage());
			procesoOk = false;
		} finally {
			cerrarConexionDS();
		}
		
		return procesoOk;
		
	}

}
