package cl.fonasa.pdf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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

import cl.fonasa.controller.TokenController;
import cl.fonasa.util.Utilidades;


public class GeneradorFilePdf {
	
	private static final Logger log = LoggerFactory.getLogger(TokenController.class);


	public String generaFilePdf(String nombreSolicitante,String nombreTipificacion,String problemaSalud,String idCaso,		String clave) throws IOException, DocumentException {
		Utilidades util = new Utilidades();
		

        FileOutputStream FILE =new FileOutputStream(clave + ".pdf");
        log.info("File :: " + clave + ".pdf");;
	            Document document = new Document(PageSize.A4, 96, 86, 150, 136);


	            PdfWriter writer = PdfWriter.getInstance(document, FILE);
	            cl.fonasa.pdf.HeadFootCotizaciones event = new  cl.fonasa.pdf.HeadFootCotizaciones();
	    		writer.setPageEvent(event);
	            document.open();
	            

	    	
	    
	 

	 
	            Paragraph paragraphLorem = new Paragraph(13f);
	            Paragraph paragraphead= new Paragraph(25f);
	            Paragraph paragraphead2= new Paragraph();
	            paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);
	            Font f0 = new Font();
	            f0.setFamily("Calibri");
	            f0.setStyle(Font.BOLD);
	            f0.setSize(10);
	            Chunk chunk0= new Chunk("ORD. 1.1G/N°   9070  30.04.2020    \r\n"            
	            		+ "ANT.: FOLIO N° " +  idCaso + "               \r\n"
	            		+ "MAT.: RESPUESTA A RECLAMO \r\n\r\n" + 
	            		"",f0);
	            Font f = new Font();
	            f.setFamily("Calibri");
	            f.setStyle(Font.NORMAL);
	            f.setSize(10);

	            
	            

	            Chunk chunk01= new Chunk("DE : ASTRID YÁÑEZ BASAURE\r\n        JEFA (S) DEPARTAMENTO GESTIÓN CIUDADANA\r\n        FONDO NACIONAL DE SALUD\r\n",f0);
	            
	            chunk01.append("\r\nA : SR. " + nombreSolicitante  + "\r\n\r\n");

	            paragraphLorem.setAlignment(Paragraph.ALIGN_JUSTIFIED);
	            Chunk chunk1= new Chunk("Con motivo de la presentación de su reclamo a través de uno de nuestros canales de contacto, "
	            		+ "en el cual nos señala su molestia por el " + nombreTipificacion + " del"
	            		+ " problema de salud \"" + problemaSalud  + "\", le informo lo siguiente:\r\n\n" ,f);


	            Font f2 = new Font();
	            f2.setFamily("Calibri");
	            f2.setStyle(Font.BOLD);
	            f2.setSize(10);
	            Chunk chunk2= new Chunk("Dado que el Hospital San Juan de Dios no realizó la prestación dentro del plazo establecido, "
	            		+ "Fonasa le designa como 2° Prestador a CLINICA BICENTENARIO, donde fue citado el día 27-03-2020 a las 11:00 horas"
	            		+ " con el Dr.Calderon, situación que le fue informada oportunamente.\r\n\n",f2);
	            
	            
	            
	            Chunk chunk3= new Chunk("En caso de disconformidad con el contenido de esta respuesta, usted podrá solicitar a la Superintendencia de Salud su "
	            						+ "revisión, debiendo acompañar copia de esta carta y de los antecedentes remitidos por esta institución.\r\n\r\n"	            
	            						+ "Saluda atentamente",f);
	            Paragraph paragraphLorem4 = new Paragraph(13f); 
	            Chunk chunk4= new Chunk("SRA. ASTRID YÁÑEZ BASAURE\r\nJEFA (S) DEPARTAMENTO GESTIÓN CIUDADANA\r\nFONDO NACIONAL DE SALUD",f2);
	            paragraphLorem.add(chunk1);
	            paragraphLorem.add(chunk2);
	            paragraphLorem.add(chunk3);
	            paragraphLorem4.setAlignment(Element.ALIGN_CENTER);
	            paragraphLorem4.add(chunk4);
	            paragraphead.add(chunk0);
	            paragraphead2.add(chunk01);
	            paragraphLorem.setAlignment(Paragraph.ALIGN_JUSTIFIED);
	            document.add(paragraphead);
	            document.add(paragraphead2);
	            document.add(paragraphLorem);

	            
	        	PdfPTable table = new PdfPTable(1);
	        	table.setTotalWidth(50f);
	    		PdfPCell image2LeftCell = new PdfPCell();
	    		image2LeftCell.setBorder(Rectangle.BOX);
	    		image2LeftCell.setFixedHeight(128f);

	    		image2LeftCell.setBorderColor(BaseColor.BLUE);
 
