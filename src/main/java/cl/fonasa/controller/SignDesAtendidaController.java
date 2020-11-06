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
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import cl.fonasa.dto.DocumentSign;
import cl.fonasa.dto.Hijos;
import cl.fonasa.dto.Payload;
import cl.fonasa.dto.Solicitud;
import cl.fonasa.pdf.GeneradorFilePdf;
import cl.fonasa.service.SignFileService;
import cl.fonasa.utils.FTP;
import cl.fonasa.utils.Utilidades;
import sun.misc.BASE64Decoder;

@RestController
@RequestMapping(path = "/", consumes =  "application/json; charset=utf-8")
public class SignDesAtendidaController {
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	private static final Logger log = LoggerFactory.getLogger(SignDesAtendidaController.class);

    String appName;
	@Autowired
	private FTP  ftp;
 
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
    @Value("${url.correoZona}")	
    private String correoZona;  
    @Value("${ws.genera.codigo.certificadoWSDL}")	
    	    private String certificadoWSDL;   

    
    @Value("${keySecret}")
	    private String keySecret;   

	@RequestMapping(value = "fea", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
	public DocumentSign firmaDocumentoDesatendida(@Context HttpServletRequest request,
			@RequestBody(required = true) Solicitud solicitud) throws IOException {
		try {
		String ordinario= obtenerOrdinario(solicitud.getRunUsuarioEjecuta(),solicitud.getIdCaso());
		solicitud.setOrdinario(ordinario);
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		String message = "archivo firmado exitosamente ";

		String codigo = "1";
		long saveDB = 1;
		FileInputStream fis = null;
		Utilidades util = new Utilidades();

		String clave = util.retornaAleatorios();
		if (solicitud.getOtp().length() > 5) {
			solicitud.setPurpose(purposeAtendido);
		} else
			solicitud.setPurpose(purposeDesatendido);

		try {
			codigo = "7";
			getTokenKey(solicitud.getRunUsuarioEjecuta(), solicitud);// fallo obtencion token obtiene datos del firmante
//obtiene respuetas 
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
			codigo = "1";
			Payload payloads = new Payload(solicitud.getRunUsuarioEjecuta(), solicitud.getEntity(),
					solicitud.getPurpose(), dateTime,keySecret);
			String ordinario = "000";

			codigo = "2";

			if (solicitud.getOrdinario() == null || "".equals(solicitud.getOrdinario().trim())) {
				// RespuestaOrdinario respuestaOrdinario=
				// getOrdinario(solicitud.getRunUsuarioEjecuta(), solicitud.getIdCaso());
				// ordinario =respuestaOrdinario.getOrdinario();// fallo obtencion ordinario

			} else {
				log.info("obtiene ordinario");
				ordinario = solicitud.getOrdinario();// fallo obtencion ordinario
			}
			// String parrafoUno =respuestaOrdinario.getParrafoUno();
			// String parrafoDos=respuestaOrdinario.getParrafoDos();

			codigo = "3";

			String content = signFilePdf(ordinario, solicitud, payloads, clave, respuesta, "", ""); // fallo Firma

			fis = new FileInputStream(clave + fileFirmadoDigital);
			codigo = "4";
			log.info("previo a subir el archivo a servidor FTP" );
			ftp.upload(solicitud.getIdCaso(), fis, "", solicitud.getPath(), clave + fileFirmadoDigital);// fallo
																											// subida
			log.info("ya subio al servidor FTP" );																						// FTP
				
			// archivo

			if (fis != null) {
				fis.close();
			} 

			codigo = "5";
			saveDB = grabaOk(solicitud.getIdCaso(), "Archivo firmado exitosamente", clave + fileFirmadoDigital, // fallo
																												// cierre
																												// de
																												// caso
					solicitud.getPath(), "pdf", "solicitudesCiudadanas");
			codigo = "6";
			try {

			if (solicitud.getHijos() == null || solicitud.getHijos().length == 0 ||  solicitud.getHijos()[0].getCasoHijo()==0 ) {
				sendCorreo( solicitud, content);
				
			} else {
				try {
				sendCorreo2( solicitud, content);
				}catch(Exception e) {
					
					sendCorreo( solicitud, content);
				}
			}
			}catch(Exception e) {
				
				sendCorreo( solicitud, content);
			}
			codigo = "0";
		} catch (IOException | ParseException | UnsupportedOperationException e) {

			log.error(e.getMessage(), e);

		} catch (Exception e) {

			log.error(e.getMessage(), e);

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
		if ("0".equals(codigo)) {
			message = "Se firmo archivo correctamente , se cerro en base de datos y se envio archivo por correo";
		} else if ("1".equals(codigo)) {
			message = "Fallo la obtención del token, no se pudo generar archivo firmado ";
		} else if ("2".equals(codigo)) {
			message = "Fallo la obtención del ordinario, no se pudo generar archivo firmado ";
		} else if ("3".equals(codigo)) {
			message = "Fallo la firma, causa posible el OTP  o Html mal formado";
		} else if ("4".equals(codigo)) {
			message = "Fallo la subida de archivo al ftp, no se pudo cerrar en base de Datos ni enviar correo";
		} else if ("5".equals(codigo)) {
			message = "Fallo el cierre en base de datos,el archivo fue firmado  y  no se pudo enviar el correo";
		} else if ("6".equals(codigo)) {
			message = "Fallo el envio de correo , se firmó el archivo y se  cerró en base de datos";
		} else if ("7".equals(codigo)) {
			message = "Fallo la obtencion del token, usuario no cuenta con firma digital registrada";
		} 


		DocumentSign documentSign = new DocumentSign();
		documentSign.setCodigo(codigo);
		documentSign.setMensaje(message);

		return documentSign;

	}

/*
 * genera PDF
 * 
 * firma archivo pdf
 * 
 * 
 * 
 * 
 */
	     
	public String signFilePdf(String ordinario,Solicitud solicitud, Payload payloads, String clave,String respuesta,String parrafoUno,String parrafoDos) throws ParseException, IOException, DocumentException, java.text.ParseException, NoSuchAlgorithmException {
    	Object[] o = null;
		log.info("signFilePdf de de ::" + solicitud.getDe());

		log.info("manda a firmar documento pdf ");
    	int[] numPage = {2};


    	if ("".equals(payloads.getEntity().trim()) || payloads.getEntity()==null) {
    		payloads.setEntity(entity);
    	}

    	payloads.setPurpose(solicitud.getPurpose());

		GeneradorFilePdf generadorFilePdf = new GeneradorFilePdf();
		SignFileService signFileService = new SignFileService();
		String fileName = "";

		solicitud.setNumeroPaginas(2 );
			if (solicitud.getTipo().trim().toUpperCase().indexOf("FELICITACI")>=0) {  
	
			fileName = generadorFilePdf.generaFileFelicitacioPdf(solicitud.getNombreSolicitante(),solicitud.getOrdinario(), solicitud.getNombreTipificacion(),  // si no es reclamo
					solicitud.getProblemaDeSalud(), solicitud.getIdCaso(), respuesta, clave,solicitud.getOrd(),solicitud.getTipo(),solicitud.getDe(),certificadoWSDL,solicitud.getRunUsuarioEjecuta(),solicitud.getGenero(),
					parrafoUno,parrafoDos,numPage,solicitud.getCargo(),solicitud.getInstitucion(),solicitud.getDzFirmante(),solicitud.getSubDeptoFirmante(),solicitud.getDepartamentoFirmante(),solicitud.getDireccionSolicitante(),solicitud.getIniciales());
		} else 		 {                                         // si es reclamo
	
			fileName = generadorFilePdf.generaFileReclamposPdf(solicitud,solicitud.getOrdinario(), 
				respuesta,clave,certificadoWSDL,parrafoUno,parrafoDos,numPage,solicitud.getCargo(),solicitud.getInstitucion(),solicitud.getDzFirmante(),solicitud.getSubDeptoFirmante(),solicitud.getDepartamentoFirmante(),solicitud.getDireccionSolicitante(),solicitud.getIniciales(),solicitud.getIdCaso());
		}
	 

			log.info("postEndpoint " +firmaDigital);
		String postEndpoint = firmaDigital;
		String resultToken = signFileService.getJWTToken(payloads);

		File file = new File(fileName);

		String fileBase64 = signFileService.encodeFileToBase64Binary(file);

		String json = "{\r\n" + "\"api_token_key\": \"" +solicitud.getApiToken() + "\",\r\n" + "\"token\":\"" + resultToken + "\",\r\n"
				+ "\"files\":[";
		json = json + "{\r\n" + "\"content-type\": \"application/pdf\"," + " \r\n" + "\"content\": \"" + fileBase64
				+ "\" \r\n";
		json = json + ",	\r\n \"description\": \"str\"," + " \r\n" + "\"checksum\":\""
				+ signFileService.verifyChecksum(file) + "\", \r\n";



		json = json +"\"layout\":";
		
		json = json + "\"<AgileSignerConfig>";
		json = json + "<Application id=\\\"THIS-CONFIG\\\">";
		json = json + "<pdfPassword/> " ;
		json = json + "<Signature>";
		json = json + "<Visible active=\\\"true\\\" layer2=\\\"false\\\" label=\\\"true\\\" pos=\\\"1\\\">";
		int page =numPage[0]-1;
		if ((solicitud.getTipo().trim().toUpperCase().indexOf("FELICITACI")>=0) ) {  
			json = json + "<llx>250</llx> ";
			json = json + "<lly>215</lly> ";
			json = json + "<urx>340</urx> ";		
			json = json + "<ury>165</ury> ";
			json = json + "<page>" + page +"</page>";

		} else {
			json = json + "<llx>250</llx> ";
			json = json + "<lly>215</lly> ";
			json = json + "<urx>340</urx> ";
			json = json + "<ury>165</ury> ";
			json = json + "<page>" + page +"</page>";

		}
		
		
		json = json + "<image>BASE64</image>";
		json = json + "<BASE64VALUE>" + solicitud.getImagenFirma() + "</BASE64VALUE>";
		json = json + "</Visible>";
		json = json + "</Signature>";
		json = json + "</Application>";
		json = json + "</AgileSignerConfig>\"";
		
				
		json = json +  "\r\n}\r\n";	
		json = json + "\r\n" + "] \r\n" + "}";

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpRequest = new org.apache.http.client.methods.HttpPost(postEndpoint);
		httpRequest.setHeader("Accept", "application/json");
		httpRequest.setHeader("Content-type", "application/json");


		httpRequest.setHeader("otp", solicitud.getOtp());

		
		StringEntity stringEntity = new StringEntity(json);
		stringEntity.setContentType("application/json");

		httpRequest.setEntity(stringEntity);
		httpRequest.setHeader("Content-type", "application/json");
		log.info("previo a firmar documento a url  " +postEndpoint);
		HttpResponse response = httpclient.execute(httpRequest); 
		if (response.getStatusLine().getStatusCode() != 200) { 
			throw new RuntimeException(
					" Failed : HTTP firma digital causaas posible otp erroneo : " + response.getStatusLine().getStatusCode());
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

		log.info("ya se firmo exitosamente  el documento" );
		

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
	//http://fosqaotdgen.fonasa.local:10010/ComponenteNotificacion/RS_CorreoConAdjuntosCC/correoConAdjuntosCC
	public void sendEmailWidthFile(long idCaso,String msg,String asunto,String correo,String content) throws ClientProtocolException, IOException, ParseException {
		String correoCC="";
		 log.info("correoCC" + correoCC);
		try {
			 correoCC=obtenerCopiaCorreo(idCaso);
			 log.info("correoCC::::::" + correoCC);
			 log.info("idCaso::::::" + idCaso);
		}catch(Exception e) {
			log.error(e.getMessage(),e );
		}
	log.info(" correoCC::" + correoCC);;
	String json = "{\r\n" + 
			"    \"asunto\": \"" + asunto + "\",\r\n" + 
			"    \"remitente\": \"NotificacionFonasa@fonasa.cl\",\r\n" + 
			"    \"destinatarios\": [\r\n" + 
			"        \""  +  correo +  "\"\r\n" + 
			"    ],\r\n" + 
		    "    \"destinatariosConCopia\":[" + 
			"    ],\r\n" + 
			"    \"destinatariosConCopiaOculta\":[ \"" +   correoCC +  "\"\r\n" + 
			"    ],\r\n" + 
			"    \"mensaje\": \"" + msg + "\",\r\n" + 
			"    \"adjuntos\": [\r\n\"solicitudCiudadana.pdf?" + content			 + "\"     ]\r\n" + 
			"}";

	CloseableHttpClient httpclient = HttpClients.createDefault();
	HttpPost httpRequest = new org.apache.http.client.methods.HttpPost(urlCorreoAdjunto);

	httpRequest.setHeader("Accept", "application/json");
	httpRequest.setHeader("Accept-Charset", "charset=UTF-8");
	httpRequest.setHeader("Content-type", "application/json;charset=UTF-8");
	httpRequest.setHeader("Content-Disposition",
			"attachment; filename=" + "document.pdf");

	StringEntity stringEntity = new StringEntity(json,"UTF-8");
	httpRequest.setEntity(stringEntity);
	HttpResponse response = httpclient.execute(httpRequest);
	if (response.getStatusLine().getStatusCode() != 200) {
		throw new RuntimeException(
				"Failed : al Enviar Correo  error code : " + response.getStatusLine().getStatusCode());
	}
	}
	public String obtenerCopiaCorreo(long numeroSolicitud) throws ClientProtocolException, IOException, ParseException {
		String correo="";
		String json = "\r\n" + 
				"{\r\n" + 
				"  \"numeroSolicitud\" : " + numeroSolicitud+ "\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"}\r\n" + 
				"" ;

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpRequest = new org.apache.http.client.methods.HttpPost(correoZona);

		httpRequest.setHeader("Accept", "application/json");
		httpRequest.setHeader("Accept-Charset", "charset=UTF-8");
		httpRequest.setHeader("Content-type", "application/json;charset=UTF-8");


		StringEntity stringEntity = new StringEntity(json,"UTF-8");
		httpRequest.setEntity(stringEntity);
		HttpResponse response = httpclient.execute(httpRequest);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException(
					"Failed : servicio obtener copia de correo : " + response.getStatusLine().getStatusCode());	
		
	}
		String output, result = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
		while ((output = br.readLine()) != null) {
			result = result + output;

		}


		Object obj = new JSONParser().parse(result);
		JSONObject jo = (JSONObject) obj; 
		Long codigo=(Long)jo.get("codigo");
		if (codigo==0) {
			correo=(String)jo.get("correo");

		}
		return correo;
	
	}
	
	
	
	
	
	
	
	
	
	
	/*
	

	  /**
	   *  obtiene datos del firmante
	   * 
	   * 
	   * 
	   * 
	   * 
	   * 
	   * @param run
	   * @param solicitud
	   * @throws Exception
	   */
	  public void getTokenKey(String run, Solicitud solicitud) throws Exception {
			log.info("obtiene token");

			CloseableHttpClient httpclient = HttpClients.createDefault();
			String endPoint = urlDatosFirma;
			log.info("endPoint::" + endPoint);
			HttpPost httppost  = new org.apache.http.client.methods.HttpPost(endPoint);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			String json = "{\r\n" + "\"run\": \"" + run  + "\", \r\n"
					+ "\"idCaso\": " + solicitud.getIdCaso()  + "\r\n" 
					+"}";
			log.info(" getTokenKey json ::" + json);
			StringEntity stringEntity = new StringEntity(json);
			httppost.setEntity(stringEntity);
			HttpResponse response = httpclient.execute(httppost );
			if (response.getStatusLine().getStatusCode() != 200) {

				throw new RuntimeException(
						" Failed : No existe usuario con firma regisdtrada en database " + response.getStatusLine().getStatusCode());
			}
			String output, result = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
			while ((output = br.readLine()) != null) {
				result = result + output;

			}


			Object obj = new JSONParser().parse(result);
			JSONObject jo = (JSONObject) obj; 
			String apiToken=(String)jo.get("apiToken");
			if (apiToken==null) {
				throw new Exception(" Failed : No existe usuario con firma regisdtrada en database ");
			}
			log.info("de de sigDes::" + (String)jo.get("nombreFirmante"));
			String institucion=(String)jo.get("institucion");
			solicitud.setApiToken(apiToken);
			solicitud.setEntity(institucion);
			solicitud.setDe( (String)jo.get("nombreFirmante"));
			solicitud.setImagenFirma((String)jo.get("imagenFirma"));
			solicitud.setCargo((String)jo.get("cargo"));
			solicitud.setInstitucion((String)jo.get("institucion"));
			solicitud.setDepartamentoFirmante((String)jo.get("departamentoFirmante"));
			solicitud.setSubDeptoFirmante((String)jo.get("subDeptoFirmante"));
			if ((String)jo.get("dzFirmante")==null) {
				solicitud.setDzFirmante("NC");
			} else
			solicitud.setDzFirmante((String)jo.get("dzFirmante"));
			if ((String)jo.get("direccionSoliciante")==null) {
				solicitud.setDireccionSolicitante(" ");
			} else
			solicitud.setDireccionSolicitante((String)jo.get("direccionSoliciante"));
			
			//solicitud.setDireccionSolicitante(	"\r\nPURISIMA N°240, DEPTO. 301/ RECOLETA/ REGION METROPOLITANA " +"\r\nSUBDEPTO. GESTIÓN DE SOLICITUDES CIUDADANAS " + "\r\nSUBDEPTO. OFICINA DE PARTES ");
			solicitud.setIniciales((String)jo.get("iniciales"));
	    }
	  
	  
	  public long grabaOk(long idCaso,String msg ,String nombreArchivo,String ruta,String extension,String alias) throws ClientProtocolException, IOException, ParseException {
		  long codigo=0;
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String endPoint = urlCerrarCaso;
			HttpPost httppost  = new org.apache.http.client.methods.HttpPost(endPoint);




			httppost.setHeader("Content-type", "application/json");
			String json = "{\r\n" + 
					"  \"numeroSolicitud\" :" +  idCaso +  ",\r\n" + 
					"  \"mensaje\" : \"" + alias + "\",\r\n" + 
					"  \"documentoAdjunto\" : [ {\r\n" + 
					"    \"nombreArchivo\" : \"" + nombreArchivo  + "\",\r\n" + 
					"    \"descripcion\" : \"string\",\r\n" + 
					"    \"path\" : \"" + ruta + "\",\r\n" + 
					"    \"extension\" : \"" + extension + "\"\r\n" + 
					"  } ]\r\n" + 
					"}";
			log.info("json::: " + json);
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
			log.info("codigo::: " + codigo);
			log.info("result::: " + result);
			return codigo;
		  
	  }
	  
	  public void sendCorreo(Solicitud solicitud,String content) throws ClientProtocolException, IOException, ParseException {
			if (solicitud.getGenero() == 1) { 
				sendEmailWidthFile(solicitud.getIdCaso(),
						"<br><h3> Estimado : " + solicitud.getNombreSolicitante()
								+ " </h3> <br/> </p><br><p>Le informamos que el caso #" + solicitud.getIdCaso()
								+ "   ha sido cerrado con la siguiente respuesta en el adjunto del correo. </p>",
						"Archivo firmado digitalmente", // fallo envio de correo
						solicitud.getEmail(), content);
			} else if (solicitud.getGenero() == 2) {
				sendEmailWidthFile(solicitud.getIdCaso(),
						"<br><h3> Estimada : " + solicitud.getNombreSolicitante()
								+ " </h3> <br/> </p><br><p>Le informamos que el caso #" + solicitud.getIdCaso()
								+ "   ha sido cerrado con la siguiente respuesta en el adjunto del correo. </p>",
						"Archivo firmado digitalmente", // fallo envio de correo
						solicitud.getEmail(), content);
			} else {
				sendEmailWidthFile(solicitud.getIdCaso(),
						"<br><h3> " + solicitud.getNombreSolicitante()
								+ "</h3> <br/> </p><br><p>Le informamos que el caso #" + solicitud.getIdCaso()
								+ "   ha sido cerrado con la siguiente respuesta en el adjunto del correo. </p>",
						"Archivo firmado digitalmente", // fallo envio de correo
						solicitud.getEmail(), content);
		}
	  }
	  
	  
	  
	  public void sendCorreo2(Solicitud solicitud,String content) throws ClientProtocolException, IOException, ParseException {	  
		
		Hijos[] inputJson1 = solicitud.getHijos();
		String hijos = "";
		for (int i = 0; i < inputJson1.length; i++) {
			Long s = inputJson1[i].getCasoHijo();

			if (i == 0) {
				hijos = s.toString();

			} else {
				hijos = hijos + "," + s.toString();
			}
		}

		if (solicitud.getGenero() == 1) {

			sendEmailWidthFile(solicitud.getIdCaso(),"<br><h3> Estimado :" + solicitud.getNombreSolicitante()
					+ "</h3> <br/> </p><br><p>Le informamos  que los casos #" + solicitud.getIdCaso() + ","
					+ hijos + " han sido cerrados con la siguiente respuesta en el adjunto del correo. </p>",
					"Archivo firmado digitalmente", // fallo envio de correo
					solicitud.getEmail(), content);
		} else if (solicitud.getGenero() == 2) {
			sendEmailWidthFile(solicitud.getIdCaso(),"<br><h3> Estimada :" + solicitud.getNombreSolicitante()
					+ "</h3> <br/> </p><br><p>Le informamos  que los casos #" + solicitud.getIdCaso() + ","
					+ hijos + " han sido cerrados con la siguiente respuesta en el adjunto del correo. </p>",
					"Archivo firmado digitalmente", // fallo envio de correo
					solicitud.getEmail(), content);
		} else {
			sendEmailWidthFile(solicitud.getIdCaso(),"<br><h3> " + solicitud.getNombreSolicitante()
					+ "</h3> <br/> </p><br><p>Le informamos  que los casos #" + solicitud.getIdCaso() + ","
					+ hijos + " han sido cerrados con la siguiente respuesta en el adjunto del correo. </p>",
					"Archivo firmado digitalmente", // fallo envio de correo
					solicitud.getEmail(), content);
		}
	  }
	  
	  
	    public String obtenerOrdinario(String runUsuario,long numeroSolicitud) throws Exception {

			String ord="";
			String postEndpoint =urlGeneraOrdinario;

			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpPost httpRequest = new HttpPost(postEndpoint);

			httpRequest.setHeader("Accept", "application/json");
			httpRequest.setHeader("Content-type", "application/json");

			String inputJson = "{\r\n"
					+ "  \"runUsuario\" : \"" +  runUsuario + "\",\r\n"
					+ "  \"numeroSolicitud\" :" + numeroSolicitud + "\r\n"
					+ "}";

			StringEntity stringEntity =   new StringEntity(inputJson,"UTF-8");
			httpRequest.setEntity(stringEntity);



			HttpResponse response = httpclient.execute(httpRequest);

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), "utf-8"));

			// Throw runtime exception if status code isn't 200
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			String output, output2 = "";

			while ((output = br.readLine()) != null) {
				output2 = output2 + output;

			}
			org.json.JSONObject resultRest = new org.json.JSONObject(output2);

		

			if (!resultRest.isNull("ordinario")) {
				ord = resultRest.getString("ordinario");
				log.info("ordinario ordinario ordinario ordinario " + ord);
			}
			else {
				ord=" " ;//calcOrdinario( zona);
				log.info("ordinario   " + ord);
			}
			return ord;
		}

}