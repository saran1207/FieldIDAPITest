package com.n4systems.model.orgs.customer;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.tools.Pager;
import com.n4systems.tools.SillyPager;
import com.n4systems.util.UserType;

public class CustomerOrgArchiverTest {
	
	private CustomerOrgArchiver archiver;
	private CustomerOrg customer;
	private UserManager userManager;
	private OrgSaver orgSaver;
	private UserSaver userSaver;
	private DivisionOrgByCustomerListLoader divisionLoader;
	private SecurityFilter securityFilter;		
	private Transaction transaction;
	
	@Before
	public void setUp(){
		customer = OrgBuilder.aCustomerOrg().buildCustomer();
		userManager = createMock(UserManager.class);
		orgSaver = createMock(OrgSaver.class);
		userSaver = createMock(UserSaver.class);
		divisionLoader = createMock(DivisionOrgByCustomerListLoader.class);
		securityFilter = createMock(SecurityFilter.class);		
		transaction = createMock(Transaction.class);
		archiver = new CustomerOrgArchiver(){
			@SuppressWarnings("unused")
			protected DivisionOrgByCustomerListLoader createDivisionsLoader(SecurityFilter filter) {
				return divisionLoader;
			}
		};

	}

	@Test
	public void testArchiveCustomer() throws Exception {
			
		User user = new User();
		Pager<User> pager = new SillyPager<User>(Collections.singletonList(user));
		DivisionOrg divisionOrg = OrgBuilder.aDivisionOrg().buildDivision();
		List<DivisionOrg> divisionList = Collections.singletonList(divisionOrg);
		
		setUserManagerExpectations(pager);
		
		setDivisionExpectations(divisionList);
		
		expect(orgSaver.update(transaction, customer)).andReturn(customer);
		
		replayExpectations();
		
		archiver.doArchive(customer, orgSaver, userSaver, securityFilter, false, transaction );
		
		verifyExpectations();
	}

	@Test
	public void testUnarchiveCustomer() throws Exception {
			
		DivisionOrg divisionOrg = OrgBuilder.aDivisionOrg().buildDivision();
		List<DivisionOrg> divisionList = Collections.singletonList(divisionOrg);
		
		setDivisionExpectations(divisionList);
		
		expect(orgSaver.update(transaction, customer)).andReturn(customer);
		
		replayExpectations();
		
		archiver.doArchive(customer, orgSaver, userSaver, securityFilter, true, transaction );
		
		verifyExpectations();
	}
	
	private void setDivisionExpectations(List<DivisionOrg> divisionList) {
		expect(divisionLoader.setCustomer(customer)).andReturn(divisionLoader);
		expect(divisionLoader.load(transaction)).andReturn(divisionList);
		for(int i = 0; i < divisionList.size(); i++) {
			BaseOrg divisionOrg = divisionList.get(i);
			expect(orgSaver.update(transaction, divisionOrg )).andReturn(divisionOrg);
		}
	}

	private void setUserManagerExpectations(Pager<User> pager) {
		expect(userManager.getUsers(securityFilter, true, 1, 100000, null, UserType.READONLY, customer)).andReturn(pager);
		
		for (int i = 0; i < pager.getList().size(); i++) {
			userManager.updateUser(pager.getList().get(i));
		}
	}
	
	private void replayExpectations() {
		replay(userManager, userSaver, divisionLoader, orgSaver);
	}
	
	private void verifyExpectations() {
		verify(userManager, userSaver, divisionLoader, orgSaver);
	}
	
}
