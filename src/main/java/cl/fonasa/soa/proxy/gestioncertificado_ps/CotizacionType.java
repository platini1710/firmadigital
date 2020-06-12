//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.04.04 a las 07:15:35 PM CLST 
//


package cl.fonasa.soa.proxy.gestioncertificado_ps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para cotizacionType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="cotizacionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="detalleCotizacion" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="RUN_COTIZANTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NOMBRE_COTIZANTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RUT_EMPLEADOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RAZON_SOCIAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PERIODO_REMUNERACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_DECLARACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_REGIMEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MONTO_RENTA_IMPONIBLE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MONTO_COTIZACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DIAS_TRABAJADOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_REMUNERACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AFILIADO_CCAF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FECHA_PAGO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "cotizacionType", propOrder = {
    "detalleCotizacion"
})
public class CotizacionType {

    protected List<CotizacionType.DetalleCotizacion> detalleCotizacion;

    /**
     * Gets the value of the detalleCotizacion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detalleCotizacion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetalleCotizacion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CotizacionType.DetalleCotizacion }
     * 
     * 
     */
    public List<CotizacionType.DetalleCotizacion> getDetalleCotizacion() {
        if (detalleCotizacion == null) {
            detalleCotizacion = new ArrayList<CotizacionType.DetalleCotizacion>();
        }
        return this.detalleCotizacion;
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
     *         &lt;element name="RUN_COTIZANTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NOMBRE_COTIZANTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RUT_EMPLEADOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RAZON_SOCIAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PERIODO_REMUNERACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_DECLARACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_REGIMEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MONTO_RENTA_IMPONIBLE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MONTO_COTIZACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DIAS_TRABAJADOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_REMUNERACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AFILIADO_CCAF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FECHA_PAGO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "runcotizante",
        "nombrecotizante",
        "rutempleador",
        "razonsocial",
        "periodoremuneracion",
        "tipodeclaracion",
        "tiporegimen",
        "montorentaimponible",
        "montocotizacion",
        "diastrabajados",
        "tiporemuneracion",
        "afiliadoccaf",
        "fechapago"
    })
    public static class DetalleCotizacion {

        @XmlElementRef(name = "RUN_COTIZANTE", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> runcotizante;
        @XmlElementRef(name = "NOMBRE_COTIZANTE", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> nombrecotizante;
        @XmlElementRef(name = "RUT_EMPLEADOR", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> rutempleador;
        @XmlElementRef(name = "RAZON_SOCIAL", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> razonsocial;
        @XmlElementRef(name = "PERIODO_REMUNERACION", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> periodoremuneracion;
        @XmlElementRef(name = "TIPO_DECLARACION", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> tipodeclaracion;
        @XmlElementRef(name = "TIPO_REGIMEN", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> tiporegimen;
        @XmlElementRef(name = "MONTO_RENTA_IMPONIBLE", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> montorentaimponible;
        @XmlElementRef(name = "MONTO_COTIZACION", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> montocotizacion;
        @XmlElementRef(name = "DIAS_TRABAJADOS", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> diastrabajados;
        @XmlElementRef(name = "TIPO_REMUNERACION", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> tiporemuneracion;
        @XmlElementRef(name = "AFILIADO_CCAF", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> afiliadoccaf;
        @XmlElementRef(name = "FECHA_PAGO", namespace = "http://www.fonasa.cl/obtenerCartolaCotizacionesNombreComp", type = JAXBElement.class, required = false)
        protected JAXBElement<String> fechapago;

        /**
         * Obtiene el valor de la propiedad runcotizante.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getRUNCOTIZANTE() {
            return runcotizante;
        }

        /**
         * Define el valor de la propiedad runcotizante.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setRUNCOTIZANTE(JAXBElement<String> value) {
            this.runcotizante = value;
        }

        /**
         * Obtiene el valor de la propiedad nombrecotizante.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getNOMBRECOTIZANTE() {
            return nombrecotizante;
        }

        /**
         * Define el valor de la propiedad nombrecotizante.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setNOMBRECOTIZANTE(JAXBElement<String> value) {
            this.nombrecotizante = value;
        }

        /**
         * Obtiene el valor de la propiedad rutempleador.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getRUTEMPLEADOR() {
            return rutempleador;
        }

        /**
         * Define el valor de la propiedad rutempleador.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setRUTEMPLEADOR(JAXBElement<String> value) {
            this.rutempleador = value;
        }

        /**
         * Obtiene el valor de la propiedad razonsocial.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getRAZONSOCIAL() {
            return razonsocial;
        }

        /**
         * Define el valor de la propiedad razonsocial.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setRAZONSOCIAL(JAXBElement<String> value) {
            this.razonsocial = value;
        }

        /**
         * Obtiene el valor de la propiedad periodoremuneracion.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getPERIODOREMUNERACION() {
            return periodoremuneracion;
        }

        /**
         * Define el valor de la propiedad periodoremuneracion.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setPERIODOREMUNERACION(JAXBElement<String> value) {
            this.periodoremuneracion = value;
        }

        /**
         * Obtiene el valor de la propiedad tipodeclaracion.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getTIPODECLARACION() {
            return tipodeclaracion;
        }

        /**
         * Define el valor de la propiedad tipodeclaracion.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setTIPODECLARACION(JAXBElement<String> value) {
            this.tipodeclaracion = value;
        }

        /**
         * Obtiene el valor de la propiedad tiporegimen.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getTIPOREGIMEN() {
            return tiporegimen;
        }

        /**
         * Define el valor de la propiedad tiporegimen.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setTIPOREGIMEN(JAXBElement<String> value) {
            this.tiporegimen = value;
        }

        /**
         * Obtiene el valor de la propiedad montorentaimponible.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getMONTORENTAIMPONIBLE() {
            return montorentaimponible;
        }

        /**
         * Define el valor de la propiedad montorentaimponible.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setMONTORENTAIMPONIBLE(JAXBElement<String> value) {
            this.montorentaimponible = value;
        }

        /**
         * Obtiene el valor de la propiedad montocotizacion.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getMONTOCOTIZACION() {
            return montocotizacion;
        }

        /**
         * Define el valor de la propiedad montocotizacion.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setMONTOCOTIZACION(JAXBElement<String> value) {
            this.montocotizacion = value;
        }

        /**
         * Obtiene el valor de la propiedad diastrabajados.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getDIASTRABAJADOS() {
            return diastrabajados;
        }

        /**
         * Define el valor de la propiedad diastrabajados.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setDIASTRABAJADOS(JAXBElement<String> value) {
            this.diastrabajados = value;
        }

        /**
         * Obtiene el valor de la propiedad tiporemuneracion.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getTIPOREMUNERACION() {
            return tiporemuneracion;
        }

        /**
         * Define el valor de la propiedad tiporemuneracion.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setTIPOREMUNERACION(JAXBElement<String> value) {
            this.tiporemuneracion = value;
        }

        /**
         * Obtiene el valor de la propiedad afiliadoccaf.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getAFILIADOCCAF() {
            return afiliadoccaf;
        }

        /**
         * Define el valor de la propiedad afiliadoccaf.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setAFILIADOCCAF(JAXBElement<String> value) {
            this.afiliadoccaf = value;
        }

        /**
         * Obtiene el valor de la propiedad fechapago.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public JAXBElement<String> getFECHAPAGO() {
            return fechapago;
        }

        /**
         * Define el valor de la propiedad fechapago.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link String }{@code >}
         *     
         */
        public void setFECHAPAGO(JAXBElement<String> value) {
            this.fechapago = value;
        }

    }

}
