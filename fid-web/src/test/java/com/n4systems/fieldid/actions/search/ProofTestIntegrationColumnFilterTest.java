package com.n4systems.fieldid.actions.search;

import static com.n4systems.fieldid.reporting.helpers.ColumnMappingBuilder.aColumnMapping;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;

public class ProofTestIntegrationColumnFilterTest {

	private static final boolean PROOFTEST_ENABLED = true;
	private static final boolean PROOFTEST_DISABLED = false;
	private static final ExtendedFeature FEATURE_THAT_IS_NOT_PROOF_TEST_INTEGRATION = ExtendedFeature.JobSites;
	
	@Test
	public void should_allow_all_mappings_through_when_proof_test_integratoin_is_enabled() throws Exception {
		ProofTestIntegrationReportColumnFilter sut = new ProofTestIntegrationReportColumnFilter(PROOFTEST_ENABLED);
		
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(ExtendedFeature.ProofTestIntegration).build()), is(true));
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(FEATURE_THAT_IS_NOT_PROOF_TEST_INTEGRATION).build()), is(true));
		assertThat(sut.available(aColumnMapping().requiringNoExtendedFeature().build()), is(true));
	}
	
	@Test
	public void should_not_allow_mappings_requiring_proof_test_integration_when_proof_test_integration_is_disable() throws Exception {
		ProofTestIntegrationReportColumnFilter sut = new ProofTestIntegrationReportColumnFilter(PROOFTEST_DISABLED);
		
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(ExtendedFeature.ProofTestIntegration).build()), is(false));
	}
	
	@Test
	public void should_allow_mappings_requiring_a_different_extened_feature_from_when_proof_test_integration_when_when_proof_test_integration_is_disabled() throws Exception {
		ProofTestIntegrationReportColumnFilter sut = new ProofTestIntegrationReportColumnFilter(PROOFTEST_DISABLED);
		
		assertThat(sut.available(aColumnMapping().requiringExtendedFeature(FEATURE_THAT_IS_NOT_PROOF_TEST_INTEGRATION).build()), is(true));
	}
	
	@Test
	public void should_allow_mappings_does_not_require_any_extened_feature_from_proof_test_when_proof_test_is_disabled() throws Exception {
		ProofTestIntegrationReportColumnFilter sut = new ProofTestIntegrationReportColumnFilter(PROOFTEST_DISABLED);
		
		assertThat(sut.available(aColumnMapping().requiringNoExtendedFeature().build()), is(true));
	}
}
