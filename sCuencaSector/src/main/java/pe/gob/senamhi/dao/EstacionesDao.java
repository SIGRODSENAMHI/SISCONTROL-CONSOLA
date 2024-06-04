package pe.gob.senamhi.dao;

import java.util.List;

import pe.gob.senamhi.bean.EstacionesBean;

public interface EstacionesDao {

	List<EstacionesBean> listaEstaciones() throws Exception;
}
