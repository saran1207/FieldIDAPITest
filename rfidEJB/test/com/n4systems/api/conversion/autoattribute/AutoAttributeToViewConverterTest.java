package com.n4systems.api.conversion.autoattribute;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import org.junit.Test;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;

public class AutoAttributeToViewConverterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void to_view_compresses_input_attributes() throws ConversionException {
		AutoAttributeToViewConverter converter = new AutoAttributeToViewConverter();
		
		AutoAttributeDefinition def = createAutoAttributeDef();
		AutoAttributeView view = converter.toView(def);
		
		assertTrue(view.getInputs() instanceof LinkedHashMap);
		assertTrue(view.getOutputs() instanceof LinkedHashMap);
		assertEquals(2, view.getInputs().size());
		assertEquals(2, view.getOutputs().size());
		assertEquals("in_opt_1", view.getInputs().get("in_field_1"));
		assertEquals("in_opt_2", view.getInputs().get("in_field_2"));
		assertEquals("out_opt_1", view.getOutputs().get("out_field_1"));
		assertEquals("out_opt_2", view.getOutputs().get("out_field_2"));
	}
	
	private AutoAttributeDefinition createAutoAttributeDef() {
		InfoOptionBean in1 = InfoOptionBeanBuilder.aStaticInfoOption().withName("in_opt_1").forField(InfoFieldBeanBuilder.aSelectBox().named("in_field_1").build()).build();
		InfoOptionBean in2 = InfoOptionBeanBuilder.aDynamicInfoOption().withName("in_opt_2").forField(InfoFieldBeanBuilder.aTextField().named("in_field_2").build()).build();
		InfoOptionBean out1 = InfoOptionBeanBuilder.aStaticInfoOption().withName("out_opt_1").forField(InfoFieldBeanBuilder.aSelectBox().named("out_field_1").build()).build();
		InfoOptionBean out2 = InfoOptionBeanBuilder.aDynamicInfoOption().withName("out_opt_2").forField(InfoFieldBeanBuilder.aTextField().named("out_field_2").build()).build();
		
		AutoAttributeCriteria crit = new AutoAttributeCriteria();
		crit.getInputs().add(in1.getInfoField());
		crit.getInputs().add(in2.getInfoField());
		crit.getOutputs().add(out1.getInfoField());
		crit.getOutputs().add(out2.getInfoField());
		
		AutoAttributeDefinition def = new AutoAttributeDefinition();
		def.setCriteria(crit);
		def.getInputs().add(in1);
		def.getInputs().add(in2);
		def.getOutputs().add(out1);
		def.getOutputs().add(out2);
		
		return def;
	}
}
