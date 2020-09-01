package cl.fonasa;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class ItextHtmlToPDFExample {
	  public static void main(String[] args)
	    {
	        try
	        {
	               OutputStream file = new FileOutputStream(new File("HTMLtoPDF.pdf"));
	            Document document = new Document();
	            PdfWriter writer = PdfWriter.getInstance(document, file);
	            StringBuilder htmlString = new StringBuilder();
	            htmlString.append(new String("<html><body> <br>This is HMTL to PDF conversion Example<table border='2' align='center'> "));
	            htmlString.append(new String("<tr><td>JavaCodeGeeks</td><td><a href='examples.javacodegeeks.com'>JavaCodeGeeks</a> </td></tr>"));               
	            htmlString.append(new String("<tr> <td> Google Here </td> <td><a href='www.google.com'>Google</a> </td> </tr></table></body></html>"));
	                             
	            document.open();
	            InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
	            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
	            document.close();
	            file.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	 
	    }
}
