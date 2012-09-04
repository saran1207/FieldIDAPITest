package com.n4systems.fieldid.service.schedule;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.asset.ScheduleSummaryEntry;
import com.n4systems.model.builders.*;
import com.n4systems.services.SecurityContext;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;

import static org.easymock.EasyMock.*;

public class MassScheduleServiceTest extends FieldIdServiceTest {


    private @TestTarget MassScheduleService massScheduleService;
    private @TestMock ScheduleService scheduleService;
    private @TestMock PersistenceService persistenceService;
    private @TestMock SecurityContext securityContext;
    private Asset asset1;
    private Asset asset2;
    private Asset asset3;
    private Tenant tenant;
    private Project project;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        asset1 = AssetBuilder.anAsset().withId(1L).build();
        asset2 = AssetBuilder.anAsset().withId(1L).build();
        asset3 = AssetBuilder.anAsset().withId(1L).build();
        tenant = TenantBuilder.n4();
        project = new Project();
    }

    @Test
    public void test_performSchedules_no_schedules() {
        AssetType assetType = AssetTypeBuilder.anAssetType().named("assetType").build();
        ScheduleSummaryEntry scheduleSummaryEntry = new ScheduleSummaryEntryBuilder().withAssetType(assetType).withAssetIds(1L, 2L, 3L).build();
        // no schedules attached to this entry.
        List<ScheduleSummaryEntry> schedules = Lists.newArrayList(scheduleSummaryEntry);

        expect(persistenceService.find(Asset.class, 1L)).andReturn(asset1);
        expect(persistenceService.find(Asset.class, 2L)).andReturn(asset2);
        expect(persistenceService.find(Asset.class, 3L)).andReturn(asset3);

        replay(persistenceService);

        massScheduleService.performSchedules(schedules, false);

        verifyTestMocks();
    }

    @Test
    public void test_performSchedules_without_duplicate_detection() {
        AssetType assetType = AssetTypeBuilder.anAssetType().named("assetType").build();
        Event event = EventBuilder.anEvent().scheduledFor(jan1_2015.toDate()).build();
        ScheduleSummaryEntry scheduleSummaryEntry = new ScheduleSummaryEntryBuilder().withAssetType(assetType).withAssetIds(1L, 2L, 3L).withScheduledEvents(event).build();
        List<ScheduleSummaryEntry> schedules = Lists.newArrayList(scheduleSummaryEntry);

        expect(persistenceService.find(Asset.class, 1L)).andReturn(asset1);
        expect(persistenceService.find(Asset.class, 2L)).andReturn(asset2);
        expect(persistenceService.find(Asset.class, 3L)).andReturn(asset3);
        expect(persistenceService.save(anyObject(EventGroup.class))).andReturn(null).times(3);
        expect(persistenceService.save(anyObject(EventSchedule.class))).andReturn(null).times(3);

        replay(persistenceService);

        massScheduleService.performSchedules(schedules,  false);

        verifyTestMocks();
    }


    @Test
    public void test_performSchedules_with_duplicate_detection() {
        AssetType assetType = AssetTypeBuilder.anAssetType().named("assetType").build();
        Event event = EventBuilder.anEvent().scheduledFor(jan1_2015.toDate()).build();
        ScheduleSummaryEntry scheduleSummaryEntry = new ScheduleSummaryEntryBuilder().withAssetType(assetType).withAssetIds(1L, 2L, 3L).withScheduledEvents(event).build();
        List<ScheduleSummaryEntry> schedules = Lists.newArrayList(scheduleSummaryEntry);
        // NOTE : i specifically used Timestamps in test because that's what you should expect.  also, they cause a bug when comparisons are not handled carefully and this test will break if that code is changed.
        Event incompleteSchedule = EventBuilder.anEvent().scheduledFor(new Timestamp(jan1_2015.toDate().getTime())).build();
        List<Event> incompleteSchedules = Lists.newArrayList(incompleteSchedule);

        event.setProject(project);
        incompleteSchedule.setProject(project);
        incompleteSchedule.setType(event.getType());

        expect(persistenceService.find(Asset.class, 1L)).andReturn(asset1);
        expect(persistenceService.find(Asset.class, 2L)).andReturn(asset2);
        expect(persistenceService.find(Asset.class, 3L)).andReturn(asset3);
        expect(scheduleService.findIncompleteSchedulesForAsset(asset1)).andReturn(incompleteSchedules);
        expect(scheduleService.findIncompleteSchedulesForAsset(asset2)).andReturn(incompleteSchedules);
        expect(scheduleService.findIncompleteSchedulesForAsset(asset3)).andReturn(incompleteSchedules);

        replay(securityContext);
        replay(scheduleService);
        replay(persistenceService);

        massScheduleService.performSchedules(schedules,  true);

        verifyTestMocks();
    }


    @Override
    protected Object createSut(Field sutField) throws Exception {
        return new MassScheduleService() {
            @Override protected Tenant getCurrentTenant() {
                return tenant;
            }
        };
    }
}
