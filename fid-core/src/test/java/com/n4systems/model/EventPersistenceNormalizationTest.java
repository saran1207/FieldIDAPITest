package com.n4systems.model;

import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.user.User;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Test;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.event.AssignedToUpdate.assignAssetToUser;
import static org.hamcrest.Matchers.not;


public class EventPersistenceNormalizationTest {
	
	@Test
	public void should_update_assigned_to_update_on_create_to_an_ignored_assignment_when_null() throws Exception {
		Event event = anEvent().withNoAssignedToUpdate().build();
		
		event.onCreate();
		
		Assert.assertThat(event.getAssignedTo(), anIngoredAssignedToUpdate());
	}
	
	@Test
	public void should_update_assigned_to_update_on_update_to_an_ignored_assignment_when_null() throws Exception {
		Event event = anEvent().withNoAssignedToUpdate().build();
		
		event.onUpdate();
		
		Assert.assertThat(event.getAssignedTo(), anIngoredAssignedToUpdate());
	}

	@Test
	public void should_not_update_an_assigned_to_update_on_create_to_when_it_has_been_filled_in() throws Exception {
		User user = null;
		Event event = anEvent().withAssignedToUpdate(assignAssetToUser(user)).build();
		
		event.onCreate();
		
		Assert.assertThat(event.getAssignedTo(), not(anIngoredAssignedToUpdate()));
	}
	
	@Test
	public void should_not_update_an_assigned_to_update_on_update_to_when_it_has_been_filled_in() throws Exception {
		User user = null;
		Event event = anEvent().withAssignedToUpdate(assignAssetToUser(user)).build();
		
		event.onUpdate();
		
		Assert.assertThat(event.getAssignedTo(), not(anIngoredAssignedToUpdate()));
	}
	
	private IgnoredAssignedTo anIngoredAssignedToUpdate() {
		return new IgnoredAssignedTo();
	}
	
	public class IgnoredAssignedTo extends TypeSafeMatcher<AssignedToUpdate> {

		@Override
		public boolean matchesSafely(AssignedToUpdate item) {
			return item.isIgnoredAssignment();
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("an ignored assign to update");
		}
		
	}
	
}
