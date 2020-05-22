package cl.fonasa.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.DocumentException;

import cl.fonasa.dto.DocumentSign;
import cl.fonasa.dto.Payload;
import cl.fonasa.dto.Solicitud;
import cl.fonasa.pdf.GeneradorFilePdf;
import cl.fonasa.service.SignFileService;
import cl.fonasa.util.FTP;
import cl.fonasa.util.Utilidades;
import sun.misc.BASE64Decoder;

@RestController
@RequestMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    String appName;
	@Autowired
	private FTP ftp;
    @Value("${ruta.server.pdf}")
	private String ruta;
    @Value("${ruta.server.url}")
	private String url;  
	private String fileFirmadoDigital="_firmado.pdf";
    @Value("${url.correoAdjunto}")		
    private String urlCorreoAdjunto;
    @Value("${api.firmaDigital}")	
    private String firmaDigital; 
    
    
    // hola probando
    @RequestMapping(value="token",produces=MediaType.APPLICATION_JSON_VALUE)
	public DocumentSign firmaDocumento(@RequestBody(required = true) Solicitud solicitud)  {
		System.out.println("payload run :" + solicitud.getIdCaso());
		FileInputStream fis = null;
		Utilidades util = new Utilidades();
		String clave = util.retornaAleatorios();

		String message ="Firma digital ok";
		String codigo ="0";
		Calendar date = Calendar.getInstance();
		long t= date.getTimeInMillis();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String dateTime=sdf2.format(new Date(t + (15 * ONE_MINUTE_IN_MILLIS))).toString();
		Payload payloads=new Payload(solicitud.getRunUsuarioEjecuta(), "Subsecretaría General de La Presidencia", "Desatendido", dateTime);
		try {
			 String content=signFilePdf(solicitud,payloads,clave);

			sendEmailWidthFile( "adjunto archivo firmado digitalmente","archivo firmado digitalmente", solicitud.getEmail(), content);
	


			log.warn("ruta ::" + ruta);      

			Path directory = Paths.get(clave + ".pdf");
			Path directory2 = Paths.get(	clave + fileFirmadoDigital + ".pdf");
		
			try {
				Files.delete(directory);
				Files.delete(directory2);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		catch(Exception e) {
			message=" fallo la firma digital " + e.getMessage();
			codigo ="1";
		}

		DocumentSign  documentSign=new DocumentSign();
		documentSign.setCodigo(codigo);
		documentSign.setMensaje(message);

		return documentSign;

	}

	  @RequestMapping(value ="/subirArchivos", method = RequestMethod.POST)
	    public ModelAndView  subirArchivos(Model model,@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles,
	    		@RequestParam("run")  String run,		@RequestParam("expiration")  String expiration,
	    		@RequestParam("purpose")  String purpose,		@RequestParam("entity")  String entity
	    		) throws IOException, NoSuchAlgorithmException, ParseException, org.json.simple.parser.ParseException, java.text.ParseException {
	    	HttpHeaders headers = new HttpHeaders();
	        Utilidades util = new Utilidades();
	    	Object[] o = null;
	        String clave ="";
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    	// set `accept` header
	    	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			String postEndpoint = firmaDigital;
			System.out.println("run ::" + run);      
			Payload payloads=new Payload( run,  entity,  purpose,   expiration);
			SignFileService signFileService = new SignFileService();

			RestTemplate restTemplate = new RestTemplate();
			String resultToken =signFileService.getJWTToken(payloads);
	    	String json= "{\r\n" + "\"api_token_key\": \"sandbox\",\r\n" +
	    			"\"token\":\"" + resultToken  + "\",\r\n" + 
	    			"\"files\":["  ;

	    	
			for (MultipartFile uploadedFile : uploadingFiles) {

				File file = new File(uploadedFile.getOriginalFilename());
				String fileBase64=signFileService.encodeFileToBase64Binary(file);
				json= json +"{\r\n" + 
	  	    			"\"content-type\": \"application/pdf\","  + " \r\n" +
	  	    			 "\"content\": \"" + fileBase64   + "\" \r\n"  ;
		    	json= json +",	\r\n \"description\": \"str\","  + " \r\n" +
		    			 "\"checksum\":\"" + signFileService.verifyChecksum(file)  + "\" \r\n" +
		    			"}\r\n";
		    	
		    	
		    	
			}
			json= json  + "\r\n" + 
					"] \r\n" +
					"}";

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpRequest = new org.apache.http.client.methods.HttpPost(postEndpoint);
			httpRequest.setHeader("Accept", "application/json");
			httpRequest.setHeader("Content-type", "application/json");
			StringEntity stringEntity = new StringEntity(json);
			httpRequest.setEntity(stringEntity);
			HttpResponse response = httpclient.execute(httpRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP firma digital error code : " + response.getStatusLine().getStatusCode());
			}
			String output, output2 = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"  ));
			while ((output = br.readLine()) != null) {
				output2 = output2 + output;
			}
			Object obj = new JSONParser().parse(output2);
			JSONObject jo = (JSONObject) obj;     
			JSONArray ja = (JSONArray) jo.get("files");    
			o = ja.toArray();       
			for (int i = 0; i < o.length ; i++) {      
				Map o1 = (Map) o[i];   
				
			     clave = util.retornaAleatorios();
				String content = (String) o1.get("content");    
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] decodedBytes = decoder.decodeBuffer(content);

				File file = new File(clave + ".pdf");;
				FileOutputStream fop = new FileOutputStream(file);

				fop.write(decodedBytes);
				fop.flush();
				fop.close();
				System.out.println("content ::" + content);      
				
			}	
			   ModelAndView modelAndView = new ModelAndView();
		    modelAndView.addObject("appName", appName);
		    modelAndView.setViewName("home");
			    return modelAndView;
	    
	    }
	    
	public String signFilePdf(Solicitud solicitud, Payload payloads, String clave) throws ParseException, IOException, DocumentException, java.text.ParseException, NoSuchAlgorithmException {
    	Object[] o = null;
		GeneradorFilePdf generadorFilePdf = new GeneradorFilePdf();
		SignFileService signFileService = new SignFileService();
		String fileName = generadorFilePdf.generaFilePdf(solicitud.getNombreSolicitante(),
				solicitud.getNombreTipificacion(), solicitud.getProblemaDeSalud(), solicitud.getIdCaso(), clave);
		String postEndpoint = firmaDigital;
		String resultToken = signFileService.getJWTToken(payloads);
		File file = new File(fileName);
		String fileBase64 = signFileService.encodeFileToBase64Binary(file);

		String json = "{\r\n" + "\"api_token_key\": \"sandbox\",\r\n" + "\"token\":\"" + resultToken + "\",\r\n"
				+ "\"files\":[";
		json = json + "{\r\n" + "\"content-type\": \"application/pdf\"," + " \r\n" + "\"content\": \"" + fileBase64
				+ "\" \r\n";
		json = json + ",	\r\n \"description\": \"str\"," + " \r\n" + "\"checksum\":\""
				+ signFileService.verifyChecksum(file) + "\" \r\n" + "}\r\n";
		json = json + "\r\n" + "] \r\n" + "}";

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpRequest = new org.apache.http.client.methods.HttpPost(postEndpoint);
		httpRequest.setHeader("Accept", "application/json");
		httpRequest.setHeader("Content-type", "application/json");
		StringEntity stringEntity = new StringEntity(json);
		httpRequest.setEntity(stringEntity);
		HttpResponse response = httpclient.execute(httpRequest);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException(
					" Failed : HTTP firma digital error code : " + response.getStatusLine().getStatusCode());
		}
		String output, output2 = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
		while ((output = br.readLine()) != null) {
			output2 = output2 + output;
		}
		Object obj = new JSONParser().parse(output2);
		JSONObject jo = (JSONObject) obj;
		JSONArray ja = (JSONArray) jo.get("files");
		o = ja.toArray();
		int i = 0;
		Map o1 = (Map) o[i];
		log.warn("url ::" + url);

	    String content = (String) o1.get("content");
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decodedBytes = decoder.decodeBuffer(content);

		File file2 = new File(clave + fileFirmadoDigital );
		FileOutputStream fop = new FileOutputStream(file2);

		fop.write(decodedBytes);
		fop.flush();
		fop.close();

		return content;
	}
	
	public void sendEmailWidthFile(String msg,String asunto,String correo,String content) throws ClientProtocolException, IOException {
	String json = "{\r\n" + "\"mensaje\": \"" + msg  +  "\",\r\n" + "\"asunto\":\"" + asunto + "\",\r\n"  +
		  "\"remitente\": \"notificacion@fonasa.cl\" ,\r\n" +
		  "\"listDestinatarios\" : {" +
		  "\"destinatario\" : [ \"" + correo + "\"] " +
		 " }," +
		 "\"listDestinatariosCC\" : { " +
		 "\"destinatario\" :  [ \"\" ] " +
		  "}," +
		  "\"listAdjuntos\" : {" + 
		  "\"data\" : [\"" + "solicitudesCiudadanas.pdf" +  content  + "\"] " +
		 " }"+
		"}";
	CloseableHttpClient httpclient = HttpClients.createDefault();
	HttpPost httpRequest = new org.apache.http.client.methods.HttpPost(urlCorreoAdjunto);
	
	httpRequest.setHeader("Accept", "application/json");
	httpRequest.setHeader("Content-type", "application/json");
	httpRequest.setHeader("Content-Disposition",
			"attachment; filename=" + "document.pdf");
	StringEntity stringEntity = new StringEntity(json);
	httpRequest.setEntity(stringEntity);
	HttpResponse response = httpclient.execute(httpRequest);
	if (response.getStatusLine().getStatusCode() != 200) {
		throw new RuntimeException(
				"Failed : HTTP firma digital error code : " + response.getStatusLine().getStatusCode());
	}
	}
}