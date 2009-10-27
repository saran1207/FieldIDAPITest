package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.model.Product;
import com.n4systems.model.safetynetwork.ProductsByNetworkId;
import com.n4systems.reporting.CertificatePrinter;
import com.n4systems.reporting.ProductCertificateGenerator;

public class DownloadManufacturerCert extends DownloadAction {

	private static Logger logger = Logger.getLogger(DownloadManufacturerCert.class);

	private static final long serialVersionUID = 1L;

	private ProductManager productManager;
	private Product product;
	private ProductCertificateGenerator certGen;

	private long linkedProductId;

	public DownloadManufacturerCert(ProductManager productManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.certGen = new ProductCertificateGenerator();
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
		try {
			
			JasperPrint p = certGen.generate(product, fetchCurrentUser());
			byte[] pdf = CertificatePrinter.printToPDF(p);
			
			fileName = "certificate-" + product.getSerialNumber() + ".pdf";
			sendFile(new ByteArrayInputStream(pdf));
			
		} catch (NonPrintableEventType npe) {
			logger.debug("Cert was non-printable", npe);
			return "cantprint";
		} catch (Exception e) {
			logger.error("Unable to download inspection cert", e);
			return ERROR;
		}

		return null;
	}

	public long getLinkedProductId() {
		return linkedProductId;
	}

	public void setLinkedProductId(long linkedProductId) {
		this.linkedProductId = linkedProductId;
	}
}
