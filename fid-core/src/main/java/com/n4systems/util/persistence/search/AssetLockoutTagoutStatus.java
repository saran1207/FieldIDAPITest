package com.n4systems.util.persistence.search;

import com.n4systems.model.api.DisplayEnum;

/**
 * Created by rrana on 2015-10-12.
 */
public enum AssetLockoutTagoutStatus implements DisplayEnum {

    ALL("All"), WITHPROCEDURES("With Procedures"), WITHOUTPROCEDURES("Without Procedures");

    private String label;

    AssetLockoutTagoutStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

}
