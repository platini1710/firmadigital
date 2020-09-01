package cl.fonasa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cl.fonasa.dto.Hijos;
import cl.fonasa.dto.Solicitud;

import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  

/**
 *
 * @author iText
 */ 

public class testService {

	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		String correo="";
		String respuesta ="\\n";

			
			respuesta="<html><body>" + respuesta + "</body></html>" ;
			System.out.println("respuesta xxxxxxxxxxxxxxxx:::" +respuesta);

			Test t=new Test();
		//	respuesta=respuesta.replaceAll("\\\\","");
			respuesta= respuesta.replaceAll("\\\\n", "\n");


			System.out.println("respuesta xxxxxxxxxxxxxxxx:::" +respuesta);
			  String str = "s1\ns2\ns3\ns4";
		        str = str.replaceAll("(\r\n|\n)", ",");
		        System.out.println(str);
}
}