//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.31 a las 06:48:54 PM CLST 
//


package cl.fonasa.soa.protocolo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para HeaderRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="HeaderRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="userID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="rolID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="SucursalID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fechaHora" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderRequest", propOrder = {
    "userID",
    "rolID",
    "sucursalID",
    "fechaHora"
})
public class HeaderRequest {

    @XmlElement(namespace = "", required = true)
    protected String userID;
    @XmlElement(namespace = "", required = true)
    protected String rolID;
    @XmlElement(name = "SucursalID", namespace = "", required = true)
    protected String sucursalID;
    @XmlElement(namespace = "", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHora;

    /**
     * Obtiene el valor de la propiedad userID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Define el valor de la propiedad userID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * Obtiene el valor de la propiedad rolID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRolID() {
        return rolID;
    }

    /**
     * Define el valor de la propiedad rolID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRolID(String value) {
        this.rolID = value;
    }

    /**
     * Obtiene el valor de la propiedad sucursalID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSucursalID() {
        return sucursalID;
    }

    /**
     * Define el valor de la propiedad sucursalID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSucursalID(String value) {
        this.sucursalID = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaHora.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHora() {
        return fechaHora;
    }

    /**
     * Define el valor de la propiedad fechaHora.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHora(XMLGregorianCalendar value) {
        this.fechaHora = value;
    }

}
