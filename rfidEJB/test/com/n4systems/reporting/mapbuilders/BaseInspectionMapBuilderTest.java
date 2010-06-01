package com.n4systems.reporting.mapbuilders;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.builders.InspectionBuilder;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ReportMap;

public class BaseInspectionMapBuilderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAllFields() {
		MapBuilder<User> performedByMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<InspectionTypeGroup> typeGroupMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<InternalOrg> orgMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<BaseOrg> ownerMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<Inspection> scheduleMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<ProductStatusBean> productStatusMapBuilder = EasyMock.createMock(MapBuilder.class);
		
		Transaction transaction = EasyMock.createMock(Transaction.class);
		
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		Inspection inspection = InspectionBuilder.anInspection().ofType(InspectionTypeBuilder.anInspectionType().build()).build();
		inspection.getType().setGroup(new InspectionTypeGroup());
		inspection.setOwner(OrgBuilder.aSecondaryOrg().build());
		inspection.setPerformedBy(UserBuilder.anEmployee().build());
		inspection.setProductStatus(new ProductStatusBean());
		
		BaseInspectionMapBuilder builder = new BaseInspectionMapBuilder(performedByMapBuilder, typeGroupMapBuilder, orgMapBuilder, ownerMapBuilder, scheduleMapBuilder, productStatusMapBuilder, new JobCertificateDataProducer());
		performedByMapBuilder.addParams(reportMap, inspection.getPerformedBy(), transaction);
		typeGroupMapBuilder.addParams(reportMap, inspection.getType().getGroup(), transaction);
		orgMapBuilder.addParams(reportMap, inspection.getPerformedBy().getOwner().getInternalOrg(), transaction);
		ownerMapBuilder.addParams(reportMap, inspection.getOwner(), transaction);
		scheduleMapBuilder.addParams(reportMap, inspection, transaction);
		productStatusMapBuilder.addParams(reportMap, inspection.getProductStatus(), transaction);
		
		EasyMock.replay(performedByMapBuilder);
		EasyMock.replay(typeGroupMapBuilder);
		EasyMock.replay(orgMapBuilder);
		EasyMock.replay(ownerMapBuilder);
		EasyMock.replay(scheduleMapBuilder);
		EasyMock.replay(productStatusMapBuilder);
		
		builder.addParams(reportMap, inspection, transaction);
		
		EasyMock.verify(performedByMapBuilder);
		EasyMock.verify(typeGroupMapBuilder);
		EasyMock.verify(orgMapBuilder);
		EasyMock.verify(ownerMapBuilder);
		EasyMock.verify(scheduleMapBuilder);
		EasyMock.verify(productStatusMapBuilder);
		
	}

}
