package com.n4systems.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.InfoOptionBeanBuilder;
import com.n4systems.test.helpers.FluentArrayList;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import static com.n4systems.model.builders.InfoFieldBeanBuilder.*;

public class AutoAttributeDefinitionTest {

	
	
	
	private AutoAttributeCriteria criteria;
	private InfoFieldBean infoField3;
	private InfoFieldBean infoField2;
	private InfoFieldBean infoField1;
	
	@Before
	public void setup() {
		infoField1 = aTextField().named("field 1").build();
		infoField2 = aTextField().named("field 2").build();
		infoField3 = aTextField().named("field 3").build();
	
		criteria = new AutoAttributeCriteria();
		criteria.getOutputs().add(infoField1);
		criteria.getOutputs().add(infoField2);
		criteria.getOutputs().add(infoField3);
	}

	@Test
	public void should_not_adjust_sanatized_outputs_if_all_are_defined() throws Exception {
		//a definition with all the output fields set with info options.
		InfoOptionBean option1 = InfoOptionBeanBuilder.aStaticInfoOption().forField(infoField1).build();
		InfoOptionBean option2 = InfoOptionBeanBuilder.aStaticInfoOption().forField(infoField2).build();
		InfoOptionBean option3 = InfoOptionBeanBuilder.aStaticInfoOption().forField(infoField3).build();
		
		List<InfoOptionBean> expectedOutputs = new FluentArrayList<InfoOptionBean>(option1, option2, option3);
		
		AutoAttributeDefinition sut = new AutoAttributeDefinition();
		sut.setCriteria(criteria);
		sut.getOutputs().addAll(expectedOutputs);
		
		List<InfoOptionBean> actualOutputs = sut.getSanitizedOutputs();
		
		assertArrayEquals(expectedOutputs.toArray(), actualOutputs.toArray());
	}
	
	
	@Test
	public void should_add_a_blank_option_if_not_all_outputs_are_defined() throws Exception {
		//a definition with all the output fields set with info options.
		InfoOptionBean option1 = InfoOptionBeanBuilder.aStaticInfoOption().forField(infoField1).build();
		InfoOptionBean option2 = InfoOptionBeanBuilder.aStaticInfoOption().forField(infoField2).build();
		
		
		List<InfoOptionBean> expectedOutputs = new FluentArrayList<InfoOptionBean>(option1, option2, InfoOptionBean.createBlankInfoOption(infoField3));
		
		AutoAttributeDefinition sut = new AutoAttributeDefinition();
		sut.setCriteria(criteria);
		sut.getOutputs().addAll(new FluentArrayList<InfoOptionBean>(option1, option2));
		
		List<InfoOptionBean> actualOutputs = sut.getSanitizedOutputs();
		
		assertArrayEquals(expectedOutputs.toArray(), actualOutputs.toArray());
	}
	
	@Test
	public void should_a_full_set_of_blank_info_options_if_no_outputs_are_define() throws Exception {
		//a definition with all the output fields set with info options.
		
		
		List<InfoOptionBean> expectedOutputs = new FluentArrayList<InfoOptionBean>(InfoOptionBean.createBlankInfoOption(infoField1), InfoOptionBean.createBlankInfoOption(infoField1), InfoOptionBean.createBlankInfoOption(infoField3));
		
		AutoAttributeDefinition sut = new AutoAttributeDefinition();
		sut.setCriteria(criteria);
		sut.getOutputs().clear();
		
		List<InfoOptionBean> actualOutputs = sut.getSanitizedOutputs();
		
		assertArrayEquals(expectedOutputs.toArray(), actualOutputs.toArray());
	}
	
}
