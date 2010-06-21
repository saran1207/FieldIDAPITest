package com.n4systems.fieldid.actions.projects;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


import com.n4systems.model.api.Listable;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.tools.Pager;
import com.n4systems.tools.SillyPager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.SimpleListable;

public class JobResourcesCrudTest {

	@SuppressWarnings("serial")
	@Test
	public void get_employees_excludes_deleted_employees() {
		List<Listable<Long>> users = new ArrayList<Listable<Long>>();
		users.add(new SimpleListable<Long>(1L, "one"));
		users.add(new SimpleListable<Long>(2L, "two"));		
		
		final UserListableLoader loader = createMock(UserListableLoader.class);
		expect(loader.load()).andReturn(users);
		replay(loader);
		
		JobResourcesCrud jrc = new JobResourcesCrud(null) {
			@Override
			protected UserListableLoader createUserListLoader() {
				return loader;
			}
			
			@Override
			public Pager<User> getPage() {
				return new SillyPager<User>(new ArrayList<User>());
			}
		};
		
		List<ListingPair> employees = jrc.getEmployees();
		
		assertEquals(users.size(), employees.size());
		for (Listable<Long> user: users) {
			assertTrue(employees.contains(new ListingPair(user)));
		}
		
		verify(loader);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void get_employees_excludes_users_already_assigned_to_job() {
		final User user = UserBuilder.anEmployee().build();
		
		List<Listable<Long>> users = new ArrayList<Listable<Long>>();
		users.add(user);
		users.add(new SimpleListable<Long>(2L, "two"));		
		
		final UserListableLoader loader = createMock(UserListableLoader.class);
		expect(loader.load()).andReturn(users);
		replay(loader);
		
		JobResourcesCrud jrc = new JobResourcesCrud(null) {
			@Override
			protected UserListableLoader createUserListLoader() {
				return loader;
			}
			
			@Override
			public Pager<User> getPage() {
				return new SillyPager<User>(Arrays.asList(user));
			}
		};
		
		List<ListingPair> employees = jrc.getEmployees();
		
		assertEquals(1, employees.size());
		assertFalse(employees.contains(new ListingPair(user)));
		assertTrue(employees.contains(new ListingPair(users.get(1))));
		
		verify(loader);
	}
}
