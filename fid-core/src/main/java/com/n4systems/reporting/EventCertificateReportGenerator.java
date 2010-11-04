package com.n4systems.reporting;

import com.n4systems.model.Event;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.utils.DateTimeDefiner;

public class EventCertificateReportGenerator extends CertificateReportGenerator<Event> {
	private Logger logger = Logger.getLogger(EventCertificateReportGenerator.class);
	
	private final EventCertificateGenerator certGenerator;
	
	private EventReportType type;
	public EventCertificateReportGenerator(EventCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public EventCertificateReportGenerator(DateTimeDefiner dateDefiner) {
		this(new EventCertificateGenerator(dateDefiner));
	}
	
	
	
	public void setType(EventReportType type) {
		this.type = type;
	}

	
	@Override
	protected void guard(){
		if (type == null) {
			throw new InvalidArgumentException("you must set up an inspeciton type");
		}
	}

	

	@Override
	protected JasperPrint singleCert(Event event)	throws ReportException {
		return certGenerator.generate(type, event, transaction);
	}

	@Override
	protected void logCertError(Event event, Exception e) {
		logger.warn("Failed to generate report for Event [" + event.getId() + "].  Moving on to next Event.", e);
	}
	
	@Override
	protected  boolean isPrintable(Event certObject) {
		return certObject.isPrintableForReportType(type);

	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	

	
}
