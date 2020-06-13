package cl.fonasa;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class test3 {
	private static final Logger log = LoggerFactory.getLogger(test3.class);
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String text = "El ssss de salud Cáncer de próstata contempla garantías explícitas en salud (GES), según lo estipula el Decreto Supremo Nº 22 vigente desde el 1/10/2019. Cabe señalar que en concordancia con lo señalado en el Artículo 11 de la ley No se entenderá que hay incumplimiento de la Garantía de oportunidad en los casos de fuerza mayor, caso fortuito, causal médica o causa imputable al beneficiario, lo que deberá ser debidamente acreditado por el FONASA. En su caso, la garantía tratamiento quirúrgico se encuentra exceptuado por indicación médica (seguimiento).";
		String text2=text.replaceAll("a","e");
		System.out.println(text2);
		log.info("setRespuesta á ::"+text );
	}

}
