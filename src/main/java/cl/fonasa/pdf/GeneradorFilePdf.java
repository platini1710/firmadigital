package cl.fonasa.pdf;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import cl.fonasa.controller.SignDesAtendidaController;
import cl.fonasa.dto.Solicitud;
import cl.fonasa.service.SignFileService;

public class GeneradorFilePdf {

	private static final Logger log = LoggerFactory.getLogger(SignDesAtendidaController.class);

	 public static final String FONT= "/Font/arial.ttf";

		public String generaFileReclamposPdf(Solicitud solicitud, String ordinario,String respuesta, String clave,
				String wsdl, String parrafoUno, String parrafoDos, int[] numPage, String cargo, String institucion,
				String dzFirmante, String subDeptoFirmante, String departamentoFirmante, String direccionSolicitante,
				String iniciales, long numeroSolicitud,String ciudad) throws IOException, DocumentException {
			String nombreSolicitante = solicitud.getNombreSolicitante();
			String nombreTipificacion = solicitud.getNombreTipificacion();
			int ord = solicitud.getOrd();
			String run = solicitud.getRunUsuarioEjecuta();

			String tipo = solicitud.getTipo().toUpperCase();
			String de = solicitud.getDe();
			String problemaSalud = solicitud.getProblemaDeSalud();
			long idCaso = solicitud.getIdCaso();

			int genero = solicitud.getGenero();
			log.info("de :: " + de + ".pdf");
	
			Date fecha = new Date();
			BaseFont fontNormal = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
			Font fontBold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD,
					BaseColor.BLACK);

			Font paragraFontNormal = FontFactory.getFont(FONT, BaseFont.CP1252, BaseFont.EMBEDDED, 10);
			paragraFontNormal.setFamily("Arial");
			paragraFontNormal.setStyle(Font.NORMAL);
			paragraFontNormal.setSize(10);
			Paragraph paragraphFirma = new Paragraph(13f);
			Chunk chunkFirma = new Chunk("SR(A). " + de.toUpperCase(), fontBold);
			paragraphFirma.setAlignment(Element.ALIGN_CENTER);
			paragraphFirma.add(chunkFirma);
			chunkFirma = new Chunk("\r\n" + cargo.toUpperCase(), fontBold);
			paragraphFirma.add(chunkFirma);
			chunkFirma = new Chunk("\r\n" + institucion.toUpperCase(), fontBold);
			paragraphFirma.add(chunkFirma);

			log.debug("File :: " + clave + ".pdf");
			;
			FileOutputStream FILE = new FileOutputStream(clave + ".pdf");
			int marginLeft =60;
			int marginRight =60;
			int marginTop =120;
			int marginBottom =250;
			Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

			PdfWriter writer = PdfWriter.getInstance(document, FILE);
			cl.fonasa.pdf.HeadFootPdf event = new cl.fonasa.pdf.HeadFootPdf();
			writer.setPageEvent(event);
			document.open();

