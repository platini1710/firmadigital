package cl.fonasa.dto;

import java.io.Serializable;

public class DocumentSign implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private String codigo;
	private String mensaje;


	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
