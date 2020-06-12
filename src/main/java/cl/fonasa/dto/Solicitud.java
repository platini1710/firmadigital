package cl.fonasa.dto;

import java.io.Serializable;

public class Solicitud implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long idCaso;
	private String runUsuarioEjecuta;
	private String entity;
	private String purpose;
	private String  nombreSolicitante;
	private String  nombreAfectado;
	private String  nombreTipificacion;
	private String   problemaDeSalud;
	private String   email;
	private String   respuesta;
	private String   otp;
	private int   ord;
	private String   tipo;
	private String   de;
	private String   apiToken;
	private String   path;
	public long getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(long idCaso) {
		this.idCaso = idCaso;
	}
	public String getRunUsuarioEjecuta() {
		return runUsuarioEjecuta;
	}
	public void setRunUsuarioEjecuta(String runUsuarioEjecuta) {
		this.runUsuarioEjecuta = runUsuarioEjecuta;
	}
	public String getNombreSolicitante() {
		return nombreSolicitante;
	}
	public void setNombreSolicitante(String nombreSolicitante) {
		this.nombreSolicitante = nombreSolicitante;
	}
	public String getNombreAfectado() {
		return nombreAfectado;
	}
	public void setNombreAfectado(String nombreAfectado) {
		this.nombreAfectado = nombreAfectado;
	}
	public String getNombreTipificacion() {
		return nombreTipificacion;
	}
	public void setNombreTipificacion(String nombreTipificacion) {
		this.nombreTipificacion = nombreTipificacion;
	}
	public String getProblemaDeSalud() {
		return problemaDeSalud;
	}
	public void setProblemaDeSalud(String problemaDeSalud) {
		this.problemaDeSalud = problemaDeSalud;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDe() {
		return de;
	}
	public void setDe(String de) {
		this.de = de;
	}

	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	
	
}
