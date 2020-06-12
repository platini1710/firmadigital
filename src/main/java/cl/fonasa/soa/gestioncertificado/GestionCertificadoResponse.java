//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.31 a las 06:48:54 PM CLST 
//


package cl.fonasa.soa.gestioncertificado;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import cl.fonasa.soa.protocolo.HeaderResponse;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="headerResponse" type="{http://soa.fonasa.cl/protocolo/}HeaderResponse"/&gt;
 *         &lt;element name="bodyResponse" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="codigoEstado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="mensajeEstado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "headerResponse",
    "bodyResponse"
})
@XmlRootElement(name = "gestionCertificadoResponse", namespace = "http://soa.fonasa.cl/gestionCertificado")
public class GestionCertificadoResponse {

    @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
    protected HeaderResponse headerResponse;
    @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado")
    protected GestionCertificadoResponse.BodyResponse bodyResponse;

    /**
     * Obtiene el valor de la propiedad headerResponse.
     * 
     * @return
     *     possible object is
     *     {@link HeaderResponse }
     *     
     */
    public HeaderResponse getHeaderResponse() {
        return headerResponse;
    }

    /**
     * Define el valor de la propiedad headerResponse.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderResponse }
     *     
     */
    public void setHeaderResponse(HeaderResponse value) {
        this.headerResponse = value;
    }

    /**
     * Obtiene el valor de la propiedad bodyResponse.
     * 
     * @return
     *     possible object is
     *     {@link GestionCertificadoResponse.BodyResponse }
     *     
     */
    public GestionCertificadoResponse.BodyResponse getBodyResponse() {
        return bodyResponse;
    }

    /**
     * Define el valor de la propiedad bodyResponse.
     * 
     * @param value
     *     allowed object is
     *     {@link GestionCertificadoResponse.BodyResponse }
     *     
     */
    public void setBodyResponse(GestionCertificadoResponse.BodyResponse value) {
        this.bodyResponse = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="codCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="codigoEstado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="mensajeEstado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codCertificado",
        "codigoEstado",
        "mensajeEstado"
    })
    public static class BodyResponse {

        @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
        protected String codCertificado;
        @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
        protected String codigoEstado;
        @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
        protected String mensajeEstado;

        /**
         * Obtiene el valor de la propiedad codCertificado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodCertificado() {
            return codCertificado;
        }

        /**
         * Define el valor de la propiedad codCertificado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodCertificado(String value) {
            this.codCertificado = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoEstado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoEstado() {
            return codigoEstado;
        }

        /**
         * Define el valor de la propiedad codigoEstado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoEstado(String value) {
            this.codigoEstado = value;
        }

        /**
         * Obtiene el valor de la propiedad mensajeEstado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMensajeEstado() {
            return mensajeEstado;
        }

        /**
         * Define el valor de la propiedad mensajeEstado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMensajeEstado(String value) {
            this.mensajeEstado = value;
        }

    }

}
