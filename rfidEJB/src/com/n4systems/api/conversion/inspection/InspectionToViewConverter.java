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
		
		view.setComments(model.getComments());
		view.setInspectionDate(model.getDate());
		view.setLocation(model.getLocation());
		view.setPrintable(model.isPrintable());
		view.setInspector(model.getInspector().getFullName());
		view.setIdentifier(model.getProduct().getSerialNumber());
		view.setStatus(model.getStatus().getDisplayName());
		
		convertOwnerFields(model.getOwner(), view);
		convertBook(model, view);
		convertProductStatus(model, view);
		convertNextDate(model, view);
		
		return view;
	}
	
	private void convertNextDate(Inspection model, InspectionView view) {
		Date nextDate = nextDateLoader.setInspection(model).load();
		view.setNextInspectionDate(nextDate);
	}

	private void convertBook(Inspection model, InspectionView view) {
		if (model.getBook() != null) {
			view.setInspectionBook(model.getBook().getName());
		}
	}

	private void convertProductStatus(Inspection model, InspectionView view) {
		if (model.getProductStatus() != null) {
			view.setProductStatus(model.getProductStatus().getName());
		}
	}

	private void convertOwnerFields(BaseOrg owner, InspectionView view) {
		view.setOrganization(owner.getInternalOrg().getName());
		
		if (owner.isExternal()) {
			view.setCustomer(owner.getCustomerOrg().getName());
		
			if (owner.isDivision()) {
				view.setDivision(owner.getDivisionOrg().getName());
			}
		}
	}
	
}
