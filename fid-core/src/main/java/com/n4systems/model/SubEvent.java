package com.n4systems.model;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.utils.AssetEvent;
import com.n4systems.util.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "subevents")
@PrimaryKeyJoinColumn(name="event_id")
public class SubEvent extends AbstractEvent<ThingEventType, Asset> implements SecurityEnhanced<SubEvent>, AssetEvent {
	private static final long serialVersionUID = 1L;
	public static final String[] ALL_FIELD_PATHS = { "modifiedBy.userID", "eventForm.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "results.criteriaImages", "asset", "asset.infoOptions", "infoOptionMap"};
	
	@Column( length = 255 )
	private String name;

    @ManyToOne(fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name="thing_event_type_id")
    private ThingEventType type;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name="asset_id")
    private Asset asset;

    @ManyToOne(optional = true)
    @JoinColumn(name="asset_status_id")
    private AssetStatus assetStatus;

	@AllowSafetyNetworkAccess
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public SubEvent() {}

	public SubEvent(Tenant tenant) {
		super(tenant);
	}

	@Override
    public String toString() {
	    return "SubEvent: " + getName() + StringUtils.indent("\n" + super.toString(), 1);
    }

	public SubEvent enhance(SecurityLevel level) {
		SubEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setType(enhance(getType(), level));
		enhanced.setAsset(enhance(getAsset(), level));
		return enhanced;
	}

    public ThingEventType getType() {
        return type;
    }

    public void setType(ThingEventType type) {
        this.type = type;
    }

    @Override
    public Asset getTarget() {
        return getAsset();
    }

    @Override
    public void setTarget(Asset target) {
        setAsset(target);
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    @Override
    protected void copyDataIntoResultingAction(AbstractEvent<?,?> event) {
        AssetEvent action = (AssetEvent) event;
        action.setAsset(getAsset());
    }
}
