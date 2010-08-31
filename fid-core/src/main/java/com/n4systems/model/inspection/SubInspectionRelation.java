package com.n4systems.model.inspection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;

@Entity
@Table(name="inspectionsmaster_inspectionssub")
public class SubInspectionRelation {

	@SuppressWarnings("unused")
	@Id
	@Column(nullable=false, insertable=false, updatable=false, unique=true)
	private Long subinspections_inspection_id;
	
	@OneToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="subinspections_inspection_id", nullable=false, insertable=false, updatable=false, unique=true)	
	private SubInspection subInspection;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="inspectionsmaster_inspection_id", nullable=false, insertable=false, updatable=false)
	private Inspection masterInspection;
	
	@Column(name="orderidx", nullable=false, insertable=false, updatable=false)
	private Integer orderidx;
	
	public SubInspectionRelation() {}

	@PrePersist
	@PreUpdate
	@PreRemove
	public void onPersistUpdateRemove() {
		throw new UnsupportedOperationException("This entity is read-only");
	}
	
	public Inspection getMasterInspection() {
		return masterInspection;
	}

	public void setMasterInspection(Inspection masterInspection) {
		this.masterInspection = masterInspection;
	}

	public SubInspection getSubInspection() {
		return subInspection;
	}

	public void setSubInspection(SubInspection subInspection) {
		this.subInspection = subInspection;
	}

	public Integer getOrderidx() {
		return orderidx;
	}

	public void setOrderidx(Integer orderidx) {
		this.orderidx = orderidx;
	}
}
