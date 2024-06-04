package pe.gob.senamhi.bean;

public class QuartzBean {

	private String codigo;
	private String className;
	private String enumName;
	private String descripcion;
	private String frecuencia;
	private String jobId;
	private String triggerId;
	private String logFilePath;
	private int estado;
	private int jobControl;
	private String appPath;
	
	public QuartzBean() {
		super();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getEnumName() {
		return enumName;
	}

	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getJobControl() {
		return jobControl;
	}

	public void setJobControl(int jobControl) {
		this.jobControl = jobControl;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	@Override
	public String toString() {
		return "QuartzBean [codigo=" + codigo + ", className=" + className
				+ ", enumName=" + enumName + ", descripcion=" + descripcion
				+ ", frecuencia=" + frecuencia + ", jobId=" + jobId
				+ ", triggerId=" + triggerId + ", logFilePath=" + logFilePath
				+ ", estado=" + estado + ", jobControl=" + jobControl
				+ ", appPath=" + appPath + "]";
	}
	
}
