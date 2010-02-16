package com.n4systems.reporting;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Inspection;
import com.n4systems.model.utils.DateTimeDefiner;

public class InspectionCertificateReportGenerator extends CertificateReportGenerator<Inspection> {
	private Logger logger = Logger.getLogger(InspectionCertificateReportGenerator.class);
	
	private final InspectionCertificateGenerator certGenerator;
	
	private InspectionReportType type;
	public InspectionCertificateReportGenerator(InspectionCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public InspectionCertificateReportGenerator(DateTimeDefiner dateDefiner) {
		this(new InspectionCertificateGenerator(dateDefiner));
	}
	
	
	
	public void setType(InspectionReportType type) {
		this.type = type;
	}

	
	@Override
	protected void guard(){
		if (type == null) {
			throw new InvalidArgumentException("you must set up an inspeciton type");
		}
	}

	

	@Override
	protected JasperPrint singleCert(Inspection inspection)	throws ReportException {
		return certGenerator.generate(type, inspection, transaction);
	}

	@Override
	protected void logCertError(Inspection inspection, Exception e) {
		logger.warn("Failed to generate report for Inspection [" + inspection.getId() + "].  Moving on to next Inspection.", e);
	}
	
	@Override
	protected  boolean isPrintable(Inspection certObject) {
		return certObject.isPrintableForReportType(type);

	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	

	
}
