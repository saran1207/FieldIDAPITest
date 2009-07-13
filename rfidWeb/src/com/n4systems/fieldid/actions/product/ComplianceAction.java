package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.compliance.ComplianceRisk;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.utils.PlainDate;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Compliance)
public class ComplianceAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final InspectionManager inspectionManager;
	private final InspectionScheduleManager inspectionScheduleManager;
	private final ProductManager productManager;
	
	private Product product;
	private Long uniqueID;
	private List<InspectionSchedule> availableSchedules;
	private Boolean complianceCheck;
	private List<ComplianceRisk> complianceRisks;
	
	
	
	public ComplianceAction(PersistenceManager persistenceManager, InspectionManager inspectionManager, InspectionScheduleManager inspectionScheduleManager, ProductManager productManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.inspectionScheduleManager = inspectionScheduleManager;
		this.productManager = productManager;
	}

	private void loadMemberFields(Long uniqueId) {
		product = productManager.findProductAllFields(uniqueId, getSecurityFilter());
	}
	
	@SkipValidation
	public String doShow() {
		if (uniqueID != null) {
			loadMemberFields(uniqueID);
		}
		if (product == null) {
			addActionErrorText("error.noproduct");
		}
		Date lastInspDate = null;
		availableSchedules = inspectionScheduleManager.getAvailableSchedulesFor(product);
		
		
		if (availableSchedules.isEmpty()) {
			complianceCheck = null;
		} else {
			
			complianceCheck = true;
			for (InspectionSchedule schedule : availableSchedules) {
				if (schedule.isPastDue()) {
					complianceCheck = false;
					break;
				}
			}
			complianceRisks = new ArrayList<ComplianceRisk>();
			for (InspectionSchedule schedule : availableSchedules) {
				// find the last inspection date for this product and inspection
				// type
				lastInspDate = inspectionManager.findLastInspectionDate(schedule);
	
				// if there are no inspections, set the last date to the date
				// the product was identified.
				if (lastInspDate == null) {
					lastInspDate = product.getIdentified();
				}
	
				complianceRisks.add(new ComplianceRisk(schedule, new PlainDate(lastInspDate)));
			}
		}

		
		return SUCCESS;
	}


	public Product getProduct() {
		return product;
	}
	
	public Boolean getComplianceCheck() {
		return complianceCheck;
	}

	public List<ComplianceRisk> getComplianceRisks() {

		return complianceRisks;
	}

	public Float getRiskPercent() {
		return (float) Math.floor(getRisk().getCurrentRisk()) / 100f;
	}

	public ComplianceRisk getRisk() {
		ComplianceRisk highestRisk = null;
		for (ComplianceRisk risk : complianceRisks) {
			if (highestRisk == null || highestRisk.getComplianceDate().after(risk.getComplianceDate())) {
				highestRisk = risk;
			}
		}
		return highestRisk;
	}

	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

}
