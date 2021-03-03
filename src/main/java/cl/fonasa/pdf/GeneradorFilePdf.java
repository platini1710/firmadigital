package cl.fonasa.pdf;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
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
import cl.fonasa.dto.Consulta;
import cl.fonasa.dto.Denuncia;
import cl.fonasa.dto.Felicitacion;
import cl.fonasa.dto.Reclamos;
import cl.fonasa.dto.Solicitud;
import cl.fonasa.dto.SolicitudDto;
import cl.fonasa.dto.UsoInterno;
import cl.fonasa.pdf.velocity.VelocityTemplateParser;
import cl.fonasa.service.SignFileService;

public class GeneradorFilePdf {

	private static final Logger log = LoggerFactory.getLogger(SignDesAtendidaController.class);

	public static final String FONT = "/Font/arial.ttf";
	public static final String company ="FONDO NACIONAL DE SALUD";
	public String generaFileReclamposPdf(Solicitud solicitud, String ordinario, String respuesta, String clave,
			String wsdl, String parrafoUno, String parrafoDos, int[] numPage, String cargo, String institucion,
			String dzFirmante, String subDeptoFirmante, String departamentoFirmante, String direccionSolicitante,
			String iniciales, long numeroSolicitud, String ciudad, String firma) throws IOException, DocumentException {
		
		
		String nombreSolicitante = solicitud.getNombreSolicitante().toUpperCase();


		String run = solicitud.getRunUsuarioEjecuta();

		String html = "";

		String tipo = solicitud.getTipo().toUpperCase();
		String de = solicitud.getDe().toUpperCase();

		long idCaso = solicitud.getIdCaso();
		respuesta=limpiaTag(respuesta);
		int intArray[] = new int[1];
		try {
		respuesta = convertImagen(respuesta, clave, intArray);	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		Date fecha = new Date();
		BaseFont fontNormal = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
		Font fontN= FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL,
				BaseColor.BLACK);
		Paragraph parrafoFirmas = parrafoFirma( cargo, institucion, de) ;
		try {
			if (tipo.indexOf("RECLAMO")>=0) {
				Reclamos reclamos=new Reclamos();
				reclamos.setCompany(company.toUpperCase());
				reclamos.setDe(de.toUpperCase());
				reclamos.setFolio(String.valueOf(idCaso));
				reclamos.setJefe(cargo.toUpperCase());
				reclamos.setOrd(ordinario.toUpperCase());
				reclamos.setPara(nombreSolicitante.toUpperCase().trim());
				reclamos.setTipo(tipo);
				reclamos.setMsg(respuesta);
				html = VelocityTemplateParser.generateHTML(reclamos);
			} else 			if (tipo.indexOf("DENUNCIA")>=0) {
				Denuncia denuncia=new Denuncia();
				denuncia.setCompany(company.toUpperCase());
				denuncia.setDe(de.toUpperCase());
				denuncia.setFolio(String.valueOf(idCaso));
				denuncia.setJefe(cargo.toUpperCase());
				denuncia.setOrd(ordinario.toUpperCase());
				denuncia.setPara(nombreSolicitante.toUpperCase().trim());
				denuncia.setTipo(tipo);
				denuncia.setMsg(respuesta);
				html = VelocityTemplateParser.generateHTML(denuncia);
			}else 			if (tipo.indexOf("SOLICITUD")>=0) {
				SolicitudDto solicitudDto=new SolicitudDto();
				solicitudDto.setCompany(company.toUpperCase());
				solicitudDto.setDe(de.toUpperCase());
				solicitudDto.setFolio(String.valueOf(idCaso));
				solicitudDto.setJefe(cargo.toUpperCase());
				solicitudDto.setOrd(ordinario.toUpperCase());
				solicitudDto.setPara(nombreSolicitante.toUpperCase().trim());
				solicitudDto.setTipo(tipo);
				solicitudDto.setMsg(respuesta);
				html = VelocityTemplateParser.generateHTML(solicitudDto);
			}
			else 			if (tipo.indexOf("CONSULTA")>=0) {
				Consulta consulta=new Consulta();
				consulta.setCompany(company.toUpperCase());
				consulta.setDe(de.toUpperCase());
				consulta.setFolio(String.valueOf(idCaso));
				consulta.setJefe(cargo.toUpperCase());
				consulta.setOrd(ordinario.toUpperCase());
				consulta.setPara(nombreSolicitante.toUpperCase().trim());
				consulta.setTipo(tipo);
				consulta.setMsg(respuesta);
				html = VelocityTemplateParser.generateHTML(consulta);
			}
			else {
				UsoInterno usoInterno=new UsoInterno();
				usoInterno.setCompany(company.toUpperCase());
				usoInterno.setDe(de.toUpperCase());
				usoInterno.setFolio(String.valueOf(idCaso));
				usoInterno.setJefe(cargo.toUpperCase());
				usoInterno.setOrd(ordinario.toUpperCase());
				usoInterno.setPara(nombreSolicitante.toUpperCase().trim());
				usoInterno.setTipo(tipo);
				usoInterno.setMsg(respuesta);
				html = VelocityTemplateParser.generateHTML(usoInterno);
			}
		} catch (Exception e) {
			log.error("File :: [" + clave + "].pdf");
		}

		if (log.isDebugEnabled()) {
			log.debug("File :: [" + clave + "].pdf");
		}
		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");


		int marginLeft = 60;
		int marginRight = 60;
		int marginTop = 120;
		int marginBottom = 260;

		numPage[1] = 0;
		numPage[2] = 0;


		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);
		int cantConFirmas = countPageFileReclamposConFirmaPdf(  html, firma, parrafoFirmas, clave, marginLeft,  marginRight,  marginTop,  marginBottom,numPage);
		int cantSinFirmas =countPageFileReclamposSinFirmaPdf( html, clave , marginLeft,  marginRight,  marginTop,  marginBottom, numPage);
		cl.fonasa.pdf.HeadFootPdf event = new cl.fonasa.pdf.HeadFootPdf(cantConFirmas, cantSinFirmas,parrafoFirmas);
		writer.setPageEvent(event);
	/*	int cantConFirmas=0;
		int cantSinFirmas=0;
		*/


