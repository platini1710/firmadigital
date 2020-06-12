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

import cl.fonasa.soa.protocolo.HeaderRequest;


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
 *         &lt;element name="headerRequest" type="{http://soa.fonasa.cl/protocolo/}HeaderRequest"/&gt;
 *         &lt;element name="bodyResquest" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="tipoCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="runCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="tramoCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "headerRequest",
    "bodyResquest"
})
@XmlRootElement(name = "gestionCertificadoRequest", namespace = "http://soa.fonasa.cl/gestionCertificado")
public class GestionCertificadoRequest {

    @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
    protected HeaderRequest headerRequest;
    @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado")
    protected GestionCertificadoRequest.BodyResquest bodyResquest;

    /**
     * Obtiene el valor de la propiedad headerRequest.
     * 
     * @return
     *     possible object is
     *     {@link HeaderRequest }
     *     
     */
    public HeaderRequest getHeaderRequest() {
        return headerRequest;
    }

    /**
     * Define el valor de la propiedad headerRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderRequest }
     *     
     */
    public void setHeaderRequest(HeaderRequest value) {
        this.headerRequest = value;
    }

    /**
     * Obtiene el valor de la propiedad bodyResquest.
     * 
     * @return
     *     possible object is
     *     {@link GestionCertificadoRequest.BodyResquest }
     *     
     */
    public GestionCertificadoRequest.BodyResquest getBodyResquest() {
        return bodyResquest;
    }

    /**
     * Define el valor de la propiedad bodyResquest.
     * 
     * @param value
     *     allowed object is
     *     {@link GestionCertificadoRequest.BodyResquest }
     *     
     */
    public void setBodyResquest(GestionCertificadoRequest.BodyResquest value) {
        this.bodyResquest = value;
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
     *         &lt;element name="tipoCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="runCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="tramoCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
        "tipoCertificado",
        "runCertificado",
        "tramoCertificado"
    })
    public static class BodyResquest {

        @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
        protected String tipoCertificado;
        @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
        protected String runCertificado;
        @XmlElement(namespace = "http://soa.fonasa.cl/gestionCertificado", required = true)
        protected String tramoCertificado;

        /**
         * Obtiene el valor de la propiedad tipoCertificado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoCertificado() {
            return tipoCertificado;
        }

        /**
         * Define el valor de la propiedad tipoCertificado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoCertificado(String value) {
            this.tipoCertificado = value;
        }

        /**
         * Obtiene el valor de la propiedad runCertificado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRunCertificado() {
            return runCertificado;
        }

        /**
         * Define el valor de la propiedad runCertificado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRunCertificado(String value) {
            this.runCertificado = value;
        }

        /**
         * Obtiene el valor de la propiedad tramoCertificado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTramoCertificado() {
            return tramoCertificado;
        }

        /**
         * Define el valor de la propiedad tramoCertificado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTramoCertificado(String value) {
            this.tramoCertificado = value;
        }

    }

}
