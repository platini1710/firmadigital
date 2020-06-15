package cl.fonasa;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class test3 {
	private static final Logger log = LoggerFactory.getLogger(test3.class);
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		String respuesta2="Cáncer";
		log.info("respuesta2 :: " + respuesta2 + ".pdf");
		  byte[] arrayAux =  respuesta2.getBytes("ISO-8859-2");
			log.info("arrayAux:: " + arrayAux + ".pdf");
		   respuesta2= new String( arrayAux, "UTF-8");
		log.info("respuesta2 encoding :: " + respuesta2 + ".pdf");
		

	}

}
