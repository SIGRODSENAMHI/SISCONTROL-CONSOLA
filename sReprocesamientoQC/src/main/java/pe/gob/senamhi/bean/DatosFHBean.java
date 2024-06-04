package pe.gob.senamhi.bean;

public class DatosFHBean {

	private String fecMin;
	private String fecMax;
	private String fecIncrem;
	private String fecHora;
	
	public DatosFHBean() {
		// TODO Auto-generated constructor stub
	}

	public DatosFHBean(String fecMin, String fecMax, String fecIncrem, String fecHora) {
		super();
		this.fecMin = fecMin;
		this.fecMax = fecMax;
		this.fecIncrem = fecIncrem;
		this.fecHora = fecHora;
	}

	@Override
	public String toString() {
		return "DatosFHBean [fecMin=" + fecMin + ", fecMax=" + fecMax + ", fecIncrem=" + fecIncrem + ", fecHora="
				+ fecHora + "]";
	}

	public String getFecMin() {
		return fecMin;
	}

	public void setFecMin(String fecMin) {
		this.fecMin = fecMin;
	}

	public String getFecMax() {
		return fecMax;
	}

	public void setFecMax(String fecMax) {
		this.fecMax = fecMax;
	}

	public String getFecIncrem() {
		return fecIncrem;
	}

	public void setFecIncrem(String fecIncrem) {
		this.fecIncrem = fecIncrem;
	}

	public String getFecHora() {
		return fecHora;
	}

	public void setFecHora(String fecHora) {
		this.fecHora = fecHora;
	}

	
}
