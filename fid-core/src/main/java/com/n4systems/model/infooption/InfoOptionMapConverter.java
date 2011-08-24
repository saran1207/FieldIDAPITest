package com.n4systems.model.infooption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringUtils;

public class InfoOptionMapConverter {

	public InfoOptionMapConverter() {}
	
	public Map<String, String> toMap(Collection<InfoOptionBean> options) {
		Map<String, String> optionMap = new LinkedHashMap<String, String>(options.size());
		for (InfoOptionBean option: options) {
			optionMap.put(option.getInfoField().getName(), option.getName());
		}
		return optionMap;
	}
	
	public List<InfoOptionBean> convertAutoAttributeInputs(Map<String, String> optionMap, AutoAttributeCriteria criteria) throws InfoOptionConversionException {
		return toList(optionMap, criteria.getInputs(), false, false, false);
	}
	
	public List<InfoOptionBean> convertAutoAttributeOutputs(Map<String, String> optionMap, AutoAttributeCriteria criteria) throws InfoOptionConversionException {
		try {
			return toList(optionMap, criteria.getOutputs(), true, true, false);
		} catch (MissingInfoOptionException e) {
			Logger.getLogger(InfoOptionMapConverter.class).error("Unexpected exception in option conversion", e);
			return new ArrayList<InfoOptionBean>();
		}
	}
	
	public List<InfoOptionBean> convertAssetAttributes(Map<String, String> optionMap, AssetType type) throws  InfoOptionConversionException {
		return toList(optionMap, type.getInfoFields(), true, true, true);
	}
	
	public List<InfoOptionBean> toList(Map<String, String> optionMap, Collection<InfoFieldBean> fields, boolean allowMissingInfoFields, boolean allowDynamicOptionCreation, boolean checkRequiredFields) throws StaticOptionResolutionException, InfoOptionConversionException {
		String optionValue;
		InfoOptionBean option;
		List<InfoOptionBean> optionList = new ArrayList<InfoOptionBean>();
		for (InfoFieldBean field: fields) {
			optionValue = optionMap.get(field.getName());
			
			if (StringUtils.isEmpty(optionValue)) {
				if (allowMissingInfoFields && (!checkRequiredFields || !field.isRequired())) {
					continue;
				} else {
					throw new MissingInfoOptionException(field);
				}
			}
			
			// try to resolve a static option first, then we'll create a dynamic one if it's not found.  
			// We do it this way to make sure the combo box static options resolve first
			option = findInfoOptionByName(optionValue, field.getInfoOptions());
			
			if (option == null) {
				// if we're not allowed to create dynamic options or the field is unable to accept them, then we have to throw
				if (!allowDynamicOptionCreation || !field.acceptsDyanmicInfoOption()) {
					throw new StaticOptionResolutionException(field, optionValue);
				} else if (InfoFieldBean.DATEFIELD_FIELD_TYPE.equals(field.getFieldType()) && !DateHelper.isValidExcelDateString(optionValue)) {  
					// if valid excel date then it will be sent as a Long in MS.  (e.g. "1318651200000")
					throw new InfoOptionConversionException("can't convert " + optionValue + " into date.", field, optionValue);
				} else {
					option = new InfoOptionBean();
					option.setInfoField(field);
					option.setName(optionValue);
					option.setStaticData(false);
				}
			}
			
			optionList.add(option);
		}
		
		return optionList;
	}
	
	private InfoOptionBean findInfoOptionByName(String name, List<InfoOptionBean> options) {
		for (InfoOptionBean option: options) {
			if (name.equals(option.getName())) {
				return option;
			}
		}
		return null;
	}
}
