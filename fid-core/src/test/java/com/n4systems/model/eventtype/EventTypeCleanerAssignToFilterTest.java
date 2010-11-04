package com.n4systems.model.eventtype;

import static com.n4systems.model.builders.EventTypeBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.model.EventType;
import org.junit.Test;

import com.n4systems.fieldid.permissions.AlwaysOffSystemSecurityGuardTestDouble;


public class EventTypeCleanerAssignToFilterTest {
	
	private final class AssignToOnSecurityGuard extends AlwaysOffSystemSecurityGuardTestDouble {
		@Override
		public boolean isAssignedToEnabled() {
			return true;
		}
	}

	@Test
	public void should_keep_assigned_to_available_if_the_target_tenant_does_not_have_assigned_to_enabled() throws Exception {
		EventType eventType = anEventType().withAssignedToAvailable().build();
		
		EventTypeCleanerAssignToFilter sut = new EventTypeCleanerAssignToFilter(new AssignToOnSecurityGuard());
		sut.clean(eventType);
		
		assertThat(eventType, hasProperty("assignedToAvailable", is(true)));
	}
	
	@Test
	public void should_keep_assigned_to_available_not_available_if_the_target_tenant_does_not_have_assigned_to_enabled() throws Exception {
		EventType eventType = anEventType().withAssignedToNotAvailable().build();
		
		EventTypeCleanerAssignToFilter sut = new EventTypeCleanerAssignToFilter(new AssignToOnSecurityGuard());
		sut.clean(eventType);
		
		assertThat(eventType, hasProperty("assignedToAvailable", is(false)));
	}
	
	@Test
	public void should_turn_off_assigned_to_available_if_the_target_tenant_does_not_have_assigned_to_is_disabled() throws Exception {
		EventType eventType = anEventType().withAssignedToAvailable().build();
		
		EventTypeCleanerAssignToFilter sut = new EventTypeCleanerAssignToFilter(new AlwaysOffSystemSecurityGuardTestDouble());
		sut.clean(eventType);
		
		assertThat(eventType, hasProperty("assignedToAvailable", is(false)));
	}
	
	@Test
	public void should_turn_off_assigned_to_available_not_available_if_the_target_tenant_does_not_have_assigned_to_is_disabled() throws Exception {
		EventType eventType = anEventType().withAssignedToNotAvailable().build();
		
		EventTypeCleanerAssignToFilter sut = new EventTypeCleanerAssignToFilter(new AlwaysOffSystemSecurityGuardTestDouble());
		sut.clean(eventType);
		
		assertThat(eventType, hasProperty("assignedToAvailable", is(false)));
	}
	
}
