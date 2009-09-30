package com.n4systems.model.product;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Product;
import com.n4systems.persistence.savers.Saver;

public class ProductSaver extends Saver<Product> {

	private UserBean modifiedBy;
	
	@Override
	public void save(EntityManager em, Product product) {
		setModifiedByOnProduct(product);
		super.save(em, product);
		
		// on save, we also need to update which will force the network Id to get setup
		// the one case where this doesn't need to happen is if it was connected on create
		if (product.getNetworkId() == null) {
			product.touch();
			update(em, product);
		}
	}

	@Override
	public Product update(EntityManager em, Product product) {
		setModifiedByOnProduct(product);
		return super.update(em, product);
	}
	
	private void setModifiedByOnProduct(Product product) {
		if (modifiedBy != null) {
			product.setModifiedBy(modifiedBy);
		}
	}

	@Override
	protected void remove(EntityManager em, Product product) {
		throw new NotImplementedException();
	}
	
	public ProductSaver setModifiedBy(UserBean modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}
}
