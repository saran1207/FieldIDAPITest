package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;

// XXX - Rename me
@Entity
@Table (name = "findproductoption_manufacture")
public class FindProductOptionManufactureBean extends LegacyBeanTenantWithCreateModifyDate implements Comparable<FindProductOptionManufactureBean> {
	private static final long serialVersionUID = 1L;
	
	private Long weight;
	private int mobileWeight;
	
	@ManyToOne
	@JoinColumn (name = "r_findproductoption")
	private FindProductOptionBean findProductOption;

	public FindProductOptionBean getFindProductOption() {
		return findProductOption;
	}
	
	public void setFindProductOption(FindProductOptionBean findProductOption) {
		this.findProductOption = findProductOption;
	}
	
	public Long getWeight() {
		return weight;
	}
	
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	
	public int getMobileWeight() {
		return mobileWeight;
	}
	
	public void setMobileWeight(int mobileWeight) {
		this.mobileWeight = mobileWeight;
	}
	
	public int compareTo(FindProductOptionManufactureBean otherOption) {
		if (getWeight().equals(otherOption.getWeight())) {
			return getUniqueID().compareTo(otherOption.getUniqueID());
		}
		
		return getWeight().compareTo(otherOption.getWeight());
	}
	
}
