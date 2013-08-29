package com.n4systems.model.parents;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.HasModifiedBy;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.user.User;
import com.n4systems.persistence.localization.LocalizedText;
import com.n4systems.persistence.utils.DefaultEntityModifiedCreatedHandler;
import com.n4systems.persistence.utils.EntityModifiedCreatedHandler;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class AbstractEntity extends BaseEntity implements Serializable, HasModifiedBy {

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	private User modifiedBy;

    @Transient
    private EntityModifiedCreatedHandler modifiedCreatedHandler;

    public AbstractEntity() {
        this(new DefaultEntityModifiedCreatedHandler());
    }

    public AbstractEntity(EntityModifiedCreatedHandler modifiedCreatedHandler) {
        this.modifiedCreatedHandler = modifiedCreatedHandler;
    }

	@Override
	protected void onCreate() {
		super.onCreate();
        modifiedCreatedHandler.onCreate(this);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
        modifiedCreatedHandler.onUpdate(this);
	}

	@AllowSafetyNetworkAccess
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date dateCreated) {
		this.created = dateCreated;
	}

	@AllowSafetyNetworkAccess
	public Date getModified() {
		return modified;
	}

	public void setModified(Date dateModified) {
		this.modified = dateModified;
	}
	
	@Override
	@AllowSafetyNetworkAccess
	public User getModifiedBy() {
		return modifiedBy;
	}

	@Override
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/** Nulls the modified field.  Will force Hibernate to save on merge. */
	public void touch() {
		modified = null;
	}

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

	@Override
	public void reset() {
		super.reset();
		created = null;
		modified = null;
		createdBy = null;
		modifiedBy = null;
	}

    @Deprecated  // just use new LocalizedText().
    protected LocalizedText updateLocalizedText(LocalizedText currentText, String text) {
        // this would be better done if a aspect for
        if (currentText==null) {
            return new LocalizedText(text);
        } else if (currentText.getText().equals(text)) {
            return currentText;
        } else {
            return new LocalizedText(text);
        }
    }

}
