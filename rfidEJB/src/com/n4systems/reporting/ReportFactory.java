package com.n4systems.reporting;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import net.sf.jasperreports.engine.JasperPrint;
import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.ConfigEntry;

@Local
public interface ReportFactory {
	
	
	/**
	 * Generates a ziped collection of pdf inspection certificate files.  PDF files will contain up to {@link ConfigEntry#REPORTING_MAX_REPORTS_PER_FILE} reports.
	 * The packaged report is generated the users home directory
	 * @param type				The type of Inspection certificate report to generate
	 * @param rInspectionDocs	A Collection of Inspection ids to generate reports for
	 * @param dateFormat		The date format to be used in the inspection report
	 * @param packageName		The name of the final zipped package (without extension).
	 * @param user				A User used for security filtering and generating the private report path for.  
	 * @return					The zipped report
	 * @throws Exception		on any issue generating inspection or packaging reports
	 */
	public File generateInspectionCertificateReport(InspectionReportType type, Collection<Long> rInspectionDocs, String packageName, UserBean user) throws ReportException, NonPrintableEventType;
	
	/**
	 * Generates a JasperPrint object for an Inspection report of type InspectionReportType.
	 * @param type			The type of report to generate
	 * @param inspection	The id of the inspection to generate the report for
	 * @param dateFormat	The date format to use in the report
	 * @return				A JasperPrint object for the generated report
	 * @throws NonPrintableEventType	When the inspection is set to not printable {@link Inspection#setPrintable(boolean)}
	 * @throws ReportException			On any problem generating the report.
	 */
	public JasperPrint generateInspectionCertificate(InspectionReportType type, Long inspectionId, UserBean user) throws NonPrintableEventType, ReportException;
	
	/**
	 * Generates a JasperPrint object for a Product report (Manufacture Cert)
	 * @param productId		The id of the Product to generate the report for
	 * @param dateFormat	The date format to use in the report
	 * @return				A JasperPrint object for the generated report 
	 * @throws ReportException	On any problem generating the report.
	 */
	public JasperPrint generateProductCertificate(Long productId, UserBean user) throws ReportException, NonPrintableManufacturerCert;
	
	/**
	 * Generates a ziped collection of pdf product certificate files.  PDF files will contain up to {@link ConfigEntry#REPORTING_MAX_REPORTS_PER_FILE} reports.
	 * @param productIds		A Collection of product ids to generate reports for
	 * @param dateFormat		The date format to be used in the inspection report
	 * @param packageName		The name of the final zipped package (without extension).
	 * @param user				A User used for security filtering and generating the private report path for.  
	 * @return					The zipped report
	 * @throws Exception		on any issue generating inspection or packaging reports
	 */
	public File generateProductCertificateReport( Collection<Long> productIds, String packageName, UserBean user) throws ReportException, EmptyReportException;
	
	
	
	/**
	 * generates the jasper print object of the pdf version of the inspection report screen.
	 * @throws ReportException	On any problem generating the report.
	 */
	public JasperPrint generateInspectionReport(ReportDefiner reportDefiner, SecurityFilter filter, UserBean user, Tenant tenant) throws ReportException;
	
	/**
	 * Renders the JasperPrint object to a pdf and writes it to an OutputStream
	 * @param	jasperPrint	The JasperPrint object to render
	 * @throws ReportException	on any problems generating the report
	 */
	public void printToPDF(JasperPrint jasperPrint, OutputStream outputStream) throws ReportException;
	
	/**
	 * Renders a List of JasperPrint objects to a pdf and writes them to an OutputStream
	 * @param	printList The List of JasperPrint objects
	 * @throws ReportException	on any problems generating the report
	 */
	public void printToPDF(List<JasperPrint> printList, OutputStream outputStream) throws ReportException;
	
	/**
	 * Renders the JasperPrint object to a pdf and returns it as a byte array
	 * @param	jasperPrint	The JasperPrint object to render
	 * @returns The pdf byte array
	 * @throws ReportException	on any problems generating the report
	 */
	public byte[] printToPDF(JasperPrint jasperPrint) throws ReportException;
	
	/**
	 * Renders a List of JasperPrint objects to a pdf and returns it as a byte array
	 * @param	printList The List of JasperPrint objects
	 * @returns The pdf byte array
	 * @throws ReportException	on any problems generating the report
	 */
	public byte[] printToPDF(List<JasperPrint> printList) throws ReportException;
}
