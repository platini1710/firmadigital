package cl.fonasa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
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
    private  SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private static final Logger log = LoggerFactory.getLogger(Test.class);
    public static final String FONTBold = "/Font/CalibriBold.ttf";
    public static final String FONTNormal= "/Font/CalibriRegular.ttf";
    public static void main(String[] args) throws DocumentException, MalformedURLException, IOException, org.json.simple.parser.ParseException {
        String clave  ="xxx";
        long ord= 9339 ;

        long idCaso=987201;
        String tipo="aasas";
        String nombreSolicitante="Augusto Espinoza";
        String de="Paulo Salazar";
        String problemaSalud="DIABETES MELLITUS TIPO 2 (7)";
        String nombreTipificacion="garantías GES";
        String respuesta="El problema de salud Cáncer de próstata contempla garantías explícitas en salud (GES), según lo estipula el Decreto Supremo Nº 22 vigente desde el 1/10/2019. Cabe señalar que en concordancia con lo señalado en el Artículo 11 de la ley No se entenderá que hay incumplimiento de la Garantía de oportunidad en los casos de fuerza mayor, caso fortuito, causal médica o causa imputable al beneficiario, lo que deberá ser debidamente acreditado por el FONASA. En su caso, la garantía tratamiento quirúrgico se encuentra exceptuado por indicación médica (seguimiento).";
        Date fecha = new Date();
        Font f2 = FontFactory.getFont(FONTBold, BaseFont.WINANSI, BaseFont.EMBEDDED, 12); 
		f2.setFamily("Calibri");

		f2.setSize(10);
		Font f5 = FontFactory.getFont(FONTNormal, BaseFont.WINANSI, BaseFont.EMBEDDED, 12); 
		f5.setFamily("Calibri");
		f5.setStyle(Font.UNDERLINE);
		f5.setSize(7);
		Font paragraNegrita =FontFactory.getFont(FONTBold, BaseFont.WINANSI, BaseFont.EMBEDDED, 12); 
		paragraNegrita.setFamily("Calibri");

		paragraNegrita.setSize(9);
		Font paragraFontNormal = FontFactory.getFont(FONTNormal, BaseFont.WINANSI, BaseFont.EMBEDDED, 12); 
		paragraFontNormal.setFamily("Calibri");
		paragraFontNormal.setStyle(Font.NORMAL);
		paragraFontNormal.setSize(10);
		log.info("File :: " + clave + ".pdf");
		;
		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");
		log.info("File :: " + clave + ".pdf");
		;
		Document document = new Document(PageSize.A4, 96, 86, 120, 120);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);
		cl.fonasa.pdf.HeadFootPdf event = new cl.fonasa.pdf.HeadFootPdf();
		writer.setPageEvent(event);
		document.open();
		
		
		
		Paragraph paragraphBody = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(25f);
		Paragraph paragraphead2 = new Paragraph();
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		
		
		//******************************Head                ******************************************************
		Chunk chunkOrd = new Chunk("ORD. 1.1G/N°   " + ord  + "  "+ new SimpleDateFormat("dd.MM.yyyy").format(fecha) + " \r\n" + "ANT.: FOLIO N° " + idCaso + "               \r\n"
				+ "MAT.: RESPUESTA A " + tipo + " \r\n\r\n" + "", paragraNegrita);

		paragraphead.add(chunkOrd);	
		//document.add(paragraphead);
		paragraphead.setAlignment(Element.ALIGN_LEFT);
		PdfPTable tableHeadRight = new PdfPTable(new float[] {5, 5 }) ;
		//tableHeadRight.setLockedWidth(true);
		tableHeadRight.setWidthPercentage(100f);
		PdfPCell HeadCellRight1 = new PdfPCell();
		HeadCellRight1.setBorder(Rectangle.NO_BORDER);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);
		
		HeadCellRight1 = new PdfPCell();
		HeadCellRight1.setPaddingTop(0f);
		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setBorder(Rectangle.NO_BORDER);
		HeadCellRight1.addElement(paragraphead);
		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);
		document.add(tableHeadRight);
		
		Chunk chunkDE = new Chunk(
				"DE : " + de
						+ "\r\n        JEFA (S) DEPARTAMENTO GESTIÓN CIUDADANA\r\n        FONDO NACIONAL DE SALUD\r\n",
				paragraNegrita);
		chunkDE.append("\r\nA : SR. " + nombreSolicitante + "\r\n\r\n");
		paragraphead2.add(chunkDE);

		document.add(paragraphead2);

		
		//******************************Body Reclamo                ******************************************************
		Chunk chunkReclamo = new Chunk(
				"Con motivo de la presentación de su reclamo a través de uno de nuestros canales de contacto, "
						+ "en el cual  nos señala su molestia por el incumplimiento de " + nombreTipificacion + " del"
						+ " problema de salud \"" + problemaSalud + "\", le informo lo siguiente:\r\n\n",
				paragraFontNormal);
		paragraphBody.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		paragraphBody.add(chunkReclamo);
		
		
		//******************************Body Respuesta                ******************************************************
		Chunk chunkRespuesta = new Chunk(respuesta + "\r\n\n", f2);
		paragraphBody.add(chunkRespuesta);	
		Chunk chunkRespuesta2 = new Chunk(
				"En caso de disconformidad con el contenido de esta respuesta, usted podrá solicitar a la Superintendencia de Salud su "
						+ "revisión, debiendo acompañar copia de esta carta y de los antecedentes remitidos por esta institución.\r\n\r\n"
						+ "Saluda atentamente",
				paragraFontNormal);

		paragraphBody.add(chunkRespuesta2);
		paragraphBody.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		document.add(paragraphBody);
		
		//******************************Firma                    ******************************************************		
		
		Paragraph paragraphFirma = new Paragraph(13f);
		Chunk chunkFirma = new Chunk(
				"SRA." + de + "\r\nJEFA (S) DEPARTAMENTO GESTIÓN CIUDADANA\r\nFONDO NACIONAL DE SALUD", f2);
		paragraphFirma.setAlignment(Element.ALIGN_CENTER);
		paragraphFirma.add(chunkFirma);
		//document.add(paragraphFirma);
		


		//******************************Firma footer                    ******************************************************		




		PdfPTable tableImgFirma = new PdfPTable(1);
		tableImgFirma.setTotalWidth(50f);
		
		PdfPCell firma = new PdfPCell();
		firma.setHorizontalAlignment(Element.ALIGN_CENTER);
		firma.setBorder(Rectangle.NO_BORDER);
		firma.addElement(paragraphFirma);
		tableImgFirma.addCell(firma);
		
		tableImgFirma.setWidthPercentage(100f);
		PdfPCell image2LeftCell = new PdfPCell();
		image2LeftCell.setBorder(Rectangle.BOX);
		image2LeftCell.setFixedHeight(128f);

		image2LeftCell.setBorderColor(BaseColor.BLUE);

		String imagePathFirma = "/imagen/firma.jpg";
		Image imgFirma = Image.getInstance(GeneradorFilePdf.class.getResource(imagePathFirma));
		imgFirma.scalePercent(30f);
		imgFirma.setAlignment(Element.ALIGN_CENTER);
		image2LeftCell.setImage(imgFirma);
		image2LeftCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		image2LeftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		image2LeftCell.setBorder(Rectangle.NO_BORDER);
		tableImgFirma.addCell(image2LeftCell);

		document.add(tableImgFirma);

		
		//******************************newPage                    ******************************************************		
		document.newPage();


		Paragraph paragraphLorem5 = new Paragraph(13f);
		Chunk chunk5 = new Chunk("DISTRIBUCIÓN:", f5);
		paragraphLorem5.add(chunk5);
		document.add(paragraphLorem5);
		f5.setStyle(Font.NORMAL);
		chunk5 = new Chunk(
				"PANAMA 1037 VILLA M.L. BOMBAL, SANTIAGO\r\nREGION METROPOLITANA\r\nServicio de Salud Metropolitano Occidente\r\nDivisión Gestión Territorial\r\nSubdepartamento GES\r\nOficina de Partes\r\n"
						+ "\r\n\r\n\r\n\r\n",
				f5);

		paragraphLorem5 = new Paragraph(13f);
		paragraphLorem5.add(chunk5);
		document.add(paragraphLorem5);

		Font f6 = new Font();
		f6.setFamily("Lucida Sans");
		f6.setStyle(Font.ITALIC);
		f6.setSize(7);
		Chunk chunk6 = new Chunk(
				"Si usted requiere mayor información de sus garantías o del AUGE en general, puede visitar nuestra página web www.fonasa.cl , en el banner \"Servicios en Línea\" opción \"Seguimiento de Garantías AUGE\" y \"Sistema de Monitoreo de Garantías\", o bien descargando la aplicación  para celulares \"GES Minsal\"."
						+ "\r\n\r\n",
				f6);
		Paragraph paragraphLorem6 = new Paragraph(13f);
		paragraphLorem6.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		paragraphLorem6.add(chunk6);
		document.add(paragraphLorem6);

		String imagePath2 = "/imagen/imagen.jpg";
		Image imagenFirma = Image.getInstance(GeneradorFilePdf.class.getResource(imagePath2));
		PdfPCell imagenCell = new PdfPCell();
		imagenCell.setBorder(Rectangle.NO_BORDER);
		imagenCell.setFixedHeight(10f);
		imagenFirma.scalePercent(100f);
		imagenFirma.setWidthPercentage(50f);
		imagenFirma.setAlignment(Element.ALIGN_LEFT);
		imagenCell.setImage(imagenFirma);
		imagenCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		imagenCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		imagenCell.setPaddingBottom(5f);

		PdfPTable table2 = new PdfPTable(1);
		table2.setWidthPercentage(50f);
		table2.setHorizontalAlignment(Element.ALIGN_LEFT);
		table2.addCell(imagenCell);

		document.add(table2);
		PdfPTable table3 = new PdfPTable(1);
		table3.setWidthPercentage(100f);
		PdfPCell cellText = new PdfPCell();
		Font f7 = new Font();
		f7.setFamily("Calibri");
		f7.setStyle(Font.NORMAL);
		f7.setSize(10);
		Paragraph p = new Paragraph("Fecha Emisión :" + new SimpleDateFormat("dd/MM/yyyy").format(fecha), f7);
		cellText.setVerticalAlignment(Element.ALIGN_CENTER);
		cellText.setBorder(Rectangle.TOP);
		cellText.addElement(p);

		table3.addCell(cellText);
		p = new Paragraph("Código Verificación:  6B327D4E-127ED250-E5BF9647-75E196E8\r\n" + "", f7);
		cellText = new PdfPCell();
		cellText.setVerticalAlignment(Element.ALIGN_CENTER);
		cellText.setBorder(Rectangle.TOP);
		// cellText.addElement(p);
		cellText.setBorder(Rectangle.NO_BORDER);
		table3.addCell(cellText);
		document.add(table3);

		paragraphFirma = new Paragraph(13f);
		f2.setStyle(Font.NORMAL);
		chunkFirma = new Chunk("\r\n\r\nEste documento incorpora firma electrónica avanzada\r\n" + "", f2);
		paragraphFirma.setAlignment(Element.ALIGN_CENTER);
		paragraphFirma.add(chunkFirma);
		document.add(paragraphFirma);
		document.close();
		FILE.flush();
		FILE.close();
				
	            document.close();
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
