package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.safetynetwork.ProductsByNetworkIdLoader;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.reporting.CertificatePrinter;
import com.n4systems.reporting.ProductCertificateGenerator;

public class DownloadManufacturerCert extends DownloadAction {

	private static Logger logger = Logger.getLogger(DownloadManufacturerCert.class);

	private static final long serialVersionUID = 1L;

	private ProductManager productManager;
	private Asset asset;
	private ProductCertificateGenerator certGen;

	private long linkedProductId;

	public DownloadManufacturerCert(ProductManager productManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.certGen = new ProductCertificateGenerator();
	}

	public String doDownloadLinked() {

		Asset ownedProduct = productManager.findAsset(uniqueID, getSecurityFilter());

		if (ownedProduct == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		ProductsByNetworkIdLoader loader = new ProductsByNetworkIdLoader(getSecurityFilter());
		loader.setNetworkId(ownedProduct.getNetworkId());
		
		List<Asset> linkedAssets = loader.load();
		
		for (Asset linkedAsset : linkedAssets) {
			if (linkedAsset.getId().equals(linkedProductId)) {
				asset = linkedAsset;
			}
		}

		return generateCertificate();
	}

    public String doDownloadSafetyNetwork() {
        asset = getLoaderFactory().createSafetyNetworkProductLoader().setProductId(uniqueID).withAllFields().load();

		if (asset == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

        return generateCertificate();
    }

	@Override
	public String doDownload() {
		asset = productManager.findAssetAllFields(uniqueID, getSecurityFilter());

		if (asset == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		return generateCertificate();
	}

	private String generateCertificate() {
		try {
			
			JasperPrint p = certGen.generate(asset, fetchCurrentUser());
			byte[] pdf = new CertificatePrinter().printToPDF(p);
			
			fileName = "certificate-" + asset.getSerialNumber() + ".pdf";
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
