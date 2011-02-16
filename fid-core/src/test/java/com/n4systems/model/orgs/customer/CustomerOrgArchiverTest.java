package com.n4systems.model.orgs.customer;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.division.DivisionOrgArchiver;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;

public class CustomerOrgArchiverTest {
	
	private CustomerOrgArchiver customerArchiver;
	private DivisionOrgArchiver divisionArchiver;
	private CustomerOrg customer;
	private DivisionOrg division;
	private User user;
	private OrgSaver orgSaver;
	private UserSaver userSaver;
	private SecurityFilter securityFilter;		
	private Transaction transaction;
	
	List<User> userList = new ArrayList<User>();
	List<DivisionOrg> divisionList = new ArrayList<DivisionOrg>();
	
	@Before
	public void setUp(){
		customer = OrgBuilder.aCustomerOrg().buildCustomer();
		division = OrgBuilder.aDivisionOrg().buildDivision();
		user = UserBuilder.aUser().build();
		orgSaver = createMock(OrgSaver.class);
		userSaver = createMock(UserSaver.class);
		securityFilter = createMock(SecurityFilter.class);		
		transaction = createMock(Transaction.class);

		userList = Collections.singletonList(user);
		divisionList = Collections.singletonList(division);
		
		divisionArchiver = new DivisionOrgArchiver() {
			@Override
			protected List<User> getUserList(SecurityFilter filter, DivisionOrg division) {
				return Collections.emptyList();
			}
		};
		
		customerArchiver = new CustomerOrgArchiver(){
			@Override
			protected List<User> getUserList(SecurityFilter filter, CustomerOrg customer) {
				return userList;
			}
			@Override
			protected List<DivisionOrg> getDivisions(SecurityFilter filter, CustomerOrg customer) {
				return divisionList;
			}
		};
		
		customerArchiver.setDivisionOrgArchiver(divisionArchiver);

	}

	@Test
	public void testArchiveCustomer() throws Exception {
					
		expect(orgSaver.update(transaction, customer)).andReturn(customer);
		expect(orgSaver.update(transaction, division)).andReturn(division);
		expect(userSaver.update(transaction, user)).andReturn(user);
		
		replayExpectations();
		
		customerArchiver.doArchive(customer, orgSaver, userSaver, securityFilter, false, transaction );
		
		verifyExpectations();
	}

	@Test
	public void testUnarchiveCustomer() throws Exception {
				
		expect(orgSaver.update(transaction, customer)).andReturn(customer);
		expect(orgSaver.update(transaction, division)).andReturn(division);
		
		replayExpectations();
		
		customerArchiver.doArchive(customer, orgSaver, userSaver, securityFilter, true, transaction );
		
		verifyExpectations();
	}
		
	private void replayExpectations() {
		replay(userSaver, orgSaver);
	}
	
	private void verifyExpectations() {
		verify(userSaver, orgSaver);
	}
	
}
