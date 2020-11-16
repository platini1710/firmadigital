package cl.fonasa.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.fonasa.dto.Payload;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoRequest;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoResponse;
import cl.fonasa.soa.protocolo.HeaderRequest;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoBindingQSService;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoPortType;
import cl.fonasa.utils.Constantes;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

public class SignFileService {
    private  SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private static final Logger log = LoggerFactory.getLogger(SignatureAlgorithm.class);
    
    public String encodeFileToBase64Binary(File file )
			throws IOException {


		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}
	private  byte[] loadFile(File file) throws IOException {

		InputStream is =new FileInputStream(file.getName());
	    long length = file.length();
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

		InputStream fis =new FileInputStream(file.getName());
        byte[] data = new byte[1024];
        int read = 0; 
        while ((read = fis.read(data)) != -1) {
            sha256.update(data, 0, read);
        };
        byte[] hashBytes = sha256.digest();
  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        String fileHash = sb.toString();
        fis.close();
        return fileHash;
    }
     
	public  String getJWTToken(Payload payloads) throws ParseException {

		String run ="";
		  Header header2 = Jwts.header();
		  header2.put("alg", "HS256");
		  header2.put("typ", "JWT");
		HashMap<String, Object> header = new HashMap<String,Object>();			
		header.put("alg", signatureAlgorithm.toString()); //HS256			
		header.put("typ","JWT");
		if  (payloads.getRun().length()>2) {
			run =payloads.getRun().substring(0, payloads.getRun().length()-2);
		} else {
			run =payloads.getRun();
		}
		JwtBuilder builder = Jwts.builder()
				.setHeader((Map<String, Object>)header2)
				.claim("entity", payloads.getEntity())
				                  
				.claim("run", run)
				.claim("expiration", payloads.getExpiration())
				.claim("purpose",payloads.getPurpose())

				.signWith(SignatureAlgorithm.HS256,  TextCodec.BASE64.encode(payloads.getKeySecret()));
	
		log.warn("Entity ::" + payloads.getEntity()); 
		log.warn("Run ::" + run);
		log.warn("Expiration ::" + payloads.getExpiration());
		log.warn("Purpose ::" + payloads.getPurpose());
		log.warn("keySecret ::" + payloads.getKeySecret());
		return builder.compact();

	}

	public  String getJWTTokenAtendido(Payload payloads) throws ParseException {



		HashMap<String, Object> header = new HashMap<String,Object>();			
		header.put("alg", signatureAlgorithm.toString()); //HS256			
		header.put("typ","JWT");


		JwtBuilder builder = Jwts.builder()
				.setHeader(header)
				.claim("entity", payloads.getEntity())
				.claim("run", payloads.getRun())
				.claim("expiration", payloads.getExpiration())
				.claim("purpose",payloads.getPurpose())

				.signWith(SignatureAlgorithm.HS256,  TextCodec.BASE64.encode(payloads.getKeySecret()));
	

		return builder.compact();
	}
	
	

	  public String generaCodigoCertificado(String rut, String tramo, String codigo,String certificadoWSDL) throws Exception {
			String valor = "";



			URL wsdlLocation = new URL(certificadoWSDL);
			GestionCertificadoBindingQSService service = new GestionCertificadoBindingQSService(wsdlLocation);
			GestionCertificadoPortType port = service.getGestionCertificadoBindingQSPort();
			HeaderRequest header = new HeaderRequest();
			header.setUserID("");
			header.setRolID("");
			header.setSucursalID("");
			GregorianCalendar gfechaActual = new GregorianCalendar();
			Calendar fechaActual = GregorianCalendar.getInstance();
			gfechaActual.setTime(fechaActual.getTime());
			header.setFechaHora(DatatypeFactory.newInstance().newXMLGregorianCalendar(gfechaActual));

			GestionCertificadoRequest request = new GestionCertificadoRequest();
			log.info("fechaActual  ::" + fechaActual.getTime());
			GestionCertificadoRequest.BodyResquest bd = new GestionCertificadoRequest.BodyResquest();
			bd.setRunCertificado(rut);
			bd.setTipoCertificado(codigo);
			bd.setTramoCertificado(tramo);

			request.setHeaderRequest(header);
			request.setBodyResquest(bd);

			GestionCertificadoResponse response = port.gestionCertificado(request);
			// valor = response.getBodyResponse().getIdCertificado().toString();

			if ("0".equals(response.getBodyResponse().getCodigoEstado())) {
				throw new Exception("error al generar codigo certificado");
			}
			valor = response.getBodyResponse().getCodCertificado();

			return valor;

		}
}
