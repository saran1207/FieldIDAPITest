package com.n4systems.model.offlineprofile;

import com.n4systems.model.PlatformType;
import com.n4systems.model.api.HasUser;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "offline_profiles")
public class OfflineProfile extends EntityWithTenant implements HasUser {
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(OfflineProfile.class);
	}	
	
	public enum SyncDuration {
		WEEK, MONTH, SIX_MONTHS, YEAR, ALL;

		public LocalDateTime getEndDate(LocalDateTime startDate) {
			switch(this) {
				case WEEK: return startDate.plusWeeks(1);
				case MONTH: return startDate.plusMonths(1);
				case SIX_MONTHS: return startDate.plusMonths(6);
				case YEAR: return startDate.plusYears(1);
				default: return null;
			}
		}
	}

	public static final SyncDuration DEFAULT_SYNC_DURATION = SyncDuration.YEAR;	

	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", updatable = false)
	private User user;

	@ElementCollection
	@CollectionTable(name = "offline_profiles_assets", joinColumns = @JoinColumn(name = "offline_profiles_id"))
	private Set<String> assets = new HashSet<String>();

	@ElementCollection
	@CollectionTable(name = "offline_profiles_orgs", joinColumns = @JoinColumn(name = "offline_profiles_id"))
	private Set<Long> organizations = new HashSet<Long>();
	
	@Enumerated(EnumType.STRING)
    @Column(name="sync_duration", nullable=false)
	private SyncDuration syncDuration = SyncDuration.YEAR;

    @Column(name="current_platform", length = 200)
    private String currentPlatform;

    @Enumerated(EnumType.STRING)
    @Column(name="current_platform_type")
    private PlatformType currentPlatformType;

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	public Set<String> getAssets() {
		return assets;
	}

	public void setAssets(Set<String> assets) {
		this.assets = assets;
	}

	public Set<Long> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<Long> organizations) {
		this.organizations = organizations;
	}

	public SyncDuration getSyncDuration() {
		return syncDuration;
	}
	
	public void setSyncDuration(SyncDuration syncDuration) {
		this.syncDuration = syncDuration;
	}

    public String getCurrentPlatform() {
        return currentPlatform;
    }

    public void setCurrentPlatform(String currentPlatform) {
        this.currentPlatform = currentPlatform;
    }

    public PlatformType getCurrentPlatformType() {
        return currentPlatformType;
    }

    public void setCurrentPlatformType(PlatformType currentPlatformType) {
        this.currentPlatformType = currentPlatformType;
    }
}
