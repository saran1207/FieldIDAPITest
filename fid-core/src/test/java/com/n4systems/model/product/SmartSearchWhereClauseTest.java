package com.n4systems.model.product;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClause.ChainOp;

public class SmartSearchWhereClauseTest {

	@Test
	public void test_use_serial_number() {
		String searchText = "search text";
		
		SmartSearchWhereClause ssClause = new SmartSearchWhereClause(searchText, true, false, false);
		
		assertEquals(1, ssClause.getClauses().size());
		assertEquals(searchText, ssClause.getClauses().get(0).getValue());
		assertEquals(ChainOp.OR, ssClause.getClauses().get(0).getChainOperator());
		assertEquals("serialNumber", ssClause.getClauses().get(0).getName());	
	}
	
	@Test
	public void test_use_rfid_number() {
		String searchText = "search text";
		
		SmartSearchWhereClause ssClause = new SmartSearchWhereClause(searchText, false, true, false);
		
		assertEquals(1, ssClause.getClauses().size());
		assertEquals(searchText, ssClause.getClauses().get(0).getValue());
		assertEquals(ChainOp.OR, ssClause.getClauses().get(0).getChainOperator());
		assertEquals("rfidNumber", ssClause.getClauses().get(0).getName());	
	}
	
	@Test
	public void test_use_customer_ref_number() {
		String searchText = "search text";
		
		SmartSearchWhereClause ssClause = new SmartSearchWhereClause(searchText, false, false, true);
		
		assertEquals(1, ssClause.getClauses().size());
		assertEquals(searchText, ssClause.getClauses().get(0).getValue());
		assertEquals(ChainOp.OR, ssClause.getClauses().get(0).getChainOperator());
		assertEquals("customerRefNumber", ssClause.getClauses().get(0).getName());	
	}
	
	@Test
	public void test_use_all_fields() {
		String searchText = "search text";
		
		SmartSearchWhereClause ssClause = new SmartSearchWhereClause(searchText, true, true, true);
		
		Set<String> paramNames = new HashSet<String>(Arrays.asList("serialNumber", "rfidNumber", "customerRefNumber")); 
		
		for (WhereClause<?> clause: ssClause.getClauses()) {
			assertTrue(paramNames.contains(clause.getName()));
			paramNames.remove(clause.getName());
		}
		
		assertTrue(paramNames.isEmpty());		
	}
}
