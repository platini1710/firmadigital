package cl.fonasa.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;

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
import cl.fonasa.soa.gestioncertificado.GestionCertificadoRequest;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoResponse;
import cl.fonasa.soa.protocolo.HeaderRequest;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoBindingQSService;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoPortType;
import cl.fonasa.util.FTP;
import cl.fonasa.util.Utilidades;
import sun.misc.BASE64Decoder;

@RestController
@RequestMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
public class SignDesAtendidaController {
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	private static final Logger log = LoggerFactory.getLogger(SignDesAtendidaController.class);

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
    @Value("${url.datosFirma}")		
    private String urlDatosFirma;
    @Value("${api.firmaDigital}")	
    private String firmaDigital; 
    
    @Value("${url.generaOrdinario}")	
    private String urlGeneraOrdinario; 
    
    @Value("${entity}")	
    private String entity;     
    @Value("${purposeDesatendido}")	
    private String purposeDesatendido;  
    @Value("${purposeAtendido}")	
    private String purposeAtendido;    
    @Value("${url.cerrarCaso}")	
    private String urlCerrarCaso;   
    @Value("${ws.genera.codigo.certificadoWSDL}")	
    	    private String certificadoWSDL;   
	@RequestMapping(value = "fea", produces = MediaType.APPLICATION_JSON_VALUE)
	public DocumentSign firmaDocumentoDesatendida(@RequestBody(required = true) Solicitud solicitud) {

		String message = "archivo firmado exitosamente ";
		String codigo = "1";
		long saveDB = 1;
		FileInputStream fis = null;
		Utilidades util = new Utilidades();
		log.info("paso 1 ::" );
		String clave = util.retornaAleatorios();
		if (solicitud.getOtp().length() > 5) {
			solicitud.setPurpose(purposeAtendido);
		} else
			solicitud.setPurpose(purposeDesatendido);
		log.info("paso 2 ::" );
		try {
			log.info("paso 3 ::" );
			getTokenKey(solicitud.getRunUsuarioEjecuta(), solicitud);
			log.info("paso 4 ::" );
			String respuesta = solicitud.getRespuesta();

			Calendar date = Calendar.getInstance();
			long t = date.getTimeInMillis();
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String dateTime = sdf2.format(new Date(t + (15 * ONE_MINUTE_IN_MILLIS))).toString();

			/*
			 * if (!"".equals(solicitud.getEntity()) || solicitud.getEntity()!=null) {
			 * entity=solicitud.getEntity(); } if (!"".equals(solicitud.getPurpose()) ||
			 * solicitud.getPurpose()!=null) { purpose=solicitud.getPurpose(); }
			 */
			log.info("paso 5 ::" );
			Payload payloads = new Payload(solicitud.getRunUsuarioEjecuta(), solicitud.getEntity(),
					solicitud.getPurpose(), dateTime);
			String ordinario = "000";
			log.info("paso 6 ::" );
			try {
				ordinario = getOrdinario(solicitud.getRunUsuarioEjecuta(), solicitud.getIdCaso());//genera ordinario
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			log.info("paso 7 ::" );
			if ("".equals(ordinario.trim()) ) {
				ordinario=String.valueOf(solicitud.getOrd());
			}
			log.info("ordinario ::" + ordinario);
			String content = signFilePdf(ordinario, solicitud, payloads, clave, respuesta);   //firma archivo
			codigo = "0";

			fis = new FileInputStream(clave + fileFirmadoDigital);
			ftp.upload(solicitud.getIdCaso(), fis, ruta, solicitud.getPath(), clave + fileFirmadoDigital);//sube archivo 

			if (fis != null) {
				fis.close();
			}

			log.info("ordinario ::" + ordinario);
			log.info("ruta ::" + solicitud.getPath());
			saveDB = grabaOk(solicitud.getIdCaso(), "archivo firmado exitosamente", clave + fileFirmadoDigital,//cierre de caso
					 solicitud.getPath(), "pdf", "solicitudesCiudadanas");
			try {
				sendEmailWidthFile("adjunto archivo firmado digitalmente", "archivo firmado digitalmente",//envio de correo
						solicitud.getEmail(), content);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		} catch (IOException | ParseException | UnsupportedOperationException e) {

			log.error(e.getMessage(), e);
			if ("1".equals(codigo) && (!"".equals(solicitud.getApiToken().trim()))) {
				message = " fallo la firma digital " + e.getMessage();
			}
			if ("".equals(solicitud.getApiToken().trim())) {
				message = " fallo el obtener ApiTokenKey " + e.getMessage();
			}
		} catch (Exception e) {

			log.error(e.getMessage(), e);
			if ("1".equals(codigo)) {
				message = " fallo la firma digital " + e.getMessage();
			}

		} finally {

			Path directory = Paths.get(clave + fileFirmadoDigital);
			Path directory2 = Paths.get(clave + ".pdf");
			try {
				Files.delete(directory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}

			try {
				Files.delete(directory2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}

		}
		log.info("saveDB :: " + saveDB);
		if (saveDB == 1) {

			message = message + ", no se cerro el caso  en base de datos";
			if ("0".equals(codigo)) {
				codigo = "1";
			} else if ("1".equals(codigo)) {
				codigo = "2";
			}

		} else {
			message = message + ", se cerro exitosamente el caso en base de datos";
			codigo = "0";
		}
		DocumentSign documentSign = new DocumentSign();
		documentSign.setCodigo(codigo);
		documentSign.setMensaje(message);

		return documentSign;

	}


	    
	public String signFilePdf(String ordinario,Solicitud solicitud, Payload payloads, String clave,String respuesta) throws ParseException, IOException, DocumentException, java.text.ParseException, NoSuchAlgorithmException {
    	Object[] o = null;
    	if ("".equals(payloads.getEntity().trim()) || payloads.getEntity()==null) {
    		payloads.setEntity(entity);
    	}
		log.info("entity::" +solicitud.getEntity());
    	payloads.setPurpose(solicitud.getPurpose());
		log.info("entros a signFilePdf ::" );
		GeneradorFilePdf generadorFilePdf = new GeneradorFilePdf();
		SignFileService signFileService = new SignFileService();
		String fileName = "";
		log.info("getTipo::" +solicitud.getTipo());
		log.info("clave::" +clave);
		if ("RECLAMO".equals(solicitud.getTipo().trim().toUpperCase())) {
			log.info("paso 2::" );
			fileName = generadorFilePdf.generaFileReclamposPdf(ordinario,solicitud.getNombreSolicitante(),
				solicitud.getNombreTipificacion(), solicitud.getProblemaDeSalud(), solicitud.getIdCaso(), 
				respuesta,clave,solicitud.getOrd(),solicitud.getTipo(),solicitud.getDe());
		}
		else  {
			fileName = generadorFilePdf.generaFileFelicitacioPdf(ordinario,solicitud.getNombreSolicitante(), solicitud.getNombreTipificacion(),
					solicitud.getProblemaDeSalud(), solicitud.getIdCaso(), respuesta, clave,solicitud.getOrd(),solicitud.getTipo(),solicitud.getDe());
		}
		log.info("paso 4::" );
		String postEndpoint = firmaDigital;
		String resultToken = signFileService.getJWTToken(payloads);
		log.info("resultToken ::" + resultToken);
		File file = new File(fileName);

		String fileBase64 = signFileService.encodeFileToBase64Binary(file);

		String json = "{\r\n" + "\"api_token_key\": \"" +solicitud.getApiToken() + "\",\r\n" + "\"token\":\"" + resultToken + "\",\r\n"
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
		log.info("solicitud.getOtp() ::" + solicitud.getOtp());
		if (solicitud.getOtp().length()>5) {
			httpRequest.setHeader("otp", solicitud.getOtp());
		}
		StringEntity stringEntity = new StringEntity(json);
		httpRequest.setEntity(stringEntity);
		HttpResponse response = httpclient.execute(httpRequest); 
		if (response.getStatusLine().getStatusCode() != 200) { 
			throw new RuntimeException(
					" Failed : HTTP firma digital causaas posible otp erroneo token erroneo : " + response.getStatusLine().getStatusCode());
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
				"Failed : al Enviar Correo  error code : " + response.getStatusLine().getStatusCode());
	}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	  @RequestMapping(value ="/firmarArchivos", method = RequestMethod.POST)
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

				
			}	
			   ModelAndView modelAndView = new ModelAndView();
		    modelAndView.addObject("appName", appName);
		    modelAndView.setViewName("home");
			    return modelAndView;
	    
	    }
	  
	  public void getTokenKey(String run, Solicitud solicitud) throws ClientProtocolException, IOException, ParseException, UnsupportedOperationException {


			CloseableHttpClient httpclient = HttpClients.createDefault();
			String endPoint = urlDatosFirma;
			HttpPost httppost  = new org.apache.http.client.methods.HttpPost(endPoint);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			String json = "{\r\n" + "\"run\": \"" + run  + "\"\r\n"  +"}";
	
			StringEntity stringEntity = new StringEntity(json);
			httppost.setEntity(stringEntity);
			HttpResponse response = httpclient.execute(httppost );
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException(
						" Failed :  FrontInt_OSB_SolicitudesCiudadanas/RS_ObtenerDatosFirma: " + response.getStatusLine().getStatusCode());
			}
			String output, result = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
			while ((output = br.readLine()) != null) {
				result = result + output;

			}


			Object obj = new JSONParser().parse(result);
			JSONObject jo = (JSONObject) obj; 
			String apiToken=(String)jo.get("apiToken");
			log.info("apiToken::" + apiToken);
			String institucion=(String)jo.get("institucion");
			solicitud.setApiToken(apiToken);
			solicitud.setEntity(institucion);
			solicitud.setDe( (String)jo.get("nombreFirmante"));

	    }
	  
	  
	  public long grabaOk(long idCaso,String msg ,String nombreArchivo,String ruta,String extension,String alias) throws ClientProtocolException, IOException, ParseException {
		  long codigo=0;
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String endPoint = urlCerrarCaso;
			HttpPost httppost  = new org.apache.http.client.methods.HttpPost(endPoint);



			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			String json = "{\r\n" + "\"numeroSolicitud\": " + idCaso  + ",\r\n "  ;
			
			json =json +  "\"mensaje\" :\"" + msg +  "\",\r\n"  ;
			json =json +   "\"documentoAdjunto\" : [{ \r\n";
			json =json +   "\"nombreArchivo\" : \"" + nombreArchivo + "\", \r\n";
			json =json +   "\"descripcion\" : \"string\", \r\n";
			json =json +   "\"path\": \"" + ruta +   "\" , \r\n" ;
			json =json +   "\"extension\": \"" + extension +   "\" \r\n" ;

			json =json +        "   }\r\n";
			json =json +        "  ]\r\n";
			json =json +   "     }";
			log.info("ruta::: " + ruta);
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

			Object obj = new JSONParser().parse(result);
			JSONObject jo = (JSONObject) obj; 
			codigo=(Long)jo.get("codigo");

			return codigo;
		  
	  }
	  
	  public String getOrdinario(String run, long numeroSolicitud) throws ClientProtocolException, IOException, ParseException, UnsupportedOperationException {  
	  	  
		  String ordinario=" ";
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String endPoint = urlGeneraOrdinario;
			HttpPost httppost  = new org.apache.http.client.methods.HttpPost(endPoint);
log.info("endPoint::: " + endPoint);


			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			String json = "{\r\n" + "\"runUsuario\": \"" + run  + "\",\r\n "  ;
			
			json =json +  "\"numeroSolicitud\" :" + numeroSolicitud +  "\r\n"  ;

			json =json +   "     }";
			log.info("json::: " + json);
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

			Object obj = new JSONParser().parse(result);
			JSONObject jo = (JSONObject) obj; 

			if (jo.get("ordinario")!=null) {
				ordinario=(String)jo.get("ordinario");
				}
			return ordinario;
		}
	  

}