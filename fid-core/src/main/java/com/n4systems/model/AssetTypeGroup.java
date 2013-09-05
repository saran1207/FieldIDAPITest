package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="assettypegroups")
public class AssetTypeGroup extends EntityWithTenant implements NamedEntity, Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false, length=40)
	private @Localized String name;

    @Column(name= "loto_device", nullable = false)
    private boolean lotoDevice = false;

    @Column(name= "loto_lock", nullable = false)
    private boolean lotoLock = false;

    @Column(nullable=false)
	private Long orderIdx;
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	
	private void trimName() {
		name = (name != null) ? name.trim() : null;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public Long getOrderIdx() {
		return orderIdx;
	}

	public void setOrderIdx(Long orderIdx) {
		this.orderIdx = orderIdx;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

    public boolean isLotoDevice() {
        return lotoDevice;
    }

    public void setLotoDevice(boolean lotoDevice) {
        this.lotoDevice = lotoDevice;
    }

    public boolean isLotoLock() {
        return lotoLock;
    }

    public void setLotoLock(boolean lotoLock) {
        this.lotoLock = lotoLock;
    }


}
