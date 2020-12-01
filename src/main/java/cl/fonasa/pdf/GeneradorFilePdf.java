package cl.fonasa.pdf;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

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
import com.itextpdf.tool.xml.XMLWorkerHelper;

import cl.fonasa.controller.SignDesAtendidaController;
import cl.fonasa.dto.Solicitud;
import cl.fonasa.service.SignFileService;

public class GeneradorFilePdf {

	private static final Logger log = LoggerFactory.getLogger(SignDesAtendidaController.class);

	public static final String FONT = "/Font/arial.ttf";

	public String generaFileReclamposPdf(Solicitud solicitud, String ordinario, String respuesta, String clave,
			String wsdl, String parrafoUno, String parrafoDos, int[] numPage, String cargo, String institucion,
			String dzFirmante, String subDeptoFirmante, String departamentoFirmante, String direccionSolicitante,
			String iniciales, long numeroSolicitud, String ciudad, String firma) throws IOException, DocumentException {
		String nombreSolicitante = solicitud.getNombreSolicitante().toUpperCase();
		String nombreTipificacion = solicitud.getNombreTipificacion().toUpperCase();
		int ord = solicitud.getOrd();
		String run = solicitud.getRunUsuarioEjecuta();

		String tipo = solicitud.getTipo().toUpperCase();
		String de = solicitud.getDe().toUpperCase();
		String problemaSalud = solicitud.getProblemaDeSalud();
		long idCaso = solicitud.getIdCaso();

		int genero = solicitud.getGenero();

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
		if (log.isDebugEnabled()) {
			log.debug("File :: [" + clave + "].pdf");
		}
		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");
		int marginLeft = 60;
		int marginRight = 60;
		int marginTop = 120;
		int marginBottom = 260;

		respuesta = "<html>	<style>\r\n" + "			h1 {\r\n" + "			font-size: 40px; " + "			}\r\n"
				+ "			\r\n " + "			p {\r\n" + "  font-family: \"Arial\" "
				+ "			   font-size:12px;\r\n" +

				"			}\r\n "
				+ "	table, th{\r\n"
				+ "  text-align: center;\r\n"
				+ "}, td {    vertical-align: middle;border:1px solid black;\r\n font-family: \"Arial\";font-size: 12pt; width:100%; border-collapse: collapse;}	"
				+ "	</style> <body><p style=\"text-align: justify;\">" + respuesta + "</p></body></html>";
		respuesta = respuesta.replaceAll("\\\\n", "\n");
		respuesta = respuesta.replaceAll("\\\\", "");

		respuesta = eliminaAllTag(respuesta, "<div", "</div>");
		respuesta = eliminaAllTag(respuesta, "<col", "</col>");
		respuesta = eliminaAllTag(respuesta, "<colgroup", "</colgroup>");
		respuesta = eliminatagOpenClose(respuesta, "<p", "</p>");// elimina exceso de tags

		int indice1 = respuesta.indexOf("p class=");
		while (indice1 > 0) {
			respuesta = respuesta.substring(0, indice1) + respuesta.substring(indice1, respuesta.indexOf(">", indice1))
					+ " align=\"justify\" " + respuesta.substring(respuesta.indexOf(">", indice1));
			indice1 = respuesta.indexOf("p class=", indice1 + 1);
		}

		respuesta = respuesta.replaceAll("<br>", "<br/>");
		respuesta = respuesta.replaceAll("<p>", "<p align=\"justify\">");
		int intArray[] = new int[1];
		;
		respuesta = convertImagen(respuesta, clave, intArray);
		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);
		int cant = countPageFileReclamposPdf(marginLeft, marginRight, marginTop, marginBottom, solicitud, ordinario,
				respuesta, clave, wsdl, parrafoUno, parrafoDos, numPage, cargo, institucion, dzFirmante,
				subDeptoFirmante, departamentoFirmante, direccionSolicitante, iniciales, numeroSolicitud, ciudad);
		cl.fonasa.pdf.HeadFootPdf event = new cl.fonasa.pdf.HeadFootPdf(cant, paragraphFirma);
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

		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		String imagePath = "firma.jpg";
		//
		byte[] data = DatatypeConverter.parseBase64Binary(firma);

