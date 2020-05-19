package cl.fonasa.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cl.fonasa.dto.Payload;
import cl.fonasa.util.Utilidades;
import cl.fonasa.utils.Constantes;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import sun.misc.BASE64Decoder;

@RestController
public class SignController {
    @Value("${spring.application.name}")
    String appName;
    String clave;
    @Value("${ruta.server.url}")
	String url ;
    private  SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    
    
    @RequestMapping("/")
    public ModelAndView  home(Model model) {

    	// build the request



    	System.out.println("home view " + appName);
        model.addAttribute("appName", appName);
    	model.addAttribute("name", appName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("appName", appName);
        modelAndView.setViewName("home");
        return modelAndView;
    }
    
    @RequestMapping(value ="/subirArchivos", method = RequestMethod.POST)
    public ModelAndView  subirArchivos(Model model,@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles,
    		@RequestParam("run")  String run,		@RequestParam("expiration")  String expiration,
    		@RequestParam("purpose")  String purpose,		@RequestParam("entity")  String entity
    		) throws IOException, NoSuchAlgorithmException, ParseException, org.json.simple.parser.ParseException {
    	HttpHeaders headers = new HttpHeaders();
        Utilidades util = new Utilidades();
    	Object[] o = null;
        String clave ="";
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	// set `accept` header
    	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		System.out.println("run ::" + run);      
		Payload payloads=new Payload( run,  entity,  purpose,   expiration);


		RestTemplate restTemplate = new RestTemplate();
		String resultToken =getJWTToken(payloads);
    	String json= "{\r\n" + "\"api_token_key\": \"sandbox\",\r\n" +
    			"\"token\":\"" + resultToken  + "\",\r\n" + 
    			"\"files\":["  ;
  
    	
		for (MultipartFile uploadedFile : uploadingFiles) {

			File file = new File(uploadedFile.getOriginalFilename());
			String fileBase64=encodeFileToBase64Binary(uploadedFile.getInputStream(),			uploadedFile.getSize());
			json= json +"{\r\n" + 
  	    			"\"content-type\": \"application/pdf\","  + " \r\n" +
  	    			 "\"content\": \"" + fileBase64   + "\" \r\n"  ;
	    	json= json +",	\r\n \"description\": \"str\","  + " \r\n" +
	    			 "\"checksum\":\"" + verifyChecksum(file)  + "\" \r\n" +
	    			"}\r\n";
	    	
	    	
	    	
		}
		json= json  + "\r\n" + 
				"] \r\n" +
				"}";
		String postEndpoint ="https://api.firma.test.digital.gob.cl/firma/v2/files/tickets";
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
    
    private String encodeFileToBase64Binary(InputStream is ,long size)
			throws IOException {


		byte[] bytes = loadFile(is,size);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}
	private  byte[] loadFile(InputStream is, long size) throws IOException {


	    long length = size;
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file ");
	    }

	    is.close();
	    return bytes;
	}
	
    public String verifyChecksum(File file) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

  
        byte[] data = new byte[1024];
        int read = 0; 
        InputStream  fis=new FileInputStream(file.getName());
        while ((read = fis.read(data)) != -1) {
            sha256.update(data, 0, read);
        };
        byte[] hashBytes = sha256.digest();
  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        String fileHash = sb.toString();

        return fileHash;
    }
    
	private  String getJWTToken(Payload payloads) throws ParseException {


		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		HashMap<String, Object> header = new HashMap<String,Object>();			
		header.put("alg", signatureAlgorithm.toString()); //HS256			
		header.put("typ","JWT");

		final java.util.Date fechaExp = sdf.parse(payloads.getExpiration());
		long milliseconds = fechaExp.getTime();
		JwtBuilder builder = Jwts.builder()
				.setHeader(header)
				.claim("entity", payloads.getEntity())
				.claim("run", payloads.getRun())
				.claim("expiration", payloads.getExpiration())
				.claim("purpose",payloads.getPurpose())

				.signWith(SignatureAlgorithm.HS256,  TextCodec.BASE64.encode(Constantes.SECRET_KEY));
		if (milliseconds >= 0) {

		//	builder.setExpiration(fechaExp);
		}

		return builder.compact();
	}
}
