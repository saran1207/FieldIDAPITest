package com.n4systems.api.conversion.inspection;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.api.model.InspectionView;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.Status;
import com.n4systems.model.inspectionbook.InspectionBookFindOrCreateLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.product.SmartSearchLoader;
import com.n4systems.model.productstatus.ProductStatusByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.persistence.Transaction;

public class InspectionToModelConverter implements ViewToModelConverter<Inspection, InspectionView> {
	private final OrgByNameLoader orgLoader;
	private final SmartSearchLoader productLoader;
	private final ProductStatusByNameLoader productStatusLoader;
	private final InspectionBookFindOrCreateLoader inspectionBookLoader;
	private final UserByFullNameLoader userLoader;
	
	private InspectionType type;
	
	public InspectionToModelConverter(OrgByNameLoader orgLoader, SmartSearchLoader productLoader, ProductStatusByNameLoader productStatusLoader, InspectionBookFindOrCreateLoader inspectionBookLoader, UserByFullNameLoader userLoader) {
		super();
		this.orgLoader = orgLoader;
		this.productLoader = productLoader;
		this.productStatusLoader = productStatusLoader;
		this.inspectionBookLoader = inspectionBookLoader;
		this.userLoader = userLoader;
	}

	@Override
	public Inspection toModel(InspectionView view, Transaction transaction) throws ConversionException {
		Inspection model = new Inspection();
		
		resolveType(model);
		resolveOwner(view, model, transaction);
		
		model.setLocation(view.getLocation());
		model.setDate(view.getInspectionDateAsDate());
		model.setComments(view.getComments());
		
		resolveStatus(view.getStatus(), model);
		resolveProduct(view, model, transaction);
		resolvePrintable(view, model);
		resolvePerformedBy(view, model, transaction);
		resolveInspectionBook(view, model, transaction);
		
		resolveProductStatus(view, model, transaction);
		
		return model;
	}

	protected void resolveType(Inspection model) {
		model.setType(type);
		model.setFormVersion(type.getFormVersion());
		model.setTenant(type.getTenant());
	}
	
	protected void resolveStatus(String statusName, Inspection model) {
		String cleanStatus = statusName.toUpperCase();
		
		Status status = Status.NA;
		if (cleanStatus.equals("PASS")) {
			status = Status.PASS;
		} else if (cleanStatus.equals("FAIL")) {
			status = Status.FAIL;
		}
		
		model.setStatus(status);
	}

	protected void resolveProduct(InspectionView view, Inspection model, Transaction transaction) {
		// the validator will ensure this returns exactly 1 product
		Product product = productLoader.setSearchText(view.getIdentifier()).load(transaction).get(0);
		model.setProduct(product);
	}

	protected void resolvePerformedBy(InspectionView view, Inspection model, Transaction transaction) {
		// the validator will ensure this returns exactly 1 user
		User performedBy = userLoader.setFullName(view.getPerformedBy()).load(transaction).get(0);
		model.setPerformedBy(performedBy);
	}

	protected void resolvePrintable(InspectionView view, Inspection model) {
		if (view.isPrintable() != null) {
			model.setPrintable(view.isPrintable());
		} else {
			model.setPrintable(type.isPrintable());
		}
	}

	protected void resolveInspectionBook(InspectionView view, Inspection model, Transaction transaction) {
		if (view.getInspectionBook() != null) {
			inspectionBookLoader.setName(view.getInspectionBook());
			inspectionBookLoader.setOwner(model.getOwner());
			InspectionBook book = inspectionBookLoader.load(transaction);
			model.setBook(book);
		}
	}

	protected void resolveProductStatus(InspectionView view, Inspection model, Transaction transaction) {
		if (view.getProductStatus() != null) {
			ProductStatusBean status = productStatusLoader.setName(view.getProductStatus()).load(transaction);
			model.setProductStatus(status);
		}
	}

	protected void resolveOwner(InspectionView view, Inspection model, Transaction transaction) {
		orgLoader.setOrganizationName(view.getOrganization());
		orgLoader.setCustomerName(view.getCustomer());
		orgLoader.setDivision(view.getDivision());
		
		BaseOrg owner = orgLoader.load(transaction);
		model.setOwner(owner);
	}

	public void setType(InspectionType type) {
		this.type = type;
	}
	
	public InspectionType getType() {
		return type;
	}
}
