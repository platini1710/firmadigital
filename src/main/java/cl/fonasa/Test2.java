package cl.fonasa;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import cl.fonasa.pdf.GeneradorFilePdf;
import cl.fonasa.service.SignFileService;

public class Test2 {
    public static final String FONTBold = "/Font/CalibriBold.ttf";
    public static final String FONTNormal= "/Font/CalibriRegular.ttf";
    
    public static void main(String[] args) throws DocumentException, MalformedURLException, IOException, org.json.simple.parser.ParseException {
        String clave  ="file2";
        String ord="13779/2020";
        String fechaOrd="2020";
        String idCaso="987906";
        String tipo="FELICITACIÓN";
        String problemaSalud="";
        String nombreTipificacion="";
        Date fecha = new Date();
        String de="PAULO VELASQUES GONZALEZ";
        String nombreSolicitante="YAZMIN NADIA MUSALEM CABELLO";
        String respuesta="Con motivo de la felicitación ingresada a través de uno de nuestros canales, en la cual agradece la  atención otorgada por la ejecutiva Guilermina Silva, de la sucursal Monjitas, ya que le otorgó buen servicio y por su disposición en la atención, al respecto señalo lo siguiente: ";






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
		chunk0 = new Chunk("asdadsa", f);
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
		
		
		
		paragraphBody.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		Chunk chunk3 = new Chunk(
				respuesta,
				f);
		paragraphBody.add(chunk3);

		PdfPTable tableBody= new PdfPTable(1);
		tableBody.setWidthPercentage(100);
		PdfPCell cellBody = new PdfPCell();
		cellBody.setVerticalAlignment(Element.ALIGN_TOP);
		cellBody.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellBody.setBorder(Rectangle.BOX);
		cellBody.setFixedHeight(168f);
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


			codVerificacion = "aaaasdasdasdasddas";

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
  		
    }
}
