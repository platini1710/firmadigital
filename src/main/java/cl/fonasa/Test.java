package cl.fonasa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cl.fonasa.dto.Payload;
import cl.fonasa.pdf.GeneradorFilePdf;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;


public class Test {
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
    private  SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");   
		Date now = new Date();
		File file = new File("C:/Users/Agusto/7TkghadtvbHrAYt.pdf");
		Calendar date = Calendar.getInstance();
		long t= date.getTimeInMillis();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		System.out.println((sdf2.format(new Date(t + (15 * ONE_MINUTE_IN_MILLIS)))).toString());
    }
      
    /**
     * Verifies file's SHA256 checksum
     * @param Filepath and name of a file that is to be verified
     * @param testChecksum the expected checksum
     * @return true if the expeceted SHA256 checksum matches the file's SHA256 checksum; false otherwise.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean verifyChecksum(String file, String testChecksum) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file);
  
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
        System.out.println("fileHash ::" + fileHash);
        return fileHash.equals(testChecksum);
    }
	private  String getJWTToken(Payload payloads) throws ParseException {


		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		HashMap<String, Object> header = new HashMap<String,Object>();			
		header.put("alg", signatureAlgorithm.toString()); //HS256			
		header.put("typ","JWT");




		JwtBuilder builder = Jwts.builder()
				.setHeader(header)
				.claim("entity", "Subsecretaría General de La Presidencia")
				.claim("run", "22222222")
				.claim("expiration", "2020-05-14T19:00:00")
				.claim("purpose", "Desatendido")

				.signWith(SignatureAlgorithm.HS256,  TextCodec.BASE64.encode("abcd"));


		return builder.compact();
	}
}
