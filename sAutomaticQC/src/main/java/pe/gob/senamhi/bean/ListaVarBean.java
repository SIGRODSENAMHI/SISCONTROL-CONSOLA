package pe.gob.senamhi.bean;

public class ListaVarBean {

	private String lista;
	
	public ListaVarBean() {
		// TODO Auto-generated constructor stub
	}

	public ListaVarBean(String lista) {
		super();
		this.lista = lista;
	}

	@Override
	public String toString() {
		return "ListaVarBean [lista=" + lista + "]";
	}

	public String getLista() {
		return lista;
	}

	public void setLista(String lista) {
		this.lista = lista;
	}
}
