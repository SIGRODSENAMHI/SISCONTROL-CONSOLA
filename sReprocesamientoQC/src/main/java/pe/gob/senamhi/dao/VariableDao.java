package pe.gob.senamhi.dao;

import java.util.List;

import pe.gob.senamhi.bean.ListaVarBean;
import pe.gob.senamhi.bean.VariableBean;

public interface VariableDao {

	public List<VariableBean> listarVariables(String codEsta) throws Exception;
	
	public ListaVarBean listaVarAsociados(String variable);
	
	
}
