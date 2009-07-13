package com.n4systems.fieldid.datatypes;

import junit.framework.TestCase;

/**
 * This class will hold all the information you see on the
 * Confirm Product Type Delete page.
 * 
 */
public class ConfirmProductTypeDelete extends TestCase {
	long products = 0;				// instances of this product type removed
	long inspections = 0;			// associated inspections removed
	long schedules = 0;				// associated scheduled inspections
	long productCodeMappings = 0;	// product code mappings for this product type
	long masterProductTypes = 0;	// master product types which used this as a sub-product type
	long masterProducts = 0;		// master products which had this as a sub-product
	long subProducts = 0;			// sub products which were attached to this as a master product
	
	public ConfirmProductTypeDelete() {
		super();
	}
	
	public String toString() {
		String s;
		s = products + "\tProduct(s) will be deleted.\n";
		s += inspections + "\tInspection(s) will be deleted.\n";
		s += schedules + "\tSchedule(s) will be deleted.\n";
		s += productCodeMappings + "\tProduct Code Mapping(s) being deleted.\n";
		s += masterProductTypes + "\tProduct Type(s) will have this removed as a Sub Product Type.\n";
		s += masterProducts + "\tProduct(s) will be detached from their Master Products. \n";
		s += subProducts + "\tSub Product(s) being detached from deleted Master Products\n";
		return s;
	}
	
	public void setProducts(long n) {
		products = n;
	}
	
	public long getProducts() {
		return products;
	}
	
	public void setInspections(long n) {
		inspections = n;
	}
	
	public long getInspections() {
		return inspections;
	}
	
	public void setSchedules(long n) {
		schedules = n;
	}

	public long getSchedules() {
		return schedules;
	}
	
	public void setProductCodeMappings(long n) {
		productCodeMappings = n;
	}
	
	public long getProductCodeMappings() {
		return productCodeMappings;
	}
	
	public void setMasterProductTypes(long n) {
		masterProductTypes = n;
	}
	
	public long getMasterProductTypes() {
		return masterProductTypes;
	}
	
	public void setMasterProducts(long n) {
		masterProducts = n;
	}
	
	public long getMasterProducts() {
		return masterProducts;
	}
	
	public void setSubProducts(long n) {
		subProducts = n;
	}
	
	public long getSubProducts() {
		return subProducts;
	}
}
