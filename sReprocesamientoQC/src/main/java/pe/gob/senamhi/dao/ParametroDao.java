package pe.gob.senamhi.dao;

import pe.gob.senamhi.bean.ParametroBean;

public interface ParametroDao {

	ParametroBean obtParametros(String nameTest, String nameVar);
}
