package cl.fonasa.dto;

import java.io.Serializable;

public class Solicitud implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idCaso;
	private String runUsuarioEjecuta;
	private String  nombreSolicitante;
	private String  nombreAfectado;
	private String  nombreTipificacion;
	private String   problemaDeSalud;
	private String   email;
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
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

}
