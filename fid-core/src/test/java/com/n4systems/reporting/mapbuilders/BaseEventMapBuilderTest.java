package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import org.easymock.EasyMock;
import org.junit.Test;


import com.n4systems.model.Event;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ReportMap;

public class BaseEventMapBuilderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAllFields() {
		MapBuilder<User> performedByMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<EventTypeGroup> typeGroupMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<InternalOrg> orgMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<BaseOrg> ownerMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<Event> scheduleMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<AssetStatus> assetStatusMapBuilder = EasyMock.createMock(MapBuilder.class);
		
		Transaction transaction = EasyMock.createMock(Transaction.class);
		
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		Event event = EventBuilder.anEvent().ofType(EventTypeBuilder.anEventType().build()).build();
		event.getType().setGroup(new EventTypeGroup());
		event.setOwner(OrgBuilder.aSecondaryOrg().build());
		event.setPerformedBy(UserBuilder.anEmployee().build());
		event.setAssetStatus(new AssetStatus());
		
		BaseEventMapBuilder builder = new BaseEventMapBuilder(performedByMapBuilder, typeGroupMapBuilder, orgMapBuilder, ownerMapBuilder, scheduleMapBuilder, assetStatusMapBuilder, new JobCertificateDataProducer());
		performedByMapBuilder.addParams(reportMap, event.getPerformedBy(), transaction);
		typeGroupMapBuilder.addParams(reportMap, event.getType().getGroup(), transaction);
		orgMapBuilder.addParams(reportMap, event.getPerformedBy().getOwner().getInternalOrg(), transaction);
		ownerMapBuilder.addParams(reportMap, event.getOwner(), transaction);
		scheduleMapBuilder.addParams(reportMap, event, transaction);
		assetStatusMapBuilder.addParams(reportMap, event.getAssetStatus(), transaction);
		
		EasyMock.replay(performedByMapBuilder);
		EasyMock.replay(typeGroupMapBuilder);
		EasyMock.replay(orgMapBuilder);
		EasyMock.replay(ownerMapBuilder);
		EasyMock.replay(scheduleMapBuilder);
		EasyMock.replay(assetStatusMapBuilder);
		
		builder.addParams(reportMap, event, transaction);
		
		EasyMock.verify(performedByMapBuilder);
		EasyMock.verify(typeGroupMapBuilder);
		EasyMock.verify(orgMapBuilder);
		EasyMock.verify(ownerMapBuilder);
		EasyMock.verify(scheduleMapBuilder);
		EasyMock.verify(assetStatusMapBuilder);
		
	}

}
