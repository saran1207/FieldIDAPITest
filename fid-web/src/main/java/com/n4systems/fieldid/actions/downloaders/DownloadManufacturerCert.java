package com.n4systems.fieldid.actions.downloaders;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.model.Asset;
import com.n4systems.model.safetynetwork.AssetsByNetworkIdLoader;
import com.n4systems.reporting.AssetCertificateGenerator;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.reporting.CertificatePrinter;

public class DownloadManufacturerCert extends DownloadAction {

	private static Logger logger = Logger.getLogger(DownloadManufacturerCert.class);

	private static final long serialVersionUID = 1L;

	private AssetManager assetManager;
	private Asset asset;
	private AssetCertificateGenerator certGen;

	private long linkedProductId;

	public DownloadManufacturerCert(AssetManager assetManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.assetManager = assetManager;
		this.certGen = new AssetCertificateGenerator();
	}

	public String doDownloadLinked() {

		Asset ownedProduct = assetManager.findAsset(uniqueID, getSecurityFilter());

		if (ownedProduct == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

		AssetsByNetworkIdLoader loader = new AssetsByNetworkIdLoader(getSecurityFilter());
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
        asset = getLoaderFactory().createSafetyNetworkAssetLoader().setAssetId(uniqueID).withAllFields().load();

		if (asset == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

        return generateCertificate();
    }

	@Override
	public String doDownload() {
		asset = assetManager.findAssetAllFields(uniqueID, getSecurityFilter());

		if (asset == null) {
			addActionError(getText("error.noasset"));
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
			logger.error("Unable to download event cert", e);
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
