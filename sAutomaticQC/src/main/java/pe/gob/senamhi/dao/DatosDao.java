package pe.gob.senamhi.dao;


import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.DatosFHBean;
import pe.gob.senamhi.bean.FlagBean;
import pe.gob.senamhi.bean.ParamObtBean;
import pe.gob.senamhi.bean.ValidarDatoBean;

public interface DatosDao {

	public ParamObtBean obtEstTrabo(String codesta, String ip, String nameApp, String nameHilo, String fecHora);
	
	public DatosBean obtDatosVar(String nvar, String codesta, String fecha, String hora);
	
	public boolean regDatoTrabajada(String codesta, String fecha, String hora);
	
	public ValidarDatoBean obtEstadoEst(String codesta, String fecHora);
	
	public DatosBean listaDatosHorarios(String nvar, String codesta, String fechaHora, Integer horAtr);
	
	public DatosBean calculoDesviacion(String nvar, String codesta, String fechaHora, Integer horAtr);
	
	public DatosBean listaDatosCuartiles(String nvar, String codesta, String fechaHora, Integer horAtr);
	
	public DatosBean calculoMedia(String nvar, String codesta, String fechaHora, Integer horAtr);
	
	public FlagBean obtCodigoFlag(String nameFuncion, String nomvar, String numRegla);
	
	public FlagBean obtCodigoFlagGeneral(String nameFuncion, String nomvar);
	
	public DatosBean sum24Horas(String nvar, String codesta, String fechaHora, Integer horAtr);
	
	
	public int validarEstado(String appIp, String nameApp) throws Exception; // String fechaHora, 
	
	public boolean errorExterno(String appIP, String appName);
	
//	Validaciones para capturar datos de las ultimas 24 horas con estado 0
	
	public DatosFHBean obtFHMinMax(String fechaHora);
	
	public DatosFHBean obtFHProcesar(String fecNumber);
	
//	public boolean regTambo(String codesta, String fecha, String hora, Double param1, Double param2, Double param3, Double param4, Double param5, Double param6, Double param7, Double param8, Double param9,
//			Double param10, Double param11, Double param12, Double param13, Double param14, Double param15, Double param16, Double param17, Double param18, Double param19, Double param20, Double param21,
//			Double param22, Double param23, Double param24, Double param25, Double param26,String fechora, Double param27, Double param28, Double param29, Double param30, Double param31, Double param32, Double param33,Double param34,
//			Double param35, Double param36, Double param37, Double param38, Double param39, Double param40, Double param41, String estado);
	
	
}
