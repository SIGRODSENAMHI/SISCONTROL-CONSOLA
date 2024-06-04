package pe.gob.senamhi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FechaHoraUtil {

	private static SimpleDateFormat simpleDateFormat;

	public static Date obtenerFechaLong(long lastModified) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lastModified);		
		return cal.getTime();
	}
	
    public static String obtenerFechaFormato(String formato){
    	SimpleDateFormat dateformat = new SimpleDateFormat(formato);
    	GregorianCalendar calendario = new GregorianCalendar();
    	String fecha = dateformat.format(calendario.getTime());
    	return fecha;
    }
	    
    public static Date agregarDias(Date fecha,int dia){
 	   Calendar cal = new GregorianCalendar();
 	   cal.setLenient(false);
 	   cal.setTime(fecha);
 	   //cal.set(cal.get(Calendar.YEAR)+ano,cal.get( Calendar.MONTH)+mes,cal.get( Calendar.DAY_OF_MONTH)+dia);
 	   cal.add(Calendar.DAY_OF_MONTH, dia);
 	   return cal.getTime();
 	   }
    
    public static Date restarDias(Date fecha,int dia){
  	   Calendar cal = new GregorianCalendar();
  	   cal.setLenient(false);
  	   cal.setTime(fecha);
  	   //cal.set(cal.get(Calendar.YEAR)+ano,cal.get( Calendar.MONTH)+mes,cal.get( Calendar.DAY_OF_MONTH)+dia);
  	   cal.add(Calendar.DAY_OF_MONTH, -dia);	   
  	   return cal.getTime();
  	   }
    
    public static Date restarDiasHoraCero(Date fecha,int dia){
  	   Calendar cal = new GregorianCalendar();
  	   cal.setLenient(false);
  	   cal.setTime(fecha);
  	   //cal.set(cal.get(Calendar.YEAR)+ano,cal.get( Calendar.MONTH)+mes,cal.get( Calendar.DAY_OF_MONTH)+dia);
  	   cal.add(Calendar.DAY_OF_MONTH, -dia);
  	   cal.set(Calendar.HOUR_OF_DAY, 0);
  	   cal.set(Calendar.MINUTE, 0);
  	   cal.set(Calendar.SECOND, 0);
  	   cal.set(Calendar.MILLISECOND, 0);
  	   return cal.getTime();
  	   }
    
