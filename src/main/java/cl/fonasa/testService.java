package cl.fonasa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.fonasa.controller.SignDesAtendidaController;

public class testService {
	private static final Logger log = LoggerFactory.getLogger(testService.class);
	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		// TODO Auto-generated method stub
		long numeroSolicitud=169;
		String ordinario=" ";

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String endPoint = "http://fodevotdgen.fonasa.local:10010/FrontInt_OSB_SolicitudesCiudadanas/RS_GeneraOrdinario";
		String run = "15410549-2";
		HttpPost httppost  = new org.apache.http.client.methods.HttpPost(endPoint);



		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("Content-type", "application/json");
		String json = "{\r\n" + "\"runUsuario\": \"" + run  + "\",\r\n "  ;
		
		json =json +  "\"numeroSolicitud\" :" + numeroSolicitud +  "\r\n"  ;

		json =json +   "     }";

		StringEntity stringEntity = new StringEntity(json);
		httppost.setEntity(stringEntity);
		HttpResponse response = httpclient.execute(httppost );
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException(
					" Failed : HTTP getOrdinario: " + response.getStatusLine().getStatusCode());
		}
		String output, result = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
		while ((output = br.readLine()) != null) {
			result = result + output;

		}
		log.info(result);;
		Object obj = new JSONParser().parse(result);
		JSONObject jo = (JSONObject) obj; 
		if (jo.get("ordinario")!=null) {
		ordinario=(String)jo.get("ordinario");
		}
	}

}
