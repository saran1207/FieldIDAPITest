
package com.plexus_online.inventory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="Job" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ManufacturingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TrackingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="JobNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PONo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LineNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CustomerCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CustomerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CustomerAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PartNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PartDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PartType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PartGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ProductType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CustomerPartNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CustomerPartDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="OrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ReleaseNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PODate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "job"
})
@XmlRootElement(name = "JobDataForTrackingNoResponse")
public class JobDataForTrackingNoResponse {

    @XmlElement(name = "Job")
    protected JobDataForTrackingNoResponse.Job job;

    /**
     * Gets the value of the job property.
     * 
     * @return
     *     possible object is
     *     {@link JobDataForTrackingNoResponse.Job }
     *     
     */
    public JobDataForTrackingNoResponse.Job getJob() {
        return job;
    }

    /**
     * Sets the value of the job property.
     * 
     * @param value
     *     allowed object is
     *     {@link JobDataForTrackingNoResponse.Job }
     *     
     */
    public void setJob(JobDataForTrackingNoResponse.Job value) {
        this.job = value;
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
     *         &lt;element name="ManufacturingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TrackingNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="JobNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PONo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LineNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CustomerCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CustomerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CustomerAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PartNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PartDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PartType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PartGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ProductType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CustomerPartNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CustomerPartDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="OrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ReleaseNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PODate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "manufacturingNo",
        "trackingNo",
        "jobNo",
        "poNo",
        "lineNo",
        "customerCode",
        "customerName",
        "customerAddress",
        "partNo",
        "partDescription",
        "partType",
        "partGroup",
        "productType",
        "customerPartNo",
        "customerPartDescription",
        "orderNo",
        "releaseNo",
        "poDate"
    })
    public static class Job {

        @XmlElement(name = "ManufacturingNo")
        protected String manufacturingNo;
        @XmlElement(name = "TrackingNo")
        protected String trackingNo;
        @XmlElement(name = "JobNo")
        protected String jobNo;
        @XmlElement(name = "PONo")
        protected String poNo;
        @XmlElement(name = "LineNo")
        protected String lineNo;
        @XmlElement(name = "CustomerCode")
        protected String customerCode;
        @XmlElement(name = "CustomerName")
        protected String customerName;
        @XmlElement(name = "CustomerAddress")
        protected String customerAddress;
        @XmlElement(name = "PartNo")
        protected String partNo;
        @XmlElement(name = "PartDescription")
        protected String partDescription;
        @XmlElement(name = "PartType")
        protected String partType;
        @XmlElement(name = "PartGroup")
        protected String partGroup;
        @XmlElement(name = "ProductType")
        protected String productType;
        @XmlElement(name = "CustomerPartNo")
        protected String customerPartNo;
        @XmlElement(name = "CustomerPartDescription")
        protected String customerPartDescription;
        @XmlElement(name = "OrderNo")
        protected String orderNo;
        @XmlElement(name = "ReleaseNo")
        protected String releaseNo;
        @XmlElement(name = "PODate")
        protected String poDate;

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
         * Gets the value of the jobNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getJobNo() {
            return jobNo;
        }

        /**
         * Sets the value of the jobNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setJobNo(String value) {
            this.jobNo = value;
        }

        /**
         * Gets the value of the poNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPONo() {
            return poNo;
        }

        /**
         * Sets the value of the poNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPONo(String value) {
            this.poNo = value;
        }

        /**
         * Gets the value of the lineNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLineNo() {
            return lineNo;
        }

        /**
         * Sets the value of the lineNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLineNo(String value) {
            this.lineNo = value;
        }

        /**
         * Gets the value of the customerCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCustomerCode() {
            return customerCode;
        }

        /**
         * Sets the value of the customerCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCustomerCode(String value) {
            this.customerCode = value;
        }

        /**
         * Gets the value of the customerName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCustomerName() {
            return customerName;
        }

        /**
         * Sets the value of the customerName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCustomerName(String value) {
            this.customerName = value;
        }

        /**
         * Gets the value of the customerAddress property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCustomerAddress() {
            return customerAddress;
        }

        /**
         * Sets the value of the customerAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCustomerAddress(String value) {
            this.customerAddress = value;
        }

        /**
         * Gets the value of the partNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartNo() {
            return partNo;
        }

        /**
         * Sets the value of the partNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartNo(String value) {
            this.partNo = value;
        }

        /**
         * Gets the value of the partDescription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartDescription() {
            return partDescription;
        }

        /**
         * Sets the value of the partDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartDescription(String value) {
            this.partDescription = value;
        }

        /**
         * Gets the value of the partType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartType() {
            return partType;
        }

        /**
         * Sets the value of the partType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartType(String value) {
            this.partType = value;
        }

        /**
         * Gets the value of the partGroup property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartGroup() {
            return partGroup;
        }

        /**
         * Sets the value of the partGroup property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartGroup(String value) {
            this.partGroup = value;
        }

        /**
         * Gets the value of the productType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProductType() {
            return productType;
        }

        /**
         * Sets the value of the productType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProductType(String value) {
            this.productType = value;
        }

        /**
         * Gets the value of the customerPartNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCustomerPartNo() {
            return customerPartNo;
        }

        /**
         * Sets the value of the customerPartNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCustomerPartNo(String value) {
            this.customerPartNo = value;
        }

        /**
         * Gets the value of the customerPartDescription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCustomerPartDescription() {
            return customerPartDescription;
        }

        /**
         * Sets the value of the customerPartDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCustomerPartDescription(String value) {
            this.customerPartDescription = value;
        }

        /**
         * Gets the value of the orderNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOrderNo() {
            return orderNo;
        }

        /**
         * Sets the value of the orderNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOrderNo(String value) {
            this.orderNo = value;
        }

        /**
         * Gets the value of the releaseNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReleaseNo() {
            return releaseNo;
        }

        /**
         * Sets the value of the releaseNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReleaseNo(String value) {
            this.releaseNo = value;
        }

        /**
         * Gets the value of the poDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPODate() {
            return poDate;
        }

        /**
         * Sets the value of the poDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPODate(String value) {
            this.poDate = value;
        }

    }

}
