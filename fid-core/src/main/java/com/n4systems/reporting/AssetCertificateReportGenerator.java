package com.n4systems.reporting;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;


import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;

public class AssetCertificateReportGenerator extends CertificateReportGenerator<Asset> {
	private Logger logger = Logger.getLogger(AssetCertificateReportGenerator.class);
	
	private final AssetCertificateGenerator certGenerator;

	private User user;
	
	public AssetCertificateReportGenerator(AssetCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public AssetCertificateReportGenerator() {
		this(new AssetCertificateGenerator());
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
