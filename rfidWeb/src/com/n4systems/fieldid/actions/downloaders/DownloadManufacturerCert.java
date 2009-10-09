package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Product;
import com.n4systems.model.safetynetwork.ProductsByNetworkId;
import com.n4systems.reporting.CertificatePrinter;
import com.n4systems.reporting.ReportFactory;

public class DownloadManufacturerCert extends DownloadAction {

	private static Logger logger = Logger.getLogger(DownloadManufacturerCert.class);

	private static final long serialVersionUID = 1L;

	private ProductManager productManager;
	private Product product;
	private ReportFactory reportFactory;

	private long linkedProductId;

	public DownloadManufacturerCert(ProductManager productManager, ReportFactory reportFactory, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.reportFactory = reportFactory;
	}

	public String doDownloadLinked() {

		Product ownedProduct = productManager.findProduct(uniqueID, getSecurityFilter());

		if (ownedProduct == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		ProductsByNetworkId loader = new ProductsByNetworkId(getSecurityFilter());
		loader.setNetworkId(ownedProduct.getNetworkId());
		
		List<Product> linkedProducts = loader.load();
		
		for (Product linkedProduct : linkedProducts) {
			if (linkedProduct.getId().equals(linkedProductId)) {
				product = linkedProduct;
			}
		}

		return generateCertificate();
	}

	@Override
	public String doDownload() {
		product = productManager.findProductAllFields(uniqueID, getSecurityFilter());

		if (product == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		return generateCertificate();
	}

	private String generateCertificate() {
		JasperPrint p = null;
		byte[] pdf = null;
		InputStream input = null;
		boolean failure = false;

		try {
			p = reportFactory.generateProductCertificate(product, fetchCurrentUser());
			pdf = CertificatePrinter.printToPDF(p);
		} catch (NonPrintableEventType nonPrintable) {
			logger.error("failed to print cert", nonPrintable);
			return "cantprint";
		} catch (ReportException reportException) {
			logger.error("failed to print cert", reportException);
			return ERROR;
		}

		try {
			fileName = "certificate-" + product.getSerialNumber() + ".pdf";

			input = new ByteArrayInputStream(pdf);
			sendFile(input);

		} catch (IOException e) {
			logger.error("failed to print cert", e);
			failure = true;
		} catch (Exception e) {
			logger.error("failed to print cert", e);
			return ERROR;
		} finally {
			IOUtils.closeQuietly(input);
		}

		return (failure) ? ERROR : null;
	}

	public long getLinkedProductId() {
		return linkedProductId;
	}

	public void setLinkedProductId(long linkedProductId) {
		this.linkedProductId = linkedProductId;
	}
}
