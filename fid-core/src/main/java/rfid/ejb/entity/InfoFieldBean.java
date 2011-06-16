package rfid.ejb.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.n4systems.model.AssetType;
import org.hibernate.annotations.Where;

import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;

@Entity
@Table(name = "infofield")
public class InfoFieldBean extends LegacyBaseEntity implements Comparable<InfoFieldBean> {
	private static final long serialVersionUID = 1L;
	private static final Long DEFUALT_WEIGHT = 0L;
	public enum InfoFieldType { 
		TextField( "Text Field" ), 
		SelectBox( "Select Box" ), 
		ComboBox( "Combo Box" ),	
		UnitOfMeasure( "Unit Of Measure" ),
		DateField( "Date Field");
		
		private String label;
		
		
		private InfoFieldType( String label ) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}
		public String getName() {
			return name();
		}
		
	}
	public final static String TEXTFIELD_FIELD_TYPE = "textfield";
	public final static String SELECTBOX_FIELD_TYPE = "selectbox";
	public final static String COMBOBOX_FIELD_TYPE = "combobox";
	public final static String UNIT_OF_MEASURE = "unitofmeasure";
	public final static String DATEFIELD_FIELD_TYPE = "datefield";

	@Column( nullable=false )
	private String name;
	
	@Column( nullable=false )
	private String fieldType;
	
	private Long weight = DEFUALT_WEIGHT;
	private boolean required;
	private boolean usingUnitOfMeasure;
	private boolean retired;
	private boolean includeTime;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "r_productinfo")
	private AssetType assetInfo;

	@ManyToOne(optional = true)
	@JoinColumn(name = "r_unitofmeasure")
	private UnitOfMeasure unitOfMeasure;

	@OneToMany(mappedBy = "infoField", targetEntity = InfoOptionBean.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@OrderBy( "weight" )
	@Where(clause="staticData = 1")
	private Set<InfoOptionBean> unfilteredInfoOptions;
	

	@SuppressWarnings("unused")
	@OneToMany(mappedBy = "infoField", targetEntity=InfoOptionBean.class, fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
	private Set<InfoOptionBean> allInfoOptionsForCasadeDeleteOnlyDoNotInteractWithThisSet;
	
	@Transient
	private List<InfoOptionBean> comboBoxInfoOptions;

	
	@PrePersist
	@PreUpdate
	protected void preSave() {
		if (!TEXTFIELD_FIELD_TYPE.equals(fieldType)) {
			usingUnitOfMeasure = false;
		}

		if( !usingUnitOfMeasure ) {
			unitOfMeasure = null;
		}
		
		// default weight.
		if (weight == null) {
			weight = 0L;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public AssetType getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(AssetType assetInfo) {
		this.assetInfo = assetInfo;
	}

	public Set<InfoOptionBean> getUnfilteredInfoOptions() {
		return unfilteredInfoOptions;
	}

	public void setUnfilteredInfoOptions(
			Set<InfoOptionBean> unfilteredInfoOptions) {

		this.unfilteredInfoOptions = unfilteredInfoOptions;
	}

	public void associateOptions() {
		for (InfoOptionBean infoOptionBean : unfilteredInfoOptions) {
			infoOptionBean.setInfoField(this);
		}
	}

	/**
	 * Returns only the static info options, sorted by weight then name
	 * 
	 * @return
	 */
	public List<InfoOptionBean> getInfoOptions() {
		return new ArrayList<InfoOptionBean>(unfilteredInfoOptions);
	}



	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public int compareTo(InfoFieldBean other) {
		if (getWeight().equals(other.getWeight())) {
			return getName().compareTo(other.getName());
		}

		return getWeight().compareTo(other.getWeight());
	}

	public boolean isUsingUnitOfMeasure() {
		return usingUnitOfMeasure;
	}

	public void setUsingUnitOfMeasure(boolean usingUnitOfMeasure) {
		this.usingUnitOfMeasure = usingUnitOfMeasure;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof InfoFieldBean) {

			return this.equals((InfoFieldBean) obj);
		} else {
			return super.equals(obj);
		}

	}

	public boolean equals(InfoFieldBean infoField) {

		if (infoField == null)
			return false;
		if (getUniqueID() == null)
			return this == infoField;

		return getUniqueID().equals(infoField.getUniqueID());
	}
	
	@Override
	public int hashCode() {
		return (getUniqueID() == null) ? super.hashCode() : getUniqueID().hashCode(); 
	}

	public boolean acceptsDyanmicInfoOption() {
		if (COMBOBOX_FIELD_TYPE.equals(fieldType)
				|| TEXTFIELD_FIELD_TYPE.equals(fieldType)
				|| DATEFIELD_FIELD_TYPE.equals(fieldType)) {
			return true;
		}
		return false;
	}

	public boolean hasStaticInfoOption() {
		if (COMBOBOX_FIELD_TYPE.equals(fieldType)
				|| SELECTBOX_FIELD_TYPE.equals(fieldType)) {
			return true;
		}
		return false;
	}

	public List<InfoOptionBean> getComboBoxInfoOptions() {
		if (comboBoxInfoOptions == null) {
			createComboBoxInfoOptions(null);
		}
		return comboBoxInfoOptions;
	}

	public void createComboBoxInfoOptions(InfoOptionBean option) {

		if (option != null && !option.isStaticData()) {
			comboBoxInfoOptions = new ArrayList<InfoOptionBean>();
			comboBoxInfoOptions.add(option);
			comboBoxInfoOptions.addAll(getInfoOptions());
		} else {
			comboBoxInfoOptions = getInfoOptions();
		}

	}

	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}

	/**
	 * Creates a dynamic InfoOptionBean setting this as it's infoField.
	 * @see #createInfoOption(String, boolean)
	 * @param name			Name of the new InfoOption
	 * @param staticData	Sets the staticData field of InfoOptionBean
	 * @return				A new InfoOption
	 */
	public InfoOptionBean createDynamicInfoOption(String name) {
		return createInfoOption(name, false);
	}
	
	/**
	 * Creates an InfoOptionBean setting this as it's infoField.  Note, does not update the <code>unfilteredInfoOptions</code> list.
	 * @param name			Name of the new InfoOption
	 * @param staticData	Sets the staticData field of InfoOptionBean
	 * @return				A new InfoOption
	 */
	public InfoOptionBean createInfoOption(String name, boolean staticData) {
		InfoOptionBean option = new InfoOptionBean();
		option.setInfoField(this);
		option.setName(name);
		option.setStaticData(staticData);
		
		return option;
	}

	@Override
	public String toString() {
		return "InfoFieldBean [fieldType=" + fieldType + ", name=" + name + ", uniqueId=" + getUniqueID() + "]";
	}

	public boolean isIncludeTime() {
		return includeTime;
	}

	public void setIncludeTime(boolean includeTime) {
		this.includeTime = includeTime;
	}
	
}
