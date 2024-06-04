package pe.gob.senamhi.bean;


public class UmbralBean {

	private Integer idumb;
	private Integer cantidad;
	private String valmin1;
	private String valmin2;
	private String valmax1;
	private String valmax2;
	private String estado;
	
	public UmbralBean() {
		// TODO Auto-generated constructor stub
	}

	public UmbralBean(Integer idumb, Integer cantidad, String valmin1, String valmin2, String valmax1, String valmax2,
			String estado) {
		super();
		this.idumb = idumb;
		this.cantidad = cantidad;
		this.valmin1 = valmin1;
		this.valmin2 = valmin2;
		this.valmax1 = valmax1;
		this.valmax2 = valmax2;
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "UmbralBean [idumb=" + idumb + ", cantidad=" + cantidad + ", valmin1=" + valmin1 + ", valmin2=" + valmin2
				+ ", valmax1=" + valmax1 + ", valmax2=" + valmax2 + ", estado=" + estado + "]";
	}

	public Integer getIdumb() {
		return idumb;
	}

	public void setIdumb(Integer idumb) {
		this.idumb = idumb;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getValmin1() {
		return valmin1;
	}

	public void setValmin1(String valmin1) {
		this.valmin1 = valmin1;
	}

	public String getValmin2() {
		return valmin2;
	}

	public void setValmin2(String valmin2) {
		this.valmin2 = valmin2;
	}

	public String getValmax1() {
		return valmax1;
	}

	public void setValmax1(String valmax1) {
		this.valmax1 = valmax1;
	}

	public String getValmax2() {
		return valmax2;
	}

	public void setValmax2(String valmax2) {
		this.valmax2 = valmax2;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	
	
}
