package com.n4systems.model.orgs;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.savers.Saver;

public class OrgSaver extends Saver<BaseOrg> {

	private final LinkedOrgListLoader linkedOrgLoader;
	
	public OrgSaver() {
		linkedOrgLoader = new LinkedOrgListLoader();
	}
	
	public OrgSaver(LinkedOrgListLoader linkedOrgLoader) {
		this.linkedOrgLoader = linkedOrgLoader;
	}
	
	@Override
	public void save(EntityManager em, BaseOrg entity) {
		super.save(em, entity);
		
		// we need to re-save the org so that the security fields get set.
		entity.touch();
		update(em, entity);
	}

	@Override
	protected BaseOrg update(EntityManager em, BaseOrg entity) {
		BaseOrg attachedOrg = super.update(em, entity);

		// only InternalOrgs can be linked to
		if (attachedOrg instanceof InternalOrg) {
			updateLinkedFields(em, (InternalOrg)attachedOrg);
		}
		
		return attachedOrg;
	}
	
	// when a linked org gets updated, we need to track down all the references to it and update them
	// with the new field values :(
	protected void updateLinkedFields(EntityManager em, InternalOrg org) {
		for (ExternalOrg linkedOrg: loadLinkedOrgs(em, org)) {
			// we'll update the fields by setting our org back onto the linked org.  The updated fields will be copyied 
			// via a premerge method before the update happens.  This way the copy logic stays with the ExternalOrg class.
			linkedOrg.setLinkedOrg(org);
			update(em, linkedOrg);
		}
	}
	
	protected List<ExternalOrg> loadLinkedOrgs(EntityManager em, InternalOrg org) {
		return linkedOrgLoader.setInternalOrgId(org.getId()).load();
	}
}