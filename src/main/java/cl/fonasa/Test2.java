package cl.fonasa;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import cl.fonasa.utils.FTP;
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
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import cl.fonasa.controller.SignDesAtendidaController;
import cl.fonasa.pdf.GeneradorFilePdf;
import cl.fonasa.service.SignFileService;

public class Test2 {

	private static final Logger log = LoggerFactory.getLogger(Test2.class);
    public static final String FONT= "/Font/Calibri-7.ttf";
    public static void main(String[] args) throws DocumentException, MalformedURLException, IOException, org.json.simple.parser.ParseException {

		FTP ftp=new FTP("foprdotdgen.fonasa.local",7522,"soliciu","Fonasa2020.,");
		//ftp.upload(solicitud.getIdCaso(), fis, "", solicitud.getPath(), clave + fileFirmadoDigital);// fallo
		// subida
    }
}