		File fileFirma = new File(imagePath);
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileFirma))) {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image2 = Image.getInstance(imagePath);
		image2.scalePercent(50f);
		image2.setAlignment(Element.ALIGN_CENTER);
		imagePath = "/imagen/firma.png";
		PdfPTable tableFirma = new PdfPTable(2);
		float[] widths = new float[] { 1f, 2f };
		tableFirma.setWidths(widths);

		tableFirma.setKeepTogether(true);
		tableFirma.setWidthPercentage(100);
		tableFirma.setKeepTogether(true);
		tableFirma.setWidthPercentage(100);
		PdfPCell cellFirma = new PdfPCell();

		Font fontBoldFirma = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9, Font.BOLD,
				BaseColor.BLACK);
		Chunk chunkfoot4 = new Chunk("SALUDA ATENTAMENTE,                             ", fontBoldFirma);
		cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellFirma.addElement(chunkfoot4);
		cellFirma.setPaddingBottom(1f);
		cellFirma.setVerticalAlignment(Element.ALIGN_TOP);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setFixedHeight(29f);
		tableFirma.addCell(cellFirma);

		cellFirma = new PdfPCell();
		chunkfoot4 = new Chunk("            Por orden del Director", fontBoldFirma);
		cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellFirma.addElement(chunkfoot4);
		cellFirma.setPaddingBottom(1f);
		cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFirma.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setFixedHeight(29f);
		tableFirma.addCell(cellFirma);

		cellFirma = new PdfPCell();
		cellFirma.setPaddingTop(0f);
		cellFirma.addElement(image2);
		cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setColspan(2);

		tableFirma.addCell(cellFirma);

		cellFirma = new PdfPCell();
		cellFirma.addElement(paragraphFirma);
		cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setColspan(2);
		tableFirma.setKeepTogether(true);
		tableFirma.setWidthPercentage(100);
		tableFirma.addCell(cellFirma);

		document.add(tableFirma);

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

		Paragraph paragraphFoot2 = new Paragraph(13f);

		chunkFirma = new Chunk("\r\n\r\nEste documento incorpora firma electrónica avanzada\r\n" + "",
				new Font(fontNormal, 12));
		paragraphFoot2.setAlignment(Element.ALIGN_CENTER);
		paragraphFoot2.add(chunkFirma);
		document.add(paragraphFoot2);
		solicitud.setNumeroPaginas(writer.getPageNumber());
		if (log.isDebugEnabled()) {
			log.debug("paso numeroPaginas::[" + writer.getPageNumber() + "]");
		}

		document.close();

		FILE.flush();
		if (log.isDebugEnabled()) {
			log.debug("getPageNumber: [" + writer.getPageNumber() + "]");
		}
		numPage[0] = writer.getPageNumber();
		FILE.close();
		if (intArray[0] > 0) {
			try {
				int cont = 0;
				while (cont < (intArray[0])) {
					File file = new File(clave + cont + ".png");
					file.delete();
					cont = cont + 1;
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return clave + ".pdf";

	}

	public String generaFileFelicitacioPdf(String nombreSolicitante, String ordinario, String nombreTipificacion,
			String problemaSalud, long idCaso, String respuesta, String clave, int ord, String tipo, String de,
			String wsdl, String run, int genero, String parrafoUno, String parrafoDos, int[] numPage, String cargo,
			String institucion, String dzFirmante, String subDeptoFirmante, String departamentoFirmante,
			String direccionSolicitante, String iniciales, String ciudad, String firma)
			throws IOException, DocumentException {

		Paragraph paragraphBody = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(13f);
		Paragraph paragrapHeadLeft = new Paragraph(13f);
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
		Paragraph paragraphFirma = new Paragraph(13f);
	     StringBuilder respuestaB 
         = new StringBuilder();
	     respuestaB.append( "<html>	<style>\r\n" + "			h1 {\r\n" + "			font-size: 40px; " + "			}\r\n");
	     respuestaB.append(  "			\r\n " + "			p {\r\n" + "  font-family: \"Arial\" " );
	     respuestaB.append(  "			   font-size:12px;\r\n" );

		respuestaB.append( 				"			}\r\n " );
		respuestaB.append(  "	table {\r\n font-family: arial;\r\n" );
		respuestaB.append( "	  border: 1px solid #000000;\r\n" );
		respuestaB.append(  "  border-collapse: collapse;\r\n" );
		respuestaB.append(  "  width: 100%;\r\n" );
		
		respuestaB.append(  "} th{\r\n" );
		respuestaB.append( "	  padding: 8px;text-align: center;\r\n vertical-align: middle;\r\n border:1px solid #000000;\r\n font-family: \"Arial\";font-size: 8pt; width:100%; border-collapse: collapse;" );
		respuestaB.append(  "}\r\n  td {  padding: 4px;text-align: left;\r\n vertical-align: middle;\r\n border:1px solid #000000;\r\n font-family: \"Arial\";font-size: 8pt; width:100%; border-collapse: collapse;}		</style> <body><p style=\"text-align: justify;\">" );
		respuestaB.append( respuesta + "</p></body></html>");
		respuesta=respuestaB.toString();
		respuesta = respuesta.replaceAll("\\\\n", "\n");
		respuesta = respuesta.replaceAll("\\\\", "");
		respuesta = eliminaAllTag(respuesta, "<div", "</div>");
		respuesta = eliminaAllTag(respuesta, "<col", "</col>");
		respuesta = eliminaAllTag(respuesta, "<colgroup", "</colgroup>");
		respuesta = eliminatagOpenClose(respuesta, "<p", "</p>");// elimina exceso de tags
		int indice1 = respuesta.indexOf("p class=");
		while (indice1 > 0) {
			respuesta = respuesta.substring(0, indice1) + respuesta.substring(indice1, respuesta.indexOf(">", indice1))
					+ " align=\"justify\" " + respuesta.substring(respuesta.indexOf(">", indice1));
			indice1 = respuesta.indexOf("p class=", indice1 + 1);
		}
		respuesta = respuesta.replaceAll("<br>", "<br/>");
		respuesta = respuesta.replaceAll("<p>", "<p align=\"justify\">");
		int intArray[] = new int[1];
		;
		respuesta = convertImagen(respuesta, clave, intArray);
		if (log.isDebugEnabled()) {
			log.debug("respuesta ::" + respuesta);
		}
		Chunk chunkFirma;
		//// paragraphBody.add(chunk2);Saluda atentamente,
		chunkFirma = new Chunk("      ".toUpperCase(), bfbold);

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
		paragraphFirma.add(Chunk.NEWLINE);
		paragraphFirma.add(Chunk.NEWLINE);

		paragraphFirma.setAlignment(Element.ALIGN_LEFT);
		chunkFirma = new Chunk(de.toUpperCase(), bfbold);
		paragraphFirma.add(chunkFirma);
		paragraphFirma.setAlignment(Element.ALIGN_CENTER);
		chunkFirma = new Chunk("\r\n" + cargo.toUpperCase(), bfbold);
		paragraphFirma.add(chunkFirma);
		chunkFirma = new Chunk("\r\n" + institucion.toUpperCase(), bfbold);
		paragraphFirma.add(chunkFirma);
		Date fecha = new Date();

		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");

		// log.info("File :: " + clave + ".pdf");;
		int marginLeft = 60;
		int marginRight = 60;
		int marginTop = 120;
		int marginBottom = 260;
		int cantConFirmas = cantidaPageFelicitacionConFirma(marginLeft, marginRight, marginTop, marginBottom,
				nombreSolicitante, ordinario, nombreTipificacion, problemaSalud, idCaso, respuesta, clave, ord, tipo,
				de, wsdl, run, genero, parrafoUno, parrafoDos, numPage, cargo, institucion, dzFirmante,
				subDeptoFirmante, departamentoFirmante, direccionSolicitante, iniciales, ciudad, firma);
		int cantSinFirmas = cantidaPageFelicitacionSinFirma(marginLeft, marginRight, marginTop, marginBottom,
				nombreSolicitante, ordinario, nombreTipificacion, problemaSalud, idCaso, respuesta, clave, ord, tipo,
				de, wsdl, run, genero, parrafoUno, parrafoDos, numPage, cargo, institucion, dzFirmante,
				subDeptoFirmante, departamentoFirmante, direccionSolicitante, iniciales, ciudad, firma);
		numPage[1] = cantConFirmas;
		numPage[2] = cantSinFirmas;
		if (log.isDebugEnabled()) {
			log.debug("cantConFirmas ::" + cantConFirmas);
			log.debug("cantSinFirmas ::" + cantSinFirmas);
		}
		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);

		cl.fonasa.pdf.HeadFootOtrosPdf event = new cl.fonasa.pdf.HeadFootOtrosPdf(departamentoFirmante,
				subDeptoFirmante, dzFirmante, cantConFirmas, cantSinFirmas, paragraphFirma);
		writer.setPageEvent(event);

		document.open();

		/*
		 * --------------------------------------------Head-----------------------------
		 * ------------------
		 */

		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);

		// ******************************Head
		// ******************************************************

		// document.add(paragraphead);
		paragraphead.setAlignment(Element.ALIGN_LEFT);
		PdfPTable tableHeadRight = new PdfPTable(new float[] { 130F, 190F });
//	tableHeadRight.setLockedWidth(true);
		// tableHeadRight.setWidthPercentage(100f);

		// headParaTit.add(para);

		// -----------------------------------------------------------------------------------**************************************
		PdfPTable tableLeft = new PdfPTable(3);
		PdfPCell text = new PdfPCell();
		Paragraph headParaTit = new Paragraph();

		headParaTit = new Paragraph(8);
		headParaTit.setAlignment(Element.ALIGN_LEFT);

		Paragraph paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);
		paraLeft.add("FONDO NACIONAL DE SALUD");

		headParaTit.add(paraLeft);

		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);

		paraLeft.add(dzFirmante);
		headParaTit.add(paraLeft);
		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);
		if (departamentoFirmante == null) {
			departamentoFirmante = " ";
		}
		if (!"".equals(departamentoFirmante.trim())) {

			paraLeft.add(departamentoFirmante.toUpperCase());
			headParaTit.add(paraLeft);
		}
		if (log.isDebugEnabled()) {
			log.debug("departamentoFirmante 	::[" + departamentoFirmante + "]");
		}
		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);
		if (log.isDebugEnabled()) {
			log.debug(" subDeptoFirmante ::::::::::::::::::::::::::::::::::::::::: [" + subDeptoFirmante + "]");
		}
		if ((subDeptoFirmante != null) && (!"".equals(subDeptoFirmante.trim()))) {
			paraLeft.add(subDeptoFirmante.toUpperCase());
			headParaTit.add(paraLeft);
		}

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

		// ------------------------------------------------------**********************************************************

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

		// para.add(ordinario.substring(4) );
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
		para.add(tipo.toUpperCase());
		para.setAlignment(Element.ALIGN_LEFT);

		HeadCellRight1.addElement(para);
		if (log.isDebugEnabled()) {
			log.debug(" dzFirmante ::" + dzFirmante);
		}
		para = new Paragraph();
		para.setFont(bfbold);

		para.add(ciudad + " ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(new SimpleDateFormat("dd-MM-yyyy").format(fecha));
		HeadCellRight1.addElement(para);

		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		document.add(tableHeadRight);

		Chunk chunkHeadleft = new Chunk("DE : " + de.toUpperCase() + "\r\n        " + cargo.toUpperCase()
				+ "\r\n        " + institucion.toUpperCase() + "\r\n", bfbold);
		if (genero == 1) {
			chunkHeadleft.append("\r\nA: SR. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else if (genero == 2) {
			chunkHeadleft.append("\r\nA: SRA. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else {
			chunkHeadleft.append("\r\nA: " + nombreSolicitante.trim() + "\r\n\r\n");
		}

		paragrapHeadLeft.add(chunkHeadleft);
		document.add(paragrapHeadLeft);

		// document.add(tableBody);
		if (log.isDebugEnabled()) {
			log.debug("respuesta::" + respuesta);
		}
		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		/*
		 * 
		 * start firma
		 * 
		 * 
		 */

		if (cantConFirmas == cantSinFirmas) {

			paragraphFirma = new Paragraph(13f);
			chunkFirma = new Chunk("SR(A). " + de.toUpperCase(), bfbold);
			paragraphFirma.setAlignment(Element.ALIGN_CENTER);
			paragraphFirma.add(chunkFirma);
			chunkFirma = new Chunk("\r\n" + cargo.toUpperCase(), bfbold);
			paragraphFirma.add(chunkFirma);
			chunkFirma = new Chunk("\r\n" + institucion.toUpperCase(), bfbold);
			paragraphFirma.add(chunkFirma);
			String imagePath = "firma.jpg";
			//
			byte[] data = DatatypeConverter.parseBase64Binary(firma);

			File fileFirma = new File(imagePath);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileFirma))) {
				outputStream.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Image image2 = Image.getInstance(imagePath);
			image2.scalePercent(50f);
			image2.setAlignment(Element.ALIGN_CENTER);
			imagePath = "/imagen/firma.png";
			PdfPTable tableFirma = new PdfPTable(2);
			float[] widths = new float[] { 1f, 2f };
			tableFirma.setWidths(widths);

			tableFirma.setKeepTogether(true);
			tableFirma.setWidthPercentage(100);
			tableFirma.setKeepTogether(true);
			tableFirma.setWidthPercentage(100);
			PdfPCell cellFirma = new PdfPCell();

			Font fontBoldFirma = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9, Font.BOLD,
					BaseColor.BLACK);
			Chunk chunkfoot4 = new Chunk("SALUDA ATENTAMENTE,                             ", fontBoldFirma);
			cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellFirma.addElement(chunkfoot4);
			cellFirma.setPaddingBottom(1f);
			cellFirma.setVerticalAlignment(Element.ALIGN_TOP);
			cellFirma.setBorder(Rectangle.NO_BORDER);
			cellFirma.setFixedHeight(29f);
			tableFirma.addCell(cellFirma);

			cellFirma = new PdfPCell();
			chunkfoot4 = new Chunk("            Por orden del Director", fontBoldFirma);
			cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellFirma.addElement(chunkfoot4);
			cellFirma.setPaddingBottom(1f);
			cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellFirma.setVerticalAlignment(Element.ALIGN_BOTTOM);
			cellFirma.setBorder(Rectangle.NO_BORDER);
			cellFirma.setFixedHeight(29f);
			tableFirma.addCell(cellFirma);

			cellFirma = new PdfPCell();
			cellFirma.setPaddingTop(0f);
			cellFirma.addElement(image2);
			cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellFirma.setBorder(Rectangle.NO_BORDER);
			cellFirma.setColspan(2);

			tableFirma.addCell(cellFirma);

			cellFirma = new PdfPCell();
			cellFirma.addElement(paragraphFirma);
			cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellFirma.setBorder(Rectangle.NO_BORDER);
			cellFirma.setColspan(2);
			tableFirma.setKeepTogether(true);
			tableFirma.setWidthPercentage(100);
			tableFirma.addCell(cellFirma);

			document.add(tableFirma);

		}

		/*
		 * 
		 * fin firma
		 */

		document.newPage();
		Chunk chunkFoot = new Chunk(iniciales + "  ", new Font(bf, 12));
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

		if (log.isDebugEnabled()) {
			log.debug("getPageNumber: [" + writer.getPageNumber() + "]");
		}
		numPage[0] = writer.getPageNumber();
		document.close();
		FILE.flush();
		FILE.close();
		if (intArray[0] > 0) {
			int cont = 0;
			try {
				while (cont < (intArray[0])) {
					File file = new File(clave + cont + ".png");
					file.delete();
					cont = cont + 1;
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return clave + ".pdf";
	}

	public String eliminaAllTag(String respuesta, String tagAbierto, String tagCerrado) {
		respuesta = eliminatag(tagAbierto, respuesta);
		respuesta = eliminatag(tagCerrado, respuesta);

		return respuesta;
	}

	public int cuentaTagAbierto(String tag, String respuesta) {
		int count1 = 0;
		int indice1 = respuesta.indexOf(tag);
		tag = "<p";
		String respuesta2 = "";
		while (indice1 > -1) {
			respuesta2 = respuesta.substring(respuesta.indexOf(tag) + tag.length(), respuesta.length());
			count1 = count1 + 1;
			indice1 = respuesta.indexOf(tag, indice1 + 1);

		}

		return count1;
	}

	public int cuentaTagCerrado(String tag, String respuesta) {
		int count2 = 0;
		int indice1 = respuesta.indexOf(tag);
		String respuesta2 = "";
		while (indice1 > -1) {
			respuesta2 = respuesta.substring(respuesta.indexOf(tag) + tag.length(), respuesta.length());
			count2 = count2 + 1;
			indice1 = respuesta.indexOf(tag, indice1 + 1);

		}

		return count2;
	}

	public String eliminatagAbierto(String tag, String respuesta, int count1, int count2) {
		if ("<p".equalsIgnoreCase(tag)) {
			tag = "<p>";
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

	public String eliminatagCerrado(String tag, String respuesta, int count1, int count2) {
		int indice1 = respuesta.indexOf(tag);
		while ((indice1 > -1) && (Math.abs(count1 - count2)) > 0) {
			int indice2 = respuesta.indexOf(">", indice1);
			respuesta = respuesta.substring(0, indice1) + respuesta.substring(indice2 + 1, respuesta.length());
			if (count1 - count2 > 0) {
				count2 = count2 + 1;
			} else {
				count1 = count1 + 1;
			}
			indice1 = respuesta.indexOf(tag);
		}

		return respuesta;
	}

	public String eliminatag(String tag, String respuesta) {
		int indice1 = respuesta.indexOf(tag);
		while (indice1 > -1) {
			int indice2 = respuesta.indexOf(">", indice1);
			respuesta = respuesta.substring(0, indice1) + respuesta.substring(indice2 + 1, respuesta.length());
			indice1 = respuesta.indexOf(tag);
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

	/********************************************************
	 * ********************************************************cantidaPageFelicitacion****************************************************
	 * 
	 * 
	 */
	public int cantidaPageFelicitacionConFirma(float marginLeft, float marginRight, float marginTop, float marginBottom,
			String nombreSolicitante, String ordinario, String nombreTipificacion, String problemaSalud, long idCaso,
			String respuesta, String clave, int ord, String tipo, String de, String wsdl, String run, int genero,
			String parrafoUno, String parrafoDos, int[] numPage, String cargo, String institucion, String dzFirmante,
			String subDeptoFirmante, String departamentoFirmante, String direccionSolicitante, String iniciales,
			String ciudad, String firma) throws IOException, DocumentException {
		int numeroPagina = 0;
		Paragraph paragraphFirma = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(13f);
		Paragraph paragrapHeadLeft = new Paragraph(13f);
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);

		Date fecha = new Date();
		String nameFile = clave.concat("ConFirma.pdf");
		FileOutputStream FILE = new FileOutputStream(nameFile);

		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);
		PdfWriter writer = PdfWriter.getInstance(document, FILE);

		document.open();

		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);

		paragraphead.setAlignment(Element.ALIGN_LEFT);
		PdfPTable tableHeadRight = new PdfPTable(new float[] { 130F, 190F });

		// -----------------------------------------------------------------------------------**************************************
		PdfPTable tableLeft = new PdfPTable(3);
		PdfPCell text = new PdfPCell();
		Paragraph headParaTit = new Paragraph();

		headParaTit = new Paragraph(8);
		headParaTit.setAlignment(Element.ALIGN_LEFT);

		Paragraph paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);
		paraLeft.add("FONDO NACIONAL DE SALUD");

		headParaTit.add(paraLeft);

		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);

		paraLeft.add(dzFirmante);
		headParaTit.add(paraLeft);
		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);
		if (departamentoFirmante == null) {
			departamentoFirmante = " ";
		}
		if (!"".equals(departamentoFirmante.trim())) {

			paraLeft.add(departamentoFirmante.toUpperCase());
			headParaTit.add(paraLeft);
		}

		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);

		if ((subDeptoFirmante != null) && (!"".equals(subDeptoFirmante.trim()))) {
			paraLeft.add(subDeptoFirmante.toUpperCase());
			headParaTit.add(paraLeft);
		}

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

		// ------------------------------------------------------**********************************************************

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

		// para.add(ordinario.substring(4) );
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
		para.add(tipo.toUpperCase());
		para.setAlignment(Element.ALIGN_LEFT);

		HeadCellRight1.addElement(para);

		para = new Paragraph();
		para.setFont(bfbold);

		para.add(ciudad + " ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(new SimpleDateFormat("dd-MM-yyyy").format(fecha));
		HeadCellRight1.addElement(para);

		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		document.add(tableHeadRight);

		Chunk chunkHeadleft = new Chunk("DE : " + de.toUpperCase() + "\r\n        " + cargo.toUpperCase()
				+ "\r\n        " + institucion.toUpperCase() + "\r\n", bfbold);
		if (genero == 1) {
			chunkHeadleft.append("\r\nA: SR. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else if (genero == 2) {
			chunkHeadleft.append("\r\nA: SRA. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else {
			chunkHeadleft.append("\r\nA: " + nombreSolicitante.trim() + "\r\n\r\n");
		}

		paragrapHeadLeft.add(chunkHeadleft);
		document.add(paragrapHeadLeft);

		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		String imagePath = "firma.jpg";
		//
		byte[] data = DatatypeConverter.parseBase64Binary(firma);

		File fileFirma = new File(imagePath);
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileFirma))) {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image2 = Image.getInstance(imagePath);
		image2.scalePercent(50f);
		image2.setAlignment(Element.ALIGN_CENTER);
		imagePath = "/imagen/firma.png";
		PdfPTable tableFirma = new PdfPTable(2);
		float[] widths = new float[] { 1f, 2f };
		tableFirma.setWidths(widths);

		tableFirma.setKeepTogether(true);
		tableFirma.setWidthPercentage(100);
		tableFirma.setKeepTogether(true);
		tableFirma.setWidthPercentage(100);
		PdfPCell cellFirma = new PdfPCell();

		Font fontBoldFirma = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9, Font.BOLD,
				BaseColor.BLACK);
		Chunk chunkfoot4 = new Chunk("SALUDA ATENTAMENTE,                             ", fontBoldFirma);
		cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellFirma.addElement(chunkfoot4);
		cellFirma.setPaddingBottom(1f);
		cellFirma.setVerticalAlignment(Element.ALIGN_TOP);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setFixedHeight(29f);
		tableFirma.addCell(cellFirma);

		cellFirma = new PdfPCell();
		chunkfoot4 = new Chunk("            Por orden del Director", fontBoldFirma);
		cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellFirma.addElement(chunkfoot4);
		cellFirma.setPaddingBottom(1f);
		cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFirma.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setFixedHeight(29f);
		tableFirma.addCell(cellFirma);

		cellFirma = new PdfPCell();
		cellFirma.setPaddingTop(0f);
		cellFirma.addElement(image2);
		cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setColspan(2);

		tableFirma.addCell(cellFirma);

		cellFirma = new PdfPCell();
		cellFirma.addElement(paragraphFirma);
		cellFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFirma.setBorder(Rectangle.NO_BORDER);
		cellFirma.setColspan(2);
		tableFirma.setKeepTogether(true);
		tableFirma.setWidthPercentage(100);
		tableFirma.addCell(cellFirma);

		document.add(tableFirma);

		numeroPagina = writer.getCurrentPageNumber();

		document.close();
		FILE.flush();
		FILE.close();
		numeroPagina = writer.getCurrentPageNumber() - 1;
		Path directory = Paths.get(nameFile);

		try {
			if (log.isDebugEnabled()) {
				log.debug("directory ::" + directory.toString() + "]");
			}
			Files.delete(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}

		return numeroPagina;

	}

	/********************************************************
	 * ********************************************************cantidaPageFelicitacion****************************************************
	 * 
	 * 
	 */
	public int cantidaPageFelicitacionSinFirma(float marginLeft, float marginRight, float marginTop, float marginBottom,
			String nombreSolicitante, String ordinario, String nombreTipificacion, String problemaSalud, long idCaso,
			String respuesta, String clave, int ord, String tipo, String de, String wsdl, String run, int genero,
			String parrafoUno, String parrafoDos, int[] numPage, String cargo, String institucion, String dzFirmante,
			String subDeptoFirmante, String departamentoFirmante, String direccionSolicitante, String iniciales,
			String ciudad, String firma) throws IOException, DocumentException {
		int numeroPagina = 0;
		Paragraph paragraphFirma = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(13f);
		Paragraph paragrapHeadLeft = new Paragraph(13f);
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		Font bfbold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);

		Date fecha = new Date();
		String nameFile = clave.concat("SinFirma.pdf");
		FileOutputStream FILE = new FileOutputStream(nameFile);

		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);
		PdfWriter writer = PdfWriter.getInstance(document, FILE);

		document.open();

		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);

		paragraphead.setAlignment(Element.ALIGN_LEFT);
		PdfPTable tableHeadRight = new PdfPTable(new float[] { 130F, 190F });

		// -----------------------------------------------------------------------------------**************************************
		PdfPTable tableLeft = new PdfPTable(3);
		PdfPCell text = new PdfPCell();
		Paragraph headParaTit = new Paragraph();

		headParaTit = new Paragraph(8);
		headParaTit.setAlignment(Element.ALIGN_LEFT);

		Paragraph paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);
		paraLeft.add("FONDO NACIONAL DE SALUD");

		headParaTit.add(paraLeft);

		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);

		paraLeft.add(dzFirmante);
		headParaTit.add(paraLeft);
		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);
		if (departamentoFirmante == null) {
			departamentoFirmante = " ";
		}
		if (!"".equals(departamentoFirmante.trim())) {

			paraLeft.add(departamentoFirmante.toUpperCase());
			headParaTit.add(paraLeft);
		}

		paraLeft = new Paragraph();
		paraLeft.setFont(bfbold);

		if ((subDeptoFirmante != null) && (!"".equals(subDeptoFirmante.trim()))) {
			paraLeft.add(subDeptoFirmante.toUpperCase());
			headParaTit.add(paraLeft);
		}

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

		// ------------------------------------------------------**********************************************************

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

		// para.add(ordinario.substring(4) );
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
		para.add(tipo.toUpperCase());
		para.setAlignment(Element.ALIGN_LEFT);

		HeadCellRight1.addElement(para);

		para = new Paragraph();
		para.setFont(bfbold);

		para.add(ciudad + " ");
		para.setAlignment(Element.ALIGN_LEFT);
		para.setFont(new Font(bf, 12));
		para.add(new SimpleDateFormat("dd-MM-yyyy").format(fecha));
		HeadCellRight1.addElement(para);

		HeadCellRight1.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeadCellRight1.setVerticalAlignment(Element.ALIGN_TOP);

		HeadCellRight1.setFixedHeight(80f);
		tableHeadRight.addCell(HeadCellRight1);

		document.add(tableHeadRight);

		Chunk chunkHeadleft = new Chunk("DE : " + de.toUpperCase() + "\r\n        " + cargo.toUpperCase()
				+ "\r\n        " + institucion.toUpperCase() + "\r\n", bfbold);
		if (genero == 1) {
			chunkHeadleft.append("\r\nA: SR. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else if (genero == 2) {
			chunkHeadleft.append("\r\nA: SRA. " + nombreSolicitante.trim() + "\r\n\r\n");
		} else {
			chunkHeadleft.append("\r\nA: " + nombreSolicitante.trim() + "\r\n\r\n");
		}

		paragrapHeadLeft.add(chunkHeadleft);
		document.add(paragrapHeadLeft);

		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		String imagePath = "firma.jpg";
		//
		byte[] data = DatatypeConverter.parseBase64Binary(firma);

		File fileFirma = new File(imagePath);
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileFirma))) {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image2 = Image.getInstance(imagePath);
		image2.scalePercent(50f);
		image2.setAlignment(Element.ALIGN_CENTER);
		imagePath = "/imagen/firma.png";

		numeroPagina = writer.getCurrentPageNumber();

		document.close();
		FILE.flush();
		FILE.close();
		numeroPagina = writer.getCurrentPageNumber() - 1;
		Path directory = Paths.get(nameFile);

		try {
			if (log.isDebugEnabled()) {
				log.debug("directory ::" + directory.toString() + "]");
			}
			Files.delete(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}

		return numeroPagina;

	}

	/*************************************************************************************************
	 * 
	 * @param marginLeft
	 * @param marginRight
	 * @param marginTop
	 * @param marginBottom
	 * @param solicitud
	 * @param ordinario
	 * @param respuesta
	 * @param clave
	 * @param wsdl
	 * @param parrafoUno
	 * @param parrafoDos
	 * @param numPage
	 * @param cargo
	 * @param institucion
	 * @param dzFirmante
	 * @param subDeptoFirmante
	 * @param departamentoFirmante
	 * @param direccionSolicitante
	 * @param iniciales
	 * @param numeroSolicitud
	 * @param ciudad
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */

	public int countPageFileReclamposPdf(float marginLeft, float marginRight, float marginTop, float marginBottom,
			Solicitud solicitud, String ordinario, String respuesta, String clave, String wsdl, String parrafoUno,
			String parrafoDos, int[] numPage, String cargo, String institucion, String dzFirmante,
			String subDeptoFirmante, String departamentoFirmante, String direccionSolicitante, String iniciales,
			long numeroSolicitud, String ciudad) throws IOException, DocumentException {

		int numeroPagina = 0;
		String nombreSolicitante = solicitud.getNombreSolicitante();
		String nombreTipificacion = solicitud.getNombreTipificacion();
		int ord = solicitud.getOrd();
		String run = solicitud.getRunUsuarioEjecuta();

		String tipo = solicitud.getTipo().toUpperCase();
		String de = solicitud.getDe();
		String problemaSalud = solicitud.getProblemaDeSalud();
		long idCaso = solicitud.getIdCaso();

		int genero = solicitud.getGenero();
		if (log.isDebugEnabled()) {
			log.debug("de :: [" + de + "].pdf");
		}
		String nameFile = clave + "111.pdf";
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
		if (log.isDebugEnabled()) {
			log.debug("File :: [" + nameFile + "]");
		}
		FileOutputStream FILE = new FileOutputStream(nameFile);

		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);

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

		InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		document.close();

		FILE.flush();

		FILE.close();
		numeroPagina = writer.getCurrentPageNumber() - 1;
		Path directory = Paths.get(nameFile);

		try {
			if (log.isDebugEnabled()) {
				log.debug("directory ::[" + directory.toString() + "]");
			}
			Files.delete(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}

		return numeroPagina;

	}

	/*********************************************************************
	 * 
	 * @param zona
	 * @return
	 */
	public String calcOrdinario(String zona) {
		String ord = "4.1K/N° ";
		if ("DZN".equals(zona)) {
			ord = "1P/N° ";
		} else if ("DZCN".equals(zona)) {
			ord = "1R/N° ";
		} else if ("DZCS".equals(zona)) {
			ord = "1S/N° ";
		} else if ("DZS".equals(zona)) {
			ord = "1T/N° ";
		}
		return ord;
	}

	public String convertImagen(String respuesta, String nombreImg, int[] arr) {
		int i = 0;
		int indiceInicio = 0;
		int indiceFin = 0;
		int indice = respuesta.indexOf("<img");
		int indice1 = 0;
		String path = "dd";
		while (indice >= 0) {
			indiceInicio = respuesta.indexOf("<img", indice1);
			indiceFin = respuesta.indexOf(">", indiceInicio) + 1;

			String srcImg = respuesta.substring(indiceInicio, indiceFin);

			String src = respuesta.substring(indiceInicio, respuesta.indexOf(">", indiceInicio) + 1);

			indice1 = src.indexOf("\"");

			src = src.substring(indice1 + 1, src.indexOf("\"", indice1 + 1));
			String[] strings = src.split(",");
			String extension;

			switch (strings[0]) {// check image's extension
			case "data:image/jpeg;base64":
				extension = ".jpeg";
				break;
			case "data:image/png;base64":
				extension = ".png";
				break;
			default:// should write cases for more images types
				extension = ".jpg";
				break;
			}
			// convert base64 string to binary data
			byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
			path = nombreImg + i + extension;
			File file = new File(path);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String s = "<img src=\"" + path + "\"/>";
			respuesta = respuesta.replace(respuesta.substring(indiceInicio, indiceFin), s);
			indice = respuesta.indexOf("<img", indiceInicio + s.length());
			if (log.isDebugEnabled()) {
				log.debug("respuesta  indice [" + indice + "]");
				log.debug("respuesta  indice [" + respuesta + "]");
			}
			i = i + 1;
			indice1 = indice;
		}
		arr[0] = i;
		return respuesta;

	}
}
