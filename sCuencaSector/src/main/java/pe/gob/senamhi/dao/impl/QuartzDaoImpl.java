package pe.gob.senamhi.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import pe.gob.senamhi.bean.QuartzBean;
import pe.gob.senamhi.dao.QuartzDao;
import pe.gob.senamhi.database.AccesoDB;

public class QuartzDaoImpl extends AccesoDB implements QuartzDao {
	private static final Logger LOGGER = Logger.getLogger(QuartzDaoImpl.class);
	String sql = null;
	
	public QuartzBean obtQuartzBeanByCodigo(String codigo) {
		QuartzBean quartzBean = null;
		sql = "SELECT CODIGO, CLASS_NAME, ENUM_NAME, DESCRIPCION, FRECUENCIA, " +
				" JOB_ID, TRIGGER_ID, LOG_FILE_PATH, ESTADO, JOB_CONTROL, APP_PATH " +
				" FROM SISCTRLCAL.quartz_control WHERE CODIGO = ?";
		try {
			getConnectionDS();
			PreparedStatement ps = ds.prepareStatement(sql);
			ps.setString(1, codigo);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				quartzBean = new QuartzBean();
				quartzBean.setCodigo(rs.getString("CODIGO"));
				quartzBean.setClassName(rs.getString("CLASS_NAME"));
				quartzBean.setEnumName(rs.getString("ENUM_NAME"));
				quartzBean.setDescripcion(rs.getString("DESCRIPCION"));
				quartzBean.setFrecuencia(rs.getString("FRECUENCIA"));
				quartzBean.setJobId(rs.getString("JOB_ID"));
				quartzBean.setTriggerId(rs.getString("TRIGGER_ID"));
				quartzBean.setLogFilePath(rs.getString("LOG_FILE_PATH"));
				quartzBean.setEstado(rs.getInt("ESTADO"));
				quartzBean.setJobControl(rs.getInt("JOB_CONTROL"));
				quartzBean.setAppPath(rs.getString("APP_PATH"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			quartzBean = null;
			LOGGER.error("Error QuartzDaoImpl.obtQuartzBeanByCodigo " + e.getMessage());
			
			e.printStackTrace();
		} finally{
			cerrarConexionDS();
		}
		return quartzBean;
	}

	public void actualizarQuartzBean(QuartzBean quartzBean) {
		sql = "UPDATE SISCTRLCAL.quartz_control " + 
				" SET " +  
				" CLASS_NAME = ?, " + 
				" ENUM_NAME = ?, " + 
				" DESCRIPCION = ?, " + 
				" FRECUENCIA = ?, " + 
				" JOB_ID = ?, " + 
				" TRIGGER_ID = ? " +  
				" WHERE CODIGO = ?";
		try {
			abrirConexionDS();
			PreparedStatement ps = ds.prepareStatement(sql);
			ps.setString(1, quartzBean.getClassName());
			ps.setString(2, quartzBean.getEnumName());
			ps.setString(3, quartzBean.getDescripcion());
			ps.setString(4, quartzBean.getFrecuencia());
			ps.setString(5, quartzBean.getJobId());
			ps.setString(6, quartzBean.getTriggerId());
			ps.setString(7, quartzBean.getCodigo());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			LOGGER.error("Error QuartzDaoImpl.actualizarQuartzBean " + e.getMessage());
			e.printStackTrace();
		} finally{
			cerrarConexionDS();
		}
	}
	
	public void actualizarFrecuenciaQuartzBean(String codigo, String frecuencia) {
		sql = "UPDATE SISCTRLCAL.quartz_control " + 
				" SET " +  
				" FRECUENCIA = ? " + 
				" WHERE CODIGO = ?";
		try {
			abrirConexionDS();
			PreparedStatement ps = ds.prepareStatement(sql);
			ps.setString(1, frecuencia);
			ps.setString(2, codigo);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			LOGGER.error("Error QuartzDaoImpl.actualizarFrecuenciaQuartzBean " + e.getMessage());
			e.printStackTrace();
		} finally{
			cerrarConexionDS();
		}
	}

	public void actualizarEstadoQuartzBean(String codigo, int jobControlEstado) {
		sql = "UPDATE SISCTRLCAL.quartz_control " + 
				" SET " +  
				" JOB_CONTROL = ? " + 
				" WHERE CODIGO = ?";
		try {
			abrirConexionDS();
			PreparedStatement ps = ds.prepareStatement(sql);
			ps.setInt(1, jobControlEstado);
			ps.setString(2, codigo);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			LOGGER.error("Error QuartzDaoImpl.actualizarEstadoQuartzBean " + e.getMessage());
			e.printStackTrace();
		} finally{
			cerrarConexionDS();
		}
	}

	public void actualizarJobEstado(String codigo, int estado) {
		sql = "UPDATE SISCTRLCAL.quartz_control " + 
				" SET " +  
				" ESTADO = ?, FECHA_UPDATE=SYSDATE " + 
				" WHERE CODIGO = ?";
		try {
			abrirConexionDS();
			PreparedStatement ps = ds.prepareStatement(sql);
			ps.setInt(1, estado);
			ps.setString(2, codigo);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			LOGGER.error("Error QuartzDaoImpl.actualizarJobEstado " + e.getMessage());
			e.printStackTrace();
		} finally{
			cerrarConexionDS();
		}
	}

	public int obtJobEstado(String codigo) {
		int estado = -1;
		sql = "SELECT ESTADO " +
				" FROM SISCTRLCAL.quartz_control WHERE CODIGO = ?";
		try {
			abrirConexionDS();
			PreparedStatement ps = ds.prepareStatement(sql);
			ps.setString(1, codigo);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				estado = rs.getInt("ESTADO");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			estado = -1;
			LOGGER.error("Error QuartzDaoImpl.obtJobEstado " + e.getMessage());
			e.printStackTrace();
		} finally{
			cerrarConexionDS();
		}
		return estado;
	}

}
