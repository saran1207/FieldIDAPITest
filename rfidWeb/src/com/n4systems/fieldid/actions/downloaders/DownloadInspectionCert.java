package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.SafetyNetworkManager;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.reporting.ReportFactory;


public class DownloadInspectionCert extends DownloadAction {
	private static Logger logger = Logger.getLogger( DownloadInspectionCert.class );
	private static final long serialVersionUID = 1L;
	
	private Long productId;
	private InspectionReportType reportType;

	private ProductManager productManager;
	private InspectionManager inspectionManager;
	private SafetyNetworkManager safetyNetworkManager;
	private Inspection inspection;
	private Product product;
	private ReportFactory reportFactory;
	
	
	public DownloadInspectionCert( InspectionManager inspectionManager, ReportFactory reportFactory, 
			SafetyNetworkManager safetyNetworkManager, ProductManager productManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.reportFactory = reportFactory;
		this.safetyNetworkManager = safetyNetworkManager;
		this.productManager = productManager;
	}

	@Override
	public String doDownload() {
		inspection =  inspectionManager.findAllFields( uniqueID, getSecurityFilter() );
		
		return printCert();
	}
	
	public String doDownloadLinkedInspection() {
		product = productManager.findProduct( productId, getSecurityFilter() );
		
		if( product == null ) {
			addActionError( getText( "error.noproduct" ) );
			return MISSING;
		}
		
		try {
			inspection =  safetyNetworkManager.findLinkedProductInspection( product, uniqueID );
		} catch(Exception e) {
			logger.error("Failed finding linked inspection", e);
		}
		
		return printCert();
	}

	private String printCert() {
		if( inspection == null ) {
			addActionError( getText( "error.noinspection" ) );
			return MISSING;
		} 
		
		JasperPrint p = null;
		byte[] pdf = null;
		InputStream input = null;
		boolean failure = false;
		
		try {
			
			 
			p = reportFactory.generateInspectionCertificate(reportType, uniqueID, fetchCurrentUser());
			pdf = reportFactory.printToPDF( p );
			
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			fileName = reportType.getReportNamePrefix() + "-" + sdf.format( inspection.getDate() ) + ".pdf";
			
			input = new ByteArrayInputStream( pdf );
			sendFile( input );
			
		} catch( NonPrintableEventType nonPrintable ) {
			logger.error( "failed to print cert", nonPrintable );
			return "cantprint";
		} catch( ReportException reportException ) {
			logger.error( "failed to print cert", reportException );
			return ERROR;
		} catch( IOException e ) {
			logger.error( "failed to print cert", e );
			failure = true;
		} catch( Exception e ) {
			logger.error( "failed to print cert", e );
			return ERROR;
		} finally {
			IOUtils.closeQuietly( input );
		}
		
		return ( failure ) ? ERROR : null;
	}

	public InspectionSearchContainer getCriteria() {
		InspectionSearchContainer criteria = null;
		
		if(getSession().containsKey(InspectionReportAction.REPORT_CRITERIA) && getSession().get(InspectionReportAction.REPORT_CRITERIA) != null) {
			criteria = (InspectionSearchContainer)getSession().get(InspectionReportAction.REPORT_CRITERIA);
		} 
		
		return criteria;
	}
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId( Long productId ) {
		this.productId = productId;
	}
	
	public String getReportType() {
		return reportType.name();
	}

	public void setReportType(String reportType) {
		this.reportType = InspectionReportType.valueOf(reportType);
	}
	
}