///////////////////////////////////////////////12/08/2013 jllanos
		
	public static Date sumarFechasHoras(Date fch, int horas){
		Calendar ahora = new GregorianCalendar();
		ahora.setTimeInMillis(fch.getTime());
		ahora.add(Calendar.HOUR, 24);
		return new Date(ahora.getTimeInMillis());
	}
	
	//Retorna Ultimo dia del Mes
	public static Date getUltimoDiaDelMes(int anio,int mes) {
		Calendar FechaFinal = new GregorianCalendar(anio, mes,0);
		FechaFinal.set(FechaFinal.get(Calendar.YEAR),
		FechaFinal.get(Calendar.MONTH),
		FechaFinal.getActualMaximum(Calendar.DAY_OF_MONTH),
		FechaFinal.getMaximum(Calendar.HOUR_OF_DAY),
		FechaFinal.getMaximum(Calendar.MINUTE),
		FechaFinal.getMaximum(Calendar.SECOND));		
		System.out.println(FechaFinal.getTime());
		return FechaFinal.getTime();
	}
	
	//Retorna Primer dia del Mes
	public static Date getPrimerDiaDelMes(int anio,int mes) {
		Calendar FechaFinal = new GregorianCalendar(anio, mes,0);
		FechaFinal.set(FechaFinal.get(Calendar.YEAR),
		FechaFinal.get(Calendar.MONTH),
		FechaFinal.getActualMinimum(Calendar.DAY_OF_MONTH),
		FechaFinal.getMinimum(Calendar.HOUR_OF_DAY),
		FechaFinal.getMinimum(Calendar.MINUTE),
		FechaFinal.getMinimum(Calendar.SECOND));		
		System.out.println(FechaFinal.getTime());
		return FechaFinal.getTime();
	}
	    
	/**26/08/2013
     * VALIDA LA FECHA HORA Y OBTIENE DATE TIME
     * @param fechaString
     * @return
     * @throws Exception
     */
	public static Date ObtFormatFechaHora2(String fechaString) throws Exception{
		Date fecha = new Date();
		String fFec;
		try {

			Calendar cal = Calendar.getInstance();
			fecha = cal.getTime();
			DateFormat formatoFecha;
			if (fechaString.trim().length() == 10) {
				throw new Exception("ES NECESARIO INGRESAR LA HORA");
			} else if (fechaString.trim().equals(null)
					|| fechaString.trim().equals("")) {
				throw new Exception("ES NECESARIO INGRESAR LA FECHA");
			} else {
				fFec = "dd/MM/yyyy HH:mm";
				formatoFecha = new SimpleDateFormat(fFec);
				fecha = formatoFecha.parse(fechaString.trim());
			}

		} catch (Exception e) {
			System.out.println("Error fecha : " + e.getMessage());
			throw new Exception("Error en fecha : " + e.getMessage());
		}
		return fecha;
	}

	
	/**
	 * Transforma un Date en String "01/01/2013 12:02:00"
	 * 
	 * @param fecha
	 * @return stringFecha
	 */
	public static String obtFechaStringConhora(Date fecha) {
		String fechaString = "";
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
		if (fecha != null) {
			fechaString = formatter.format(fecha);
		}
		return fechaString;
	}
	
	public static String obtFechaStringSinhora(Date fecha) {
		String fechaString = "";
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		if (fecha != null) {
			fechaString = formatter.format(fecha);
		}
		return fechaString;
		
	}
	
	
	public static String obtHoraStringSinfecha(Date fecha) {
		String horaString = "";
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		if (fecha != null) {
			horaString = formatter.format(fecha);
		}
		return horaString;
	}
	
	public static double obtDiferenciaDeFechasDouble(Date fecIni, Date fecFin) {
		int segundos = 0;
		try {
			segundos = Integer.parseInt(""+ ((fecFin.getTime() - fecIni.getTime()) / 1000));	
		} catch (Exception e) {
			System.out.println(fecIni + "\t" + fecFin);
			e.printStackTrace();
		}
		
		double minutos1 = segundos / 60 * 1.0;
		double horas = minutos1 / 60 * 1.0;
		double tiempo = Util.round(horas, 2);
		return tiempo;
	}
	
	public static String fechaActualString(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
		Calendar calendar = Calendar.getInstance();
		String fechaString = dateFormat.format(calendar.getTime());
		return fechaString;
	}
	
	/**
	 * Fecha del dia de hoy en formato ISO yyyymmdd
	 * */
	public static String fechaHoyISO(){
		simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = Calendar.getInstance().getTime();
		return simpleDateFormat.format(date);
	}
	
	public static String getDateFormat(String format){
		Date date = new Date(); 
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
		return dt1.format(date);
	}
	
	public static String carpetaFechaISO2(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
	
	public static String obtenerHoraISO(){
        DateFormat dateFormat = new SimpleDateFormat("HHmm");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
	
	public static String fechaAnioMes(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
    
	public static String fechaDia(){
        DateFormat dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
	
	public static String mesDesc(String mes){
        if(mes.equals("01")){
            return "ENE";
        }else if(mes.equals("02")){
            return "FEB";
        }else if(mes.equals("03")){
            return "MAR";
        }else if(mes.equals("04")){
            return "ABR";
        }else if(mes.equals("05")){
            return "MAY";
        }else if(mes.equals("06")){
            return "JUN";
        }else if(mes.equals("07")){
            return "JUL";
        }else if(mes.equals("08")){
            return "AGO";
        }else if(mes.equals("09")){
            return "SET";
        }else if(mes.equals("10")){
            return "OCT";
        }else if(mes.equals("11")){
            return "NOV";
        }else if(mes.equals("12")){
            return "DIC";
        }else{
            return "NNN";
        }
    }
}
