package com.n4systems.fieldid.actions.asset;

import com.google.common.collect.Lists;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.security.UserSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SmartListAction extends AbstractAction {

    private List<AssetResult> assetList = Lists.newArrayList();

    private String term;

    @Autowired
    private AssetService assetService;

    public SmartListAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    public String doSmartList() {
        return SUCCESS;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<AssetResult> getAssetList() {
        assetList = assetService.findAssetByIdentifiersForNewSmartSearch(term, new UserSecurityFilter(getCurrentUser()))
                                .stream()
                                .map(asset -> new AssetResult(asset))
                                .collect(Collectors.toList());

        return assetList;
    }

    public void setAssetList(List<AssetResult> assetList) {
        this.assetList = assetList;
    }

    public class AssetResult {
        private String id;
        private String identifier;
        private String rfidNumber;
        private String customerRefNumber;
        private String assetType;

        public AssetResult(Asset asset) {
            this.id = String.valueOf(asset.getId());
            this.identifier = asset.getIdentifier();
            this.rfidNumber = asset.getRfidNumber() == null ? "" : asset.getRfidNumber();
            this.customerRefNumber = asset.getCustomerRefNumber() == null ? "" : asset.getCustomerRefNumber();
            this.assetType = asset.getType().getDisplayName();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getRfidNumber() {
            return rfidNumber;
        }

        public void setRfidNumber(String rfidNumber) {
            this.rfidNumber = rfidNumber;
        }

        public String getCustomerRefNumber() {
            return customerRefNumber;
        }

        public void setCustomerRefNumber(String customerRefNumber) {
            this.customerRefNumber = customerRefNumber;
        }

        public String getAssetType() {
            return assetType;
        }

        public void setAssetType(String assetType) {
            this.assetType = assetType;
        }
    }
}
