package com.n4systems.fieldid.service.event;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.FieldIdServicesUnitTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.reporting.WorkSummaryRecord;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.persistence.QueryBuilder;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

public class EventServiceTest extends FieldIdServicesUnitTest {

    private @TestTarget EventService eventService;
    private @TestMock PersistenceService persistenceService;
    private @TestMock SecurityContext securityContext;
    private AssetType assetType;
    private EventType eventType;
    private User user;
    private BaseOrg org;
    private UserSecurityFilter securityFilter;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        assetType = AssetTypeBuilder.anAssetType().build();
        eventType = EventTypeBuilder.anEventType().build();
        user = UserBuilder.aFullUser().build();
        org = OrgBuilder.aPrimaryOrg().build();
        securityFilter = new UserSecurityFilter(user);
    }

    @Test
    public void test_getMonthlyWorkSummary() {
        LocalDate dayInMonth = jan1_2011;
        WorkSummaryRecord day3 = new WorkSummaryRecord(100L, jan1_2011.plusDays(2).toDate());
        WorkSummaryRecord day5 = new WorkSummaryRecord(500L, jan1_2011.plusDays(4).toDate());
        WorkSummaryRecord day17 = new WorkSummaryRecord(1700L, jan1_2011.plusDays(16).toDate());
        WorkSummaryRecord day29 = new WorkSummaryRecord(2900L, jan1_2011.plusDays(28).toDate());
        List<WorkSummaryRecord> data = Lists.newArrayList(day3, day5, day17, day29);

        expect(securityContext.getUserSecurityFilter()).andReturn(securityFilter);
//        expectLastCall().times(2);
        replay(securityContext);
        expect(persistenceService.findAll(anyObject(QueryBuilder.class))).andReturn(data);
        replay(persistenceService);

        Map<LocalDate, Long> result = eventService.getMontlyWorkSummary(dayInMonth, user, org, assetType, eventType);

        // recall we are looking at weeks that start on sunday.  so we'll need Dec 26-Feb5  = 6+31+5
        assert(result.size()==31 + 6 + 5);
        for (LocalDate date:result.keySet()) {
            Long value = result.get(date);
            Long expected = getExpectedValueForDate(date,data);  // should be padded as 0 for dates with no values.
            assertEquals(expected,value);
        }
    }

    @Test
    public void test_Work() {
        //getWork(DateRange dateRange, User user, BaseOrg org, AssetType assetType, EventType eventType, int limit) {

    }

    private Long getExpectedValueForDate(LocalDate date, List<WorkSummaryRecord> data) {
        for (WorkSummaryRecord record:data) {
            if (record.getDate().equals(date.toDate())) {
                return record.getCount();
            }
        }
        return 0L;
    }




}
