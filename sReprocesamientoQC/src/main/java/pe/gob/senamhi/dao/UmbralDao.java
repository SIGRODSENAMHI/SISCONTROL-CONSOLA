package pe.gob.senamhi.dao;

import pe.gob.senamhi.bean.UmbralBean;

public interface UmbralDao {
	
	public UmbralBean validarExistencia(String codtest, String varbl, String codest, String detPer, String detHora) throws Exception;
	public UmbralBean obtenerUmbrales(String codtest, String varbl, String codest, String detPer, String nomPer, String detHor) ;
	
}
