package com.n4systems.fieldid.actions.search;

import static com.n4systems.fieldid.reporting.helpers.ColumnMappingBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;


public class AssignedToReportColumnFilterTest {

	
	private static final boolean ASSIGNED_TO_ENABLED = true;
	private static final boolean ASSIGNED_TO_DISABLED = false;
	private static final ExtendedFeature FEATURE_THAT_IS_NOT_ASSIGN_TO = ExtendedFeature.JobSites;

	@Test
	public void should_allow_all_mappings_through_when_assiged_to_is_enabled() throws Exception {
		AssignedToReportColumnFilter sut = new AssignedToReportColumnFilter(ASSIGNED_TO_ENABLED);
		
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(ExtendedFeature.AssignedTo).build()), is(true));
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(FEATURE_THAT_IS_NOT_ASSIGN_TO).build()), is(true));
		assertThat(sut.available(aColumnMapping().requiringNoExtendedFeature().build()), is(true));
	}
	
	
	@Test
	public void should_not_allow_mappings_requireing_assign_to_when_assign_to_is_disable() throws Exception {
		AssignedToReportColumnFilter sut = new AssignedToReportColumnFilter(ASSIGNED_TO_DISABLED);
		
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(ExtendedFeature.AssignedTo).build()), is(false));
	}
	
	@Test
	public void should_allow_mappings_requiring_a_different_extened_feature_from_assigned_to_when_assign_to_when_assign_to_is_disabled() throws Exception {
		AssignedToReportColumnFilter sut = new AssignedToReportColumnFilter(ASSIGNED_TO_DISABLED);
		
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(FEATURE_THAT_IS_NOT_ASSIGN_TO).build()), is(true));
	}
	
	@Test
	public void should_allow_mappings_does_not_require_any_extened_feature_from_assigned_to_when_assign_to_when_assign_to_is_disabled() throws Exception {
		AssignedToReportColumnFilter sut = new AssignedToReportColumnFilter(ASSIGNED_TO_DISABLED);
		
		assertThat(sut.available(aColumnMapping().requiringNoExtendedFeature().build()), is(true));
	}
}
