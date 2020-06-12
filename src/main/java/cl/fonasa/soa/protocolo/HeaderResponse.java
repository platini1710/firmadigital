//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.31 a las 06:48:54 PM CLST 
//


package cl.fonasa.soa.protocolo;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para HeaderResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="HeaderResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="transaccionID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="estadoID" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="estadoMSG" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "HeaderResponse", propOrder = {
    "transaccionID",
    "estadoID",
    "estadoMSG",
    "fechaHora"
})
public class HeaderResponse {

    @XmlElement(namespace = "", required = true)
    protected String transaccionID;
    @XmlElement(namespace = "", required = true)
    protected BigInteger estadoID;
    @XmlElement(namespace = "", required = true)
    protected String estadoMSG;
    @XmlElement(namespace = "", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHora;

    /**
     * Obtiene el valor de la propiedad transaccionID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransaccionID() {
        return transaccionID;
    }

    /**
     * Define el valor de la propiedad transaccionID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransaccionID(String value) {
        this.transaccionID = value;
    }

    /**
     * Obtiene el valor de la propiedad estadoID.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEstadoID() {
        return estadoID;
    }

    /**
     * Define el valor de la propiedad estadoID.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEstadoID(BigInteger value) {
        this.estadoID = value;
    }

    /**
     * Obtiene el valor de la propiedad estadoMSG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoMSG() {
        return estadoMSG;
    }

    /**
     * Define el valor de la propiedad estadoMSG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoMSG(String value) {
        this.estadoMSG = value;
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
