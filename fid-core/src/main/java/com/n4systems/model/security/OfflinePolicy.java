package com.n4systems.model.security;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by agrabovskis on 2018-07-23.
 */
@Embeddable
public class OfflinePolicy implements Serializable {

    private static int DEFAULT_MAX_OFFLINE_DAYS = 30;

    private int maxOfflineDays;

    public OfflinePolicy() {
        this(DEFAULT_MAX_OFFLINE_DAYS);
    }

    public OfflinePolicy(int maxOfflineDays) {
        this.maxOfflineDays = maxOfflineDays;
    }

    public int getMaxOfflineDays() {
        return maxOfflineDays;
    }

    public void setMaxOfflineDays(int maxOfflineDays) {
        this.maxOfflineDays = maxOfflineDays;
    }
}
