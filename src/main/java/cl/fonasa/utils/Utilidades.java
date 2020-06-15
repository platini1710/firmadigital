package cl.fonasa.utils;

public class Utilidades {
    String caracteres = "";

    public String retornaAleatorios() {
        char[] chr = {'A', 'B', 'C', 'D', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        char[] aleatorio = new char[25];
        for (int i = 0; i <= 14; i++) {
            aleatorio[i] = chr[(int) (Math.random() * 59)];
            caracteres=caracteres+aleatorio[i];
        }
        return caracteres;
    }
}
