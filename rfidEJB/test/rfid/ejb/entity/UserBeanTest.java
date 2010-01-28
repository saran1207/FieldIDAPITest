package rfid.ejb.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserBeanTest {
	
	@Test
	public void pre_persist_ensures_referral_key_is_set() {
		UserBean user = new UserBean();
		user.setReferralKey(null);
		
		user.prePersist();
		
		assertNotNull(user.getReferralKey());
	}
	
	@Test
	public void pre_merge_ensures_referral_key_is_set() {
		UserBean user = new UserBean();
		user.setReferralKey(null);
		
		user.preMerge();
		
		assertNotNull(user.getReferralKey());
	}
	
}
