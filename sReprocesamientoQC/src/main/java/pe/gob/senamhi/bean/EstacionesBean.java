package pe.gob.senamhi.bean;

public class EstacionesBean {

	private Integer fila;
	private String codesta;
	
	public EstacionesBean() {
		// TODO Auto-generated constructor stub
	}
	
	public EstacionesBean(Integer fila, String codesta) {
		super();
		this.fila = fila;
		this.codesta = codesta;
	}

	@Override
	public String toString() {
		return "EstacionesBean [fila=" + fila + ", codesta=" + codesta + "]";
	}

	public Integer getFila() {
		return fila;
	}

	public void setFila(Integer fila) {
		this.fila = fila;
	}

	public String getCodesta() {
		return codesta;
	}

	public void setCodesta(String codesta) {
		this.codesta = codesta;
	}
	
	
}
