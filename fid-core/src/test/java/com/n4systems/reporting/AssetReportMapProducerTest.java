package com.n4systems.reporting;

import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.model.Asset;
import com.n4systems.util.DateTimeDefinition;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static com.n4systems.model.builders.AssetBuilder.anAsset;
import static com.n4systems.model.builders.UserBuilder.anEmployee;
import static com.n4systems.reporting.ReportMapEntryMatcher.hasReportEntry;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class AssetReportMapProducerTest {

	
	private static final DateTimeDefinition DEFAULT_DATE_TIME_DEFINITION = new DefaultedDateTimeDefiner();

	@Test
	public void should_be_make_assigned_user_to_the_users_first_name_and_last_name_available_when_asset_assinged() throws Exception {
		Asset asset = anAsset().assignedTo(anEmployee().withFirstName("first").withLastName("last").build()).build();
		
		AssetReportMapProducer sut = new AssetReportMapProducer(asset, createMockLastEventDateService(), DEFAULT_DATE_TIME_DEFINITION, null);
		
		Map<String, Object> reportMap = sut.produceMap();
		
		assertThat(reportMap, hasReportEntry(equalTo("assignedUserName"), equalTo((Object)"first last")));
	}
	
	@Test
	public void should_be_make_assigned_user_unassigned_when_asset_is_unassigned() throws Exception {
		Asset asset = anAsset().unassigned().build();
		
		AssetReportMapProducer sut = new AssetReportMapProducer(asset, createMockLastEventDateService(), DEFAULT_DATE_TIME_DEFINITION, null);
		
		Map<String, Object> reportMap = sut.produceMap();
		
		assertThat(reportMap, hasReportEntry(equalTo("assignedUserName"), equalTo((Object)"Unassigned")));
	}


    private LastEventDateService createMockLastEventDateService() {
        Date date = new Date();
        LastEventDateService mockService = createMock(LastEventDateService.class);
        expect(mockService.findLastEventDate(anyObject(Asset.class))).andReturn(date);
        return mockService;
    }

	
	
}
