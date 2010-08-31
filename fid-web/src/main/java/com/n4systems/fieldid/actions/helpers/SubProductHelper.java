package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.api.NamedEntity;

public class SubProductHelper implements NamedEntity {

	private String label;
	private Product product;

	public SubProductHelper() {
		super();
	}

	public SubProductHelper(String label, Product product) {
		super();
		setLabel(label);
		this.product = product;
	}

	@Deprecated
	public String getName() {
		return getLabel();
	}

	@Deprecated
	public void setName(String label) {
		setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		if (label != null) {
			label = label.trim();
			if (label.length() == 0) {
				label = null;
			}
		}
		this.label = label;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public static List<SubProductHelper> convert(List<SubProduct> subProducts) {
		List<SubProductHelper> subComponents = new ArrayList<SubProductHelper>();
		for (SubProduct subProduct : subProducts) {
			if (subProduct != null) {
				subComponents.add(new SubProductHelper(subProduct.getLabel(), subProduct.getProduct()));
			}
		}
		return subComponents;
	}

}
