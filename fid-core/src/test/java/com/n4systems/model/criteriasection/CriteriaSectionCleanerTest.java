package com.n4systems.model.criteriasection;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.builders.CriteriaSectionBuilder;
import com.n4systems.model.builders.OneClickCriteriaBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CriteriaSectionCleanerTest {
	private static final String title = "title";
	private static final boolean retired = false; 
	private static List<Criteria> criteria;
	
	@Before
	public void setupCriteria() {
		criteria = Arrays.<Criteria>asList(
					OneClickCriteriaBuilder.aCriteria().build(),
					OneClickCriteriaBuilder.aCriteria().withRetired(true).build(),
					OneClickCriteriaBuilder.aCriteria().build()
				);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void clean_cleans_required_fields() {
		CriteriaSection section = buildSection();
		Tenant newTenant = TenantBuilder.aTenant().build();

		assertNotNull(section.getId());
		assertNotNull(section.getCreated());
		assertNotNull(section.getModified());
		assertNotNull(section.getModifiedBy());
		
		Cleaner<Criteria> criteriaCleaner = EasyMock.createMock(Cleaner.class);
		criteriaCleaner.clean(criteria.get(0));
		criteriaCleaner.clean(criteria.get(2));
		EasyMock.replay(criteriaCleaner);
		
		CriteriaSectionCleaner cleaner = new CriteriaSectionCleaner(newTenant, criteriaCleaner);
		cleaner.clean(section);
		
		assertNull(section.getId());
		assertNull(section.getCreated());
		assertNull(section.getModified());
		assertNull(section.getModifiedBy());
		
		assertSame(newTenant, section.getTenant());
		assertSame(title, section.getTitle());
		assertSame(retired, section.isRetired());
		
		assertEquals(2, section.getCriteria().size());
		
		EasyMock.verify(criteriaCleaner);
	}
	
	private CriteriaSection buildSection() {
		CriteriaSection section = CriteriaSectionBuilder.aCriteriaSection().withTitle(title).withRetired(retired).withCriteria(criteria).build();
        section.setModifiedBy(UserBuilder.aUser().build());
		return section;
	}
	
}
