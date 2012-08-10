package com.n4systems.reporting;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.reporting.ReportMapEntryMatcher.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.n4systems.model.Asset;
import com.n4systems.util.DateTimeDefinition;


public class AssetReportMapProducerTest {

	
	private static final DateTimeDefinition DEFAULT_DATE_TIME_DEFINITION = new DefaultedDateTimeDefiner();

	@Test
	public void should_be_make_assigned_user_to_the_users_first_name_and_last_name_available_when_asset_assinged() throws Exception {
		Asset asset = anAsset().assignedTo(anEmployee().withFirstName("first").withLastName("last").build()).build();
		
		AssetReportMapProducer sut = new AssetReportMapProducer(asset, DEFAULT_DATE_TIME_DEFINITION, null);
		
		Map<String, Object> reportMap = sut.produceMap();
		
		assertThat(reportMap, hasReportEntry(equalTo("assignedUserName"), equalTo((Object)"first last")));
	}
	
	@Test
	public void should_be_make_assigned_user_unassigned_when_asset_is_unassigned() throws Exception {
		Asset asset = anAsset().unassigned().build();
		
		AssetReportMapProducer sut = new AssetReportMapProducer(asset, DEFAULT_DATE_TIME_DEFINITION, null);
		
		Map<String, Object> reportMap = sut.produceMap();
		
		assertThat(reportMap, hasReportEntry(equalTo("assignedUserName"), equalTo((Object)"Unassigned")));
	}
	
	
}
