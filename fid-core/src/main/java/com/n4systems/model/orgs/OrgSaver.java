package com.n4systems.model.orgs;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

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
	public BaseOrg update(EntityManager em, BaseOrg entity) {
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
			updateDivisionsSecurityInformation((CustomerOrg) entity, em);
		}
	}

	private void updateDivisionsSecurityInformation(CustomerOrg customer, EntityManager em) {
		QueryBuilder<DivisionOrg> divisionOrgBuilder = new QueryBuilder<DivisionOrg>(DivisionOrg.class, new TenantOnlySecurityFilter(customer.getTenant().getId()));
		divisionOrgBuilder.addWhere(WhereClauseFactory.create("customerOrg.id", customer.getId()));
		
		for (DivisionOrg division: divisionOrgBuilder.getResultList(em)) {
			division.touch();
			em.merge(division);
		}
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
			// we'll update the fields by touching our linked org.  The updated fields will be copied 
			// via a premerge method before the update happens.  This way the copy logic stays with the ExternalOrg class.
			linkedOrg.touch();
			update(em, linkedOrg);
		}
	}
	
	protected List<ExternalOrg> loadLinkedOrgs(EntityManager em, InternalOrg org) {
		return linkedOrgLoader.setInternalOrgId(org.getId()).load(em);
	}
}
