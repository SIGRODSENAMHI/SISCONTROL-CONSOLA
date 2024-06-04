package pe.gob.senamhi.enums;

import org.quartz.Job;

import pe.gob.senamhi.bean.QuartzBean;
import pe.gob.senamhi.dao.QuartzDao;
import pe.gob.senamhi.dao.impl.QuartzDaoImpl;
import pe.gob.senamhi.quartzjob.QCAutomaticJob;
import pe.gob.senamhi.util.PropiedadesUtil;

public enum QuartzJobEnum {
	
	QC_AUTOMATIC_JOB(PropiedadesUtil.obtenerPropiedad("configuracion", "codigo.proceso.atumatic.qc"), QCAutomaticJob.class);
	
	private final String codigo;
    private final Class<? extends Job> job;
    
	private QuartzJobEnum(String codigo, Class<? extends Job> job) {
		this.codigo = codigo;
		this.job = job;
	}
	
	public String codigo() { return codigo; }
	public Class<? extends Job> job() { return job; }
	
	public QuartzBean getQuartzBean(){
		QuartzDao quartzDao = new QuartzDaoImpl();
		QuartzBean quartzBean = quartzDao.obtQuartzBeanByCodigo(codigo);		
		return quartzBean;
	}

}

