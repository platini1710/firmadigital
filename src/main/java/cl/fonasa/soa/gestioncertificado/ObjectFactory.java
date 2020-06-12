
package cl.fonasa.soa.gestioncertificado;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import cl.fonasa.soa.protocolo.Fault;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cl.fonasa.soa.gestioncertificado package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GestionCertificadoFault_QNAME = new QName("http://soa.fonasa.cl/gestionCertificado", "gestionCertificadoFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cl.fonasa.soa.gestioncertificado
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GestionCertificadoRequest }
     * 
     */
    public GestionCertificadoRequest createGestionCertificadoRequest() {
        return new GestionCertificadoRequest();
    }

    /**
     * Create an instance of {@link GestionCertificadoResponse }
     * 
     */
    public GestionCertificadoResponse createGestionCertificadoResponse() {
        return new GestionCertificadoResponse();
    }

    /**
     * Create an instance of {@link GestionCertificadoRequest.BodyResquest }
     * 
     */
    public GestionCertificadoRequest.BodyResquest createGestionCertificadoRequestBodyResquest() {
        return new GestionCertificadoRequest.BodyResquest();
    }

    /**
     * Create an instance of {@link GestionCertificadoResponse.BodyResponse }
     * 
     */
    public GestionCertificadoResponse.BodyResponse createGestionCertificadoResponseBodyResponse() {
        return new GestionCertificadoResponse.BodyResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soa.fonasa.cl/gestionCertificado", name = "gestionCertificadoFault")
    public JAXBElement<Fault> createGestionCertificadoFault(Fault value) {
        return new JAXBElement<Fault>(_GestionCertificadoFault_QNAME, Fault.class, null, value);
    }

}
