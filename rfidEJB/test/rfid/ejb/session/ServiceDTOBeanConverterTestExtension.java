package rfid.ejb.session;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.FindOwnerByLegacyIds;

public class ServiceDTOBeanConverterTestExtension extends ServiceDTOBeanConverterImpl {

	private FindOwnerByLegacyIds findOwner;
	
	@Override
	protected FindOwnerByLegacyIds getFindOwnerByLegacyIds(long tenantId) {
		return findOwner;
	}

	public void setFindOwner(FindOwnerByLegacyIds findOwner) {
		this.findOwner = findOwner;
	}
	
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
}
