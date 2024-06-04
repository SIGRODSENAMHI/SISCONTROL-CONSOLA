package pe.gob.senamhi.dao;

import java.util.List;

import pe.gob.senamhi.bean.EstacionesBean;

public interface EstacionesDao {

	public List<EstacionesBean> listarEstaciones(String fecHora, String grupoEsta) throws Exception;
}
