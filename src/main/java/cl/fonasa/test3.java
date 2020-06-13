package cl.fonasa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class test3 {
	private static final Logger log = LoggerFactory.getLogger(test3.class);
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String rawString = "Entwickeln Sie mit Vergnügen";
		byte[] bytes = rawString.getBytes(StandardCharsets.UTF_8);
		 
		String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
		log.info("rawString á ::"+rawString );
		log.info("setRespuesta á ::"+utf8EncodedString );
	}

}
