package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.Inspection;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.reporting.ReportFactory;


public class DownloadInspectionCert extends DownloadAction {
	private static Logger logger = Logger.getLogger( DownloadInspectionCert.class );
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private InspectionReportType reportType;
	private ReportFactory reportFactory;
	
	public DownloadInspectionCert(ReportFactory reportFactory, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.reportFactory = reportFactory;
	}

	@Override
	public String doDownload() {
		Inspection inspection = getLoaderFactory().createSafetyNetworkInspectionLoader().setId(uniqueID).fetchAllFields().load();
		
		return printCert(inspection);
	}

	private String printCert(Inspection inspection) {
		if(inspection == null) {
			addActionError( getText( "error.noinspection" ) );
			return MISSING;
		} 

		boolean failure = true;
		try {
			JasperPrint p = reportFactory.generateInspectionCertificate(reportType, uniqueID, fetchCurrentUser());
			byte[] pdf = reportFactory.printToPDF(p);
			
			fileName = constructReportFileName(inspection);

			sendFile(new ByteArrayInputStream(pdf));
			failure = false;
			
		} catch(NonPrintableEventType npe) {
			logger.debug("Cert was non-printable", npe);
			return "cantprint";
		} catch(Exception e) {
			logger.error("Unable to download inspection cert", e);
		}
		
		return (failure) ? ERROR : null;
	}

	private String constructReportFileName(Inspection inspection) {
		return reportType.getReportNamePrefix() + "-" + dateFormatter.format(inspection.getDate()) + ".pdf";
	}

	public InspectionSearchContainer getCriteria() {
		InspectionSearchContainer criteria = null;
		
		if(getSession().containsKey(InspectionReportAction.REPORT_CRITERIA) && getSession().get(InspectionReportAction.REPORT_CRITERIA) != null) {
			criteria = (InspectionSearchContainer)getSession().get(InspectionReportAction.REPORT_CRITERIA);
		} 
		
		return criteria;
	}
	
	public String getReportType() {
		return reportType.name();
	}

	public void setReportType(String reportType) {
		this.reportType = InspectionReportType.valueOf(reportType);
	}
	
}
