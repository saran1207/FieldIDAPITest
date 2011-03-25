package com.n4systems.export.converters;

import org.apache.commons.lang.StringUtils;

import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.UnitOfMeasureCriteriaResult;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class CriteriaResultConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return CriteriaResult.class.isAssignableFrom(type);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		CriteriaResult result = (CriteriaResult) source;
		
		writer.startNode("Result");
		writer.addAttribute("Name", result.getCriteria().getDisplayText());
		
		String value = null;
		if (result instanceof TextFieldCriteriaResult) {
			value = ((TextFieldCriteriaResult) result).getValue();
		} else if (result instanceof UnitOfMeasureCriteriaResult) {
			value = getUnitOfMeasureStringValue((UnitOfMeasureCriteriaResult) result);
		} else if (result instanceof SelectCriteriaResult) {
			value = ((SelectCriteriaResult) result).getValue();
		} else if (result instanceof OneClickCriteriaResult) {
			value = ((OneClickCriteriaResult) result).getState().getDisplayText();
		} else if (result instanceof ComboBoxCriteriaResult) {
			value = ((ComboBoxCriteriaResult) result).getValue();
		}
		writer.addAttribute("Value", StringUtils.stripToEmpty(value));
		
		writeIterable(writer, context, "Recommendations", result.getRecommendations());
		writeIterable(writer, context, "Deficiencies", result.getDeficiencies());
		
		writer.endNode();
	}
	
    private String getUnitOfMeasureStringValue(UnitOfMeasureCriteriaResult result) {
        UnitOfMeasureCriteria criteria = (UnitOfMeasureCriteria) result.getCriteria();
        String unitOfMeasureValue = "";
        if (result.getPrimaryValue() != null) {
            unitOfMeasureValue += result.getPrimaryValue() + " " + criteria.getPrimaryUnit().getName();
        }
        if (result.getSecondaryValue() != null) {
            unitOfMeasureValue += " " + result.getSecondaryValue() + " " + criteria.getSecondaryUnit().getName();
        }
        return unitOfMeasureValue.trim();
    }

}
