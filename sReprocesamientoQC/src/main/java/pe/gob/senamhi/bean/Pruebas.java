package pe.gob.senamhi.bean;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.FunctionDao;
import pe.gob.senamhi.dao.QCDataDao;
import pe.gob.senamhi.dao.UmbralDao;
import pe.gob.senamhi.dao.VariableDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.dao.impl.FunctionDaoImpl;
import pe.gob.senamhi.dao.impl.QCDataDaoImpl;
import pe.gob.senamhi.dao.impl.UmbralDaoImpl;
import pe.gob.senamhi.dao.impl.VariableDaoImpl;
import pe.gob.senamhi.threads.ProcesoVariableThread;
import pe.gob.senamhi.util.Algoritmos;
import pe.gob.senamhi.util.EnviaEmailThread;
import pe.gob.senamhi.util.FechaHoraUtil;
import pe.gob.senamhi.util.PropiedadesUtil;
import pe.gob.senamhi.util.Util;

public class Pruebas {
	private static UmbralDao umbral = new UmbralDaoImpl();
	private static Double Linf1 = 0.00;
	private static Double Lsup1 = 0.00;
	private static Double Linf2 = 0.00;
	private static Double Lsup2 = 0.00;
	private static QCDataDao regData = new QCDataDaoImpl();
	private static VariableDao varList = new VariableDaoImpl();
	private static FunctionDao nameFunt = new FunctionDaoImpl();
	private static Algoritmos ecu = new Algoritmos();
	private static String lvariables = "N_AIRTEMP,N_MAXAT,N_MINAT";
	private static String codesta = "47E1E358";
	private static DatosDao datosDao = new DatosDaoImpl();
	private static DatosBean datosBean = null;
	private static FlagBean flagBean = null;;
	private static DatosFHBean datosFHBean = null;
	private static List<FunctionBean> totalFunctions = null;
	private static String FIni = "";
	private static String FFin = "";
	private static String Fec_Pasar = "";
	final static String correoDestino = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos");

