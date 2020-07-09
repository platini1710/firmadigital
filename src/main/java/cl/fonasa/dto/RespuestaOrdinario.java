package cl.fonasa.dto;

import java.io.Serializable;

public class RespuestaOrdinario implements Serializable{
private String ordinario;
private String parrafoUno;
private String parrafoDos;
public String getOrdinario() {
	return ordinario;
}
public void setOrdinario(String ordinario) {
	this.ordinario = ordinario;
}
public String getParrafoUno() {
	return parrafoUno;
}
public void setParrafoUno(String parrafoUno) {
	this.parrafoUno = parrafoUno;
}
public String getParrafoDos() {
	return parrafoDos;
}
public void setParrafoDos(String parrafoDos) {
	this.parrafoDos = parrafoDos;
}

}
