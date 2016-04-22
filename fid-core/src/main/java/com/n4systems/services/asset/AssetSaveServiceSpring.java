package com.n4systems.services.asset;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.AssetAttachmentSaver;
import com.n4systems.model.asset.AssetImageFileSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.InfoOptionBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * This class is currently used by new web services (IOS+) to save assets.
 * 1. Sets asset imageName field.
 * 2. Saves asset.
 * 3. Saves asset attachments.
 * 4. Saves asset image.
 * If we are using this in web site or elsewhere, it may need few fixes. E.g.: mobileGuid needs to be set.
 * This class was created because mixing Spring and Legacy services caused MySQL locks.
 * This class also uses some legacy methods.
 * 
 */
@Transactional
public class AssetSaveServiceSpring extends FieldIdPersistenceService {
	
	private static Logger logger = Logger.getLogger(AssetSaveServiceSpring.class);

	@Autowired
	private AssetService assetService;

    public Asset createWithHistory(Asset asset, List<AssetAttachment> uploadedAttachments, byte[] imageData, String assetImageName) {
        Asset createdAsset = create(asset, uploadedAttachments, imageData, assetImageName);

        AddAssetHistory addAssetHistory = assetService.getAddAssetHistory();

        if (addAssetHistory == null) {
            addAssetHistory = new AddAssetHistory();
            addAssetHistory.setTenant(getCurrentTenant());
            addAssetHistory.setUser(getCurrentUser());
        }

        addAssetHistory.setOwner(asset.getOwner());
        addAssetHistory.setAssetType(asset.getType());
        addAssetHistory.setAssetStatus(asset.getAssetStatus());
        addAssetHistory.setPurchaseOrder(asset.getPurchaseOrder());
        addAssetHistory.setLocation(asset.getAdvancedLocation());
        addAssetHistory.setInfoOptions(reconcileInfoOptions(asset.getInfoOptions()));
        addAssetHistory.setAssignedUser(asset.getAssignedUser());

        persistenceService.saveOrUpdate(addAssetHistory);

        return createdAsset;
    }

    private List<InfoOptionBean> reconcileInfoOptions(Collection<InfoOptionBean> options) {
        //WEB-3944 -- We need to reattach selected static info options in order to avoid a detached entity exception
        List<InfoOptionBean> reconciledOptions = new ArrayList<>();

        for (InfoOptionBean option : options) {
            if (option.isStaticData()) {
                QueryBuilder<InfoOptionBean> query = new QueryBuilder<>(InfoOptionBean.class, new OpenSecurityFilter());
                query.addSimpleWhere("uniqueID", option.getUniqueID());
                InfoOptionBean foundOption = persistenceService.find(query);
                reconciledOptions.add(foundOption);
            } else {
                reconciledOptions.add(option);
            }
        }
        return reconciledOptions;

    }

	public Asset create(Asset asset, List<AssetAttachment> uploadedAttachments, byte[] imageData, String imageFileName) {
		try {
            if (asset.getType().isArchived()) {
                asset.archiveEntity();
            }
            
			asset = assetService.create(asset);//, getCurrentUser());
			saveUploadedAttachments(asset, uploadedAttachments);
			saveAssetImage(asset, imageData, imageFileName, true);
			return asset;
		} catch (SubAssetUniquenessException e) {
			throw new ProcessFailureException("could not save asset", e);
		}
	}

	public Asset update(Asset asset, List<AssetAttachment> assetAttachments, byte[] imageData, String imageFileName, boolean updateImage) {
		try {
			asset = assetService.updateWithSubassets(asset);//, getCurrentUser());
            if (assetAttachments != null) {
                // When attachments are null, we mean do not update attachments here. The mobile takes care of attachments
                // separately by calling ApiAttachmentResource
                reconcileAttachments(asset, assetAttachments);
            }
			saveAssetImage(asset, imageData, imageFileName, updateImage);
			return asset;
		} catch (SubAssetUniquenessException e) {
			throw new ProcessFailureException("could not save asset", e);
		}
	}

    private void reconcileAttachments(Asset asset, List<AssetAttachment> assetAttachments) {
        List<AssetAttachment> existingAttachments = assetService.findAssetAttachments(asset);
        for (AssetAttachment existingAttachment : existingAttachments) {
            if (!assetAttachments.contains(existingAttachment)) {
                File attachedFile = PathHandler.getAssetAttachmentFile(existingAttachment);

                if (attachedFile.exists()) {
                    boolean successfulDelete = attachedFile.delete();
                    if (!successfulDelete) {
                        logger.error("Couldn't delete asset attachment: " + attachedFile.getAbsolutePath());
                    }
                }

                persistenceService.delete(existingAttachment);
            }
        }

        List<AssetAttachment> newAttachments = new ArrayList<>();
        for (AssetAttachment assetAttachment : assetAttachments) {
            if (assetAttachment.isNew()) {
                newAttachments.add(assetAttachment);
            } else {
                persistenceService.update(assetAttachment);
            }
        }

        saveUploadedAttachments(asset, newAttachments);
    }

    private void saveUploadedAttachments(Asset asset, List<AssetAttachment> uploadedAttachments) {
		if (uploadedAttachments != null) {
			AssetAttachmentSaver saver = new AssetAttachmentSaver(getCurrentUser(), asset);
			for (AssetAttachment attachment : uploadedAttachments) {
				saver.save(getEntityManager(), attachment);
			}
		}
	}

	private void saveAssetImage(Asset asset, byte[] imageData, String imageFileName, boolean updateImage) {
        if (!updateImage) {
            return;
        }
		if (imageData != null) {
			logger.info("Saved Asset Image to Amazon S3 for " + asset.getIdentifier());
			asset.setImageName(imageFileName);
			AssetImageFileSaver assetImageFileSaver =  new AssetImageFileSaver(asset, imageFileName);
			assetImageFileSaver.setData(imageData);
			assetImageFileSaver.save();
		} else if(asset.getImageName() != null) { // imageData is null but asset has imageName. So we need to remove image.
			logger.info("Removed Asset Image from Amazon S3 for " + asset.getIdentifier());
			asset.setImageName(null);
			AssetImageFileSaver assetImageFileSaver =  new AssetImageFileSaver(asset, imageFileName);
			assetImageFileSaver.remove();
		}
	}

}
