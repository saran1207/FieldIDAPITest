package com.n4systems.model.inspectiontype;

import javax.persistence.EntityManager;

import org.junit.Test;
import static org.junit.Assert.*;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.DummyEntityManager;

public class InspectionTypeUniqueNameLoaderTest {

	private class TestInspectionTypeUniqueNameLoader extends InspectionTypeUniqueNameLoader {
		private final int conflictingNameMax;
		private int conflictingNameCount = 0;
		
		public TestInspectionTypeUniqueNameLoader(SecurityFilter filter, int conflictingNameMax) {
			super(filter);
			this.conflictingNameMax = conflictingNameMax;
		}

		@Override
		protected boolean typeWithSameNameExists(EntityManager em, SecurityFilter filter, String name) {
			conflictingNameCount++;
			return (conflictingNameCount < conflictingNameMax);
		}
	}
	
	@Test
	public void test_load_multiple_conflicts() {
		String origName = "Inspection Type Name";
		
		TestInspectionTypeUniqueNameLoader loader = new TestInspectionTypeUniqueNameLoader(null, 5);
		loader.setName(origName);
		
		assertEquals(origName + " - 5", loader.load(new DummyEntityManager(), new OpenSecurityFilter()));
	}
	
	@Test
	public void test_load_no_conflicts() {
		String origName = "Inspection Type Name";
		
		TestInspectionTypeUniqueNameLoader loader = new TestInspectionTypeUniqueNameLoader(null, 0);
		loader.setName(origName);
		
		assertEquals(origName + " - 1", loader.load(new DummyEntityManager(), new OpenSecurityFilter()));
	}
	
	@Test(expected=RuntimeException.class)
	public void test_load_throws_exception_on_too_many_conflicts() {
		TestInspectionTypeUniqueNameLoader loader = new TestInspectionTypeUniqueNameLoader(null, 501);
		loader.setName("Inspection Type Name");
		loader.load(new DummyEntityManager(), new OpenSecurityFilter());
	}
	
}
