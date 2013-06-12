package com.n4systems.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipleAssetConfiguration implements Serializable {

    private boolean configurationComplete = false;
    private List<AssetConfiguration> assetConfigs = new ArrayList<AssetConfiguration>();

    private RangeConfiguration rangeConfiguration = new RangeConfiguration();
    private String batchIdentifier;

    public List<AssetConfiguration> getAssetConfigs() {
        return assetConfigs;
    }

    public void setAssetConfigs(List<AssetConfiguration> assetConfigs) {
        this.assetConfigs = assetConfigs;
    }

    public RangeConfiguration getRangeConfiguration() {
        return rangeConfiguration;
    }

    public void setRangeConfiguration(RangeConfiguration rangeConfiguration) {
        this.rangeConfiguration = rangeConfiguration;
    }

    public String getBatchIdentifier() {
        return batchIdentifier;
    }

    public void setBatchIdentifier(String batchIdentifier) {
        this.batchIdentifier = batchIdentifier;
    }

    public boolean isConfigurationComplete() {
        return configurationComplete;
    }

    public void setConfigurationComplete(boolean configurationComplete) {
        this.configurationComplete = configurationComplete;
    }

    public void reset() {
        assetConfigs.clear();
        rangeConfiguration = new RangeConfiguration();
        batchIdentifier = null;
    }

    public static class RangeConfiguration implements Serializable {
        private String prefix;
        private Integer start;
        private String suffix;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }

    public static class AssetConfiguration implements Serializable {
        private String identifier;
        private String rfidNumber;
        private String customerRefNumber;

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
    }
}
