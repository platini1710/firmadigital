package cl.fonasa;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import cl.fonasa.dto.Payload;
import cl.fonasa.pdf.GeneradorFilePdf;
import cl.fonasa.service.SignFileService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;


public class Test extends PdfPageEventHelper{
    private  SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private static final Logger log = LoggerFactory.getLogger(Test.class);
    public static final String FONT= "/Font/arial.ttf";

	public static void main(String[] args) throws Exception {
    	System.out.println("prop\u00f3sito  general " + "ORD-413".substring(4));
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

	public String eliminaAllTag(String respuesta, String tagAbierto, String tagCerrado) {
		respuesta = eliminatag(tagAbierto, respuesta);
		respuesta = eliminatag(tagCerrado, respuesta);

		return respuesta;
	}
	
	public int cuentaTagAbierto(String tag, String respuesta  ) {
		int count1=0;
	
		int indice1=respuesta.indexOf(tag);
		tag="<p";
		String respuesta2="" ;

		while (indice1>-1) {
			respuesta2 = respuesta.substring(respuesta.indexOf(
					tag)+tag.length(),respuesta.length());
			count1 =count1 + 1;

			 indice1=respuesta.indexOf(tag,indice1 +1);

		}

		return count1;
	}
		
	public int cuentaTagCerrado(String tag, String respuesta  ) {
		int count2=0;
	
		int indice1=respuesta.indexOf(tag);
	
		String respuesta2="" ;

		while (indice1>-1) {
			respuesta2 = respuesta.substring(respuesta.indexOf(
					tag)+tag.length(),respuesta.length());
			count2 =count2 + 1;

			 indice1=respuesta.indexOf(tag,indice1 +1);

		}

		return count2;
	}
		
	public String eliminatagAbierto(String tag, String respuesta,int count1, int count2  ) {
  if ("<p".equalsIgnoreCase(tag )) {
	  tag="<p>";
  }
  log.info(tag);
		 int  indice1=respuesta.indexOf(tag);
		  log.info("indice 1 " +indice1);
		while ((indice1>-1) && (Math.abs(count1-count2))>0){
			 int  indice2=respuesta.indexOf(">",indice1);

			respuesta = respuesta.substring(0,indice1) + respuesta.substring(indice2 +1 ,respuesta.length());


				count2 =count2 + 1;

			 indice1=respuesta.indexOf(tag);
		}

		return respuesta;
	}
	public String eliminatagCerrado(String tag, String respuesta,int count1, int count2  ) {

		 int  indice1=respuesta.indexOf(tag);
		while ((indice1>-1) && (Math.abs(count1-count2))>0){
			 int  indice2=respuesta.indexOf(">",indice1);
			respuesta = respuesta.substring(0,indice1) + respuesta.substring(indice2  ,respuesta.length());

			if (count1-count2>0) {
				
				count2 =count2 + 1;

			} else {
				count1 =count1 + 1;

			}

			 indice1=respuesta.indexOf(tag);
		}

		return respuesta;
	}
	public String eliminatag(String tag, String respuesta ) {

		 int  indice1=respuesta.indexOf(tag);
		while (indice1>-1) {
			 int  indice2=respuesta.indexOf(">",indice1);

			respuesta = respuesta.substring(0,indice1) + respuesta.substring(indice2 + 1 ,respuesta.length());
			System.out.println("tag" +tag);
			System.out.println("respuesta" +respuesta);

			 indice1=respuesta.indexOf(tag);
		}

		return respuesta;
	}	
	
	public String  eliminatagOpenClose(String respuesta,String tagAbierto,String tagCerrado) {

		int count1=cuentaTagAbierto( tagAbierto,  respuesta  );
		

		int count2=cuentaTagCerrado( tagCerrado,  respuesta  );


			if (count1>count2) {
				respuesta=eliminatagAbierto(tagAbierto,  respuesta, count1,  count2  );
			} else {

				respuesta=eliminatagCerrado(tagCerrado,  respuesta, count1,  count2  );
			}
			return respuesta;
		}
	
	public int cantidaPage1(String respuesta,float marginLeft,float  marginRight,float  marginTop, float marginBottom ) throws DocumentException, IOException {

		Date fecha = new Date();
		String clave = "yyy";
		String ordinario = "ORD-251";
		String idCaso = "100293";
		String tipo = "Felicitaciones".toUpperCase();
		String nombreSolicitante = "ASTRID VALERIA YÁÑEZ BASAURE";
		String de = "LUISAHNA VELANDIA";
		String run = "15410549-2";
		int genero = 1;
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
int numeroPagina=0;

		Font fontNormal = new Font(Font.getFamily("Arial"), 12, Font.NORMAL);
//Font f2= new Font();
		fontNormal.setFamily("Arial");
		fontNormal.setStyle(Font.NORMAL);
		fontNormal.setSize(8);
		Font fontNormalUnderline = new Font();
		fontNormalUnderline.setFamily("Arial");
		fontNormalUnderline.setStyle(Font.UNDERLINE);
		fontNormalUnderline.setSize(12);
		Font paragraNegrita = new Font();
		paragraNegrita.setFamily("Arial");
		paragraNegrita.setStyle(Font.NORMAL);
		paragraNegrita.setSize(12);
		Font paragraFontNormal = new Font();
		paragraFontNormal.setFamily("Arial");
		paragraFontNormal.setStyle(Font.NORMAL);
		paragraFontNormal.setSize(12);
		Paragraph paragraphFirma = new Paragraph(13f);
		Chunk chunkFirma;
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		chunkFirma = new Chunk("Saluda atentamente.,                                                                                          \udddd".toUpperCase(),bfbold);
		paragraphFirma.add(chunkFirma);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);

		 chunkFirma =  new Chunk("SR(A)." + de, new Font(bf, 12));
		paragraphFirma.add(chunkFirma);
		paragraphFirma.setAlignment(Element.ALIGN_CENTER);


		
		chunkFirma = new Chunk(
				"AUGUSTO ESPINOZA NEIRA\r\nJEFE(A) DEPARTAMENTO GESTIÓN CIUDADANA\r\nFONDO NACIONAL DE SALUD", new Font(bf, 12));
        bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		paragraphFirma.add(chunkFirma);

		Document document ;
		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");


			 document = new Document(PageSize.LEGAL, marginLeft,marginRight,marginTop,marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);
		cl.fonasa.pdf.HeadFootOtrosPdf event = new cl.fonasa.pdf.HeadFootOtrosPdf("saaddas","dsfdsfsd","DZN");
		writer.setPageEvent(event);
		document.open();


	

		respuesta="Ahora el futurista  no, en caso de que se apruebe el proyecto de ley que permitirá un segundo retiro del 10% de los fondos de las AFP, acordando que serás más bien un traspié para el \"futuro de las pensiones\r\n" + 
				"\r\n" + 
				"Fuente: Emol.com - https://www.emol.com/noticias/Nacional/2020/10/29/1002197/Bellolio-por-10.htmlvehículo debe cumplir  vehículo debe cumplir  vehículo debe cumplir  vehículo debe cumplir igual trámite ante las autoridades aeronáuticas de Europa para lograr la certificación que le permita volar de forma legal. Desde 2015, el diseño del PAL-V Liberty ha pasado por la certificación de aviación con la Agencia Europea de Seguridad Aérea (EASA), y se espera su finalización en 2022. Si bien el trámite es complejo, explica el sitio Jalopnik, los encargados del proyecto se benefician de la experiencia de certificación acumulada en el programa de pruebas de vuelo anterior y que fue realizado con el PAL-V Uno. Según se explicó, para ser certificado como una aeronave, el PAL-V Liberty debe ser sometido a más de 1200 informes de revisión, los que finalizan con 150 horas de pruebas de vuelo.\r\n" + 
				"\r\n" + 
				"Fuente: mlAhora ccccc el futurista vehículo debe cumplir igual trámite ante las autoridades aeronáuticas de Europa para lograr la certificación que le permita volar de forma legal. Desde 2015, el diseño del PAL-V Liberty ha pasado por la certificación de aviación con la Agencia Europea de Seguridad Aérea (EASA), y se espera su finalización en 2022. Si bien el trámite es complejo, explica el sitio Jalopnik, los encargados del proyecto se benefician de la experiencia de certificación acumulada en el programa de pruebas de vuelo anterior y que fue realizado con el PAL-V Uno. Según se explicó, para ser certificado como una aeronave, el PAL-V Liberty debe ser sometido a más de 1200 informes de revisión, los que finalizan con 150 horas de pruebas de vuelo.\r\n" + 
				"\r\n" + 
				"Fuente: Emdos del proyecto Indicó que el hombre \"se identificó como teniente coronel del Ejército en retiro\", mientras que las víctimas son dos jóvenes. No hubo disparos ni personas lesionadas a raíz del incident\r\n" + 
				"\r\n" + 
				"Fuente: Emol.com - https://www.emol.com/noticias/Nacional/2020/11/02/1002496/Militar-retiro-amenazo-vecinos-arma.htmlse benefician de asadads sasda rrrrrr la experiencia de dddd certificación acumulada XXXX en el  se benefician de la experiencia de certificación acumulada zzzz\r\n" + 
				"\r\n" + 
				"Fuent Ahora el futurista   Indicó que el hombre \"se identificó como teniente coronel del Ejército en retiro\", mientras que las víctimas son dos jóvenes. No hubo disparos ni personas lesionadas a raíz del incident\r\n" + 
				"\r\n" + 
				"Fuente: Emol.com - https://www.emol.com/noticias/Nacional/2020/11/02/1002496/Militar-retiro-amenazo-vecinos-arma.html Ahora el futurista fffffffffffffffffffffffffffffffffffffffffffffffff   Ahora el futurista fffffffffffffffffffffffffffffffffffffffffffffffff   " + 
				"";
	//	log.info("respuesta :::" + respuesta);
		respuesta = "<html>	<style>\r\n" + "			h1 {\r\n" + "			font-size: 40px; " + "			}\r\n"
				+ "			\r\n " + "			p {\r\n" + "  font-family: \"Arial\" "
				+ "			   font-size:12px;\r\n" +

				"			}\r\n "
				+ "	table, th, td {border:1px solid black;\r\n font-family: \"Arial\";font-size: 12pt; width:100%; border-collapse: collapse;}		</style> +<body><p style=\"text-align: justify;\">" + respuesta + "</p></body></html>";
		


		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		numeroPagina=writer.getPageNumber();

		

		document.close();

		FILE.flush();

		FILE.close();
		document.close();
		FILE.flush();
		FILE.close();
		return numeroPagina;
	}
	
	
	public int cantidaPage2(String respuesta ,float  marginLeft,float marginRight,float marginTop) throws DocumentException, IOException {

		Date fecha = new Date();
		String clave = "zzz";
		String ordinario = "ORD-251";
		String idCaso = "100293";
		String tipo = "Felicitaciones".toUpperCase();
		String nombreSolicitante = "ASTRID VALERIA YÁÑEZ BASAURE";
		String de = "LUISAHNA VELANDIA";
		String run = "15410549-2";
		int genero = 1;
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
int numeroPagina=0;

		Font fontNormal = new Font(Font.getFamily("Arial"), 12, Font.NORMAL);
//Font f2= new Font();
		fontNormal.setFamily("Arial");
		fontNormal.setStyle(Font.NORMAL);
		fontNormal.setSize(8);
		Font fontNormalUnderline = new Font();
		fontNormalUnderline.setFamily("Arial");
		fontNormalUnderline.setStyle(Font.UNDERLINE);
		fontNormalUnderline.setSize(12);
		Font paragraNegrita = new Font();
		paragraNegrita.setFamily("Arial");
		paragraNegrita.setStyle(Font.NORMAL);
		paragraNegrita.setSize(12);
		Font paragraFontNormal = new Font();
		paragraFontNormal.setFamily("Arial");
		paragraFontNormal.setStyle(Font.NORMAL);
		paragraFontNormal.setSize(12);
		Paragraph paragraphFirma = new Paragraph(13f);

		Chunk chunkFirma;
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		chunkFirma = new Chunk("Saluda atentamente.,                                                                                          \udddd".toUpperCase(),bfbold);
		paragraphFirma.add(chunkFirma);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);

