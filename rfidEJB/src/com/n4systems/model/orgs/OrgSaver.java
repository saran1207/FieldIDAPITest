package com.n4systems.model.orgs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.AddressInfo;
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
		ensureAddressInfoIsNotNull(entity);
		
		super.save(em, entity);
		
		// we need to re-save the org so that the security fields get set.
		entity.touch();
		update(em, entity);
	}

	@Override
	protected BaseOrg update(EntityManager em, BaseOrg entity) {
		ensureAddressInfoIsNotNull(entity);
		
		if (entity.getAddressInfo().isNew()) {
			em.persist(entity.getAddressInfo());
		}
		
		BaseOrg attachedOrg = super.update(em, entity);

		// only InternalOrgs can be linked to
		if (attachedOrg instanceof InternalOrg) {
			updateLinkedFields(em, (InternalOrg)attachedOrg);
		}
		
		propogateSecurityInformation(attachedOrg, em);
		
		return attachedOrg;
	}
	
	private void propogateSecurityInformation(BaseOrg entity, EntityManager em) {
		if (entity instanceof CustomerOrg) {
			CustomerOrg customer = (CustomerOrg)entity;
			updateDivisionsSecurityInformation(customer, em);
		}
	}

	private void updateDivisionsSecurityInformation(CustomerOrg customer, EntityManager em) {
		String updateQuery = "UPDATE " + BaseOrg.class.getName() + " SET secondaryOrg = :secondaryOrg WHERE customerOrg = :customer";
		
		Query query = em.createQuery(updateQuery);
			
		query.setParameter("secondaryOrg", customer.getSecondaryOrg());
		query.setParameter("customer", customer);
		
		query.executeUpdate();
		
	}

	protected void ensureAddressInfoIsNotNull(BaseOrg entity) {
		if (entity.getAddressInfo() == null) {
			entity.setAddressInfo(new AddressInfo());
		}
	}
	
	// when a linked org gets updated, we need to track down all the references to it and update them
	// with the new field values :(
	protected void updateLinkedFields(EntityManager em, InternalOrg org) {
		for (ExternalOrg linkedOrg: loadLinkedOrgs(em, org)) {
			// we'll update the fields by touching our linked org.  The updated fields will be copyied 
			// via a premerge method before the update happens.  This way the copy logic stays with the ExternalOrg class.
			linkedOrg.touch();
			update(em, linkedOrg);
		}
	}
	
	protected List<ExternalOrg> loadLinkedOrgs(EntityManager em, InternalOrg org) {
		return linkedOrgLoader.setInternalOrgId(org.getId()).load(em);
	}
}