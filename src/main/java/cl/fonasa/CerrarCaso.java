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

public class CerrarCaso {

	public static void main(String[] args) throws ParseException, ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost  = new org.apache.http.client.methods.HttpPost("http://fosqaotdgen.fonasa.local:10010/FrontInt_OSB_FonasaResuelve/RS_CerrarCaso");

int idCaso=89;
String msg="msg";
String nombreArchivo="msg";
String ruta="c:/msg";
String extension="pdf";
String alias="test";
		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("Content-type", "application/json");
		String json = "{\r\n" + "\"idCaso\": " + idCaso  + ",\r\n "  ;
		json =json + 		"\"mensajes\": [";
		json =json + " \r\n { " ;
		json =json + "\"mensaje\":\"" + msg +  "\",\r\n"  ;
		json =json + "\"documentos\": [\r\n" ;
		json =json +  " {\r\n";
		json =json +  "\"nombreArchivo\":\"" + nombreArchivo +  "\" , \r\n" ;
		json =json +   "\"ruta\": \"" + ruta +   "\" , \r\n" ;
		json =json +   "\"extension\": \"" + extension +   "\" ,\r\n" ;
		json =json +    "\"alias\": \"" + alias +   "\" \r\n" ;
		json =json +        "              } \r\n";
		json =json +        "              ]\r\n";
		json =json +        "   }\r\n";
		json =json +        "  ]\r\n";
		json =json +   "     }";

		StringEntity stringEntity = new StringEntity(json);
		httppost.setEntity(stringEntity);
		HttpResponse response = httpclient.execute(httppost );
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException(
					" Failed : HTTP RS_CerrarCaso: " + response.getStatusLine().getStatusCode());
		}
		String output, result = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
		while ((output = br.readLine()) != null) {
			result = result + output;

		}
		System.out.println("inputJson :: "+ result);
		Object obj = new JSONParser().parse(result);
		JSONObject jo = (JSONObject) obj; 
		Long codigo=(Long)jo.get("codigo");
		System.out.println("inputJson :: "+ result);
		
	}

}
