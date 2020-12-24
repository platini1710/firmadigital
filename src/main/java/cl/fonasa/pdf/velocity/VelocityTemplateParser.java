package cl.fonasa.pdf.velocity;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import cl.fonasa.dto.Consulta;
import cl.fonasa.dto.Denuncia;
import cl.fonasa.dto.Felicitacion;
import cl.fonasa.dto.Reclamos;
import cl.fonasa.dto.SolicitudDto;
import cl.fonasa.dto.UsoInterno;

public class VelocityTemplateParser< T extends Reclamos> {
	
	
	public static <T> String generateHTML(T  o)throws Exception {

		// initialize velocity engine
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		Template t =null;

		// add that list to a VelocityContext
		VelocityContext context = new VelocityContext();
		if (o instanceof Reclamos) {

			context.put("Ord",((Reclamos) o).getOrd());
			context.put("Folio",((Reclamos) o).getFolio());
			context.put("Tipo",((Reclamos) o).getTipo());
			context.put("Commpany",((Reclamos) o).getCompany());
			context.put("De", ((Reclamos) o).getDe());
			context.put("A", ((Reclamos) o).getPara());
			context.put("Jefe", ((Reclamos) o).getJefe());
			context.put("msg", ((Reclamos) o).getMsg());
		//	 t = ve.getTemplate("templates/Reclamo.vm");
			 t = ve.getTemplate("templates/Reclamo.vm");

			// get the Template
		}	else	if (o instanceof Denuncia) {

			context.put("Ord",((Denuncia) o).getOrd());
			context.put("Folio",((Denuncia) o).getFolio());
			context.put("Tipo",((Denuncia) o).getTipo());
			context.put("Commpany",((Denuncia) o).getCompany());
			context.put("De", ((Denuncia) o).getDe());
			context.put("A", ((Denuncia) o).getPara());
			context.put("Jefe", ((Denuncia) o).getJefe());
			context.put("msg", ((Denuncia) o).getMsg());
			t = ve.getTemplate("templates/Denuncia.vm");

			// get the Template
		}	else	if (o instanceof Consulta) {

			context.put("Ord",((Consulta) o).getOrd());
			context.put("Folio",((Consulta) o).getFolio());
			context.put("Tipo",((Consulta) o).getTipo());
			context.put("Commpany",((Consulta) o).getCompany());
			context.put("De", ((Consulta) o).getDe());
			context.put("A", ((Consulta) o).getPara());
			context.put("Jefe", ((Consulta) o).getJefe());
			context.put("msg", ((Consulta) o).getMsg());
			t = ve.getTemplate("templates/Consulta.vm");

		}else	if (o instanceof SolicitudDto) {

			context.put("Ord",((SolicitudDto) o).getOrd());
			context.put("Folio",((SolicitudDto) o).getFolio());
			context.put("Tipo",((SolicitudDto) o).getTipo());
			context.put("Commpany",((SolicitudDto) o).getCompany());
			context.put("De", ((SolicitudDto) o).getDe());
			context.put("A", ((SolicitudDto) o).getPara());
			context.put("Jefe", ((SolicitudDto) o).getJefe());
			context.put("msg", ((SolicitudDto) o).getMsg());
			t = ve.getTemplate("templates/Solicitud.vm");

		}else	if (o instanceof Felicitacion) {

			context.put("Ord",((Felicitacion) o).getOrd());
			context.put("Folio",((Felicitacion) o).getFolio());
			context.put("Tipo",((Felicitacion) o).getTipo());
			context.put("Commpany",((Felicitacion) o).getCompany());
			context.put("De", ((Felicitacion) o).getDe());
			context.put("A", ((Felicitacion) o).getPara());
			context.put("Jefe", ((Felicitacion) o).getJefe());
			context.put("msg", ((Felicitacion) o).getMsg());
			context.put("dzFirmante", ((Felicitacion) o).getDzFirmante());
			context.put("departamentoFirmante", ((Felicitacion) o).getDepartamentoFirmante());
			context.put("subDepartamentoFirmante", ((Felicitacion) o).getSubDeptoFirmante());
			context.put("ciudadDate", ((Felicitacion) o).getCiudadDate());
			t = ve.getTemplate("templates/Felicitacion.vm");

		}		else {

			context.put("Ord",((UsoInterno) o).getOrd());
			context.put("Folio",((UsoInterno) o).getFolio());
			context.put("Tipo",((UsoInterno) o).getTipo());
			context.put("Commpany",((UsoInterno) o).getCompany());
			context.put("De", ((UsoInterno) o).getDe());
			context.put("A", ((UsoInterno) o).getPara());
			context.put("Jefe", ((UsoInterno) o).getJefe());
			context.put("msg", ((UsoInterno) o).getMsg());
			 t = ve.getTemplate("templates/UsoInterno.vm");

			// get the Template
		}
		
		
		
		

		
		// render the template into a Writer, here a StringWriter
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}
}
