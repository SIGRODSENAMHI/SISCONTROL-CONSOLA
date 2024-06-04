package pe.gob.senamhi.dao;

import pe.gob.senamhi.bean.FrecuenciaBean;

public interface FrecuenciaDao {

	public FrecuenciaBean validarFrecuencia(String fecha, String hora,String codigo);
}