			Paragraph paragraphBody = new Paragraph(13f);
			Paragraph paragraphead = new Paragraph(25f);
			Paragraph paragraphead2 = new Paragraph();
			paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);

			// ******************************Head
			// ******************************************************

			PdfPTable tableHeadRight = new PdfPTable(new float[] { 130F, 190F });
			PdfPCell HeadCellRight1 = new PdfPCell();
			HeadCellRight1.setBorder(Rectangle.NO_BORDER);

			HeadCellRight1.setFixedHeight(80f);
			tableHeadRight.addCell(HeadCellRight1);

			HeadCellRight1 = new PdfPCell();
			HeadCellRight1.setPaddingTop(0f);
			HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
			HeadCellRight1.setBorder(Rectangle.NO_BORDER);
			Paragraph para = new Paragraph();
			para.setFont(fontBold);
			para.add("ORD. " + ordinario);
			para.setFont(new Font(fontNormal, 12));

			para.add("  " + new SimpleDateFormat("dd.MM.yyyy").format(fecha));
			HeadCellRight1.addElement(para);
			para = new Paragraph();
			para.setFont(fontBold);
			para.add("ANT.: FOLIO N° ");
			para.setAlignment(Element.ALIGN_LEFT);
			para.setFont(new Font(fontNormal, 12));
			para.add(String.valueOf(idCaso));
			HeadCellRight1.addElement(para);

			para = new Paragraph();
			para.setFont(fontBold);
			para.add("MAT.: ");
			para.setFont(new Font(fontNormal, 12));
			para.add("RESPUESTA A  ");
			para.setAlignment(Element.ALIGN_LEFT);
			para.setFont(new Font(fontNormal, 12));
			para.add(tipo.toUpperCase());

			HeadCellRight1.addElement(para);

			HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
			HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

			HeadCellRight1.setFixedHeight(80f);
			tableHeadRight.addCell(HeadCellRight1);

			document.add(tableHeadRight);

			Chunk chunkDE = new Chunk("DE : " + de + "\r\n        " + cargo.toUpperCase() + "\r\n        "
					+ institucion.toUpperCase() + "\r\n", fontBold);
			if (genero == 1) {
				chunkDE.append("\r\nA: SR. " + nombreSolicitante.trim() + "\r\n\r\n");
			} else if (genero == 2) {
				chunkDE.append("\r\nA: SRA. " + nombreSolicitante.trim() + "\r\n\r\n");
			} else {
				chunkDE.append("\r\nA: " + nombreSolicitante.trim() + "\r\n\r\n");
			}
			paragraphead2.add(chunkDE);

			document.add(paragraphead2);

			// ******************************Body Reclamo
			// ******************************************************

			respuesta = "<html>	<style>\r\n" + "			h1 {\r\n" + "			font-size: 40px; "
					+ "			}\r\n" + "			\r\n " + "			p {\r\n" + "  font-family: \"Arial\" "
					+ "			   font-size:12px;\r\n" +

					"			}\r\n "
					+ "	table, th, td {border:1px solid black;\r\n font-family: \"Arial\";font-size: 12pt; width:100%; border-collapse: collapse;}	"
					+ "	</style> <body><p style=\"text-align: justify;\">" + respuesta + "</p></body></html>";
			respuesta = respuesta.replaceAll("\\\\n", "\n");
			respuesta = respuesta.replaceAll("\\\\", "");

			respuesta = eliminaAllTag(respuesta, "<div", "</div>");
			respuesta = eliminaAllTag(respuesta, "<col", "</col>");
			respuesta = eliminaAllTag(respuesta, "<colgroup", "</colgroup>");
			respuesta = eliminatagOpenClose(respuesta, "<p", "</p>");// elimina exceso de tags

			int indice1 = respuesta.indexOf("p class=");
			while (indice1 > 0) {
				respuesta = respuesta.substring(0, indice1)
						+ respuesta.substring(indice1, respuesta.indexOf(">", indice1)) + " align=\"justify\" "
						+ respuesta.substring(respuesta.indexOf(">", indice1));
				indice1 = respuesta.indexOf("p class=", indice1 + 1);
			}

			respuesta = respuesta.replaceAll("<br>", "<br/>");
			respuesta = respuesta.replaceAll("<p>", "<p align=\"justify\">");

			InputStream is = new ByteArrayInputStream(respuesta.getBytes());
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			cantidaPage2(respuesta, marginLeft, marginRight, marginTop, marginBottom);
			int cantipaginas2 = cantidaPage2(respuesta, marginLeft, marginRight, marginTop, marginBottom);
	/*		if (writer.getPageNumber() < cantipaginas2) {

				document.newPage();
			}*/

			// ******************************Firma
			// ******************************************************

			// document.add(paragraphFirma);

			// ******************************Firma footer
			// ******************************************************

			// document.add(tableImgFirma);

			// ******************************newPage
			// ******************************************************
			PdfContentByte cb = writer.getDirectContent();

			ColumnText ct = new ColumnText(cb);
			Phrase myText = new Phrase(paragraphFirma);
			ct.setSimpleColumn(myText, 30, -750, 580, 117, 15, Element.ALIGN_CENTER);
			ct.go();
			cb = writer.getDirectContent();
			ct = new ColumnText(cb);
			chunkFirma = new Chunk("        Saluda atentamente.,".toUpperCase(), fontBold);
			myText = new Phrase(chunkFirma);

			ct.setSimpleColumn(myText, 30, -750, 580, 250, 15, Element.ALIGN_LEFT);
			ct.go();
			document.newPage();

			Chunk chunkFoot = new Chunk(iniciales + "  ", new Font(fontNormal, 12));
			Paragraph paragraphFoot = new Paragraph(13f);
			PdfPTable tableFooter = new PdfPTable(1);
			tableFooter.setWidthPercentage(100);
			PdfPCell cellFoot = new PdfPCell();
			paragraphFoot.setAlignment(Paragraph.ALIGN_LEFT);
			paragraphFoot.add(chunkFoot);
			chunkFoot = new Chunk("\r\nDISTRIBUCIÓN: \r\n", new Font(fontNormal, 12));
			paragraphFoot.add(chunkFoot);
			chunkFoot = new Chunk(direccionSolicitante, new Font(fontNormal, 12));
			paragraphFoot.add(chunkFoot);

			document.add(paragraphFoot);

			String imagePath2 = "/imagen/imagen.jpg";
			Image imagenFirma = Image.getInstance(GeneradorFilePdf.class.getResource(imagePath2));
			PdfPCell imagenCell = new PdfPCell();
			imagenCell.setBorder(Rectangle.NO_BORDER);
			imagenCell.setFixedHeight(10f);
			imagenFirma.scalePercent(100f);
			imagenFirma.setWidthPercentage(50f);
			imagenFirma.setAlignment(Element.ALIGN_LEFT);
			// imagenCell.setImage(imagenFirma);
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

			Paragraph p = new Paragraph("Fecha Emisión :" + new SimpleDateFormat("dd/MM/yyyy").format(fecha),
					new Font(fontNormal, 12));
			cellText.setVerticalAlignment(Element.ALIGN_CENTER);
			cellText.setBorder(Rectangle.TOP);
			cellText.addElement(p);

			table3.addCell(cellText);
			SignFileService codigoCertificadoUtil = new SignFileService();
			String codVerificacion = "";
			try {
				String runSinDv = run.substring(0, run.length() - 2) + run.substring(run.length() - 1, run.length());
				codVerificacion = codigoCertificadoUtil.generaCodigoCertificado(runSinDv, "X", "7", wsdl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
				;
			}
			p = new Paragraph("Código Verificación:" + codVerificacion + "r\n" + "", new Font(fontNormal, 12));
			cellText = new PdfPCell();
			cellText.setVerticalAlignment(Element.ALIGN_CENTER);
			cellText.setBorder(Rectangle.TOP);
			cellText.addElement(p);
			cellText.setBorder(Rectangle.NO_BORDER);
			table3.addCell(cellText);
			document.add(table3);

			paragraphFirma = new Paragraph(13f);

			chunkFirma = new Chunk("\r\n\r\nEste documento incorpora firma electrónica avanzada\r\n" + "",
					new Font(fontNormal, 12));
			paragraphFirma.setAlignment(Element.ALIGN_CENTER);
			paragraphFirma.add(chunkFirma);
			document.add(paragraphFirma);
			solicitud.setNumeroPaginas(writer.getPageNumber());
			log.debug("paso numeroPaginas::" + writer.getPageNumber());
			document.close();

			FILE.flush();
			log.debug("getPageNumber: " + writer.getPageNumber());
			numPage[0] = writer.getPageNumber();
			FILE.close();
			return clave + ".pdf";

		}

	public String generaFileFelicitacioPdf( String nombreSolicitante,  String ordinario,String nombreTipificacion,
			String problemaSalud, long idCaso, String respuesta, String clave, int ord, String tipo, String de,
			String wsdl, String run, int genero, String parrafoUno, String parrafoDos, int[] numPage,String cargo,String institucion,
			String dzFirmante,String subDeptoFirmante ,String departamentoFirmante,String direccionSolicitante,String iniciales,String ciudad)
			throws IOException, DocumentException {

		Date fecha = new Date();

		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");

		// log.info("File :: " + clave + ".pdf");;
		int marginLeft =60;
		int marginRight =60;
		int marginTop =120;
		int marginBottom =250;
		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);
		cl.fonasa.pdf.HeadFootOtrosPdf event = new cl.fonasa.pdf.HeadFootOtrosPdf(departamentoFirmante,subDeptoFirmante,dzFirmante);
		writer.setPageEvent(event);
		document.open();

		Paragraph paragraphBody = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(13f);
		Paragraph paragrapHeadLeft = new Paragraph(13f);
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
		/*
		 * --------------------------------------------Head-----------------------------
		 * ------------------
		 */


		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);

		// ******************************Head
		// ******************************************************

		// document.add(paragraphead);
		paragraphead.setAlignment(Element.ALIGN_LEFT);
		PdfPTable tableHeadRight = new PdfPTable(new float[] { 130F, 190F});
