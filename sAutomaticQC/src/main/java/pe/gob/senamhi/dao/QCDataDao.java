package pe.gob.senamhi.dao;


public interface QCDataDao {

//	P_USUARIO,P_NAME_APP,P_NAME_HILO,P_IP,P_MOTIVO,P_COD_ESTA,P_VARIEBLE,P_DATO,P_FECHA,P_HORA
//	P_CODIGO_OLD,P_CODIGO_NEW
	public boolean registrar(String idusu, String nameapp, String namehilo, String ip, String motivo, String codest, String codvar, Double dato,String flagold,String flagnew,String fechareg, String horareg);
	
//	P_NAME_APP IN VARCHAR2,P_NAME_HILO IN VARCHAR2,P_IP IN VARCHAR2,P_COD_ESTA IN VARCHAR2,P_FECHA IN VARCHAR2,P_HORA IN VARCHAR2
	public boolean completarDatos(String nameApp, String nameHilo, String nameIP, String codEsta, String fecha, String hora);

}
