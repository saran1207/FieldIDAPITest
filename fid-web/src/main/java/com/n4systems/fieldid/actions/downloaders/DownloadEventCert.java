package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.service.certificate.CertificateService;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.Event;
import com.n4systems.reporting.EventReportType;


public class DownloadEventCert extends DownloadAction {
	private static Logger logger = Logger.getLogger( DownloadEventCert.class );
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private EventReportType reportType;
	
	@Autowired
	private CertificateService certificateService;
	
	public DownloadEventCert(com.n4systems.ejb.PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doDownload() {
		// if we're in a vendor context we need to look events for assigned assets rather than registered assets
		Event event = getLoaderFactory().createSafetyNetworkEventLoaderAssignedOrRegistered().setId(uniqueID).load();
		
		return printCert(event);
	}

	private String printCert(Event event) {
		if(event == null) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		}
		
		try {
			byte[] pdf = certificateService.generateEventCertificatePdf(reportType, event.getId());
			
			fileName = constructReportFileName(event);
			sendFile(new ByteArrayInputStream(pdf));
			
			return null;
		} catch(NonPrintableEventType npe) {
			logger.debug("Cert was non-printable", npe);
			return "cantprint";
		} catch(Exception e) {
			logger.error("Unable to download event cert", e);
			return ERROR;
		}
	}

	private String constructReportFileName(Event event) {
		return reportType.getReportNamePrefix() + "-" + dateFormatter.format(event.getDate()) + ".pdf";
	}

	public EventSearchContainer getCriteria() {
		return getSession().getReportCriteria();
	}
	
	public String getReportType() {
		return reportType.name();
	}

	public void setReportType(String reportType) {
		this.reportType = EventReportType.valueOf(reportType);
	}
	
}
