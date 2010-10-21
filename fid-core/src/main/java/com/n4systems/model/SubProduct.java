package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table(name="subproducts")
public class SubProduct extends AbstractEntity implements UnsecuredEntity {

	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(name="product_id")
	private Asset asset;
	
	@ManyToOne
    @JoinColumn(name="masterproduct_id")
	private Asset masterAsset;
	
	@Column(length=255)
	private String label;
	
	@Column(nullable=false)
	private Long weight;

	public SubProduct() {
		this(null, null, null);
	}
	
	public SubProduct(Asset asset, Asset master) {
		this(null, asset, master);
	}
	
	public SubProduct(String label, Asset asset, Asset master) {
		super();
		this.label = label;
		this.asset = asset;
		this.masterAsset = master;
	}


	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
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
			return getAsset().equals(subProduct.getAsset());
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return getAsset().hashCode();
	}

	public Asset getMasterProduct() {
		return masterAsset;
	}

	public void setMasterProduct(Asset masterProduct) {
		this.masterAsset = masterProduct;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
}
