package com.n4systems.api.conversion.autoattribute;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;
import com.n4systems.model.builders.TenantBuilder;

public class AutoAttributeToModelConverterTest {
	private AutoAttributeCriteria crit;	
	private AutoAttributeToModelConverter converter;

	@Before
	public void setup_converter() {
		crit = createCriteria();
		converter = new AutoAttributeToModelConverter(crit);
	}
	
	@Test
	public void test_to_model_converts_options() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		view.getOutputs().put("out1", "out1-1");
		view.getOutputs().put("out2", "out2-1");
		view.getOutputs().put("out3", "out3-dynamic");
		
		AutoAttributeDefinition def = converter.toModel(view, null);
		assertEquals(2, def.getInputs().size());
		assertEquals(3, def.getOutputs().size());
	
		for (InfoOptionBean opt: def.getInputs()) {
			assertEquals(view.getInputs().get(opt.getInfoField().getName()), opt.getName());
		}
		
		for (InfoOptionBean opt: def.getOutputs()) {
			assertEquals(view.getOutputs().get(opt.getInfoField().getName()), opt.getName());
		}
	}
	
	@Test
	public void to_model_sets_criteria_and_tenant_on_def() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		
		AutoAttributeDefinition def = converter.toModel(view, null);
		assertSame(crit, def.getCriteria());
		assertSame(crit.getTenant(), def.getTenant());

	}
	
	@Test(expected=ConversionException.class)
	public void to_model_throws_on_missing_input_criteria() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in2", "in2-1");
		
		converter.toModel(view, null);
	}
	
	@Test(expected=ConversionException.class)
	public void to_model_throws_on_unresolvable_input_criteria() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "bleh");
		
		converter.toModel(view, null);
	}
	
	@Test(expected=ConversionException.class)
	public void to_model_throws_on_missing_static_output_criteria() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		view.getOutputs().put("out2", "bleh");
		
		converter.toModel(view, null);
	}
	
	@Test
	public void to_model_allows_missing_output_fields_1() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		view.getOutputs().put("out1", "out1-1");
		view.getOutputs().put("out2", "out2-1");
		
		AutoAttributeDefinition def = converter.toModel(view, null);
		assertEquals(2, def.getOutputs().size());
	}
	
	@Test
	public void to_model_allows_missing_output_fields_2() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		view.getOutputs().put("out2", "out2-1");
		
		AutoAttributeDefinition def = converter.toModel(view, null);
		assertEquals(1, def.getOutputs().size());
	}
	
	@Test
	public void to_model_allows_missing_output_fields_3() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		
		AutoAttributeDefinition def = converter.toModel(view, null);
		assertEquals(0, def.getOutputs().size());
	}
	
	@Test
	public void to_model_creates_dynamic_option_for_combo_box_when_static_option_cant_be_found() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		view.getOutputs().put("out1", "new");
		
		AutoAttributeDefinition def = converter.toModel(view, null);
		assertEquals(1, def.getOutputs().size());
		assertEquals("new", def.getOutputs().get(0).getName());
		assertNull(def.getOutputs().get(0).getUniqueID());
	}
	
	@Test
	public void to_model_resolves_static_option_before_creating_dynamic() throws ConversionException {
		AutoAttributeView view = new AutoAttributeView();
		view.getInputs().put("in1", "in1-1");
		view.getInputs().put("in2", "in2-2");
		view.getOutputs().put("out1", "out1-1");
		
		AutoAttributeDefinition def = converter.toModel(view, null);
		assertEquals(1, def.getOutputs().size());
		assertEquals("out1-1", def.getOutputs().get(0).getName());
		assertNotNull(def.getOutputs().get(0).getUniqueID());
	}
	
	private AutoAttributeCriteria createCriteria() {
		InfoOptionBeanBuilder optionBuilder = InfoOptionBeanBuilder.aStaticInfoOption();
		
		InfoFieldBean in1 = InfoFieldBeanBuilder.aComboBox().withName("in1").build();
		in1.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(in1).withName("in1-1").build(),
				optionBuilder.forField(in1).withName("in1-2").build()
		)));
		
		InfoFieldBean in2 = InfoFieldBeanBuilder.aSelectBox().withName("in2").build();
		in2.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(in2).withName("in2-1").build(),
				optionBuilder.forField(in2).withName("in2-2").build()
		)));

		InfoFieldBean out1 = InfoFieldBeanBuilder.aComboBox().withName("out1").build();
		out1.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(out1).withName("out1-1").build()
		)));

		InfoFieldBean out2 = InfoFieldBeanBuilder.aSelectBox().withName("out2").build();
		out2.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(out2).withName("out2-1").build(),
				optionBuilder.forField(out2).withName("out2-2").build()
		)));
		
		InfoFieldBean out3 = InfoFieldBeanBuilder.aTextField().withName("out3").build();
		out3.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>());
				
		AutoAttributeCriteria crit = new AutoAttributeCriteria();
		crit.setTenant(TenantBuilder.aTenant().build());
		crit.getInputs().add(in1);
		crit.getInputs().add(in2);
		crit.getOutputs().add(out1);
		crit.getOutputs().add(out2);
		crit.getOutputs().add(out3);
		return crit;
	}
}