//	tableHeadRight.setLockedWidth(true);
	//	tableHeadRight.setWidthPercentage(100f);

		// headParaTit.add(para);
		
		


		//-----------------------------------------------------------------------------------**************************************
		 PdfPTable tableLeft = new PdfPTable(3);
        PdfPCell text = new PdfPCell();
	      Paragraph headParaTit = new Paragraph();

        headParaTit = new Paragraph(8);
        headParaTit.setAlignment(Element.ALIGN_LEFT);
     
        Paragraph paraLeft = new Paragraph (); 
        paraLeft.setFont(bfbold);
        paraLeft.add("FONDO NACIONAL DE SALUD");
 
        headParaTit.add(paraLeft);


        paraLeft = new Paragraph (); 
        paraLeft.setFont(bfbold);
/*        division="DIVISIÓN SERVICIO AL USUARIO" ; 
        if (dzFirmante!=null) {
            if ("DZN".equals(dzFirmante.toUpperCase())) {
            	division="DIRECCIÓN ZONAL NORTE";
            }else if ("DZCN".equals(dzFirmante.toUpperCase())) {
            	division="DIRECCIÓN ZONAL CENTRO NORTE" ; 
            }else if ("DZCS".equals(dzFirmante.toUpperCase())) {
            	division="DIRECCIÓN ZONAL CENTRO SUR" ; 
            }
            else if ("DZS".equals(dzFirmante.toUpperCase())) {
            	division="DIRECCIÓN ZONAL SUR" ; 
            } else {
            	division="DIVISIÓN SERVICIO AL USUARIO" ; 
            }
        }
*/
        paraLeft.add(dzFirmante);
        headParaTit.add(paraLeft);
        paraLeft = new Paragraph ();  
        paraLeft.setFont(bfbold);
        if (departamentoFirmante==null) {
        		departamentoFirmante=" ";
        }

        paraLeft.add(departamentoFirmante.toUpperCase());
        headParaTit.add(paraLeft);
		log.info("departamentoFirmante 	::" + departamentoFirmante);;
		paraLeft = new Paragraph ();  
		paraLeft.setFont(bfbold);
		log.info(" subDeptoFirmante ::::::::::::::::::::::::::::::::::::::::: " + subDeptoFirmante);;
		if ((subDeptoFirmante!=null) && (!"".equals(subDeptoFirmante.trim()))) {
		paraLeft.add(subDeptoFirmante.toUpperCase());
		}
        headParaTit.add(paraLeft);
        
        text.setColspan(3);
        text.setVerticalAlignment(Element.ALIGN_TOP);
        text.addElement(headParaTit);
        text.setPaddingTop(0);
        text.setBorder(Rectangle.NO_BORDER);

        text.setBorder(Rectangle.NO_BORDER);
        tableLeft.addCell(text);    
        tableLeft.setWidthPercentage(100);
        PdfPCell text2 = new PdfPCell();
        
        text2.setBorder(Rectangle.NO_BORDER);
  

        text2.setColspan(3);
        tableLeft.addCell(text2); 
        
		document.add(tableLeft);
		
		//------------------------------------------------------**********************************************************
		
		

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
		para.add("ORD. " + ordinario);
		para.setFont(new Font(bf, 12));

		//para.add(ordinario.substring(4) );
		HeadCellRight1.addElement(para);
		para = new Paragraph();
		para.setFont(bfbold);
		para.add("ANT.: FOLIO N° ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(String.valueOf(idCaso));
		HeadCellRight1.addElement(para);
		
		para = new Paragraph();

		para.setFont(bfbold);
		para.add("MAT.: ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add("RESPUESTA A ");
		para.setFont(new Font(bf, 12));
		para.add(tipo.toUpperCase() );
		para.setAlignment(Element.ALIGN_LEFT);
	
		
	
		HeadCellRight1.addElement(para);
		log.info(" dzFirmante ::" +dzFirmante);
		para = new Paragraph();
		para.setFont(bfbold);
	
		para.add(ciudad  + " ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add( new SimpleDateFormat("dd-MM-yyyy").format(fecha));
		HeadCellRight1.addElement(para);
		
		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		document.add(tableHeadRight);
		
		
		
		
		
		System.out.println("cargo   " +cargo);
		System.out.println("institucion  " +institucion);
		Chunk chunkHeadleft= new Chunk(
				"DE : " + de.toUpperCase()
						+ "\r\n        " +cargo.toUpperCase()  +"\r\n        " +institucion.toUpperCase() + "\r\n",
						bfbold);
		if (genero == 1) {
			chunkHeadleft.append("\r\nA: SR. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else if (genero == 2) {
			chunkHeadleft.append("\r\nA: SRA. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else {
			chunkHeadleft.append("\r\nA: " + nombreSolicitante.trim() + "\r\n\r\n");
		}



		Paragraph paragraphFirma= new Paragraph(13f);
		Paragraph saludoAntesDeFirma= new Paragraph(13f);
		Chunk chunkFirma ;
		//// paragraphBody.add(chunk2);Saluda atentamente.,
		chunkFirma = new Chunk("      ".toUpperCase(),bfbold);

		paragraphFirma.setAlignment(Element.ALIGN_LEFT);
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
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);	
		paragraphFirma.setAlignment(Element.ALIGN_LEFT);
		chunkFirma = new Chunk(de.toUpperCase(),bfbold );
		paragraphFirma.add(chunkFirma);
		paragraphFirma.setAlignment(Element.ALIGN_CENTER);
		chunkFirma = new Chunk( "\r\n" + cargo.toUpperCase(),bfbold );
		paragraphFirma.add(chunkFirma);
		chunkFirma = new Chunk("\r\n" +  institucion.toUpperCase(),bfbold );
		paragraphFirma.add(chunkFirma);	
		paragrapHeadLeft.add(chunkHeadleft);
		document.add(paragrapHeadLeft);



		// document.add(tableBody);

		respuesta = "<html>	<style>\r\n" + "			h1 {\r\n" + "			font-size: 40px; " + "			}\r\n"
				+ "			\r\n " + "			p {\r\n" + "  font-family: \"Arial\" "
				+ "			   font-size:12px;\r\n" +

				"			}\r\n "
				+ "	table, th, td {border:1px solid black;\r\n font-family: \"Arial\";font-size: 12pt; width:100%; border-collapse: collapse;}		</style> <body><p style=\"text-align: justify;\">" + respuesta + "</p></body></html>";
		respuesta= respuesta.replaceAll("\\\\n", "\n");
		respuesta=respuesta.replaceAll("\\\\","");
		respuesta = eliminaAllTag(respuesta, "<div", "</div>");
		respuesta=eliminaAllTag(respuesta,"<col","</col>");
		respuesta = eliminaAllTag(respuesta, "<colgroup", "</colgroup>");
		respuesta = eliminatagOpenClose(respuesta, "<p", "</p>");// elimina exceso de tags
		int indice1 = respuesta.indexOf("p class=");
		while (indice1 > 0) {
			respuesta = respuesta.substring(0, indice1) + respuesta.substring(indice1, respuesta.indexOf(">", indice1))
					+ " align=\"justify\" " + respuesta.substring(respuesta.indexOf(">", indice1));
			indice1 = respuesta.indexOf("p class=", indice1 + 1);
		}
		respuesta = respuesta.replaceAll("<br>", "<br/>");
		respuesta=respuesta.replaceAll("<p>", "<p align=\"justify\">"); 
log.debug("respuesta::" + respuesta);
		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		int cantipaginas2 =cantidaPage2(respuesta, marginLeft, marginRight, marginTop, marginBottom) ;
	
		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(50f);
		PdfPCell image2LeftCell = new PdfPCell();

		image2LeftCell.setFixedHeight(128f);

		image2LeftCell.setBorderColor(BaseColor.BLUE);

		/*
		 * String imagePath = "/imagen/firma.jpg"; Image image3 =
		 * Image.getInstance(GeneradorFilePdf.class.getResource(imagePath));
		 * image3.scalePercent(30f); image3.setAlignment(Element.ALIGN_CENTER);
		 * image2LeftCell.setImage(image3);
		 */
		image2LeftCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		image2LeftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		image2LeftCell.setBorder(Rectangle.NO_BORDER);
		table.addCell(image2LeftCell);

		image2LeftCell = new PdfPCell();
		image2LeftCell.addElement(paragraphFirma);
		image2LeftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		image2LeftCell.setBorder(Rectangle.NO_BORDER);
		table.addCell(image2LeftCell);
		// document.add(table);

		// document.add(paragraphLorem4);
		PdfContentByte cb = writer.getDirectContent();
		ColumnText ct = new ColumnText(cb);
		Phrase myText = new Phrase(paragraphFirma);
		ct.setSimpleColumn(myText, 30, -750, 580, 300, 15, Element.ALIGN_CENTER);
		ct.go();
		 cb = writer.getDirectContent();
		 ct = new ColumnText(cb);
			chunkFirma = new Chunk("        Saluda atentamente.,".toUpperCase(),bfbold);
		 myText = new Phrase(chunkFirma);
		
		ct.setSimpleColumn(myText, 30, -750,  580, 250,  15, Element.ALIGN_LEFT);
		ct.go();
		document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
		document.newPage();
		Chunk chunkFoot = new Chunk(iniciales +"  ", new Font(bf, 12));
		Paragraph paragraphFoot = new Paragraph(13f);
		PdfPTable tableFooter = new PdfPTable(1);
		tableFooter.setWidthPercentage(100);
		PdfPCell cellFoot = new PdfPCell();
		paragraphFoot.setAlignment(Paragraph.ALIGN_LEFT);
		paragraphFoot.add(chunkFoot);
		chunkFoot = new Chunk("\r\nDISTRIBUCIÓN: \r\n", new Font(bf, 12));
		paragraphFoot.add(chunkFoot);
		chunkFoot = new Chunk(direccionSolicitante, new Font(bf, 12));
		paragraphFoot.add(chunkFoot);
		paragraphFoot.setAlignment(Element.ALIGN_LEFT);
		cellFoot.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFoot.setVerticalAlignment(Element.ALIGN_LEFT);
		cellFoot.addElement(paragraphFoot);
		cellFoot.setBorder(Rectangle.NO_BORDER);
		tableFooter.addCell(cellFoot);
		SignFileService codigoCertificadoUtil = new SignFileService();
		String codVerificacion = "";
		try {
			String runSinDv = run.substring(0, run.length() - 2) + run.substring(run.length() - 1, run.length());
			codVerificacion = codigoCertificadoUtil.generaCodigoCertificado(runSinDv, "X", "7", wsdl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			;
		}
		cellFoot = new PdfPCell();
		paragraphFoot = new Paragraph(13f);
		chunkFoot = new Chunk("\r\n" + codVerificacion + "\r\nCódigo de Verificación ", new Font(bf, 12));
		paragraphFoot.add(chunkFoot);
		paragraphFoot.setAlignment(Element.ALIGN_CENTER);
		cellFoot.addElement(paragraphFoot);
		cellFoot.setBorder(Rectangle.NO_BORDER);
		tableFooter.addCell(cellFoot);
		document.add(tableFooter);
		log.info("getPageNumber: " + writer.getPageNumber());
		numPage[0] = writer.getPageNumber();
		document.close();
		FILE.flush();
		FILE.close();
		return clave + ".pdf";
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
		
	public String eliminatagAbierto(String tag, String respuesta, int count1, int count2) {
		  if ("<p".equalsIgnoreCase(tag )) {
			  tag="<p>";
		  }
		int indice1 = respuesta.indexOf(tag);
		while ((indice1 > -1) && (Math.abs(count1 - count2)) > 0) {
			int indice2 = respuesta.indexOf(">", indice1);
			respuesta = respuesta.substring(0, indice1) + respuesta.substring(indice2 + 1, respuesta.length());
			count2 = count2 + 1;
			indice1 = respuesta.indexOf(tag);
		}
		return respuesta;
	}
	public String eliminatagCerrado(String tag, String respuesta,int count1, int count2  ) {
		 int  indice1=respuesta.indexOf(tag);
		while ((indice1>-1) && (Math.abs(count1-count2))>0){
			 int  indice2=respuesta.indexOf(">",indice1);
			respuesta = respuesta.substring(0,indice1) + respuesta.substring(indice2 +1 ,respuesta.length());
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
			respuesta = respuesta.substring(0,indice1) + respuesta.substring(indice2 +1 ,respuesta.length());		
			 indice1=respuesta.indexOf(tag);
		}
		return respuesta;
	}	
	
	public String eliminatagOpenClose(String respuesta, String tagAbierto, String tagCerrado) {
		
		int count1 = cuentaTagAbierto(tagAbierto, respuesta);
		int count2 = cuentaTagCerrado(tagCerrado, respuesta);
		if (count1 > count2) {
			respuesta = eliminatagAbierto(tagAbierto, respuesta, count1, count2);
		} else {
			respuesta = eliminatagCerrado(tagCerrado, respuesta, count1, count2);
		}
		return respuesta;
	}
	
	
	
	
	
	
	
	public int cantidaPage1(String respuesta,float marginLeft,float marginRight,float marginTop,float marginBottom)
			throws IOException, DocumentException {
		int numeroPagina=0;
		FileOutputStream FILE = new FileOutputStream("yyy" + ".pdf");
		// log.info("File :: " + clave + ".pdf");;

		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);


		document.open();


		Paragraph paragraphBody = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(13f);
		Paragraph paragrapHeadLeft = new Paragraph(13f);
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
		/*
		 * --------------------------------------------Head-----------------------------
		 * ------------------
		 */


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

		para.add("120" );
		HeadCellRight1.addElement(para);
		para = new Paragraph();
		para.setFont(bfbold);
		para.add("ANT.: FOLIO N° ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(String.valueOf(123L));
		HeadCellRight1.addElement(para);
		
		para = new Paragraph();

		para.setFont(bfbold);
		para.add("MAT.: ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add("RESPUESTA A ");
		para.setFont(new Font(bf, 12));
		para.add("tipo".toUpperCase() );
		para.setAlignment(Element.ALIGN_LEFT);
	
		
	
		HeadCellRight1.addElement(para);

		para = new Paragraph();
		para.setFont(bfbold);
		String ciudad="SANTIAGO, " ;

		para.add(ciudad);
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add("12-122020");
		HeadCellRight1.addElement(para);
		
		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		

		Chunk chunkHeadleft= new Chunk(
				"DE : " + "de".toUpperCase()
						+ "\r\n        " +"cargo".toUpperCase()  +"\r\n        " +"institucion".toUpperCase() + "\r\n",
						bfbold);

			chunkHeadleft.append("\r\nA: SR. " + "XXXXX" + "\r\n\r\n");




			paragrapHeadLeft.add(chunkHeadleft);
			document.add(paragrapHeadLeft);










		// document.add(tableBody);


		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		numeroPagina=writer.getPageNumber();
		document.close();
		FILE.flush();
		FILE.close();
		return numeroPagina;
	
	}
	public int cantidaPage2(String respuesta,float marginLeft,float marginRight,float marginTop,float marginBottom)
			throws IOException, DocumentException {

		int numeroPagina=0;
		FileOutputStream FILE = new FileOutputStream("zzz" + ".pdf");
		// log.info("File :: " + clave + ".pdf");;

			marginBottom=marginBottom+ 80f;
		
		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);


		document.open();



		
		Paragraph paragraphBody = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(13f);
		Paragraph paragrapHeadLeft = new Paragraph(13f);
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
		/*
		 * --------------------------------------------Head-----------------------------
		 * ------------------
		 */


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

		para.add("120" );
		HeadCellRight1.addElement(para);
		para = new Paragraph();
		para.setFont(bfbold);
		para.add("ANT.: FOLIO N° ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(String.valueOf(123L));
		HeadCellRight1.addElement(para);
		
		para = new Paragraph();

		para.setFont(bfbold);
		para.add("MAT.: ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add("RESPUESTA A ");
		para.setFont(new Font(bf, 12));
		para.add("tipo".toUpperCase() );
		para.setAlignment(Element.ALIGN_LEFT);
	
		
	
		HeadCellRight1.addElement(para);

		para = new Paragraph();
		para.setFont(bfbold);
		String ciudad="SANTIAGO, " ;

		para.add(ciudad);
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add("12-122020");
		HeadCellRight1.addElement(para);
		
		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		document.add(tableHeadRight);
		
		
		

		Chunk chunkHeadleft= new Chunk(
				"DE : " + "de".toUpperCase()
						+ "\r\n        " +"cargo".toUpperCase()  +"\r\n        " +"institucion".toUpperCase() + "\r\n",
						bfbold);

			chunkHeadleft.append("\r\nA: SR. " + "XXXXX" + "\r\n\r\n");




			paragrapHeadLeft.add(chunkHeadleft);
			document.add(paragrapHeadLeft);






		// document.add(tableBody);


		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		numeroPagina=writer.getPageNumber();
		document.close();
		FILE.flush();
		FILE.close();
		return numeroPagina;
	}
	public String calcOrdinario(String zona) {
		String ord="4.1K/N° ";
		if ("DZN".equals(zona)) {
			ord= "1P/N° ";
		}else if ("DZCN".equals(zona)) {
			ord= "1R/N° ";
		}else if ("DZCS".equals(zona)) {
			ord= "1S/N° ";
		}else if ("DZS".equals(zona)) {
			ord= "1T/N° ";
		}
		return ord;
	}
	



}
