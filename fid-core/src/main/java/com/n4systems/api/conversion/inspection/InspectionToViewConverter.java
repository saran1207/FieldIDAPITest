package com.n4systems.api.conversion.inspection;

import java.util.Date;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.InspectionView;
import com.n4systems.model.Inspection;
import com.n4systems.model.inspectionschedule.NextInspectionDateByInspectionLoader;
import com.n4systems.model.orgs.BaseOrg;

public class InspectionToViewConverter implements ModelToViewConverter<Inspection, InspectionView> {
	private final NextInspectionDateByInspectionLoader nextDateLoader;
	
	public InspectionToViewConverter(NextInspectionDateByInspectionLoader nextDateLoader) {
		this.nextDateLoader = nextDateLoader;
	}
	
	@Override
	public InspectionView toView(Inspection model) throws ConversionException {
		InspectionView view = new InspectionView();
		
		convertDirectFields(model, view);
		converterPerformedBy(model, view);
		convertAssetIdentifier(model, view);
		convertInspectionStatus(model, view);
		convertOwnerFields(model.getOwner(), view);
		convertBook(model, view);
		convertAssetStatus(model, view);
		convertNextDate(model, view);
		
		return view;
	}

	protected void convertDirectFields(Inspection model, InspectionView view) {
		view.setComments(model.getComments());
		view.setDatePerformed(model.getDate());
		view.setLocation(model.getAdvancedLocation().getFreeformLocation());
		view.setPrintable(model.isPrintable());
	}

	protected void convertInspectionStatus(Inspection model, InspectionView view) {
		view.setStatus(model.getStatus().getDisplayName());
	}

	protected void convertAssetIdentifier(Inspection model, InspectionView view) {
		view.setIdentifier(model.getAsset().getSerialNumber());
	}

	protected void converterPerformedBy(Inspection model, InspectionView view) {
		view.setPerformedBy(model.getPerformedBy().getFullName());
	}
	
	protected void convertNextDate(Inspection model, InspectionView view) {
		Date nextDate = nextDateLoader.setInspection(model).load();
		view.setNextInspectionDate(nextDate);
	}

	protected void convertBook(Inspection model, InspectionView view) {
		if (model.getBook() != null) {
			view.setInspectionBook(model.getBook().getName());
		}
	}

	protected void convertAssetStatus(Inspection model, InspectionView view) {
		if (model.getAssetStatus() != null) {
			view.setAssetStatus(model.getAssetStatus().getName());
		}
	}

	protected void convertOwnerFields(BaseOrg owner, InspectionView view) {
		view.setOrganization(owner.getInternalOrg().getName());
		
		if (owner.isExternal()) {
			view.setCustomer(owner.getCustomerOrg().getName());
		
			if (owner.isDivision()) {
				view.setDivision(owner.getDivisionOrg().getName());
			}
		}
	}
	
}
