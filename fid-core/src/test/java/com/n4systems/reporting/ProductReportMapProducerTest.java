package com.n4systems.reporting;

import static com.n4systems.model.builders.ProductBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.reporting.ReportMapEntryMatcher.*;
import static com.n4systems.reporting.mapbuilders.ReportField.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.ReportMap;


public class ProductReportMapProducerTest {

	
	private static final DateTimeDefinition DEFAULT_DATE_TIME_DEFINITION = new DefaultedDateTimeDefiner();

	@Test
	public void should_be_make_assigned_user_to_the_users_first_name_and_last_name_available_when_asset_assinged() throws Exception {
		Product product = aProduct().assignedTo(anEmployee().withFirstName("first").withLastName("last").build()).build();
		
		ProductReportMapProducer sut = new ProductReportMapProducer(product, DEFAULT_DATE_TIME_DEFINITION);
		
		ReportMap<Object> reportMap = sut.produceMap();
		
		assertThat(reportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object)"first last")));
	}
	
	@Test
	public void should_be_make_assigned_user_unassigned_when_asset_is_unassigned() throws Exception {
		Product product = aProduct().unassigned().build();
		
		ProductReportMapProducer sut = new ProductReportMapProducer(product, DEFAULT_DATE_TIME_DEFINITION);
		
		ReportMap<Object> reportMap = sut.produceMap();
		
		assertThat(reportMap, hasReportEntry(equalTo(ASSIGNED_USER.getParamKey()), equalTo((Object)"Unassigned")));
	}
	
	
}
