package pe.gob.senamhi.bean;

public class UsuarioBean {

	private String codigo;
	private String nombres;
	private String apellidos;
	private String correo;
	
	public UsuarioBean() {
		// TODO Auto-generated constructor stub
	}

	public UsuarioBean(String codigo, String nombres, String apellidos, String correo) {
		super();
		this.codigo = codigo;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.correo = correo;
	}

	@Override
	public String toString() {
		return "UsuarioBean [codigo=" + codigo + ", nombres=" + nombres + ", apellidos=" + apellidos + ", correo="
				+ correo + "]";
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
}
