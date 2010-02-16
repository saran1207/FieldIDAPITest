package com.n4systems.reporting;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Product;

public class ProductCertificateReportGenerator extends CertificateReportGenerator<Product> {
	private Logger logger = Logger.getLogger(ProductCertificateReportGenerator.class);
	
	private final ProductCertificateGenerator certGenerator;

	private UserBean user;
	
	public ProductCertificateReportGenerator(ProductCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public ProductCertificateReportGenerator() {
		this(new ProductCertificateGenerator());
	}
	
	public void setUser(UserBean user) {
		this.user = user;
	}
	
	@Override
	protected void guard() {
		if (user == null) {
			throw new InvalidArgumentException("the user can not be null");
		}
		
	}


	@Override
	protected JasperPrint singleCert(Product product) throws ReportException {
		return certGenerator.generate(product, user);
	}

	@Override
	protected void logCertError(Product product, Exception e) {
		logger.warn("Failed to manufacturer certificate for Product [" + product.getId() + "].  Moving on to next Product.", e);
	}


	@Override
	protected boolean isPrintable(Product certObject) {
		return certObject.getType().isHasManufactureCertificate();
	}
	

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