		 chunkFirma =  new Chunk("SR(A)." + de, new Font(bf, 12));
		paragraphFirma.add(chunkFirma);
		paragraphFirma.setAlignment(Element.ALIGN_CENTER);
;

		
		chunkFirma = new Chunk(
				"AUGUSTO ESPINOZA NEIRA\r\nJEFE(A) DEPARTAMENTO GESTIÓN CIUDADANA\r\nFONDO NACIONAL DE SALUD", new Font(bf, 12));
        bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		paragraphFirma.add(chunkFirma);

		
		Document document ;
		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");
		Rectangle two = new Rectangle(200,200);


			 document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, 56);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);
		cl.fonasa.pdf.HeadFootOtrosPdf event = new cl.fonasa.pdf.HeadFootOtrosPdf("saaddas","dsfdsfsd","DZN");
		writer.setPageEvent(event);
		document.open();

		Paragraph paragraphBody = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(25f);
		Paragraph paragraphead2 = new Paragraph();
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);

		// ******************************Head
		// ******************************************************

		// document.add(paragraphead);
		paragraphead.setAlignment(Element.ALIGN_LEFT);
		PdfPTable tableHeadRight = new PdfPTable(new float[] { 130F, 190F});
//	tableHeadRight.setLockedWidth(true);
	//	tableHeadRight.setWidthPercentage(100f);

		// headParaTit.add(para);

		PdfPCell HeadCellRight1 = new PdfPCell();
		HeadCellRight1.setBorder(Rectangle.NO_BORDER);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		HeadCellRight1 = new PdfPCell();
		HeadCellRight1.setPaddingTop(0f);
		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setBorder(Rectangle.NO_BORDER);
		Paragraph para = new Paragraph();
		para.setFont(bfbold);
		para.add("ORD. N° ");
		para.setFont(new Font(bf, 12));

		para.add(ordinario );
		HeadCellRight1.addElement(para);
		para = new Paragraph();
		para.setFont(bfbold);
		para.add("ANT.: FOLIO N° ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(idCaso );
		HeadCellRight1.addElement(para);
		
		para = new Paragraph();
		para.setFont(bfbold);
		para.add("MAT.: RESPUESTA A  ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(tipo );
		HeadCellRight1.addElement(para);
		
		para = new Paragraph();
		para.setFont(bfbold);
		para.add("SANTIAGO ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add( new SimpleDateFormat("dd/MM/yyyy").format(fecha));
		HeadCellRight1.addElement(para);
		
		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		document.add(tableHeadRight);

		Chunk chunkDE = new Chunk(
				"DE : " + de.trim()
						+ "\r\n        JEFE(A) DEPARTAMENTO GESTIÓN CIUDADANA\r\n        FONDO NACIONAL DE SALUD\r\n",
				new Font(bf, 12));
		if (genero == 1) {
			chunkDE.append("\r\nA: Sr. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else if (genero == 2) {
			chunkDE.append("\r\nA: Sra. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else {
			chunkDE.append("\r\nA: " + nombreSolicitante.trim() + "\r\n\r\n");
		}
		paragraphead2.add(chunkDE);

		document.add(paragraphead2);

	


	//	log.info("respuesta :::" + respuesta);
		respuesta = "<html>	<style>\r\n" + "			h1 {\r\n" + "			font-size: 40px; " + "			}\r\n"
				+ "			\r\n " + "			p {\r\n" + "  font-family: \"Arial\" "
				+ "			   font-size:12px;\r\n" +

				"			}\r\n "
				+ "	table, th, td {border:1px solid black;\r\n font-family: \"Arial\";font-size: 12pt; width:100%; border-collapse: collapse;}		</style> +<body><p style=\"text-align: justify;\">" + respuesta + "</p></body></html>";
		

		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
	


		// ******************************Firma
		// ******************************************************

		// document.add(paragraphFirma);

		// ******************************Firma footer
		// ******************************************************

		PdfPTable tableImgFirma = new PdfPTable(1);
		tableImgFirma.setTotalWidth(50f);

		tableImgFirma.setWidthPercentage(100f);
		PdfPCell image2LeftCell = new PdfPCell();
		image2LeftCell.setBorder(Rectangle.NO_BORDER);
		image2LeftCell.setFixedHeight(128f);

		image2LeftCell.setBorderColor(BaseColor.BLUE);




		PdfContentByte cb = writer.getDirectContent();
		ColumnText ct = new ColumnText(cb);
		Phrase myText = new Phrase(paragraphFirma);
		ct.setSimpleColumn(myText, 0, -750,  580, 250,  15, Element.ALIGN_CENTER);
		ct.go();
		// ******************************newPage
		// ******************************************************


		document.close();

		FILE.flush();

		FILE.close();
		document.close();
		FILE.flush();
		FILE.close();
		return numeroPagina;
	}
	 public void onEndPage(PdfWriter writer, Document document) {
	       System.out.println("qwewqewq");
	        //this.addWatermark(writer);

}
}