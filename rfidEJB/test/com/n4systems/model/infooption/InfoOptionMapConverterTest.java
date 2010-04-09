package com.n4systems.model.infooption;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;

public class InfoOptionMapConverterTest {
	private InfoOptionMapConverter converter = new InfoOptionMapConverter();
	private List<InfoFieldBean> fields;
	private InfoFieldBean comboField;
	private InfoFieldBean selectField;
	private InfoFieldBean textField;
	
	@Before
	public void setup_info_fields() {
		InfoOptionBeanBuilder optionBuilder = InfoOptionBeanBuilder.aStaticInfoOption();

		comboField = InfoFieldBeanBuilder.aComboBox().named("combo").build();
		comboField.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(comboField).withName("cf-1").build(),
				optionBuilder.forField(comboField).withName("cf-2").build()
		)));

		selectField = InfoFieldBeanBuilder.aSelectBox().named("select").build();
		selectField.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(selectField).withName("sf-1").build(),
				optionBuilder.forField(selectField).withName("sf-2").build()
		)));
		
		textField = InfoFieldBeanBuilder.aTextField().named("text").build();
		textField.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>());
		
		fields = Arrays.asList(comboField, selectField, textField);
	}
	
	@Test
	public void to_map_converts_all_option_types() {
		List<InfoOptionBean> options = Arrays.asList(
				comboField.getInfoOptions().get(0),
				selectField.getInfoOptions().get(1),
				InfoOptionBeanBuilder.aDynamicInfoOption().forField(textField).withName("dynamic").build()
		);
		
		Map<String, String> optionMap = converter.toMap(options);
		
		assertEquals(3, optionMap.size());
		assertEquals(comboField.getInfoOptions().get(0).getName(), optionMap.get(comboField.getName()));
		assertEquals(selectField.getInfoOptions().get(1).getName(), optionMap.get(selectField.getName()));
		assertEquals("dynamic", optionMap.get(textField.getName()));
	}
	
	@Test
	public void to_list_converts_all_option_types() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), comboField.getInfoOptions().get(1).getName());
		optionMap.put(selectField.getName(), selectField.getInfoOptions().get(0).getName());
		optionMap.put(textField.getName(), "dynamic");
		
		List<InfoOptionBean> options = converter.toList(optionMap, fields, false, true, false);
		
		assertEquals(3, options.size());
		for (InfoOptionBean opt: options) {
			assertEquals(optionMap.get(opt.getInfoField().getName()), opt.getName());
		}
	}
	
	@Test(expected=MissingInfoOptionException.class)
	public void to_list_throws_on_missing_field_when_allow_missing_is_off() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), comboField.getInfoOptions().get(1).getName());
		optionMap.put(selectField.getName(), selectField.getInfoOptions().get(0).getName());
		
		converter.toList(optionMap, fields, false, true, false);
	}
	
	@Test
	public void to_list_ignores_missing_field_option_when_allow_missing_is_on() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), comboField.getInfoOptions().get(1).getName());
		optionMap.put(selectField.getName(), selectField.getInfoOptions().get(0).getName());
		
		assertEquals(2, converter.toList(optionMap, fields, true, true, false).size());
	}
	
	@Test
	public void to_list_ignores_missing_field_option_when_allow_missing_is_on_empty_list() throws InfoOptionConversionException {
		assertTrue(converter.toList(new LinkedHashMap<String, String>(), fields, true, true, false).isEmpty());
	}
	
	@Test(expected=StaticOptionResolutionException.class)
	public void to_list_throws_on_unresolvable_static_option_when_create_dynamic_is_off() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), "bleh");
		optionMap.put(selectField.getName(), selectField.getInfoOptions().get(0).getName());
		optionMap.put(textField.getName(), "dynamic");
		
		converter.toList(optionMap, fields, true, false, false);
	}
	
	@Test
	public void to_list_ignores_unresolvable_static_option_when_create_dynamic_is_on() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), "unresolvable");
		optionMap.put(selectField.getName(), selectField.getInfoOptions().get(0).getName());
		optionMap.put(textField.getName(), "dynamic");
		
		assertEquals(3, converter.toList(optionMap, fields, true, true, false).size());
	}
	
	@Test(expected=StaticOptionResolutionException.class)
	public void to_list_always_throws_on_unresolvable_static_only_field() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), comboField.getInfoOptions().get(1).getName());
		optionMap.put(selectField.getName(), "unresolvable");
		optionMap.put(textField.getName(), "dynamic");
		
		converter.toList(optionMap, fields, true, true, false);
	}
	
	@Test
	public void to_list_creates_dynamic_option_for_combo_box_when_static_option_cant_be_found() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), "dynamic");
		
		List<InfoOptionBean> options = converter.toList(optionMap, fields, true, true, false);
		
		assertEquals(1, options.size());
		assertEquals("dynamic", options.get(0).getName());
		assertNull(options.get(0).getUniqueID());
	}
	
	@Test
	public void to_list_resolves_static_option_before_creating_dynamic() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(),comboField.getInfoOptions().get(1).getName());
		
		List<InfoOptionBean> options = converter.toList(optionMap, fields, true, true, false);

		assertEquals(1, options.size());
		assertEquals(comboField.getInfoOptions().get(1).getName(), options.get(0).getName());
		assertNotNull(options.get(0).getUniqueID());
	}
	
	@Test
	public void handles_null_map_values() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), null);
		
		List<InfoOptionBean> options = converter.toList(optionMap, fields, true, true, false);

		assertTrue(options.isEmpty());
	}
	
	@Test(expected=MissingInfoOptionException.class)
	public void handles_null_map_values_when_allow_missing_off() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), null);
		
		List<InfoOptionBean> options = converter.toList(optionMap, fields, false, true, false);

		assertTrue(options.isEmpty());
	}
	
	@Test
	public void handles_null_map_values_2() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), null);
		
		List<InfoOptionBean> options = converter.toList(optionMap, fields, true, false, false);

		assertTrue(options.isEmpty());
	}
	
	@Test
	public void to_list_does_not_create_blank_options() throws InfoOptionConversionException {
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		optionMap.put(comboField.getName(), "");
		
		List<InfoOptionBean> options = converter.toList(optionMap, fields, true, true, false);
		assertTrue(options.isEmpty());
	}
}