		document.open();

	


		// ******************************Body Reclamo
		// ******************************************************

		XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
		xmlWorkerHelper.getDefaultCssResolver(true);
		xmlWorkerHelper.parseXHtml(writer, document, new StringReader(html));
		/*************************************************fin firma ****************************
		 * 
		 *		 inicio Firma
		 * 
		 */

		if (cantConFirmas == cantSinFirmas) {
			PdfPTable tablaFirma=agregaFirma(  parrafoFirmas, firma);
			document.add(tablaFirma);
		}
		
		
		
		
		
		
		document.newPage();

		Chunk chunkFoot = new Chunk(iniciales + "  ", new Font(fontNormal, 12));
		Paragraph paragraphFoot = new Paragraph(13f);
		paragraphFoot.setFont(fontN);
		PdfPTable tableFooter = new PdfPTable(1);
		tableFooter.setWidthPercentage(100);

		paragraphFoot.setAlignment(Paragraph.ALIGN_LEFT);
		paragraphFoot.add(chunkFoot);
		chunkFoot = new Chunk("\r\nDISTRIBUCIÓN: \r\n", new Font(fontNormal, 12));
		paragraphFoot.add(chunkFoot);

		chunkFoot = new Chunk(direccionSolicitante, new Font(fontNormal, 12));
		paragraphFoot.add(direccionSolicitante);

		document.add(paragraphFoot);

        document.add(Chunk.NEWLINE);
 





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

		Chunk	chunkFirma = new Chunk("\r\n\r\nEste documento incorpora firma electrónica avanzada\r\n" + "",
				new Font(fontNormal, 12));
		paragraphFoot2.setAlignment(Element.ALIGN_CENTER);
		paragraphFoot2.add(chunkFirma);
		document.add(paragraphFoot2);
		solicitud.setNumeroPaginas(writer.getPageNumber());
		if (log.isDebugEnabled()) {
			log.debug("paso numeroPaginas::[" + writer.getPageNumber() + "]");
		}
		
		
		
		/*************************************************fin firma ****************************
		 * 
		 *		 Fin Firma
		 * 
		 */
		
		
		
		


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


