package cl.fonasa.pdf;


import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class HeadFootOtrosPdf extends PdfPageEventHelper {
    public static final String FONTBold = "/Font/CalibriBold.ttf";
    public static final String FONTNormal= "/Font/CalibriRegular.ttf";
    
	private static final Logger log = LoggerFactory.getLogger(GeneradorFilePdf.class);
    private PdfTemplate t;
    private Image total;
    


    public HeadFootOtrosPdf() {

    }
    public void onOpenDocument(PdfWriter writer, Document document) {

        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer);
        addFooter(writer);
    }

    private void addHeader(PdfWriter writer) {
        PdfPTable table = new PdfPTable(3);
        try {
            Font f0 =FontFactory.getFont(FONTBold, BaseFont.WINANSI, BaseFont.EMBEDDED, 8); 
            f0.setFamily("Calibri");
            f0.setStyle(Font.BOLD);
            f0.setSize(8);
            table.setWidths(new float[]{1, 4, 1});
            table.setTotalWidth(495);

            table.setLockedWidth(true);
            // add image
            String imagePath="/imagen/LOG-FON.jpg";
            Image logo = Image.getInstance(GeneradorFilePdf.class.getResource(imagePath));

            //   logo.scaleToFit(110,110);GeneradorFilePdf
            logo.setAbsolutePosition(0, 0);
            logo.setAlignment(Element.ALIGN_LEFT);
            PdfPCell logoLeftCell = new PdfPCell();
            logoLeftCell.setImage(logo);
            logoLeftCell.setFixedHeight(68f);
            logoLeftCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(logoLeftCell);
            // add text
            PdfPCell text = new PdfPCell();
       

            //chunk1.setUnderline(1.5f, -1);

            Paragraph headParaTit = new Paragraph();

            headParaTit.setAlignment(Paragraph.ALIGN_CENTER);
            text.addElement(headParaTit);
            




   
            text.setFixedHeight(68f);
            text.setBorder(Rectangle.NO_BORDER);
            table.addCell(text);
            
            
            
            
            
            text = new PdfPCell();
            logoLeftCell = new PdfPCell();
            imagePath="/imagen/logoFonasa.png";
            logo = Image.getInstance(GeneradorFilePdf.class.getResource(imagePath));

            logo.setAlignment(Element.ALIGN_RIGHT);
            logoLeftCell.setImage(logo);
            logoLeftCell.setFixedHeight(68f);
            logoLeftCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoLeftCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(logoLeftCell);
            // write content

            headParaTit = new Paragraph(12);
            headParaTit.setAlignment(Element.ALIGN_LEFT);
            Chunk chunk0= new Chunk("FONASA NIVEL CENTRAL\r\nDIVISIÓN SERVICIO AL USUARIO\r\nDPTO. GESTIÓN CIUDADANA\r\nSUBDPTO GESTIÓN DE SOLICITUDES CIUDADANAS " ,f0 ) ;  
            headParaTit.add(chunk0);
            text.setColspan(3);
            text.setVerticalAlignment(Element.ALIGN_TOP);
            text.addElement(headParaTit);
            text.setPaddingTop(0);
            text.setBorder(Rectangle.NO_BORDER);
            logoLeftCell.setBorder(Rectangle.NO_BORDER);
            text.setBorder(Rectangle.NO_BORDER);
            table.addCell(text);    
            table.setWidthPercentage(100);
            PdfPCell text2 = new PdfPCell();
            
            text2.setBorder(Rectangle.NO_BORDER);
      

            text2.setColspan(3);
            table.addCell(text2); 
            

            
            
            
            table.writeSelectedRows(0, 2, 54, 810, writer.getDirectContent());
        } catch (DocumentException de) {
            log.error(de.getMessage(), de);
            throw new ExceptionConverter(de);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ExceptionConverter(e);
        }
    }

     private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(1);
        try {
            // set defaults
            footer.setWidths(new int[]{1});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(20);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.WHITE);

            Font f0 = new Font();
            f0.setFamily("Calibri");
            f0.setStyle(Font.NORMAL);
            f0.setSize(10);
            // add copyright
           // footer.addCell(new Phrase("", f0));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Phrase phrase= new Phrase("");
      		PdfPCell imagenCell = new PdfPCell();
      	  Paragraph paragraphLorem4 = new Paragraph(); 
      	paragraphLorem4.add(phrase);
        paragraphLorem4.setAlignment(Element.ALIGN_RIGHT);
      		imagenCell.addElement(paragraphLorem4 );
      		imagenCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      		imagenCell.setPaddingRight(65f);
      		imagenCell.setBorder(Rectangle.NO_BORDER);
            footer.addCell(imagenCell);

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total);
            totalPageCount.setBorder(Rectangle.NO_BORDER);

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
             log.error(de.getMessage(), de);

        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {

        ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                new Phrase(""),
                2, 2, 0);

    }
}









