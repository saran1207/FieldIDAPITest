package rfid.ejb.session;

import com.n4systems.ejb.legacy.impl.ServiceDTOBeanConverterImpl;

import javax.persistence.EntityManager;

public class ServiceDTOBeanConverterTestExtension extends ServiceDTOBeanConverterImpl {
	
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
}
