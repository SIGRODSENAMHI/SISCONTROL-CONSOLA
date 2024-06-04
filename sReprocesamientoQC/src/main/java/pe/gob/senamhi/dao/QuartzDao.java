package pe.gob.senamhi.dao;

import pe.gob.senamhi.bean.QuartzBean;

public interface QuartzDao {

	QuartzBean obtQuartzBeanByCodigo(String codigo);
	void actualizarQuartzBean(QuartzBean quartzBean);
	void actualizarEstadoQuartzBean(String codigo, int jobControlEstado);
	void actualizarFrecuenciaQuartzBean(String codigo, String frecuencia);
	void actualizarJobEstado(String codigo, int estado);
	int obtJobEstado(String codigo);
	
}
