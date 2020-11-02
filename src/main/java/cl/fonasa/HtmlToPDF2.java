package cl.fonasa;

import java.io.FileOutputStream;
import java.io.StringReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class HtmlToPDF2 {

	  // itextpdf-5.4.1.jar  http://sourceforge.net/projects/itext/files/iText/
	  // xmlworker-5.4.1.jar http://sourceforge.net/projects/xmlworker/files/
	  public static void main(String ... args ) {
		    try {
		      Document document = new Document(PageSize.LETTER);
		      PdfWriter pdfWriter = PdfWriter.getInstance
		           (document, new FileOutputStream("testpdf.pdf"));
		      document.open();
		      document.addAuthor("Real Gagnon");
		      document.addCreator("Real's HowTo");
		      document.addSubject("Thanks for your support");
		      document.addCreationDate();
		      document.addTitle("Please read this");

		      XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

		      String str = "<html><body>"+
		        "<a href='http://www.rgagnon.com/howto.html'><b>Real's HowTo</b></a>" +
		        "<h1>Show your support</h1>" +
		   "</body></html>";
		      worker.parseXHtml(pdfWriter, document, new StringReader(str));
		   System.out.println( "size page "+  pdfWriter.getPageSize());
		      document.close();
		      System.out.println("Done.");
		      }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		  }

}
