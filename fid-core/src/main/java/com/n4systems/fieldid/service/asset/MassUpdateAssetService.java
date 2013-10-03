package com.n4systems.fieldid.service.asset;

import com.google.common.collect.Sets;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;
import com.n4systems.util.mail.MailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MassUpdateAssetService extends FieldIdPersistenceService{

    @Autowired
    private MailService mailService;

    @Autowired
    private AssetService assetService;

    private Logger logger = Logger.getLogger(MassUpdateAssetService.class);

    private static final String COMMENTS = "comments";
    private static final String NON_INTEGRATION_ORDER_NUMBER = "nonIntegrationOrderNumber";
    private static final String PUBLISHED = "published";
    private static final String IDENTIFIED = "identified";
    private static final String PURCHASE_ORDER = "purchaseOrder";
    private static final String ASSET_STATUS = "assetStatus";
    private static final String ASSIGNED_USER = "assignedUser";
    private static final String LOCATION = "location";
    private static final String OWNER = "owner";

    public Long updateAssets(List<Long> ids, Asset assetModificationData, Map<String, Boolean> values, User modifiedBy, String orderNumber) throws UpdateFailureException, UpdateConatraintViolationException {
        Long result = 0L;
        Set<Asset> assetsUpdated = Sets.newHashSet();
        try {
            for (Long id : ids) {
                Asset asset = assetService.getAsset(id, Asset.POST_FETCH_ALL_PATHS);
                asset = assetService.fillInSubAssetsOnAsset(asset);
                updateAsset(asset, assetModificationData, values, orderNumber);
                assetService.update(asset, modifiedBy);
                assetsUpdated.add(asset);
                result++;
            }
        } catch (SubAssetUniquenessException e) {
            throw new UpdateFailureException(e);
        } catch (EntityExistsException cve) {
            throw new UpdateConatraintViolationException(cve);
        }
        return result;
    }

    private void updateAsset(Asset asset, Asset assetModificationData, Map<String, Boolean> values, String orderNumber) {
        for (Map.Entry<String, Boolean> entry : values.entrySet()) {
            if (entry.getValue() == true) {
                if (entry.getKey().equals(OWNER)) {
                    asset.setOwner(assetModificationData.getOwner());
                }
                if (entry.getKey().equals(LOCATION)) {
                    asset.setAdvancedLocation(assetModificationData.getAdvancedLocation());
                }

                if (entry.getKey().equals(ASSIGNED_USER)) {
                    asset.setAssignedUser(assetModificationData.getAssignedUser());
                }

                if (entry.getKey().equals(ASSET_STATUS)) {
                    asset.setAssetStatus(assetModificationData.getAssetStatus());
                }

                if (entry.getKey().equals(PURCHASE_ORDER)) {
                    asset.setPurchaseOrder(assetModificationData.getPurchaseOrder());
                }

                if (entry.getKey().equals(IDENTIFIED)) {
                    asset.setIdentified(assetModificationData.getIdentified());
                }

                if (entry.getKey().equals(PUBLISHED)) {
                    asset.setPublished(assetModificationData.isPublished());
                }

                if (entry.getKey().equals(NON_INTEGRATION_ORDER_NUMBER)) {
                    setOrderNumber(asset, orderNumber);
                }

                if (entry.getKey().equals(COMMENTS)) {
                    asset.setComments(assetModificationData.getComments());
                }
            }
        }
    }

    private void setOrderNumber(Asset asset, String orderNumber) {
        if (orderNumber != null) {
            asset.setNonIntergrationOrderNumber(orderNumber.trim());
        }
    }

    public void sendFailureEmailResponse(List<Long> ids, User modifiedBy) {
        String subject="Mass update of assets failed";
        String body="Failed to update " + ids.size() + " assets";
        sendEmailResponse(subject, body, modifiedBy);
    }

    public void sendSuccessEmailResponse(List<Long> ids, User modifiedBy) {
        String subject="Mass update of assets completed";
        String body="Updated " + ids.size() + " assets successfully";
        sendEmailResponse(subject, body, modifiedBy);
    }

    private void sendEmailResponse(String subject, String body, User modifiedBy) {
        try {
            mailService.sendMessage(new MailMessage(subject, body, modifiedBy.getEmailAddress()));
        } catch (MessagingException e) {
            logger.error("Unable to send Event Type removal email", e);
        }
    }

}
