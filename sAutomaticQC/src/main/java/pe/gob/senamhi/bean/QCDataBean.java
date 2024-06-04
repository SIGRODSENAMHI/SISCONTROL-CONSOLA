package pe.gob.senamhi.bean;

public class QCDataBean {

	private Integer idqc;
	private Integer idusu;
	private String nameapp;
	private String namehilo;
	private String ip;
	private String motivo;
	private String codest;
	private String codvar;
	private Double dato;
	private String fecha;
	private String hora;
	private String fechareg;
	private String horareg;
	
	public QCDataBean() {
		// TODO Auto-generated constructor stub
	}

	public QCDataBean(Integer idqc, Integer idusu, String nameapp, String namehilo, String ip, String motivo,
			String codest, String codvar, Double dato, String fecha, String hora, String fechareg, String horareg) {
		super();
		this.idqc = idqc;
		this.idusu = idusu;
		this.nameapp = nameapp;
		this.namehilo = namehilo;
		this.ip = ip;
		this.motivo = motivo;
		this.codest = codest;
		this.codvar = codvar;
		this.dato = dato;
		this.fecha = fecha;
		this.hora = hora;
		this.fechareg = fechareg;
		this.horareg = horareg;
	}

	@Override
	public String toString() {
		return "QCDataBean [idqc=" + idqc + ", idusu=" + idusu + ", nameapp=" + nameapp + ", namehilo=" + namehilo
				+ ", ip=" + ip + ", motivo=" + motivo + ", codest=" + codest + ", codvar=" + codvar + ", dato=" + dato
				+ ", fecha=" + fecha + ", hora=" + hora + ", fechareg=" + fechareg + ", horareg=" + horareg + "]";
	}

	public Integer getIdqc() {
		return idqc;
	}

	public void setIdqc(Integer idqc) {
		this.idqc = idqc;
	}

	public Integer getIdusu() {
		return idusu;
	}

	public void setIdusu(Integer idusu) {
		this.idusu = idusu;
	}

	public String getNameapp() {
		return nameapp;
	}

	public void setNameapp(String nameapp) {
		this.nameapp = nameapp;
	}

	public String getNamehilo() {
		return namehilo;
	}

	public void setNamehilo(String namehilo) {
		this.namehilo = namehilo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getCodest() {
		return codest;
	}

	public void setCodest(String codest) {
		this.codest = codest;
	}

	public String getCodvar() {
		return codvar;
	}

	public void setCodvar(String codvar) {
		this.codvar = codvar;
	}

	public Double getDato() {
		return dato;
	}

	public void setDato(Double dato) {
		this.dato = dato;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getFechareg() {
		return fechareg;
	}

	public void setFechareg(String fechareg) {
		this.fechareg = fechareg;
	}

	public String getHorareg() {
		return horareg;
	}

	public void setHorareg(String horareg) {
		this.horareg = horareg;
	}

	
}
