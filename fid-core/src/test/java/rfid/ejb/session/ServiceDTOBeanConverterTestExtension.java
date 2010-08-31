package rfid.ejb.session;

import javax.persistence.EntityManager;

import com.n4systems.ejb.legacy.impl.ServiceDTOBeanConverterImpl;

public class ServiceDTOBeanConverterTestExtension extends ServiceDTOBeanConverterImpl {
	
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
}
