package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.certificate.CertificateService;
import com.n4systems.model.Asset;
import com.n4systems.model.safetynetwork.AssetsByNetworkIdLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;

public class DownloadManufacturerCert extends DownloadAction {

	private static Logger logger = Logger.getLogger(DownloadManufacturerCert.class);

	private static final long serialVersionUID = 1L;

	private AssetManager assetManager;
	private Asset asset;
	
	@Autowired
	private CertificateService certificateGenerator;

	private long linkedAssetId;

	public DownloadManufacturerCert(AssetManager assetManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.assetManager = assetManager;
	}

	public String doDownloadLinked() {

		Asset ownedAsset = assetManager.findAsset(uniqueID, getSecurityFilter());

		if (ownedAsset == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

		AssetsByNetworkIdLoader loader = new AssetsByNetworkIdLoader(getSecurityFilter());
		loader.setNetworkId(ownedAsset.getNetworkId());
		
		List<Asset> linkedAssets = loader.load();
		
		for (Asset linkedAsset : linkedAssets) {
			if (linkedAsset.getId().equals(linkedAssetId)) {
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

	public String doDownload() {
		asset = assetManager.findAssetAllFields(uniqueID, getSecurityFilter());

		if (asset == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

		return generateCertificate();
	}

	private String generateCertificate() {
        Locale previousLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
		try {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(getCurrentUser().getLanguage());
			byte[] pdf = certificateGenerator.generateAssetCertificatePdf(asset);
			
			fileName = "certificate-" + asset.getIdentifier() + ".pdf";
			sendFile(new ByteArrayInputStream(pdf));
			
		} catch (NonPrintableEventType npe) {
			logger.debug("Cert was non-printable", npe);
			return "cantprint";
		} catch (Exception e) {
			logger.error("Unable to download event cert", e);
			return ERROR;
		} finally {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(previousLanguage);
        }

		return null;
	}

	public long getLinkedAssetId() {
		return linkedAssetId;
	}

	public void setLinkedAssetId(long linkedAssetId) {
		this.linkedAssetId = linkedAssetId;
	}
}
