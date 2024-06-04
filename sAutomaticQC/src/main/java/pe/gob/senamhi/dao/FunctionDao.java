package pe.gob.senamhi.dao;

import java.util.List;

import pe.gob.senamhi.bean.FunctionBean;

public interface FunctionDao {

	public List<FunctionBean> obtNameFunction(String nvar, Integer opcion) throws Exception;
	
	public String obtFechaStringBD();
	
	public String obtHoraStringBD();
}
