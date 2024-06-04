package pe.gob.senamhi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import pe.gob.senamhi.util.EnviaEmailThread;
import pe.gob.senamhi.util.PropiedadesUtil;

public class AccesoDB {
	private static final Logger LOGGER = Logger.getLogger(AccesoDB.class);
	protected Connection ds;
	protected Connection tm;
	final String appName = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String correoDestino = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");

	public Connection getConnectionDS() {
		try {
			Class.forName(PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.driverClassNameDS")).newInstance();
			String dbURL = PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.db_urlDS");
			String user = PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.usernameDS");
			String pass = PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.passwordDS");
			ds = DriverManager.getConnection(dbURL, user, pass);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("No se pudo establecer conexion con DS");
			 String error = "No se pudo establecer conexion con la DB " + e.getMessage() + "<br>"; 
			 for (StackTraceElement el : e.getStackTrace()) { 
				 error +=el.toString() + appName +"<br>"; 
			 }
			 LOGGER.info(error);
			 new EnviaEmailThread(error,correoDestino).start();
		}
		return ds;
	}

	public void abrirConexionDS() {
		getConnectionDS();
	}

	public void cerrarConexionDS() {
		try {
			if (ds != null && !ds.isClosed()) {
//				Thread.sleep();
				ds.close();
			} else {
				LOGGER.info("Excepción capturada al intentar concluir...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("Error " + e.getStackTrace().toString());
		} 
	}
	
//	public Connection getConnectionTambo() {
//		try {
//			Class.forName(PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.driverClassNameTM")).newInstance();
//			String dbURL = PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.db_urlTM");
//			String user = PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.usernameTM");
//			String pass = PropiedadesUtil.obtenerPropiedadJdbc("jdbc", "jdbc.passwordTM");
//			tm = DriverManager.getConnection(dbURL, user, pass);
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			LOGGER.info("No se pudo establecer conexion con TAMBO");
//			 String error = "No se pudo establecer conexion con la DB TAMBO" + e.getMessage() + "<br>"; 
//			 for (StackTraceElement el : e.getStackTrace()) { 
//				 error +=el.toString() + appName +"<br>"; 
//			 }
//			 LOGGER.info(error);
//			 new EnviaEmailThread(error,correoDestino).start();
//		}
//		return tm;
//	}
//
//	public void abrirConexionTambo() {
//		getConnectionTambo();
//	}
//
//	public void cerrarConexionTambo() {
//		try {
//			if (tm != null && !tm.isClosed()) {
//				tm.close();
////				Thread.sleep(100);
//			} else {
//				LOGGER.info("Excepción capturada al intentar concluir TAMBO...");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			LOGGER.error("Error " + e.getStackTrace().toString());
//		} 
//	}
}
