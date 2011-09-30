package com.n4systems.fieldid.actions.asset;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.customer.CustomerOrgsWithNameLoader;
import com.n4systems.model.safetynetwork.AllPreAssignedAssetsLoader;
import com.n4systems.model.safetynetwork.BulkRegisterHelper;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BulkRegisterSafetyNetwork extends MultiAddAssetCrud {

    private static Logger logger = Logger.getLogger(BulkRegisterSafetyNetwork.class);

    private static final int MAX_REGISTERED_ITEMS = 250;

    private Long networkAssetTypeId;
    private Long vendorId;
    private PrimaryOrg vendor;

    private boolean useOwnerFromAssets;
    private boolean useDatesFromAssets;

    public BulkRegisterSafetyNetwork(PersistenceManager persistenceManager, OrderManager orderManager, LegacyAsset legacyAssetManager, AssetCodeMappingService assetCodeMappingServiceManager, EventScheduleManager eventScheduleManager) {
        super(persistenceManager, orderManager, legacyAssetManager, assetCodeMappingServiceManager, eventScheduleManager);
    }

    @SkipValidation
    public String doBulkRegister() {
        vendor = getLoaderFactory().createVendorLinkedOrgLoader().setLinkedOrgId(vendorId).load();

        AllPreAssignedAssetsLoader safetyNetworkPreAssignedAssetLoader = getLoaderFactory().createAllPreAssignedAssetsLoader().setVendor(vendor).setCustomer(getPrimaryOrg());
        BulkRegisterHelper helper = new BulkRegisterHelper(safetyNetworkPreAssignedAssetLoader);
        addIdentifiersBasedOn(helper.getPreassignedAssetsOfType(networkAssetTypeId));

        return SUCCESS;
    }

    @SkipValidation
    public String doRegister() {
        AssetViewModeConverter converter = new AssetViewModeConverter(getLoaderFactory(), orderManager, getUser());
        AssetSaveService saver = new AssetSaveService(legacyAssetManager, fetchCurrentUser());

        List<AssetIdentifierView> assetIdentifiers = getIdentifiers();

        for (AssetIdentifierView assetIdentifier : assetIdentifiers) {
            Asset assetToSave = converter.viewToModel(assetView);

            Asset linkedAsset = persistenceManager.find(Asset.class, assetIdentifier.getAssetId());

            assetToSave.setIdentifier(assetIdentifier.getIdentifier());
            assetToSave.setCustomerRefNumber(assetIdentifier.getReferenceNumber());
            assetToSave.setRfidNumber(assetIdentifier.getRfidNumber());

            assetToSave.setLinkedAsset(linkedAsset);
            assetWebModel.fillInAsset(assetToSave);

            if (useDatesFromAssets) {
                assetToSave.setIdentified(linkedAsset.getIdentified());
            }

            if (useOwnerFromAssets) {
                assetToSave.setOwner(findOrCreateOwner(linkedAsset.getOwner()));
            }

            saver.setAsset(assetToSave);
            saver.setUploadedAttachments(copyUploadedFiles());
            Asset savedAsset = saver.create();

            scheduleEvents(savedAsset);
            saver.clear();
        }

        addFlashMessageText(getText("label.bulk_register_complete", new String[]{identifiers.size() + ""}));

        return SUCCESS;
    }

    private BaseOrg findOrCreateOwner(BaseOrg owner) {
        if (owner instanceof DivisionOrg) {
            return findOrCreateCustomerWithMatchingName(owner);
        } else {
            return getPrimaryOrg();
        }
    }

    private BaseOrg findOrCreateCustomerWithMatchingName(BaseOrg owner) {
        CustomerOrgsWithNameLoader customerOrgWithNameLoader = getLoaderFactory().createCustomerOrgWithNameLoader();
        customerOrgWithNameLoader.setName(owner.getName());
        List<CustomerOrg> orgsWithMatchingName = customerOrgWithNameLoader.load();
        if (!orgsWithMatchingName.isEmpty()) {
            return orgsWithMatchingName.get(0);
        }

        CustomerOrg org = new CustomerOrg();
        org.setName(owner.getName());
        org.setParent(getPrimaryOrg());
        org.setTenant(getTenant());
        persistenceManager.save(org);
        return org;
    }

    private void addIdentifiersBasedOn(List<Asset> items) {
        if (items.size() > MAX_REGISTERED_ITEMS) {
            List<Asset> shortenedItems = new ArrayList<Asset>(MAX_REGISTERED_ITEMS);
            Iterator<Asset> it = items.iterator();
            for (int i = 0; i < MAX_REGISTERED_ITEMS; i++) {
                shortenedItems.add(it.next());
            }
            items = shortenedItems;
        }
        List<AssetIdentifierView> identifiers = new ArrayList<AssetIdentifierView>(items.size());
        for (Asset item : items) {
            AssetIdentifierView identifier = new AssetIdentifierView();
            identifier.setIdentifier(item.getIdentifier());
            identifier.setRfidNumber(item.getRfidNumber());
            identifier.setAssetId(item.getId());
            identifiers.add(identifier);
        }
        setIdentifiers(identifiers);
    }

    public Long getNetworkAssetTypeId() {
        return networkAssetTypeId;
    }

    public void setNetworkAssetTypeId(Long networkAssetTypeId) {
        this.networkAssetTypeId = networkAssetTypeId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public PrimaryOrg getVendor() {
        return vendor;
    }

    public void setVendor(PrimaryOrg vendor) {
        this.vendor = vendor;
    }

    public boolean isPublishedCatalog(Tenant tenant) {
        try {
            CatalogService catalogService = new CatalogServiceImpl(persistenceManager, tenant);
            return catalogService.hasCatalog();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUseOwnerFromAssets() {
        return useOwnerFromAssets;
    }

    public void setUseOwnerFromAssets(boolean useOwnerFromAssets) {
        this.useOwnerFromAssets = useOwnerFromAssets;
    }

    public boolean isUseDatesFromAssets() {
        return useDatesFromAssets;
    }

    public void setUseDatesFromAssets(boolean useDatesFromAssets) {
        this.useDatesFromAssets = useDatesFromAssets;
    }
    
    @Override
    public boolean isMultiAdd() {
    	return false;
    }
}
