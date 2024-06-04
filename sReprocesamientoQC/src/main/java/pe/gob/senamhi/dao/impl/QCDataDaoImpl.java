package pe.gob.senamhi.dao.impl;

import java.sql.CallableStatement;

import org.apache.log4j.Logger;

import pe.gob.senamhi.dao.QCDataDao;
import pe.gob.senamhi.database.AccesoDB;

public class QCDataDaoImpl extends AccesoDB implements QCDataDao{
	private static final Logger LOGGER = Logger.getLogger(QCDataDaoImpl.class);
	
	public boolean registrar(String idusu, String nameapp, String namehilo, String ip, String motivo, String codest,
			String codvar, Double dato, String flagold, String flagnew,String fechareg, String horareg) {
		boolean procesoOk;
		CallableStatement cstmt;
		String sql = "";
		try {
			abrirConexionDS();
			sql = "{CALL SISCTRLCAL.PKG_REGISTRAR_DATA_QC.SP_REGISTRAR(?,?,?,?,?,?,?,?,?,?,?,?)}";
			cstmt = ds.prepareCall(sql);
			cstmt.setString(1, idusu);
			cstmt.setString(2, nameapp);
			cstmt.setString(3, namehilo);
			cstmt.setString(4, ip);
			cstmt.setString(5, motivo);
			cstmt.setString(6, codest);
			cstmt.setString(7, codvar);
			cstmt.setDouble(8, dato);
			cstmt.setString(9, flagold);
			cstmt.setString(10, flagnew);
//			cstmt.setArray(10, (Array) flagnew);
			cstmt.setString(11, fechareg);
			cstmt.setString(12, horareg);
			
			cstmt.executeQuery();
			procesoOk = true;
			cstmt.close();
		} catch (Exception e) {
			LOGGER.error("Error QCDataDaoImpl.registrar " + e);
			procesoOk = false;
		} finally {
			cerrarConexionDS();
		}
		
		return procesoOk;
	}

	public boolean completarDatos(String nameApp, String nameHilo, String nameIP, String codEsta, String fecha,
			String hora) {
		boolean procesoOk;
		CallableStatement cstmt;
		String sql = "";
		try {
			abrirConexionDS();
			sql = "{CALL SISCTRLCAL.PKG_QUALITY_CONTROL.SP_COMP_DATOS_TRABAJ(?,?,?,?,?,?)}";
			cstmt = ds.prepareCall(sql);
			cstmt.setString(1, nameApp);
			cstmt.setString(2, nameHilo);
			cstmt.setString(3, nameIP);
			cstmt.setString(4, codEsta);
			cstmt.setString(5, fecha);
			cstmt.setString(6, hora);
			
			cstmt.executeQuery();
			procesoOk = true;
			cstmt.close();
		} catch (Exception e) {
			LOGGER.error("Error QCDataDaoImpl.completarDatos " + e);
			procesoOk = false;
		} finally {
			cerrarConexionDS();
		}
		
		return procesoOk;
	}

	
}
