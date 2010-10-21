package com.n4systems.reporting;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;


import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;

public class ProductCertificateReportGenerator extends CertificateReportGenerator<Asset> {
	private Logger logger = Logger.getLogger(ProductCertificateReportGenerator.class);
	
	private final ProductCertificateGenerator certGenerator;

	private User user;
	
	public ProductCertificateReportGenerator(ProductCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public ProductCertificateReportGenerator() {
		this(new ProductCertificateGenerator());
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	protected void guard() {
		if (user == null) {
			throw new InvalidArgumentException("the user can not be null");
		}
		
	}


	@Override
	protected JasperPrint singleCert(Asset asset) throws ReportException {
		return certGenerator.generate(asset, user);
	}

	@Override
	protected void logCertError(Asset asset, Exception e) {
		logger.warn("Failed to manufacturer certificate for Asset [" + asset.getId() + "].  Moving on to next Asset.", e);
	}


	@Override
	protected boolean isPrintable(Asset certObject) {
		return certObject.getType().isHasManufactureCertificate();
	}
	

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
