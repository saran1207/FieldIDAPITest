package rfid.ejb.session;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.model.UnitOfMeasure;


@Interceptors({TimingInterceptor.class})
@Stateless
public class UnitOfMeasureManagerImpl implements UnitOfMeasureManager {

	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;

	
	public UnitOfMeasure getUnitOfMeasureForInfoField(Long infoFieldId) {
		UnitOfMeasure unitMeasure = null;
		
		try {
			Query query = em.createQuery("select i.unitOfMeasure from InfoFieldBean i where i.uniqueID = :infoFieldId");
			query.setParameter("infoFieldId", infoFieldId);
	
			unitMeasure = (UnitOfMeasure)query.getSingleResult();
		} catch(NoResultException e) {}
		
		return unitMeasure;
	}

	
	
	
}
