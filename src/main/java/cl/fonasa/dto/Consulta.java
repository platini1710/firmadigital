package cl.fonasa.dto;

import java.io.Serializable;

public class Consulta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private	String ord;
private	String folio;
private	String tipo;
private	String company;
private	String de;
private	String para;
private	String jefe;
private String msg;
public String getOrd() {
	return ord;
}
public void setOrd(String ord) {
	this.ord = ord;
}
public String getFolio() {
	return folio;
}
public void setFolio(String folio) {
	this.folio = folio;
}
public String getTipo() {
	return tipo;
}
public void setTipo(String tipo) {
	this.tipo = tipo;
}
public String getCompany() {
	return company;
}
public void setCompany(String company) {
	this.company = company;
}
public String getDe() {
	return de;
}
public void setDe(String de) {
	this.de = de;
}
public String getPara() {
	return para;
}
public void setPara(String para) {
	this.para = para;
}
public String getJefe() {
	return jefe;
}
public void setJefe(String jefe) {
	this.jefe = jefe;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}




}
