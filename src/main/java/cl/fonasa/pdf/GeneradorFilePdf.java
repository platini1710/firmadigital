package cl.fonasa.pdf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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

import cl.fonasa.controller.SignDesAtendidaController;
import cl.fonasa.service.SignFileService;
import cl.fonasa.util.Utilidades;


public class GeneradorFilePdf {
	
	private static final Logger log = LoggerFactory.getLogger(SignDesAtendidaController.class);

    public static final String FONTBold = "/Font/CalibriBold.ttf";
    public static final String FONTNormal= "/Font/CalibriRegular.ttf";
	public String generaFileReclamposPdf(String ordinario,String nombreSolicitante, String nombreTipificacion, String problemaSalud,
			long idCaso, String respuesta, String clave, int ord, String tipo, String de,String wsdl,String run)
			throws IOException, DocumentException {

		    
			  byte[] arrayAux = respuesta.getBytes("ISO-8859-1");
			  respuesta= new String(arrayAux, "UTF-8");
		log.info("respuesta :: " + respuesta + ".pdf");
			Date fecha = new Date();
		 	Font f2 = FontFactory.getFont(FONTBold, BaseFont.WINANSI, BaseFont.EMBEDDED, 10); 
			f2.setFamily("Calibri");
			f2.setStyle(Font.BOLD);
			f2.setSize(10);
			Font f5 = FontFactory.getFont(FONTNormal, BaseFont.WINANSI, BaseFont.EMBEDDED, 7); 
			f5.setFamily("Calibri");
			f5.setStyle(Font.UNDERLINE);
			f5.setSize(7);
			Font paragraNegrita = FontFactory.getFont(FONTBold, BaseFont.WINANSI, BaseFont.EMBEDDED, 9); 
			paragraNegrita.setFamily("Calibri");
			paragraNegrita.setStyle(Font.BOLD);
			paragraNegrita.setSize(9);
			Font paragraFontNormal = FontFactory.getFont(FONTNormal, BaseFont.WINANSI, BaseFont.EMBEDDED, 10); 
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
			Chunk chunkOrd = new Chunk("ORD. 1.1G/N°   " + ordinario  + "  "+ new SimpleDateFormat("dd.MM.yyyy").format(fecha) + " \r\n" + "ANT.: FOLIO N° " + idCaso + "               \r\n"
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
			PdfPTable tableBody= new PdfPTable(1);
			tableBody.setWidthPercentage(100);
			PdfPCell cellBody = new PdfPCell();
			cellBody.setVerticalAlignment(Element.ALIGN_TOP);
			cellBody.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellBody.setBorder(Rectangle.NO_BORDER);
			cellBody.setFixedHeight(84f);
			cellBody.addElement(paragraphBody);
			tableBody.addCell(cellBody);

			//******************************Body Respuesta                ******************************************************
			cellBody = new PdfPCell();
			paragraphBody = new Paragraph(13f);
			Chunk chunkRespuesta = new Chunk(respuesta + "\r\n\n", f2);
			paragraphBody.add(chunkRespuesta);	
			Chunk chunkRespuesta2 = new Chunk(
					"En caso de disconformidad con el contenido de esta respuesta, usted podrá solicitar a la Superintendencia de Salud su "
							+ "revisión, debiendo acompañar copia de esta carta y de los antecedentes remitidos por esta institución.\r\n\r\n"
							+ "Saluda atentamente",
					paragraFontNormal);

			paragraphBody.add(chunkRespuesta2);
			paragraphBody.setAlignment(Paragraph.ALIGN_JUSTIFIED);
			cellBody.setVerticalAlignment(Element.ALIGN_TOP);
			cellBody.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellBody.addElement(paragraphBody);
			cellBody.setBorder(Rectangle.NO_BORDER);
			cellBody.setFixedHeight(84f);
			tableBody.addCell(cellBody);
			document.add(tableBody);
			
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
			

			
			tableImgFirma.setWidthPercentage(100f);
			PdfPCell image2LeftCell = new PdfPCell();
			image2LeftCell.setBorder(Rectangle.BOX);
			image2LeftCell.setFixedHeight(128f);

			image2LeftCell.setBorderColor(BaseColor.BLUE);

			//String imagePathFirma = "/imagen/firma.jpg";
			//Image imgFirma = Image.getInstance(GeneradorFilePdf.class.getResource(imagePathFirma));
		//	imgFirma.scalePercent(30f);
		//	imgFirma.setAlignment(Element.ALIGN_CENTER);
		//	image2LeftCell.setImage(imgFirma);
			// image2LeftCell = new PdfPCell();
			image2LeftCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			image2LeftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			image2LeftCell.setBorder(Rectangle.NO_BORDER);
			tableImgFirma.addCell(image2LeftCell);
			
			PdfPCell firma = new PdfPCell();
			firma.setHorizontalAlignment(Element.ALIGN_CENTER);
			firma.setBorder(Rectangle.NO_BORDER);
			firma.addElement(paragraphFirma);
			tableImgFirma.addCell(firma);
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
			SignFileService codigoCertificadoUtil = new SignFileService();
			String codVerificacion="";
			try {
				String runSinDv=run.substring(0, run.length()-2) +run.substring(run.length()-1, run.length());
				codVerificacion = codigoCertificadoUtil.generaCodigoCertificado(runSinDv, "X",
						"7",wsdl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);;
			}
			p = new Paragraph("Código Verificación:" + codVerificacion  + "r\n" + "", f7);
			cellText = new PdfPCell();
			cellText.setVerticalAlignment(Element.ALIGN_CENTER);
			cellText.setBorder(Rectangle.TOP);
			cellText.addElement(p);
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
		return clave + ".pdf";

	}
	
	public String generaFileFelicitacioPdf(String ordinario,String nombreSolicitante, String nombreTipificacion, String problemaSalud,
			long idCaso, String respuesta, String clave, int ord, String tipo, String de,String wsdl,String run)
			throws IOException, DocumentException {

		Date fecha = new Date();

		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");
		// log.info("File :: " + clave + ".pdf");;
		Document document = new Document(PageSize.A4, 56, 56, 140, 136);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);
		cl.fonasa.pdf.HeadFootOtrosPdf event = new cl.fonasa.pdf.HeadFootOtrosPdf();
		writer.setPageEvent(event);
		document.open();

		Paragraph paragraphBody = new Paragraph(13f);
		Paragraph paragraphead = new Paragraph(13f);
		Paragraph paragraphead2 = new Paragraph(13f);
		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
		Font f0 = new Font();
		f0.setFamily("Calibri");
		f0.setStyle(Font.BOLD);
		f0.setSize(8);
		Font f = new Font();
		f.setFamily("Calibri");
		f.setStyle(Font.NORMAL);
		f.setSize(8);
		/*
		 * --------------------------------------------Head-----------------------------
		 * ------------------
		 */
		Chunk chunk0 = new Chunk("ORD. 1.1G/N°   ", f0);
		paragraphead.setAlignment(Element.ALIGN_RIGHT);
		paragraphead.add(chunk0);
		chunk0 = new Chunk(ordinario, f);
		paragraphead.add(chunk0);

		chunk0 = new Chunk("\r\nANT.:   ", f0);
		paragraphead.setAlignment(Element.ALIGN_RIGHT);
		paragraphead.add(chunk0);
		chunk0 = new Chunk("Folio N° " + idCaso, f);
		paragraphead.add(chunk0);

		chunk0 = new Chunk("\r\nMAT.:   ", f0);
		paragraphead.setAlignment(Element.ALIGN_RIGHT);
		paragraphead.add(chunk0);
		chunk0 = new Chunk("RESPUESTA A " + tipo, f);
		paragraphead.add(chunk0);

		chunk0 = new Chunk("\r\nSANTIAGO, " + new SimpleDateFormat("dd/MM/yyyy").format(fecha), f0);
		paragraphead.setAlignment(Element.ALIGN_RIGHT);
		PdfPTable table0 = new PdfPTable(new float[] { 6, 4 });
		table0.setWidthPercentage(100);
		PdfPCell tit = new PdfPCell();
		tit.setBorder(Rectangle.NO_BORDER);
		table0.addCell(tit);
		tit = new PdfPCell();
		paragraphead.setAlignment(Paragraph.ALIGN_CENTER);
		paragraphead.add(chunk0);
		paragraphead.setAlignment(Element.ALIGN_LEFT);
		tit.setHorizontalAlignment(Element.ALIGN_CENTER);
		tit.setVerticalAlignment(Element.ALIGN_LEFT);
		tit.addElement(paragraphead);
		tit.setBorder(Rectangle.NO_BORDER);
		table0.addCell(tit);
		document.add(table0);
		Chunk chunk01 = new Chunk(
				"DE : " + de
						+ "\r\n        JEFA (S) DEPARTAMENTO GESTIÓN CIUDADANA\r\n         FONDO NACIONAL DE SALUD\r\n",
				f0);
		chunk01.append("\r\nA : SR. " + nombreSolicitante + "\r\n\r\n");
		Font f2 = new Font();
		f2.setFamily("Calibri");
		f2.setStyle(Font.BOLD);
		f2.setSize(10);

		Paragraph paragraphLorem4 = new Paragraph(13f);
		Chunk chunk4 = new Chunk(de + "\r\nJEFA (S) DEPARTAMENTO GESTIÓN CIUDADANA\r\nFONDO NACIONAL DE SALUD", f2);


		////paragraphBody.add(chunk2);
	
		paragraphLorem4.setAlignment(Element.ALIGN_CENTER);
		paragraphLorem4.add(chunk4);

		paragraphead2.add(chunk01);
		document.add(paragraphead2);
		
		
		PdfPTable tableBody= new PdfPTable(1);
		tableBody.setWidthPercentage(100);
		paragraphBody.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		Chunk chunk3 = new Chunk(
				respuesta,
				f);
		paragraphBody.add(chunk3);
		paragraphBody.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		PdfPCell cellBody = new PdfPCell();
		cellBody.setVerticalAlignment(Element.ALIGN_TOP);
		cellBody.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellBody.setBorder(Rectangle.NO_BORDER);
		cellBody.setFixedHeight(148f);
		cellBody.addElement(paragraphBody);
		tableBody.addCell(cellBody);

		document.add(tableBody);

		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(50f);
		PdfPCell image2LeftCell = new PdfPCell();
	
		image2LeftCell.setFixedHeight(128f);

		image2LeftCell.setBorderColor(BaseColor.BLUE);

		/*String imagePath = "/imagen/firma.jpg";
		Image image3 = Image.getInstance(GeneradorFilePdf.class.getResource(imagePath));
		image3.scalePercent(30f);
		image3.setAlignment(Element.ALIGN_CENTER);
		image2LeftCell.setImage(image3);*/
		image2LeftCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		image2LeftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		image2LeftCell.setBorder(Rectangle.NO_BORDER);
		table.addCell(image2LeftCell);
		
		image2LeftCell = new PdfPCell();
		image2LeftCell.addElement(paragraphLorem4);
		image2LeftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		image2LeftCell.setBorder(Rectangle.NO_BORDER);
		table.addCell(image2LeftCell);
		document.add(table);

		//document.add(paragraphLorem4);

		Chunk chunkFoot = new Chunk("AYM / pvm   ", f0);
		Paragraph paragraphFoot = new Paragraph(13f);
		PdfPTable tableFooter = new PdfPTable(1);
		tableFooter.setWidthPercentage(100);
		PdfPCell cellFoot = new PdfPCell();
		paragraphFoot.setAlignment(Paragraph.ALIGN_LEFT);
		paragraphFoot.add(chunkFoot);
		chunkFoot = new Chunk("\r\nDISTRIBUCIÓN: ", f0);
		paragraphFoot.add(chunkFoot);
		chunkFoot = new Chunk("\r\nPURISIMA N°240, DEPTO. 301/ RECOLETA/ REGION METROPOLITANA "
				+ "\r\nSUBDEPTO. GESTIÓN DE SOLICITUDES CIUDADANAS " + "\r\nSUBDEPTO. OFICINA DE PARTES ", f);
		paragraphFoot.add(chunkFoot);
		paragraphFoot.setAlignment(Element.ALIGN_LEFT);
		cellFoot.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellFoot.setVerticalAlignment(Element.ALIGN_LEFT);
		cellFoot.addElement(paragraphFoot);
		cellFoot.setBorder(Rectangle.NO_BORDER);
		tableFooter.addCell(cellFoot);
		SignFileService codigoCertificadoUtil = new SignFileService();
		String codVerificacion="";
		try {
			String runSinDv=run.substring(0, run.length()-2) +run.substring(run.length()-1, run.length());
			codVerificacion = codigoCertificadoUtil.generaCodigoCertificado(runSinDv, "X",
					"7",wsdl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);;
		}
		cellFoot = new PdfPCell();
		paragraphFoot=new Paragraph(13f);
		chunkFoot= new Chunk("\r\n" + codVerificacion +"\r\nCodigo de Verificación " ,f0 );
		paragraphFoot.add(chunkFoot);
	  paragraphFoot.setAlignment(Element.ALIGN_CENTER);
		cellFoot.addElement(paragraphFoot);
		cellFoot.setBorder(Rectangle.NO_BORDER);
		tableFooter.addCell(cellFoot);
		document.add(tableFooter);
		document.close();
		FILE.flush();
		FILE.close();
		return clave + ".pdf";
	}
}
