package com.n4systems.model.criteria;

import com.n4systems.model.ButtonGroup;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.OneClickCriteriaBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CriteriaCleanerTest {
	private static final String displayText = "display_text";
	private static final ButtonGroup STATES = new ButtonGroup();
	private static final boolean principal = true;
	private static final boolean retired = false;
	private static final List<String> recommendations = Arrays.asList("rec 1", "rec 2");
	private static final List<String> deficiencies = Arrays.asList("def 1", "def 2");
	
	@Test
	public void clean_cleans_required_fields() {
		OneClickCriteria criteria = buildCriteria();
		Tenant newTenant = TenantBuilder.aTenant().build();
		
		assertNotNull(criteria.getId());
		assertNotNull(criteria.getCreated());
		assertNotNull(criteria.getModified());
		assertNotNull(criteria.getModifiedBy());
        assertNotNull(criteria.getTenant());

		CriteriaCleaner cleaner = new CriteriaCleaner(newTenant);
		cleaner.clean(criteria);
		
		assertNull(criteria.getId());
		assertNull(criteria.getCreated());
		assertNull(criteria.getModified());
		assertNull(criteria.getModifiedBy());
		
		assertSame(newTenant, criteria.getTenant());
		assertSame(displayText, criteria.getDisplayText());
		assertSame(STATES, criteria.getButtonGroup());
		assertSame(principal, criteria.isPrincipal());
		assertSame(retired, criteria.isRetired());
		
		// Rec's and Def's should be equal but NOT the same
		assertEquals(recommendations, criteria.getRecommendations());
		assertNotSame(recommendations, criteria.getRecommendations());
		assertEquals(deficiencies, criteria.getDeficiencies());
		assertNotSame(deficiencies, criteria.getDeficiencies());
	}

	private OneClickCriteria buildCriteria() {
		OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().build();
		criteria.setDisplayText(displayText);
		criteria.setButtonGroup(STATES);
		criteria.setPrincipal(principal);
		criteria.setRetired(retired);
		criteria.setRecommendations(recommendations);
		criteria.setDeficiencies(deficiencies);
        criteria.setTenant(TenantBuilder.aTenant().build());
        criteria.setModifiedBy(UserBuilder.aUser().build());
		return criteria;
	}
	
}
