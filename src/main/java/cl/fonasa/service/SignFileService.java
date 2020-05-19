package cl.fonasa.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.simple.JSONObject;

import cl.fonasa.dto.Payload;
import cl.fonasa.utils.Constantes;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.commons.codec.binary.Base64;

public class SignFileService {
    private  SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    
    
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

        return fileHash;
    }
    
	public  String getJWTToken(Payload payloads) throws ParseException {



		HashMap<String, Object> header = new HashMap<String,Object>();			
		header.put("alg", signatureAlgorithm.toString()); //HS256			
		header.put("typ","JWT");



		JwtBuilder builder = Jwts.builder()
				.setHeader(header)
				.claim("entity", payloads.getEntity())
				.claim("run", payloads.getRun())
				.claim("expiration", payloads.getExpiration())
				.claim("purpose",payloads.getPurpose())

				.signWith(SignatureAlgorithm.HS256,  TextCodec.BASE64.encode(Constantes.SECRET_KEY));
	

		return builder.compact();
	}


}
