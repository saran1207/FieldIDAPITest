package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

import com.n4systems.model.Event;
import com.n4systems.reporting.EventCertificateGenerator;
import com.n4systems.reporting.EventReportType;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.CertificatePrinter;


public class DownloadEventCert extends DownloadAction {
	private static Logger logger = Logger.getLogger( DownloadEventCert.class );
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private EventReportType reportType;
	
	public DownloadEventCert(com.n4systems.ejb.PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	public String doDownload() {
		
		// if we're in a vendor context we need to look events for assigned products rather than registered products
		Event event = getLoaderFactory().createSafetyNetworkEventLoaderAssignedOrRegistered().setId(uniqueID).load();
		
		return printCert(event);
	}

	private String printCert(Event event) {
		if(event == null) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		} 

		boolean failure = true;
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
			
			EventCertificateGenerator certGen = new EventCertificateGenerator(new DateTimeDefiner(getUser()));
			JasperPrint p = certGen.generate(reportType, event, transaction);
			
			byte[] pdf = new CertificatePrinter().printToPDF(p);
			
			fileName = constructReportFileName(event);

			sendFile(new ByteArrayInputStream(pdf));
			failure = false;
			
			PersistenceManager.finishTransaction(transaction);
		} catch(NonPrintableEventType npe) {
			logger.debug("Cert was non-printable", npe);
			PersistenceManager.rollbackTransaction(transaction);
			return "cantprint";
		} catch(Exception e) {
			logger.error("Unable to download event cert", e);
			PersistenceManager.rollbackTransaction(transaction);
		}
		
		return (failure) ? ERROR : null;
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
