package pe.gob.senamhi.threads;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.EstacionesBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.quartzjob.QCAutomaticJob;
import pe.gob.senamhi.util.PropiedadesUtil;

public class ControlCalidadThread extends Thread{
	
	final String appname = PropiedadesUtil.obtenerPropiedad("configuracion", "app.name");
	final String correoDestino = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");
	final String host = PropiedadesUtil.obtenerPropiedad("configuracion", "servicio.name.host");
	
	private static final Logger LOGGER = Logger.getLogger(QCAutomaticJob.class);
	
	private DatosDao datDao = new DatosDaoImpl();
	private DatosBean datEstaciones = null;
	private Double lonSig;
	private Double latSig;
	private String codEsta;
	
	private String output;
	private String datos;
	private JsonArray array1;
	private String lista1;
	private String lista2;
	private JsonParser parser = new JsonParser();
	private JsonObject json1;
	private JsonObject json2;
	private JsonObject json3;
	
	private String codCuenca;
	private String nomCuenca;
	private String nomClimt;
	private String codClimt;
	private String regClimt;
	
	private List<EstacionesBean> listaEsta;
	private Integer pIni;
	private Integer pFin;
	

	public ControlCalidadThread(List<EstacionesBean> listaEsta, Integer pIni, Integer pFin) {
		this.listaEsta = listaEsta;
		this.pIni = pIni;
		this.pFin = pFin;
	}
	
	@Override
	public void run() {
		try {
			
			for (int i = pIni; i < pFin; i++) {
				
				codEsta = listaEsta.get(i).getCodesta();
				datEstaciones = datDao.datosEstacion(codEsta);
				lonSig = datEstaciones.getLonsig();
				latSig = datEstaciones.getLatsig();
				
				try {

			        URL url = new URL("https://idesep.senamhi.gob.pe/geoserver/g_semap_esta_ctrl_calidad/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=g_semap_esta_ctrl_calidad:view_semap_esta&maxFeatures=5&outputFormat=application%2Fjson&viewparams=qlon:"+lonSig+";qlat:"+latSig+";radio_metros:0");
			        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			        conn.setRequestMethod("GET");
			        conn.setRequestProperty("Accept", "application/json");
			        if (conn.getResponseCode() != 200) {
			            throw new RuntimeException("Failed : HTTP Error code : "
			                    + conn.getResponseCode());
			        }
			        
			        InputStreamReader in = new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8);
			        BufferedReader br = new BufferedReader(in);
			        
			        while ((output = br.readLine()) != null) {
			        	datos = output;
			        }
			        
			        
		        	json1 = (JsonObject) parser.parse(datos);
		        	
		        	array1 = json1.get("features").getAsJsonArray();
		        	lista1 = array1.get(0).toString();
		        	
		        	json2 = (JsonObject) parser.parse(lista1);
		        	
		        	lista2 = json2.get("properties").toString();
		        	
		        	json3 = (JsonObject) parser.parse(lista2);
		        	
		        	codCuenca = extraerCaracteres(json3.get("codigo_cuenca").toString());
		        	nomCuenca = extraerCaracteres(json3.get("nombre_cuenca").toString());
		        	nomClimt = extraerCaracteres(json3.get("sector_climatico").toString());
		        	codClimt = extraerCaracteres(json3.get("cod_sector_climatico").toString());
		        	regClimt = extraerCaracteres(json3.get("cod_region_climatico").toString());
		        	
			        conn.disconnect();

			    } catch (Exception e) {
			    	LOGGER.info("Exception in NetClientGet:- " + e);
			    }
				
				if (datDao.registrarDatos(codEsta, codCuenca, nomCuenca, nomClimt, codClimt)) {
					LOGGER.info("Registrado Correctamente");
				} else {
					LOGGER.error("Ocurrio un error al intentar registrar");
				}
				
			}
				
			
		} catch (Exception e) {
			LOGGER.info("Error " + e.getMessage());

		}
	}
	
	public String extraerCaracteres(String caracter) {
		
    	String extraerp = caracter.substring(0,1); 
    	String extraeru = caracter.substring(caracter.length()-1); 
    	String remplazado=caracter.replace(extraerp,""); 
    	String remplazadofinal=remplazado.replace(extraeru, "");
    	
    	return remplazadofinal;
	}
	
}