	public static void main(String[] args) {
		String dat2 = "";
		List<String> flag = new ArrayList<String>();
		
//		String cod = "M0000001, M0110302";
//		if (cod.equals("M0000001, M0110302")) {
//			System.out.println("TRUE");
//		} else {
//			System.out.println("FALSE");
//		}
		
//		int vlm = Util.contarArrayOfString(cod);
//		System.out.println(vlm);
//		
//		String[] dato = cod.split(", ");
//
//		String dat1 = dato[0];
//		
//		if (dato.length == 2) {
//			dat2 = dato[1];
//		}
//		
//
//		flag.add(dat1);
//		if (!dat2.equals("")) {
//			flag.add(dat2);
//		}
//		
		datosBean = datosDao.listaDatosHorarios("N_AIRTEMP", "472501F4", "05/05/2020 09:00:00",1);
		System.out.println(datosBean.getDath());
//		
//		String cod2 = flag.toString();
//		System.out.println(cod2 + " -- " + flag.size());
		
		
//		new EnviaEmailThread("Prueba de envio de correos SISCONTROL",correoDestino).start();
		
//		String fecha = nameFunt.obtFechaStringBD();
//		String hora = nameFunt.obtHoraStringBD();
//		System.out.println(fecha);
//		System.out.println(hora);
//		Double val;
//		val = (double) 24;
//		System.out.println(val);
//		
//		DecimalFormat formateador = new DecimalFormat("####.0");
//
//		// Esto sale en pantalla con dos decimales, es decir, 3,43
//		System.out.println (formateador.format (val));
//		
//		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
//		simbolos.setDecimalSeparator('.');
//		DecimalFormat formateador1 = new DecimalFormat("####0.0",simbolos);
//
//		// Esto sale en pantalla con punto decimal, es decir, 3.4324,
//		System.out.println (Double.parseDouble("24"));
		
//		long millisI = System.currentTimeMillis();
//		java.sql.Date dateI = new java.sql.Date(millisI);
//		String fecha = FechaHoraUtil.obtHoraStringSinfecha(dateI);
//		System.out.println(fecha);
//		UmbralBean umbralBean= umbral.obtenerUmbrales("LNR", "N_HUMEDAD", "472B2370","OCTUBRE","MENSUAL HORARIO","14:00:00");  //472DF5D6   
//		System.out.println(umbralBean.getEstado());
//		String number = "11,25";
//		System.out.println(Double.parseDouble(number.replace(",", ".")));
		
//		System.out.println(Util.convertToDouble("11,25"));
		
//		if (!umbralBean.getEstado().equals("SD")) {
//			Linf1 = Util.convertToDouble(umbralBean.getValmin1());
//			Lsup1 = Util.convertToDouble(umbralBean.getValmax1());
//			System.out.println(Linf1);
//			System.out.println(Lsup1);
////			System.out.println(umbralBean.getValmin1());
//		}else {
//			System.out.println("No se encontro umbrales para la variable");
//		}
//		
//		String mes = Util.obtenerMes();
//		System.out.println(mes);
		
//		pruebas de las funciones migradas a procedures
//		datosBean = datosDao.obtDatosVar("N_NIVELAGUA", "47E1763A", "25/10/2019", "15:00:00");
//		System.out.println("FN1: "+datosBean.getDato1());
//		datosBean = datosDao.listaDatosHorarios("N_AIRTEMP", "47280292", "31/01/2020 08:00:00", 2);
//		datosBean = datosDao.listaDatosHorarios("N_AIRTEMP", "47280292", "31/01/2020 08:00:00", 2);
//		if (datosBean != null) {
//			System.out.println("FN2: "+datosBean.getCodesta());
//			System.out.println("FN2: "+datosBean.getDath());
//		} else {
//			System.out.println("error, dato null");
//		}
		
//		String flagPrueba =  ecu.ConsistenciaTemporalPaso_N_HUMEDAD("N_HUMEDAD", "31/01/2020", "08:00:00", "47280292", 80.0);
//		System.out.println(flagPrueba);
//		
//		String flagPrueba1 =  ecu.ConsistenciaTemporalPersistencia_N_HUMEDAD("N_HUMEDAD", "01/02/2020", "07:00:00", "47280292", 84.0);
//		System.out.println("FLAG: " + flagPrueba1);
//		datosBean = datosDao.calculoDesviacion("N_NIVELAGUA", "47E1763A", "25/10/2019 15:00:00", 5);
//		System.out.println("FN3: "+datosBean.getCodesta());
//		System.out.println("FN3: "+datosBean.getDesv());
//		
//		datosBean = datosDao.listaDatosCuartiles("N_NIVELAGUA", "47E1763A", "25/10/2019 15:00:00", 5);
//		System.out.println("FN4: "+datosBean.getCodesta());
//		System.out.println("FN4: "+datosBean.getQ1());
//		System.out.println("FN4: "+datosBean.getQ2());
//		System.out.println("FN4: "+datosBean.getQ3());
//		System.out.println("FN4: "+datosBean.getRic());
//		
//		datosBean = datosDao.calculoMedia("N_NIVELAGUA", "47E1763A", "25/10/2019 15:00:00", 5);
//		System.out.println("FN5: "+datosBean.getCodesta());
//		System.out.println("FN5: "+datosBean.getMedia());
		
//		try {
//			totalFunctions = nameFunt.obtNameFunction("N_LLUVIA",2);
//			System.out.println("Lista " + totalFunctions.size());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		flagBean = datosDao.obtCodigoFlag("LimitesNacional", "N_LLUVIA","R2");
//		System.out.println("FN6: "+flagBean.getCodFlag());
//		
//		try {
//			umbralBean = umbral.validarExistencia("LNR","N_HUMEDAD","47E1763A","NOVIEMBRE","11:00:00");
//			System.out.println(umbralBean.getCantidad());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		flagBean = datosDao.obtCodigoFlagGeneral("ConsistenciaTemporalPaso", "N_NIVELAGUA");
//		System.out.println("FN7: "+flagBean.getCodFlagEsta());
//		
//		
//		datosBean = datosDao.sum24Horas("N_NIVELAGUA", "47E1763A", "25/10/2019 15:00:00", 23);
//		System.out.println("FN8: "+datosBean.getCodesta());
//		System.out.println("FN8: "+datosBean.getSum24h());
//		
//		datosFHBean = datosDao.obtFHMinMax("25/10/2019 15:00:00");
//		System.out.println("FN9: "+datosFHBean.getFecMin());
//		System.out.println("FN9: "+datosFHBean.getFecMax());
//		
//		datosFHBean = datosDao.obtFHProcesar("20191025150000");
//		System.out.println("FN10: "+datosFHBean.getFecIncrem());
//		System.out.println("FN10: "+datosFHBean.getFecHora());
		
//		if (datosDao.regTambo("114121","2019-11-02","01:20:08",7.7,7.7,19.0,3.8,92.0,81.0,100.0,29.0,1.6,4.2,5.5,53.0,44.0,50.0,638.0,638.5,634.0,0.0,6.0,349.0,281.0,518.0,0.3,1.5,3.4,14.4,"2019-10-22 11:30:07",-999.0,-999.0,-999.0,-999.0, -999.0,-999.0,-999.0,-999.0,-999.0,-999.0,-999.0,-999.0,-999.0,-999.0,-999.0,"H")) {
//			System.out.println("Registrado correctamente!");
//		} else {
//			System.out.println("Ocurrio un error!");
//		} Error de Estacion: 4729867C 07:00:00 DICIEMBRE MENSUAL HORARIO
//		UmbralBean umbralBean;
//		try {
//			umbralBean = umbral.validarExistencia("LNE", "N_HUMEDAD", "47280292","FEBRERO","07:00:00");
//			if (umbralBean != null) {
//				Integer cant = umbralBean.getCantidad();
//				System.out.println(cant);
//			}else {
//				System.out.println("No se encontro umbrales para la variable");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		umbralBean= umbral.obtenerUmbrales("LNE", "N_HUMEDAD", "47280292","FEBRERO", "MENSUAL HORARIO","07:00:00");
//		
//		if (umbralBean != null) {
//			Linf1 = Util.convertToDouble(umbralBean.getValmin1());
//			Linf2 = Util.convertToDouble(umbralBean.getValmin2());
//			Lsup1 = Util.convertToDouble(umbralBean.getValmax1());
//			Lsup2 = Util.convertToDouble(umbralBean.getValmax2());
//			System.out.println("L_INF2: " + Linf2);
//			System.out.println("L_INF1: " + Linf1);
//			System.out.println("L_SUP1: " + Lsup1);
//			System.out.println("L_SUP2: " + Lsup2);
//		}
//		
//		String vfec1 = "01/02/2020";
//		String[] valor = vfec1.split("/");
//		Integer mesCap = Integer.parseInt(valor[1]);
//		Integer ms = mesCap-1;
//		String mes = Util.obtenerMes(ms);
//		System.out.println(mes);
		
//		String flagTemp = ProcesoVariableThread.evalRegEstaNacional("N_HUMEDAD", "01/02/2020", "07:00:00", "47280292", 84.0);
		
//		flagBean = datosDao.obtCodigoFlag("LimitesEstacion", "N_HUMEDAD", "R2");
//		String codflag = flagBean.getCodFlag();
//		
//		System.out.println("D"+codflag);
//		
//		String cod = ecu.LimitesEstacion("N_HUMEDAD", "01/01/2020", "07:00:00", "47280292", 60.0);
////		String cod = ecu.LimitesRegional("N_AIRTEMP", "30/01/2020", "14:00:00", "47280292", 15.4);
//		System.out.println(cod);
//		
//		String cod1 = ecu.ConsistenciaTemporalPaso("N_HUMEDAD", "01/01/2020", "07:00:00", "47280292", 60.0);
//		String cod2 = ecu.ConsistenciaTemporalPersistencia("N_HUMEDAD", "01/01/2020", "07:00:00", "47280292", 60.0);
//		System.out.println(cod1);
//		System.out.println(cod2);
//		System.out.println(Math.E);
		
//		if (regData.registrar(1, "sys-control", "HILO: 1", "127.0.0.1", "", "4720D05C", "N_AIRTEMP", 24.4, "", "B","","")) {
//			System.out.println("Registrado correctamente");
//		}else {
//			System.out.println("Error: ");
//		}
		
//		ArrayList<String> flag = new ArrayList<String>(); 
//
//		for (int x=0;x<10;x++) {
//			flag.add("hola");
//		  	System.out.println(flag);
//		}
//		
//		String hora = "07:00:00";
//		Integer valor = Integer.parseInt(hora.split(":")[0]);
//		System.out.println(valor+"primero");
//		if (valor < 7) {
//			System.out.println(valor+"hola");
//		} else {
//			System.out.println(valor);
//		}
		
		
//		prueba = conPaso.listaDatosTA(lvariables, codesta);
//		System.out.println(prueba.getCodesta()+" MIN: "+prueba.getDatmin()+" MAX: "+prueba.getDatmax()+" MAX24H: "+prueba.getDat1h()+" MIN24H: "+prueba.getDat2h());
//		
		
		
//		 try {
//
//	            URL url = new URL("http://idesep.senamhi.gob.pe/geoserver/g_semap_esta_ctrl_calidad/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=g_semap_esta_ctrl_calidad:view_semap_esta&maxFeatures=50&outputFormat=application%2Fjson&viewparams=qlon:-75.28361;qlat:-11.95028;radio_metros:5000");//your url i.e fetch data from .
//	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	            conn.setRequestMethod("GET");
//	            conn.setRequestProperty("Accept", "application/json");
//	            if (conn.getResponseCode() != 200) {
//	                throw new RuntimeException("Failed : HTTP Error code : "
//	                        + conn.getResponseCode());
//	            }
//	            InputStreamReader in = new InputStreamReader(conn.getInputStream());
//	            BufferedReader br = new BufferedReader(in);
//	            String output;
//	            while ((output = br.readLine()) != null) {
//	                System.out.println(output);
//	            }
//	            conn.disconnect();
//
//	        } catch (Exception e) {
//	            System.out.println("Exception in NetClientGet:- " + e);
//	        }
		
//		new EnviaEmailThread("Prueba de envio 2", "oti13@senamhi.gob.pe").start();
//		char[] chr={'A','C','D'};
//		char[] aleatorio=new char[10];
// 
//		for(int i=0;i<=9;i++){
//			aleatorio[i]=chr[(int)(Math.random()*3)];
//			System.out.print(aleatorio[i]);//solo muestra el arreglo
//		}
		
//		Random aleatorio = new Random();
//		String alfa = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		String cadena = "";
//		int numero;
//		int forma;
//		forma = (int)(aleatorio.nextDouble()*alfa.length()-1+0);
//		numero = (int)(aleatorio.nextDouble()*999*100);
//		cadena = cadena+alfa.charAt(forma)+numero;
//		
//		System.out.println("CLAVE: " + cadena);
		

//		int contador = 0 ;
//        InputStreamReader leer = new InputStreamReader(System.in);
//        BufferedReader br = new BufferedReader(leer);
//        System.out.print("Escriba cuantas cadenas quiere generar: ");
//        int numero1 = Integer.parseInt(br.readLine());  
//        while(contador < numero1)
//        {
//        
//               String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZQ";
//              String cadena = "";
//             int longitudCadena = (int) Math.floor(Math.random()*20+1);  //Se genera aleatoriamente la longitud de la cadena actual
//              for (int x = 0; x < longitudCadena; x++)
//              {
//                  int caracter = (int) Math.floor(Math.random()*27); //Generamos la cadena
//                  cadena = cadena + letras.charAt(caracter);
//              }
//              System.out.println(cadena);
//          }
		
//		String clave = PasswordGenerator.getPassword(
//				PasswordGenerator.MINUSCULAS+
//				PasswordGenerator.MAYUSCULAS+
//				PasswordGenerator.ESPECIALES,8);
//		System.out.println(clave);
		
//		List<Double> miArrayList = new ArrayList<Double>();
//		HashMap<String, Double> myArray = new HashMap<String, Double>();
//		String cadena = miArrayList.toString();
//		Integer num = cadena.length();
//		
//		myArray.put("name", 12.4);
//		myArray.put("dato", 12.4);
//		
//		String horas = "";
//		long millisI = System.currentTimeMillis();
//		java.sql.Date dateI = new java.sql.Date(millisI);
//		String fecha = FechaHoraUtil.obtFechaStringSinhora(dateI);
//		String dHora = FechaHoraUtil.obtHoraStringSinfecha(dateI);
//		String hora = dHora.substring(0, 2);
//		Integer tam = hora.toString().length();
//		horas = fecha + " " + hora + ":00:00";
//		if (tam == 2) {
//			horas = hora+":00:00";
//		} else {
//			horas = "0"+hora+":00:00";
//		}
//		if (regData.completarDatos("qc-automatic-v1", "HILO: 1", "172.25.0.240", "4724966C","12/08/2019","15:00:00")) {
//			System.out.println("Registrado correctamente!");
//		}else {
//			System.out.println("Registro sin exito!: ");
//		}
//		String flag;
//		prueba = conPaso.listaDatosTA("N_AIRTEMP", "4729B3E6","12/08/2019 16:00:00",3);
//		Double dath3 = 0.00; ConsistenciaTemporalPaso LimitesNacional  ConsistenciaTemporalPersistencia
//		String cod = ecu.LimitesDuros("N_LLUVIA", "21/08/2019", "15:00:00", "4727C11E", 0.0);
//		if (cod.equals("M")) {
//			System.out.println(cod);
//		} else {
//			System.out.println("B");
//		}
		
//		frecBean = frec.validarFrecuencia("23/08/2019", "16:00:00","00001");
//		String valdat = frecBean.getMensaje();
//		System.out.println("ESTADO DE QUARTZ: "+valdat);
//		if (valdat.equals("1")) {
//			System.out.println("JOB DISPONIBLE - ESPERANDO PARA INICIAR ...");
//		} else {
//			System.out.println("JOB OCUPADO");
//		}

//		if (regData.registrar(1, "qc-automatic-v1", "HILO: 1", "172.25.0.240", null, "4724E0FC", "N_HUMEDAD", 100.0, null, "B, DFF","12/08/2019","13:00:00")) {
//			System.out.println(cod);
//		}
//		System.out.println(dHora);
//		frecBean = frec.validarFrecuencia(fecha, dHora,"00001");
//		String valdat = frecBean.getMensaje();
//		System.out.println(valdat);
		
		
		
		
//		String fecHora = "13/09/2019 11:00:00";
//		datosFHBean = datosDao.obtFHMinMax(fecHora);
//		FIni = datosFHBean.getFecMin();
//		FFin = datosFHBean.getFecMax();
//		System.out.println(FIni + " - " + FFin);
//		
//		if (FIni != FFin) {
//			System.out.println("Diferente");
//		} else {
//			System.out.println("Igual");
//		}
////		Inicio del while
//		while (!FIni.equals(FFin)) {
//			datosFHBean = datosDao.obtFHProcesar(FIni);
//			FIni = datosFHBean.getFecIncrem();
//			Fec_Pasar = datosFHBean.getFecHora();
//			System.out.println("FECHA INICIO: " + FIni + "FECHA FIN: " + FFin);
//		}
		
//		String datos[] = fecHora.split(" ");
//		String hora1 = datos[1];
//		System.out.println(hora1);
		
//		String [] pais = {"Spain", "ES", "ESP", "724", "Yes"};
//
//		String archCSV = "D:\\ISO-Codes.csv";
//		CSVWriter writer = new CSVWriter(new FileWriter(archCSV));
//
//		writer.writeNext(pais);
//
//		writer.close();
		
//		List<UsuarioBean> usuarios = new ArrayList<UsuarioBean>();
//        
//        usuarios.add(new UsuarioBean("1001","Jose","Ramirez Torres","jramirez89@hotmail.com"));
//        usuarios.add(new UsuarioBean("1002","Saul","Gaviria Garcia","sgaviria12@gmail.com"));
//        usuarios.add(new UsuarioBean("1003","Maria","Torres Mendoza","mtorres12@yahoo.com"));
//         
//        String outputFile = "D:\\usuarios_export.csv";
//        boolean alreadyExists = new File(outputFile).exists();
//         
//        if(alreadyExists){
//            File ficheroUsuarios = new File(outputFile);
//            ficheroUsuarios.delete();
//        }        
//         
//        try {
//         
//            CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
//             
//            csvOutput.write("Codigo");
//            csvOutput.write("Nombres");
//            csvOutput.write("Apellidos");
//            csvOutput.write("Correo");
//            csvOutput.endRecord();
//             
//            for(UsuarioBean us : usuarios){
//                 
//                csvOutput.write(us.getCodigo());
//                csvOutput.write(us.getNombres());
//                csvOutput.write(us.getApellidos());
//                csvOutput.write(us.getCorreo());
//                csvOutput.endRecord();                   
//            }
//             
//            csvOutput.close();
//         
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		
//		System.out.println(Util.capturarIP());
		
	}
}
