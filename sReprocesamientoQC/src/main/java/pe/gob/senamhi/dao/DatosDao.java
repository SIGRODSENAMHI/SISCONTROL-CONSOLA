package pe.gob.senamhi.dao;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.DatosFHBean;
import pe.gob.senamhi.bean.FlagBean;
import pe.gob.senamhi.bean.ParamObtBean;
import pe.gob.senamhi.bean.ValidarDatoBean;

public interface DatosDao {

	public ParamObtBean obtEstTrabo(String ip, String nameApp, String nameHilo);
	
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
	
	public int regDisponibles() throws Exception;
	
	public int validarEstado(String appIp, String nameApp) throws Exception;
	
	public boolean errorExterno(String appIP, String appName);
	
//	Validaciones para capturar datos de las ultimas 24 horas con estado 0
	
	public DatosFHBean obtFHMinMax();
	
	public DatosFHBean obtFHProcesar(String fecNumber);
}
