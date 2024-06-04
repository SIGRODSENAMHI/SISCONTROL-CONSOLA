package pe.gob.senamhi.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Pruebas {

	public static void main(String[] args) {
		
		String output;
		String datos = null;
		JsonArray prube;
		JsonArray prube1;
		
		try {

	        URL url = new URL("http://idesep.senamhi.gob.pe/geoserver/g_semap_esta_ctrl_calidad/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=g_semap_esta_ctrl_calidad:view_semap_esta&maxFeatures=5&outputFormat=application%2Fjson&viewparams=qlon:-79.61317;qlat:-4.74767;radio_metros:0");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP Error code : "
	                    + conn.getResponseCode());
	        }
	        
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        BufferedReader br = new BufferedReader(in);
	        
//	        System.out.println(br);
	        
	        while ((output = br.readLine()) != null) {
//	            System.out.println(lon+" "+lat);
//	            prube = new ArrayList(Arrays.asList(output));
	            datos = output;
	        }
	        
	        JsonParser parser = new JsonParser();
        	JsonObject json = (JsonObject) parser.parse(datos);
        	
        	prube = json.get("features").getAsJsonArray();
        	String datEst = prube.get(0).toString();
        	
        	JsonObject json1 = (JsonObject) parser.parse(datEst);
        	
        	String datEst1 = json1.get("properties").toString();
        	
        	System.out.println(datEst1);
        	
        	JsonObject json2 = (JsonObject) parser.parse(datEst1);
        	String codCuenca = json2.get("codigo_cuenca").toString();
        	String nomCuenca = json2.get("nombre_cuenca").toString();
        	String nomClimt = json2.get("sector_climatico").toString();
        	String codClimt = json2.get("cod_sector_climatico").toString();
        	String regClimt = json2.get("cod_region_climatico").toString();
        	
        	System.out.println(codCuenca + nomCuenca + nomClimt + codClimt + regClimt);
        	
        	// crear funcion
//        	int cadena1 = datEst.length();//ubico el tamaño de la cadena
//        	String extraerp = datEst.substring(0,1); // Extraigo laprimera letra
//        	String extraeru = datEst.substring(datEst.length()-1); //Extraigo la ultima letra letra
//        	String remplazado=datEst.replace(extraerp,""); // quitamos el primer caracter
//        	String remplazadofinal=remplazado.replace(extraeru, "");// se quita el ultimo caracter
//        	
//        	System.out.println(datEst);
//        	JsonParser parser1 = new JsonParser();
//        	JsonObject json1 = (JsonObject) parser1.parse(remplazadofinal);
        	
	        
	        
//	        Gson gson = new Gson();
//			ParametroEntity[] parametroEntity = gson.fromJson(prube, ParametroEntity[].class);
//			for (ParametroEntity parametroEntitys : parametroEntity) {
//				serviceFactory.getParametroService().registrar(parametroEntitys);
//			}
	        
//	        LOGGER.info("LISTADO A DEVOLVER: " + prube);
	        
	        conn.disconnect();

	    } catch (Exception e) {
	    	System.out.println(e);
//	    	LOGGER.info("Exception in NetClientGet:- " + e);
	    }
		
		

	}

}
