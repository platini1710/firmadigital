package cl.fonasa;

import java.util.StringTokenizer;

public class test {
	private   int privado = 1; 
    int paquete = 2;  
    protected int protegido = 3;
    public    int publico = 4;

    private void metodoPrivado() {
        System.out.println("Soy un método privado de la clase ClaseDelPaqueteAcceso.");
    }
    void metodoDePaquete() { 
        System.out.println("Soy un método de paquete de la clase ClaseDelPaqueteAcceso.");
    }
    protected void metodoProtegido() {
        System.out.println("Soy un método protegido de la clase ClaseDelPaqueteAcceso.");
    }
    public void metodoPublico() {
        System.out.println("Soy un método público de la clase ClaseDelPaqueteAcceso.");
}

    public static void main(String[] args) {
    	String nombre="Angel Franco García";
    	StringTokenizer tokens=new StringTokenizer(nombre);
    	while(tokens.hasMoreTokens()){
                System.out.println(3%2);
            }
    }
}