		Paragraph paragraphead = new Paragraph(13f);

		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);

		BaseFont bf = BaseFont.createFont(FONT, BaseFont.CP1257, BaseFont.EMBEDDED);
		Font fontNormal= FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL,
				BaseColor.BLACK);
		int intArray[] = new int[1];
		
		respuesta=limpiaTag(respuesta);
		try {
			respuesta = convertImagen(respuesta, clave, intArray);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		
		if (log.isDebugEnabled()) {
			log.debug("respuesta ::" + respuesta);
		}

		//// paragraphBody.add(chunk2);Saluda atentamente,

		Date fecha = new Date();
		Paragraph parrafoFirmas = parrafoFirma( cargo, institucion, de) ;
		FileOutputStream FILE = new FileOutputStream(clave + ".pdf");

		// log.info("File :: " + clave + ".pdf");;
		int marginLeft = 60;
		int marginRight = 60;
		int marginTop = 120;
		int marginBottom = 260;



		String html="";

				try {
		
					Felicitacion felicitacion=new Felicitacion();
					felicitacion.setCompany(company.toUpperCase());
					felicitacion.setDe(de.toUpperCase());
					felicitacion.setFolio(String.valueOf(idCaso));
					felicitacion.setJefe(cargo.toUpperCase());
					felicitacion.setOrd(ordinario.toUpperCase());
					felicitacion.setPara(nombreSolicitante.toUpperCase().trim());
					felicitacion.setTipo(tipo);
					felicitacion.setMsg(respuesta);
					felicitacion.setDzFirmante(dzFirmante);
					felicitacion.setDepartamentoFirmante(departamentoFirmante);
					felicitacion.setSubDeptoFirmante(subDeptoFirmante);
					felicitacion.setCiudadDate(ciudad + " " +new SimpleDateFormat("dd-MM-yyyy").format(fecha));
					html = VelocityTemplateParser.generateHTML(felicitacion);
			
			} catch (Exception e) {
				log.error("File :: [" + clave + "].pdf");
			}

		int cantConFirmas = countPageFileReclamposConFirmaPdf(  html, firma, parrafoFirmas, clave, marginLeft,  marginRight,  marginTop,  marginBottom,numPage);
		int cantSinFirmas =countPageFileReclamposSinFirmaPdf( html, clave , marginLeft,  marginRight,  marginTop,  marginBottom, numPage);

		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);

		cl.fonasa.pdf.HeadFootOtrosPdf event = new cl.fonasa.pdf.HeadFootOtrosPdf(departamentoFirmante,
				subDeptoFirmante, dzFirmante, cantConFirmas, cantSinFirmas, parrafoFirmas);
		writer.setPageEvent(event);

		document.open();

		/*
		 * --------------------------------------------Head-----------------------------
		 * ------------------
		 */



		// ------------------------------------------------------**********************************************************


		// document.add(tableBody);
		if (log.isDebugEnabled()) {
			log.debug("respuesta::" + respuesta);
		}
		


		XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
		xmlWorkerHelper.getDefaultCssResolver(true);
		xmlWorkerHelper.parseXHtml(writer, document, new StringReader(html));

		/*
		 * 
		 * start firma
		 * 
		 * 
		 */
		if (cantConFirmas == cantSinFirmas) {


			PdfPTable tablaFirma=agregaFirma(  parrafoFirmas, firma);
			document.add(tablaFirma);
		}
		
		/*
		 * 
		 * fin firma
		 */

		document.newPage();
		Chunk chunkFoot = new Chunk(iniciales + "  ", new Font(bf, 12));
		Paragraph paragraphFoot = new Paragraph(13f);
		paragraphFoot.setFont(fontNormal);
		PdfPTable tableFooter = new PdfPTable(1);
		tableFooter.setWidthPercentage(100);
		PdfPCell cellFoot = new PdfPCell();
		paragraphFoot.setAlignment(Paragraph.ALIGN_LEFT);
		paragraphFoot.add(chunkFoot);
		chunkFoot = new Chunk("\r\nDISTRIBUCIÓN: \r\n", new Font(bf, 12));
		paragraphFoot.add(chunkFoot);

		chunkFoot = new Chunk(direccionSolicitante, new Font(bf, 12));
	//	paragraphFoot.add(chunkFoot);
		paragraphFoot.add(direccionSolicitante);
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

	public int countPageFileReclamposConFirmaPdf(String html,String firma,Paragraph parrafoFirmas,String clave,int marginLeft,int  marginRight,int  marginTop,int  marginBottom,int[] numPage) throws IOException, DocumentException {


String nameFile=clave + "2.pdf";
int numeroPagina=0;



		if (log.isDebugEnabled()) {
			log.debug("File :: [" + clave + "].pdf");
		}
		FileOutputStream FILE = new FileOutputStream(nameFile);


		
		int intArray[] = new int[1];
		;

		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);

		document.open();

		Paragraph paragraphead = new Paragraph(25f);

		paragraphead.setAlignment(Paragraph.ALIGN_RIGHT);



	
		// ******************************Body Reclamo
		// ******************************************************

		InputStream is = new ByteArrayInputStream(html.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		PdfPTable tablaFirma=agregaFirma(  parrafoFirmas, firma);
		document.add(tablaFirma);


		document.close();

		FILE.flush();
		if (log.isDebugEnabled()) {
			log.debug("getPageNumber: [" + writer.getPageNumber() + "]");
		}
		numPage[1] = writer.getPageNumber();
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

	
	
	public int countPageFileReclamposSinFirmaPdf(String html,String clave ,int marginLeft, int marginRight, int marginTop, int marginBottom,int[] numPage) throws IOException, DocumentException {

		String nameFile=clave + "1.pdf";
		FileOutputStream FILE = new FileOutputStream(nameFile);
		Document document = new Document(PageSize.LEGAL, marginLeft, marginRight, marginTop, marginBottom);

		PdfWriter writer = PdfWriter.getInstance(document, FILE);

		document.open();


		InputStream is = new ByteArrayInputStream(html.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);


		document.close();

		FILE.flush();
		if (log.isDebugEnabled()) {
			log.debug("getPageNumber: [" + writer.getPageNumber() + "]");
		}
		numPage[2] = writer.getPageNumber();
		FILE.close();
		
		int numeroPagina = writer.getCurrentPageNumber() - 1;
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
			String[] arrImg = src.split(",");
			String extension;

			switch (arrImg[0]) {// check image's extension
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
			
			


			byte[] data = DatatypeConverter.parseBase64Binary(arrImg[1]);
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
	
	
	public String limpiaTag(String respuesta) {
		
	    StringBuilder respuestaB 
        = new StringBuilder();
	    respuestaB.append( "\r\n<style>\r\n	");


		respuestaB.append(  "	table {\r\n font-family: Arial;\r\n" );
	    respuestaB.append( "			   font-size:12px;\r\n" );
		respuestaB.append( "  border-style: groove;\r\n");
		respuestaB.append( "  border-width: 0px;\r\n");
		respuestaB.append( "  border-color: black; \r\n");
		respuestaB.append(  "  		border-collapse: collapse;\r\n" );
		respuestaB.append(  "  		width: 100%;\r\n" );
		
		respuestaB.append(  "}\r\n");
		respuestaB.append(  "th{\r\n" );
		respuestaB.append( "	  padding: 8px;text-align: center;\r\n vertical-align: middle;\r\n border:1px solid #000000;\r\n font-family: \"Arial\";font-size: 12px; width:100%; border-collapse: collapse;" );
		respuestaB.append(  "\r\n}\r\n  td { \r\n padding: 4px;\r\n text-align: left;\r\n vertical-align: middle;\r\n border-style: groove;\r\n border-width:1px ;\r\n   border-color: black;\r\n font-family: \"Arial\";\r\n font-size: 12px;\r\n  width:100%;\r\n  border-collapse: collapse;\r\n  	 	    padding: 1px;\r\n }		\r\n </style>\r\n "+ respuesta);
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

		int indiceBR = respuesta.toLowerCase().indexOf("<br");
		while (indiceBR > 0) {
			
			respuesta = respuesta.substring(0, indiceBR) + "<br/>" + respuesta.substring(respuesta.indexOf(">", indiceBR) +1, respuesta.length());
			indiceBR=respuesta.toLowerCase().indexOf("<br", indiceBR +1) ;
		}
		indiceBR = respuesta.toLowerCase().indexOf("<meta");
		while (indiceBR > 0) {
	
			respuesta = respuesta.substring(0, indiceBR) + "<meta/>" + respuesta.substring(respuesta.indexOf(">", indiceBR) +1, respuesta.length());
			indiceBR=respuesta.toLowerCase().indexOf("<meta", indiceBR +1) ;
		}
		 indiceBR = respuesta.toLowerCase().indexOf("<link");
		while (indiceBR > 0) {
	
			respuesta = respuesta.substring(0, indiceBR) + "<link/>" + respuesta.substring(respuesta.indexOf(">", indiceBR) +1, respuesta.length());
			indiceBR=respuesta.toLowerCase().indexOf("<link", indiceBR +1) ;
		}
		//respuesta = respuesta.replaceAll("<p>", "<p align=\"justify\">");
		
	return respuesta;
	}
	public Paragraph parrafoFirma(String cargo,String institucion,String de) {
		Font fontBold = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD,
				BaseColor.BLACK);
	Paragraph paragraphFirma = new Paragraph(13f);
	Chunk chunkFirma = new Chunk("SR(A). " + de.toUpperCase(), fontBold);
	paragraphFirma.setAlignment(Element.ALIGN_CENTER);
	paragraphFirma.add(chunkFirma);
	chunkFirma = new Chunk("\r\n" + cargo.toUpperCase(), fontBold);
	paragraphFirma.add(chunkFirma);
	chunkFirma = new Chunk("\r\n" + institucion.toUpperCase(), fontBold);
	paragraphFirma.add(chunkFirma);
	return paragraphFirma;
	}
	
	
	public PdfPTable agregaFirma(Paragraph paragraphFirma,String firma) throws MalformedURLException, IOException, DocumentException {


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
		return tableFirma;
}
	
	
	
	
	
	
}