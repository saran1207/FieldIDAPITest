package com.n4systems.api.conversion.autoattribute;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.model.AutoAttributeDefinition;

public class AutoAttributeToViewConverter implements ModelToViewConverter<AutoAttributeDefinition, AutoAttributeView> {
	
	public AutoAttributeToViewConverter() {}
	
	@Override
	public AutoAttributeView toView(AutoAttributeDefinition model) throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		
		// put the input/output fields into TreeSets so they come out ordered
		Set<InfoFieldBean> inputFields = new TreeSet<InfoFieldBean>(model.getCriteria().getInputs());
		Set<InfoFieldBean> outputFields = new TreeSet<InfoFieldBean>(model.getCriteria().getOutputs());
		
		compressAttributes(view.getInputs(), inputFields, model.getInputs());
		compressAttributes(view.getOutputs(), outputFields, model.getOutputs());
		
		return view;
	}
	
	private void compressAttributes(Map<String, String> viewAttribs, Set<InfoFieldBean> criteriaFields, List<InfoOptionBean> definitionOptions) {
		// hunt down each definition info option for each critera info field
		for (InfoFieldBean field: criteriaFields) {
			for (InfoOptionBean option: definitionOptions) {
				if (option.getInfoField().equals(field)) {
					viewAttribs.put(field.getName(), option.getName());
					break;
				}
			}
		}
	}
}
