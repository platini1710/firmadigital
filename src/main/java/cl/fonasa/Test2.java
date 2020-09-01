package cl.fonasa;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String clave  ="file2";
        String ord="13779/2020";
        String fechaOrd="2020";
        String idCaso="987906";
        String tipo="FELICITACIÓN";
        String problemaSalud="";
        String nombreTipificacion="";
        Date fecha = new Date();
        String dee="PAULO VELASQUES GONZALEZ";
        String nombreSolicitante="YAZMIN NADIA MUSALEM CABELLO";





		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");
		// log.info("File :: " + clave + ".pdf");;
		Document document = new Document(PageSize.A4,50, 50, 150, 156);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);

		document.open();
		

		//document.add(tableBody);
		String respuesta ="<p align=\\\"justify\\\">Con motivo de la presentación de su reclamo ingresado en uno de nuestros canales de contacto,\\n    en el cual nos señala su molestia por el incumplimiento de garantías GES del problema de salud \\\"Cáncer gástrico\\\", \\n    del **, informo a usted lo siguiente:</p><br><p align=\\\"justify\\\"><p>\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n</p><table  cellpadding=\\\"0\\\" cellspacing=\\\"0\\\"  >\\n<!--StartFragment-->\\n <colgroup><col  >\\n <col  >\\n <col  >\\n </colgroup><tbody><tr  >\\n  <td  class=\\\"xl66\\\"  >Usuario:\\n  RUN<br>\\n    Contraseña: N° Serie<br>\\n    <br>\\n    11728551-0&nbsp; A028034257<br>\\n    22365160-7&nbsp; 513808950<br>\\n    8223599-K&nbsp; 515784655<br>\\n    20837788-4&nbsp; 517151454<br>\\n    15252806-K&nbsp; 105918342<br>\\n    7690204-6&nbsp; 105477015<br>\\n    17122970-7&nbsp; 518027476<br>\\n    10002236-2&nbsp; 108144401<br>\\n    9297948-2&nbsp; 521229910</td>\\n  <td class=\\\"xl65\\\"  >Crear Solicitud</td>\\n  <td class=\\\"xl66\\\"  >Permite crear\\n  una solicitud a traves del formulario solicitudes ciudadanas</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl67\\\"  >&nbsp;</td>\\n  <td class=\\\"xl68\\\" >&nbsp;</td>\\n  <td class=\\\"xl67\\\"  >&nbsp;</td>\\n </tr>\\n <tr  >\\n  <td rowspan=\\\"5\\\"  class=\\\"xl66\\\"  >Administrador<br>\\n    Base de Conocimiento<br>\\n    </td>\\n  <td class=\\\"xl65\\\" >Buscador de Preguntas</td>\\n  <td class=\\\"xl65\\\" >Permite la busqueda\\n  de preguntas</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Ver Presentacion de una pregunta</td>\\n  <td class=\\\"xl65\\\" >Permite ver el\\n  contenido de una preguntas</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Crear Pregunta</td>\\n  <td class=\\\"xl65\\\" >Permite crear una\\n  pregunta especifica</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Editar Pregunta</td>\\n  <td class=\\\"xl65\\\" >Permite editar una\\n  pregunta especifica</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Eliminar Pregunta</td>\\n  <td class=\\\"xl65\\\" >Permite eliminar una\\n  pregunta especifica</td>\\n </tr>\\n <tr  >\\n  <td rowspan=\\\"4\\\"  class=\\\"xl66\\\"  >Administrador<br>\\n    Base de Conocimiento<br>\\n    </td>\\n  <td class=\\\"xl65\\\" >Buscador Casos Fonasa\\n  Resuelve</td>\\n  <td class=\\\"xl65\\\" >Permite realizar la\\n  busqueda de casos correspondiente a Fonasa Resuelve</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Descartar Caso</td>\\n  <td class=\\\"xl65\\\" >Permite descartar\\n  caso para ser evaluado o tomado para generar contenido</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Crear Pregunta&nbsp;</td>\\n  <td class=\\\"xl65\\\" >Permite un acceso\\n  rápido para crear preguntas</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Ver detalle caso&nbsp;</td>\\n  <td class=\\\"xl65\\\" >Permite ver el\\n  histórico del caso para ver toda la información del caso</td>\\n </tr>\\n <tr  >\\n  <td rowspan=\\\"4\\\"  class=\\\"xl66\\\"  >Administrador<br>\\n    Base de Conocimiento<br>\\n    </td>\\n  <td class=\\\"xl65\\\" >Buscador Tags</td>\\n  <td class=\\\"xl66\\\"  >Permite\\n  consultar los Tags creados, cuenta con filtros para buscar por nombre o\\n  estado</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Crear Tags</td>\\n  <td class=\\\"xl65\\\" >Se despliega un modal\\n  y Permite crear un nuevo Tag</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Editar Tags</td>\\n  <td class=\\\"xl65\\\" >Permite la edición de\\n  los datos del Tag</td>\\n </tr>\\n <tr  >\\n  <td  class=\\\"xl65\\\" >Eliminar Tags</td>\\n  <td class=\\\"xl65\\\" >Permite eliminar un\\n  Tag existente</td>\\n </tr>\\n<!--EndFragment-->\\n</tbody></table></p><br><p align=\\\"justify\\\">En caso de disconformidad con el contenido de esta respuesta,usted podrá solicitar a la Superintendencia de Salud su revisión, debiendo acompañar copia de esta carta y de los antecedentes remitidos por esta institución.</p>";

		

		int indice1=respuesta.indexOf("p class=");
		while (indice1>0) {
			System.out.println(	respuesta.substring(indice1));


			respuesta=respuesta.substring(0, indice1) +respuesta.substring(indice1,respuesta.indexOf(">",indice1)) +  " align=\"justify\" "  + respuesta.substring(respuesta.indexOf(">",indice1));

		indice1=respuesta.indexOf("p class=",indice1 +1);
		}

		respuesta=respuesta.replaceAll("<br>", "<br/>");
		respuesta=respuesta.replaceAll("<colgroup>", "</colgroup>");
		//respuesta=respuesta.replaceAll("<col ", "</col>");
		respuesta="<html><body>" + respuesta + "</body></html>" ;

		
	
		//log.info("getPageNumber: " + document.getPageNumber());

		 //  k=k.replaceAll("\n", "");

			//System.out.println(k);
		  InputStream is = new ByteArrayInputStream(respuesta.getBytes());
		
		    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		
		document.close();
		FILE.flush();
		FILE.close();
  		
    }
}
