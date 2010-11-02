package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

import com.n4systems.model.Event;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.CertificatePrinter;
import com.n4systems.reporting.InspectionCertificateGenerator;
import com.n4systems.reporting.InspectionReportType;


public class DownloadInspectionCert extends DownloadAction {
	private static Logger logger = Logger.getLogger( DownloadInspectionCert.class );
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private InspectionReportType reportType;
	
	public DownloadInspectionCert(com.n4systems.ejb.PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	public String doDownload() {
		
		// if we're in a vendor context we need to look inspections for assigned products rather than registered products
		Event event = getLoaderFactory().createSafetyNetworkInspectionLoaderAssignedOrRegistered().setId(uniqueID).load();
		
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
			
			InspectionCertificateGenerator certGen = new InspectionCertificateGenerator(new DateTimeDefiner(getUser()));
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
			logger.error("Unable to download inspection cert", e);
			PersistenceManager.rollbackTransaction(transaction);
		}
		
		return (failure) ? ERROR : null;
	}

	private String constructReportFileName(Event event) {
		return reportType.getReportNamePrefix() + "-" + dateFormatter.format(event.getDate()) + ".pdf";
	}

	public InspectionSearchContainer getCriteria() {
		return getSession().getReportCriteria();
	}
	
	public String getReportType() {
		return reportType.name();
	}

	public void setReportType(String reportType) {
		this.reportType = InspectionReportType.valueOf(reportType);
	}
	
}
