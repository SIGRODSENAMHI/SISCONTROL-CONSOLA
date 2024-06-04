package pe.gob.senamhi.dao;


import pe.gob.senamhi.bean.DatosBean;

public interface DatosDao {

	DatosBean datosEstacion(String codesta) throws Exception;
	
	public boolean registrarDatos(String codEsta, String codCuenca, String nomCuenca, String secClimt, String codClimt);
}
