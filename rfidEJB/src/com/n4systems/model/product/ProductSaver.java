package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;


import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Product;
import com.n4systems.model.user.User;
import com.n4systems.persistence.savers.Saver;

public class ProductSaver extends Saver<Product> {
	private final RecursiveLinkedChildProductLoader linkedProductLoader; 
	private User modifiedBy;
	
	public ProductSaver(RecursiveLinkedChildProductLoader linkedProductLoader) {
		this.linkedProductLoader = linkedProductLoader;
	}
	
	public ProductSaver() {
		this(new RecursiveLinkedChildProductLoader());
	}
	
	@Override
	public void save(EntityManager em, Product product) {
		// Due to the way Product is setup right now, we can call persist on it 
		// or we'll get lazy loads, until this is fixed, we'll just always call update 
		update(em, product);
	}

	@Override
	public Product update(EntityManager em, Product product) {
		// we need to capture this now, as it will be lost after merge
		boolean linkedProductChanged = product.linkedProductHasChanged();

		setModifiedByOnProduct(product);
		Product managedProduct = super.update(em, product);
		
		managedProduct = resave(em, managedProduct);
		
		if (linkedProductChanged && !product.isNew()) {
			forceNetworkIdRecalc(em, product);
		}
		
		return managedProduct;
	}
	
	/**
	 * Performs an update without any linked product checks and resaves
	 * @param em		EntityManager
	 * @param product	Product
	 */
	public void simpleUpdate(EntityManager em, Product product) {
		super.update(em, product);
	}

	private Product resave(EntityManager em, Product product) {
		// if the networkid is null, we need to update which will force the network Id to get setup
		// the one case where this doesn't need to happen is if it was connected on create
		if (product.getNetworkId() == null) {
			product.touch();
			product = super.update(em, product);
		}
		return product;
	}
	
	private void forceNetworkIdRecalc(EntityManager em, Product product) {
		List<Product> linkedProducts = linkedProductLoader.setProduct(product).load(em);
		
		for (Product linkedProduct: linkedProducts) {
			linkedProduct.touch();
			super.update(em, linkedProduct);
		}
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
	
	public ProductSaver setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}
}
