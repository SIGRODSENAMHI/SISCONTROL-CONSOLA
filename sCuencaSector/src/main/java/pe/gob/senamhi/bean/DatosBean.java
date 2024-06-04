package pe.gob.senamhi.bean;

public class DatosBean {

	private String codesta;
	private Double latsig;
	private Double lonsig;
	
	public DatosBean() {
		// TODO Auto-generated constructor stub
	}

	public DatosBean(String codesta, Double latsig, Double lonsig) {
		super();
		this.codesta = codesta;
		this.latsig = latsig;
		this.lonsig = lonsig;
	}

	@Override
	public String toString() {
		return "DatosBean [codesta=" + codesta + ", latsig=" + latsig + ", lonsig=" + lonsig + "]";
	}

	public String getCodesta() {
		return codesta;
	}

	public void setCodesta(String codesta) {
		this.codesta = codesta;
	}

	public Double getLatsig() {
		return latsig;
	}

	public void setLatsig(Double latsig) {
		this.latsig = latsig;
	}

	public Double getLonsig() {
		return lonsig;
	}

	public void setLonsig(Double lonsig) {
		this.lonsig = lonsig;
	}
	
}
