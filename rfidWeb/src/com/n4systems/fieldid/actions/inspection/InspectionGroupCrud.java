package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;

public class InspectionGroupCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(InspectionGroupCrud.class);

	private InspectionGroup inspectionGroup;
	private Product product;
	private ProductType productType;

	private List<Product> products;

	private List<InspectionGroup> inspectionGroups;

	private String search;

	private InspectionManager inspectionManager;
	private LegacyProductType productTypeManager;
	private ProductManager productManager;

	public InspectionGroupCrud(InspectionManager inspectionManager, LegacyProductType productTypeManager, ProductManager productManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.productTypeManager = productTypeManager;
		this.productManager = productManager;
	}

	@Override
	protected void initMemberFields() {

	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		product = productManager.findProduct(uniqueId, getSecurityFilter());
	}

	public String doList() {
		// if no search param came just show the form.
		if (search != null && search.length() > 0) {
			try {
				products = productManager.findProductByIdentifiers(getSecurityFilter(), search);
				// if there is only one forward. directly to the group view
				// screen.
				if (products.size() == 1) {
					product = products.get(0);
					uniqueID = product.getId();
					return "oneFound";
				}
			} catch (Exception e) {
				logger.error("Failed to look up Products", e);
				addActionError(getText("error.failedtoload"));
				return ERROR;
			}

		}

		return SUCCESS;
	}

	public String doShow() {
		if (product == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		inspectionGroups = inspectionManager.findAllInspectionGroups(getSecurityFilter(), uniqueID, "inspections");
		return SUCCESS;
	}

	public String doDelete() {
		return SUCCESS;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		if (search != null) {
			search = search.trim();
		}
		this.search = search;
	}

	public InspectionGroup getInspectionGroup() {
		return inspectionGroup;
	}

	public List<Product> getProducts() {
		return products;
	}

	public List<InspectionGroup> getInspectionGroups() {
		return inspectionGroups;
	}

	public List<InspectionType> getInspectionTypes(Long productTypeId) {
		List<InspectionType> inspectionTypes = new ArrayList<InspectionType>();
		List<AssociatedInspectionType> associatedInspectionTypes = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(getProduct().getType()).load();
		for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes) {
			inspectionTypes.add(associatedInspectionType.getInspectionType());
		}
		return inspectionTypes;
		
	}

	public boolean isMasterInspection(Long id) {
		return inspectionManager.isMasterInspection(id);
	}

}