	            String imagePath = "/imagen/firma.jpg";
	    		Image image3 = Image.getInstance(GeneradorFilePdf.class.getResource(imagePath));
	    		image3.scalePercent(30f);
	    		image3.setAlignment(Element.ALIGN_CENTER);
	    		image2LeftCell.setImage(image3);
	    		image2LeftCell.setVerticalAlignment( Element.ALIGN_BOTTOM );
	    		image2LeftCell.setHorizontalAlignment(Element.ALIGN_CENTER );
	    		image2LeftCell.setBorder(Rectangle.NO_BORDER);
	    		table.addCell(image2LeftCell);

	            document.add(table);
	            document.add(paragraphLorem4);
	            document.newPage();
	            
	            
	            Font f5 = new Font();
	            f5.setFamily("Calibri");
	            f5.setStyle(Font.UNDERLINE);
	            f5.setSize(7);
	            Paragraph paragraphLorem5 = new Paragraph(13f); 
	            Chunk chunk5= new Chunk("DISTRIBUCIÓN:",f5);
	            paragraphLorem5.add(chunk5);
	            document.add(paragraphLorem5);
	            f5.setStyle(Font.NORMAL);
	            chunk5= new Chunk("PANAMA 1037 VILLA M.L. BOMBAL, SANTIAGO\r\nREGION METROPOLITANA\r\nServicio de Salud Metropolitano Occidente\r\nDivisión Gestión Territorial\r\nSubdepartamento GES\r\nOficina de Partes\r\n" + 
	            		"\r\n\r\n\r\n\r\n",f5);
	            
	            
	            paragraphLorem5 = new Paragraph(13f); 
	            paragraphLorem5.add(chunk5);
	            document.add(paragraphLorem5);
	            
	            
	            
	            Font f6 = new Font();
	            f6.setFamily("Lucida Sans");
	            f6.setStyle(Font.ITALIC);
	            f6.setSize(7);
	            Chunk chunk6= new Chunk("Si usted requiere mayor información de sus garantías o del AUGE en general, puede visitar nuestra página web www.fonasa.cl , en el banner \"Servicios en Línea\" opción \"Seguimiento de Garantías AUGE\" y \"Sistema de Monitoreo de Garantías\", o bien descargando la aplicación  para celulares \"GES Minsal\"." +
	            		"\r\n\r\n",f6);
	            Paragraph paragraphLorem6 = new Paragraph(13f); 
	            paragraphLorem6.setAlignment(Paragraph.ALIGN_JUSTIFIED);
	            paragraphLorem6.add(chunk6);
	            document.add(paragraphLorem6);
	            
	            String imagePath2 = "/imagen/imagen.jpg";
	    		Image imagen2 = Image.getInstance(GeneradorFilePdf.class.getResource(imagePath2));
	      		PdfPCell imagenCell = new PdfPCell();
	      		imagenCell.setBorder(Rectangle.NO_BORDER);
	      		imagenCell.setFixedHeight(10f);
	    		imagen2.scalePercent(100f);
	    		imagen2.setWidthPercentage(50f);
	    		imagen2.setAlignment(Element.ALIGN_LEFT);
	    		imagenCell.setImage(imagen2);
	    		imagenCell.setVerticalAlignment( Element.ALIGN_BOTTOM );
	    		imagenCell.setHorizontalAlignment(Element.ALIGN_LEFT );
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
	            Paragraph p = new Paragraph("Fecha Emisión :  30/04/2020 ",f7);
	            cellText.setVerticalAlignment(Element.ALIGN_CENTER);
	            cellText.setBorder(Rectangle.TOP);
	        	cellText.addElement(p);

	      		table3.addCell(cellText);
	      		p = new Paragraph("Código Verificación:  6B327D4E-127ED250-E5BF9647-75E196E8\r\n" + 
	      				"",f7);
	      		cellText = new PdfPCell();
	            cellText.setVerticalAlignment(Element.ALIGN_CENTER);
	            cellText.setBorder(Rectangle.TOP);
	        	cellText.addElement(p);
	            cellText.setBorder(Rectangle.NO_BORDER);
	      		table3.addCell(cellText);
	            document.add(table3);
	            
	            paragraphLorem4 = new Paragraph(13f); 
	            f2.setStyle(Font.NORMAL);
	             chunk4= new Chunk("\r\n\r\nEste documento incorpora firma electrónica avanzada\r\n" + 
	             		"",f2);
		            paragraphLorem4.setAlignment(Element.ALIGN_CENTER);
		            paragraphLorem4.add(chunk4);
		            document.add(paragraphLorem4);     
	            document.close();

	            FILE.close();
	  			return clave + ".pdf";
	  	
	}
	

}
