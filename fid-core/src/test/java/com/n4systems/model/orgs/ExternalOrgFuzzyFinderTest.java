package com.n4systems.model.orgs;

import static com.n4systems.model.builders.CustomerOrgBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.test.helpers.FluentArrayList;
public class ExternalOrgFuzzyFinderTest {

	
	
	@Test
	public void should_not_find_customer_when_no_customers_exist() throws Exception {
		List<CustomerOrg> listOfCustomers = new ArrayList<CustomerOrg>();
		
		ExternalOrgFuzzyFinder<CustomerOrg> sut = new ExternalOrgFuzzyFinder<CustomerOrg>();
		
		CustomerOrg actualResult = sut.find("code", "SOME NAME", listOfCustomers);
		
		assertNull("customer should not have been found", actualResult);
	}
	
	@Test
	public void should_not_find_customer_name_does_not_match() throws Exception {
		List<CustomerOrg> listOfCustomers = new FluentArrayList<CustomerOrg>().stickOn(aPrimaryCustomerOrg().withName("NAME THAT WONT MATCH").build());
		
		ExternalOrgFuzzyFinder<CustomerOrg> sut = new ExternalOrgFuzzyFinder<CustomerOrg>();
		
		CustomerOrg actualResult = sut.find("code", "SOME NAME", listOfCustomers);
		
		assertNull("customer should not have been found", actualResult);
	}
	
	
	@Test
	public void should_find_customer_with_fuzzy_matching_name() throws Exception {
		
		CustomerOrg customer = aPrimaryCustomerOrg().withName("SOME    NAME").build();
		List<CustomerOrg> listOfCustomers = new FluentArrayList<CustomerOrg>().stickOn(customer);
		
		ExternalOrgFuzzyFinder<CustomerOrg> sut = new ExternalOrgFuzzyFinder<CustomerOrg>();
		
		CustomerOrg actualResult = sut.find("code", "SOME NAME", listOfCustomers);
		
		assertEquals("customer should be found", customer, actualResult);
	}
	
	
	
	@Test
	public void should_find_first_customer_with_a_matching_name() throws Exception {
		
		CustomerOrg customer = aPrimaryCustomerOrg().withName("SOME   NAME").build();
		CustomerOrg customerThatWillNotBeFound = aPrimaryCustomerOrg().withName("SOME NAME").build();
		
		List<CustomerOrg> listOfCustomers = new FluentArrayList<CustomerOrg>().stickOn(customer, customerThatWillNotBeFound);
		
		ExternalOrgFuzzyFinder<CustomerOrg> sut = new ExternalOrgFuzzyFinder<CustomerOrg>();
		
		CustomerOrg actualResult = sut.find("code", "SOME NAME", listOfCustomers);
		
		assertEquals("customer should be found", customer, actualResult);
	}
	
	@Test
	public void should_find_by_name_when_more_than_one_code_matches() throws Exception {
		CustomerOrg customerThatWontBeFound1 = aPrimaryCustomerOrg().withName("Name that will not match").withCode("code 1").build();
		CustomerOrg customerThatWontBeFound2 = aPrimaryCustomerOrg().withName("Name that will also not match").withCode("code 1").build();
		CustomerOrg customerThatWillBeFound = aPrimaryCustomerOrg().withName("SOME NAME").build();
		
		List<CustomerOrg> listOfCustomers = new FluentArrayList<CustomerOrg>().stickOn(customerThatWontBeFound1, customerThatWillBeFound, customerThatWontBeFound2);
		
		ExternalOrgFuzzyFinder<CustomerOrg> sut = new ExternalOrgFuzzyFinder<CustomerOrg>();
		
		CustomerOrg actualResult = sut.find("code 1", "SOME NAME", listOfCustomers);
		
		assertEquals("customer should be found", customerThatWillBeFound, actualResult);
	}
	
	@Test
	public void should_find_by_code_when_only_one_code_matches() throws Exception {
		CustomerOrg customerThatWillBeFound = aPrimaryCustomerOrg().withName("Name that will not match").withCode("code 1").build();
		CustomerOrg customerThatWontBeFound1 = aPrimaryCustomerOrg().withName("Name that will also not match").withCode("some code").build();
		CustomerOrg customerThatWontBeFound2 = aPrimaryCustomerOrg().withName("SOME NAME").build();
		
		List<CustomerOrg> listOfCustomers = new FluentArrayList<CustomerOrg>().stickOn(customerThatWontBeFound1, customerThatWillBeFound, customerThatWontBeFound2);
		
		ExternalOrgFuzzyFinder<CustomerOrg> sut = new ExternalOrgFuzzyFinder<CustomerOrg>();
		
		CustomerOrg actualResult = sut.find("code 1", "SOME NAME", listOfCustomers);
		
		assertEquals("customer should be found", customerThatWillBeFound, actualResult);
	}

	
}
