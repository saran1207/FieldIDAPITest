package com.n4systems.reporting.mapbuilders;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.Transaction;

public class BaseInspectionMapBuilder extends AbstractMapBuilder<Inspection> {
	private final MapBuilder<User> performedByMapBuilder;
	private final MapBuilder<InspectionTypeGroup> typeGroupMapBuilder;
	private final MapBuilder<InternalOrg> orgMapBuilder;
	private final MapBuilder<BaseOrg> ownerMapBuilder;
	private final MapBuilder<Inspection> scheduleMapBuilder;
	private final MapBuilder<ProductStatusBean> productStatusMapBuilder;
	private final JobCertificateDataProducer jobCertificateDataProducer;
	
	public BaseInspectionMapBuilder(MapBuilder<User> performedByMapBuilder, MapBuilder<InspectionTypeGroup> typeGroupMapBuilder, MapBuilder<InternalOrg> orgMapBuilder, MapBuilder<BaseOrg> ownerMapBuilder, MapBuilder<Inspection> scheduleMapBuilder, MapBuilder<ProductStatusBean> productStatusMapBuilder
			, JobCertificateDataProducer jobCertificateDataProducer) {
		this.performedByMapBuilder = performedByMapBuilder;
		this.typeGroupMapBuilder = typeGroupMapBuilder;
		this.orgMapBuilder = orgMapBuilder;
		this.ownerMapBuilder = ownerMapBuilder;
		this.scheduleMapBuilder = scheduleMapBuilder;
		this.productStatusMapBuilder = productStatusMapBuilder;
		this.jobCertificateDataProducer = jobCertificateDataProducer;
	}
	
	public BaseInspectionMapBuilder(DateTimeDefiner dateDefiner) {
		this(
			new PerformedByMapBuilder(),
			new InspectionTypeGroupMapBuilder(),
			new OrganizationMapBuilder(),
			new OwnerMapBuilder(),
			new InspectionScheduleMapBuilder(dateDefiner),
			new ProductStatusMapBuilder(),
			new JobCertificateDataProducer()
		);
	}
	
	@Override
	protected void setAllFields(Inspection entity, Transaction transaction) {
		setAllFields(performedByMapBuilder, entity.getInspector(), transaction);
		setAllFields(typeGroupMapBuilder, entity.getType().getGroup(), transaction);
		setAllFields(orgMapBuilder, entity.getInspector().getOwner().getInternalOrg(), transaction);
		setAllFields(ownerMapBuilder, entity.getOwner(), transaction);
		setAllFields(productStatusMapBuilder, entity.getProductStatus(), transaction);
		setAllFields(scheduleMapBuilder, entity, transaction);
		
		
		if (entity.getSchedule() != null)
			setAllFields(jobCertificateDataProducer, entity.getSchedule().getProject(), transaction);
	}


}
