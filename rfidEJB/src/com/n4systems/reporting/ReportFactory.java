package com.n4systems.reporting;

import java.io.File;
import java.util.Collection;

import javax.ejb.Local;

import net.sf.jasperreports.engine.JasperPrint;
import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Product;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.ConfigEntry;

@Local
public interface ReportFactory {
	
	/**
	 * Generates a JasperPrint object for a Product report (Manufacture Cert)
	 * @param product		The Product to generate the report for
	 * @param dateFormat	The date format to use in the report
	 * @return				A JasperPrint object for the generated report 
	 * @throws ReportException	On any problem generating the report.
	 */
	public JasperPrint generateProductCertificate(Product product, UserBean user) throws ReportException, NonPrintableManufacturerCert;
	
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
	
}
