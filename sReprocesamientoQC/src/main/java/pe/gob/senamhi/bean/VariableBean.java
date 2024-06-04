package pe.gob.senamhi.bean;

public class VariableBean {

	private Integer fila;
	private Integer idvar;
	private String nomvar;
	private String desvar;
	private String abrevar;
	
	public VariableBean() {
		// TODO Auto-generated constructor stub
	}

	public VariableBean(Integer fila, Integer idvar, String nomvar, String desvar, String abrevar) {
		super();
		this.fila = fila;
		this.idvar = idvar;
		this.nomvar = nomvar;
		this.desvar = desvar;
		this.abrevar = abrevar;
	}

	@Override
	public String toString() {
		return "VariableBean [fila=" + fila + ", idvar=" + idvar + ", nomvar=" + nomvar + ", desvar=" + desvar
				+ ", abrevar=" + abrevar + "]";
	}

	public Integer getFila() {
		return fila;
	}

	public void setFila(Integer fila) {
		this.fila = fila;
	}

	public Integer getIdvar() {
		return idvar;
	}

	public void setIdvar(Integer idvar) {
		this.idvar = idvar;
	}

	public String getNomvar() {
		return nomvar;
	}

	public void setNomvar(String nomvar) {
		this.nomvar = nomvar;
	}

	public String getDesvar() {
		return desvar;
	}

	public void setDesvar(String desvar) {
		this.desvar = desvar;
	}

	public String getAbrevar() {
		return abrevar;
	}

	public void setAbrevar(String abrevar) {
		this.abrevar = abrevar;
	}

	
	
	
}
