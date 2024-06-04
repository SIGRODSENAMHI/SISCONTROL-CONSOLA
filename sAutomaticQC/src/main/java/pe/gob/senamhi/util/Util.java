package pe.gob.senamhi.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class Util {

	private static final Logger LOGGER = Logger.getLogger(Util.class);
	private static String mes;
	static String MES[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SETIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
	
	
	public static void copyFile(File srcFile, File destDir) {
		if (srcFile.exists()) {
			try {
				FileUtils.copyFileToDirectory(srcFile, destDir);
				LOGGER.info("Se Copió");
			} catch (Exception e) {
				LOGGER.error("No puso descargar archivo" + srcFile.getName() + " " + e.getMessage());
			}
			

		} else {
			LOGGER.info("No existe la imagen");
		}
	}

	public static void crearDirectorio(File path) {
		if (path.mkdirs()) {
			LOGGER.info("Se creó el directorio");
		} else {
			LOGGER.info("Existe el directorio");
		}

	}

	public static void crearArchivo(Path path) {
		try {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			LOGGER.info("El fichero fue creado");
		} catch (Exception e) {
			LOGGER.error("Error, el fichero existe. " + e.getMessage());
		}
	}

	public static void crearCarpeta(Path path) {
		try {
			Files.createDirectories(path.getParent());
			// Files.createFile(path);
			LOGGER.info("El fichero fue creado");
		} catch (Exception e) {
			LOGGER.error("Error, el fichero existe. " + e.getMessage());
		}
	}

	public static void addText(String path, String to_apped) {
		try {
			FileWriter fw = new FileWriter(path, true);
			PrintWriter writer = new PrintWriter(fw);
			writer.append(to_apped);
			writer.append("\r\n");
			writer.close();
			fw.close();
			//LOGGER.info("Se escribió correctamente en el texto.");
		} catch (Exception e) {
			LOGGER.error("Error añadiendo datos al fichero." + e.getMessage(), e);
		}
	}
	
	
	public static boolean isEntero(String cadena) throws Exception {
		try{
			Integer.parseInt(cadena.trim());
			return true;
		} catch(Exception e){
			return false;
		}
		
	}
	
	public static boolean isDouble(String cadena) throws Exception {
		try{
			Double.parseDouble(cadena.trim());
			return true;
		} catch(Exception e){
			return false;
		}
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
	
	public static void eliminarArchivo(File archivo) {
		if (archivo.exists()) {
			archivo.delete();
			LOGGER.info("Se elimino con exito");
		}
	}
	
	public static String capturarIP() {
		String hostname = "senamhi-sis-ctrl";
		String ip = "";
		try {
			InetAddress ipaddress = InetAddress.getByName(hostname);
		    ip = ipaddress.getHostAddress();
		} catch (Exception e) {
			LOGGER.error("No se encontro la IP");
			e.printStackTrace();
		}
		return ip;
	}
	
	public static String capturarIP_COPIA() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			LOGGER.error("No se encontro la IP");
			e.printStackTrace();
		}
		return ip;
	}
	
	public static Double CapturarPesoArchivo(Float longitud) {
		DecimalFormat df = new DecimalFormat("#.00");
		Double peso = 0.00;
		/*if(longitud>1073741824) // 1073741824  1024000000
			peso = df.format(longitud/1073741824) + " Gb";
		else if(longitud>1048576) //1048576 1024000
			peso = df.format(longitud/1048576) + " Mb";
		else if(longitud>1024)
			peso = df.format(longitud/1024) + " Kb";
		else
			peso = df.format(longitud) + " bytes";*/
		
		peso = Double.parseDouble(df.format(longitud/1024));
		
		
		return peso;
	}
	
	public static Double MediaA(Integer n) {
		Double media = 0.00;
		
		return media;
	}
	
	
	public static String obtenerMes() {
		Calendar c1 = Calendar.getInstance();
		mes = MES[c1.get(Calendar.MONTH)];
//	   System.out.println("MES: " + c1.get(Calendar.MONTH));
	   
	   return mes;
	}
	
	public static String obtenerMesxFecha(Integer numMes) {
		
		mes = MES[numMes];
		   
		return mes;
	}
	
	public static double convertToDouble(String temp){
		String a = temp;
	    String s = a.replaceAll(",",".").trim();          
	    String f = s.replaceAll(" ", ""); 
	    double result = Double.parseDouble(f); 
	    return result;
	}
	
	public static int contarArrayOfString(String texto) {
		int valor = 0;
		String dato1 = "";
		String dato2 = "";
		List<String> flag = new ArrayList<String>();
		
		String[] dato = texto.split(", ");

		dato1 = dato[0];
		
		if (dato.length == 2) {
			dato2 = dato[1];
		}
		
		flag.add(dato1);
		
		if (!dato2.equals("")) {
			flag.add(dato2);
		}
		
		valor = flag.size();
		
		return valor;
	}

}
