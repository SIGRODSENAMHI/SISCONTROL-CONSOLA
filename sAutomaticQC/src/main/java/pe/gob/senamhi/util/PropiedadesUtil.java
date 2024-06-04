package pe.gob.senamhi.util;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public final class PropiedadesUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(PropiedadesUtil.class);
	
	private PropiedadesUtil(){
		
	}
	
	public static final String obtenerPropiedad(String archivo, String clave){
		String mensaje = "";
		try {
			//USER ESTE PASO PARA LAS PRUEBAS
			ResourceBundle rs = ResourceBundle.getBundle(archivo);
			mensaje = rs.getString(clave);
			
			//USAR ESTE PASO PARA PRODUCCION
//			Properties property;
//		    FileInputStream fs = null;
//		    fs = new FileInputStream(Constantes.ARCHIVO_CONFIGURACION);
//	        property = new Properties();
//	        /*cargamos el archivo de propiedades*/
//	        property.load(fs);
//	        mensaje = property.getProperty(clave);
//	        fs.close();
			
		} catch (Exception exception) {
			LOGGER.error("Error en obtenerPropiedad :: "+exception.getMessage());
		}
		return mensaje;
    }
	
	public static final String obtenerPropiedadJdbc(String archivo, String clave){
		String mensaje = "";
		try {
			ResourceBundle rs = ResourceBundle.getBundle(archivo);
			mensaje = rs.getString(clave);
			
		} catch (Exception exception) {
			LOGGER.error("Error en obtenerPropiedad :: "+exception.getMessage());
		}
		return mensaje;
    }

}
