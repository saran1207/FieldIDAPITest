
package com.plexus_online.inventory;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Container" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SerialNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TrackingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ManufacturingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "container"
})
@XmlRootElement(name = "JobDataForTrackingNo")
public class JobDataForTrackingNo {

    @XmlElement(name = "Container")
    protected JobDataForTrackingNo.Container container;

    /**
     * Gets the value of the container property.
     * 
     * @return
     *     possible object is
     *     {@link JobDataForTrackingNo.Container }
     *     
     */
    public JobDataForTrackingNo.Container getContainer() {
        return container;
    }

    /**
     * Sets the value of the container property.
     * 
     * @param value
     *     allowed object is
     *     {@link JobDataForTrackingNo.Container }
     *     
     */
    public void setContainer(JobDataForTrackingNo.Container value) {
        this.container = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="SerialNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TrackingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ManufacturingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "serialNo",
        "trackingNo",
        "manufacturingNo"
    })
    public static class Container {

        @XmlElement(name = "SerialNo")
        protected String serialNo;
        @XmlElement(name = "TrackingNo")
        protected String trackingNo;
        @XmlElement(name = "ManufacturingNo")
        protected String manufacturingNo;

        /**
         * Gets the value of the serialNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSerialNo() {
            return serialNo;
        }

        /**
         * Sets the value of the serialNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSerialNo(String value) {
            this.serialNo = value;
        }

        /**
         * Gets the value of the trackingNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTrackingNo() {
            return trackingNo;
        }

        /**
         * Sets the value of the trackingNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTrackingNo(String value) {
            this.trackingNo = value;
        }

        /**
         * Gets the value of the manufacturingNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getManufacturingNo() {
            return manufacturingNo;
        }

        /**
         * Sets the value of the manufacturingNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setManufacturingNo(String value) {
            this.manufacturingNo = value;
        }

    }

}
