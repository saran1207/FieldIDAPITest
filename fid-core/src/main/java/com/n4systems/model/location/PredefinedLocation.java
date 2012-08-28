package com.n4systems.model.location;

import com.google.common.base.Preconditions;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.persistence.PersistenceManager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Entity(name = "predefinedlocations")
public class PredefinedLocation extends ArchivableEntityWithOwner implements NamedEntity, TreeNode, HasOwner  {

    @Column(nullable = false, length = 255)
	private String name;

	@ManyToOne
	private PredefinedLocation parent;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@JoinTable(name="predefinedlocations_searchids", joinColumns = {@JoinColumn(name="predefinedlocation_id")})
	@Column(name="search_id")
	private List<Long> searchIds = new ArrayList<Long>();
	
	public PredefinedLocation() {
		super();
	}

	public PredefinedLocation(Tenant tenant, PredefinedLocation parent, BaseOrg org) {
		super(tenant, org);
		this.parent = parent;
	}
	
	/*
	 * XXX - this should be a @PostPersist.  Unfortunately, there's a bug in our current 
	 * version of hibernate that won't run PostPersiste annotated methods if post-persists are 
	 * defined in the persistence.xml.
	 * Also note this only needs to be done on save since locations cannot be moved around
	 */
	public void rebuildSearchIds(EntityManager em) {
		searchIds.clear();
		if (parent != null) {
			// parents need to be re-attached to avoid lazy loads on the searchIds
			PersistenceManager.reattach(em, parent);
			searchIds.addAll(parent.getSearchIds());
		}
		searchIds.add(getId());
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public PredefinedLocation getParent() {
		return parent;
	}
	
	public void setParent(PredefinedLocation parentNode) {
		parent=parentNode;
        if (parentNode!=null) {
            setOwner(null);
        }
	}

    @Override
    public void setOwner(BaseOrg owner) {
        Preconditions.checkState(getParent()==null || owner==null, "can only set Org owner on top level location node.");
        super.setOwner(owner);
    }

    public boolean hasParent() {
		return parent != null;
	}

	public int levelNumber() {
		if (hasParent()) {
			return parent.levelNumber() + 1;
		}
		return 1;
	}
	
	public List<Long> getSearchIds() {
		return new ArrayList<Long>(searchIds);
	}
	
	public String getFullName() {
		return hasParent() ? parent.getFullName() + " > " + name : name;
	}

    public BaseOrg getBaseOrg() {
        PredefinedLocation node = this;
        while (node!=null) {
            if (node.getOwner()!=null) {
                return node.getOwner();
            }
            node = node.getParent();
        }
        return null;
    }
}
