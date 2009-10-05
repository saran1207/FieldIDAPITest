package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table(name="subproducts")
public class SubProduct extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@OneToOne()
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToOne()
	private Product masterProduct;
	
	@Column(length=255)
	private String label;
	
	@Column(nullable=false)
	private Long weight;

	public SubProduct() {
		this(null, null, null);
	}
	
	public SubProduct(Product product, Product master) {
		this(null, product, master);
	}
	
	public SubProduct(String label, Product product, Product master) {
		super();
		this.label = label;
		this.product = product;
		this.masterProduct = master;
	}


	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		cleanLabel();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		cleanLabel();
	}
	
	private void cleanLabel() {
		if (label != null) {
			label = label.trim();
			if (label.length() == 0) {
				label = null;
			}
		}
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SubProduct && obj != null) {
			SubProduct subProduct = (SubProduct) obj;
			return product.equals(subProduct.getProduct());
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return product.hashCode();
	}

	public Product getMasterProduct() {
		return masterProduct;
	}

	public void setMasterProduct(Product masterProduct) {
		this.masterProduct = masterProduct;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
}
