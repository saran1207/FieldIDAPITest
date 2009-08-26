package com.n4systems.fieldidadmin.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.promocode.PromoCode;
import com.n4systems.model.promocode.PromoCodeSaver;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.persistence.loaders.NonSecureIdLoader;
import com.opensymphony.xwork2.Preparable;

public class PromoCodeAction extends AbstractAdminAction implements Preparable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private PromoCode promoCode;
	private Map<String, Boolean> extendedFeatures = new HashMap<String, Boolean>();
	
	private List<PromoCode> promoCodes;
	
	public void prepare() throws Exception {
		if (id != null) {
			NonSecureIdLoader<PromoCode> promoLoader = new NonSecureIdLoader<PromoCode>(PromoCode.class);
			promoLoader.setId(id);
			promoCode = promoLoader.load();
			
			for (ExtendedFeature feature : promoCode.getExtendedFeatures()) {
				extendedFeatures.put(feature.name(), true);
			}			
		} else {
			promoCode = new PromoCode();
		}
	}
	
	public String doList() {
		AllEntityListLoader<PromoCode> promoListLoader = new AllEntityListLoader<PromoCode>(PromoCode.class);
		
		promoCodes = promoListLoader.load();
		
		return SUCCESS;
	}
	
	public String doShowForm() {
		
		return INPUT;
	}
	
	public String doSave() {
		populateExtendedFeatures();
		
		PromoCodeSaver saver = new PromoCodeSaver();
		saver.saveOrUpdate(promoCode);
		
		return SUCCESS;
	}
	
	private void populateExtendedFeatures() {

		Set<ExtendedFeature> newExtendedFeatures = new HashSet<ExtendedFeature>();
		
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			if (extendedFeatures.get(feature.name())) {
				newExtendedFeatures.add(feature);
			}
		}
		
		promoCode.setExtendedFeatures(newExtendedFeatures);
	}

	public List<PromoCode> getPromoCodes() {
		return promoCodes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PromoCode getPromoCode() {
		return promoCode;
	}

	public ExtendedFeature[] getAvailableExtendedFeatures() {
		return ExtendedFeature.values();
	}

	@SuppressWarnings("unchecked")
	public Map getExtendedFeatures() {
		return extendedFeatures;
	}

	@SuppressWarnings("unchecked")
	public void setExtendedFeatures(Map extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}

	
	
}
