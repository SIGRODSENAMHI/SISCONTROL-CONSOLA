package pe.gob.senamhi.model;

import pe.gob.senamhi.dao.QuartzDao;
import pe.gob.senamhi.dao.impl.QuartzDaoImpl;
import pe.gob.senamhi.util.Constantes;

public class QuartzModel {

	public void actualizarFrecuenciaQuartz(String codigo, String frecuencia){
		QuartzDao quartzDao = new QuartzDaoImpl();
		quartzDao.actualizarFrecuenciaQuartzBean(codigo, frecuencia);
	}
	
	public void actualizarJobControlQuartz(String codigo, int jobControlEstado){
		QuartzDao quartzDao = new QuartzDaoImpl();
		quartzDao.actualizarEstadoQuartzBean(codigo, jobControlEstado);
	}
	
	public void liberarJob(String codigo){
		QuartzDao quartzDao = new QuartzDaoImpl();
		quartzDao.actualizarJobEstado(codigo, Constantes.JOB_LIBRE);
	}
	
	public void bloquearJob(String codigo){
		QuartzDao quartzDao = new QuartzDaoImpl();
		quartzDao.actualizarJobEstado(codigo, Constantes.JOB_OCUPADO);
	}
	
	public boolean isJobDisponible(String codigo){
		QuartzDao quartzDao = new QuartzDaoImpl();
		int estadoActual = quartzDao.obtJobEstado(codigo);
		return estadoActual==Constantes.JOB_LIBRE;
	}
}
