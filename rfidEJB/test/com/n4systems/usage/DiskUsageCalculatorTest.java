package com.n4systems.usage;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.model.TenantOrganization;
import static org.junit.Assert.*;

import static com.n4systems.model.builders.TenantBuilder.*;



public class DiskUsageCalculatorTest {

	@Test
	public void should_count_user_images_tenant() {
		TenantOrganization tenant = aManufacturer("unirope");
		List<TenantOrganization> tenants = new ArrayList<TenantOrganization>();
		tenants.add(tenant);
		
		DiskUsageCalculator sut = new DiskUsageCalculator(tenants);
		List<TenantDiskUsageSummary> summaries = sut.calculateUsages();
		
		assertTrue(0 < summaries.iterator().next().getUserUsage().getNumberOfFiles());
		assertTrue(0 < summaries.iterator().next().getUserUsage().getTotalSize());
	}
}
