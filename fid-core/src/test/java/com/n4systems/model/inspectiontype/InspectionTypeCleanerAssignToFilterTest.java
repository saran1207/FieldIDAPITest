package com.n4systems.model.inspectiontype;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.permissions.AlwaysOffSystemSecurityGuardTestDouble;
import com.n4systems.model.InspectionType;


public class InspectionTypeCleanerAssignToFilterTest {

	
	
	
	
	private final class AssignToOnSecurityGuard extends AlwaysOffSystemSecurityGuardTestDouble {
		@Override
		public boolean isAssignedToEnabled() {
			return true;
		}
	}

	@Test
	public void should_keep_assigned_to_available_if_the_target_tenant_does_not_have_assigned_to_enabled() throws Exception {
		InspectionType inspectionType = anInspectionType().withAssignedToAvailable().build();
		
		InspectionTypeCleanerAssignToFilter sut = new InspectionTypeCleanerAssignToFilter(new AssignToOnSecurityGuard());
		sut.clean(inspectionType);
		
		assertThat(inspectionType, hasProperty("assignedToAvailable", is(true)));
	}
	
	@Test
	public void should_keep_assigned_to_available_not_available_if_the_target_tenant_does_not_have_assigned_to_enabled() throws Exception {
		InspectionType inspectionType = anInspectionType().withAssignedToNotAvailable().build();
		
		InspectionTypeCleanerAssignToFilter sut = new InspectionTypeCleanerAssignToFilter(new AssignToOnSecurityGuard());
		sut.clean(inspectionType);
		
		assertThat(inspectionType, hasProperty("assignedToAvailable", is(false)));
	}
	
	
	
	@Test
	public void should_turn_off_assigned_to_available_if_the_target_tenant_does_not_have_assigned_to_is_disabled() throws Exception {
		InspectionType inspectionType = anInspectionType().withAssignedToAvailable().build();
		
		InspectionTypeCleanerAssignToFilter sut = new InspectionTypeCleanerAssignToFilter(new AlwaysOffSystemSecurityGuardTestDouble());
		sut.clean(inspectionType);
		
		assertThat(inspectionType, hasProperty("assignedToAvailable", is(false)));
	}
	
	@Test
	public void should_turn_off_assigned_to_available_not_available_if_the_target_tenant_does_not_have_assigned_to_is_disabled() throws Exception {
		InspectionType inspectionType = anInspectionType().withAssignedToNotAvailable().build();
		
		InspectionTypeCleanerAssignToFilter sut = new InspectionTypeCleanerAssignToFilter(new AlwaysOffSystemSecurityGuardTestDouble());
		sut.clean(inspectionType);
		
		assertThat(inspectionType, hasProperty("assignedToAvailable", is(false)));
	}
	
}
