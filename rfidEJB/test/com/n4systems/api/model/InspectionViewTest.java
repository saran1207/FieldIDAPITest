package com.n4systems.api.model;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class InspectionViewTest {

	@Test
	public void test_get_next_inspection_date_as_date() {
		Date date = new Date();
		
		InspectionView view = new InspectionView();
		
		view.setNextInspectionDate(date);
		
		assertSame(date, view.getNextInspectionDateAsDate());
	}
	
	@Test
	public void test_get_next_inspection_date_as_date_allows_null() {
		InspectionView view = new InspectionView();
		assertNull(view.getNextInspectionDateAsDate());
	}
	
	@Test(expected=ClassCastException.class)
	public void test_get_next_inspection_date_as_date_throws_exception_on_wrong_type() {
		InspectionView view = new InspectionView();
		view.setNextInspectionDate("bad date type");
		
		view.getNextInspectionDateAsDate();
	}
	
	@Test
	public void test_get_inspection_date_as_date() {
		Date date = new Date();
		
		InspectionView view = new InspectionView();
		
		view.setInspectionDate(date);
		
		assertSame(date, view.getInspectionDateAsDate());
	}
	
	@Test
	public void test_get_inspection_date_as_date_allows_null() {
		InspectionView view = new InspectionView();
		assertNull(view.getInspectionDateAsDate());
	}
	
	@Test(expected=ClassCastException.class)
	public void test_get_inspection_date_as_date_throws_exception_on_wrong_type() {
		InspectionView view = new InspectionView();
		view.setInspectionDate("bad date type");
		
		view.getInspectionDateAsDate();
	}
	
	@Test
	public void set_printable_encodes_boolean() {
		InspectionView view = new InspectionView();
		
		view.setPrintable(true);
		assertEquals("Y", view.getPrintable());
		
		view.setPrintable(false);
		assertEquals("N", view.getPrintable());
	}
	
	@Test
	public void is_printable_allows_null() {
		InspectionView view = new InspectionView();
		assertNull(view.isPrintable());
	}
	
	@Test
	public void is_printable_is_true_on_Y() {
		InspectionView view = new InspectionView();
		view.setPrintable("Y");
		assertTrue(view.isPrintable());
	}
	
	@Test
	public void is_printable_defaults_false() {
		InspectionView view = new InspectionView();
		view.setPrintable("N");
		assertFalse(view.isPrintable());
		
		view.setPrintable("a");
		assertFalse(view.isPrintable());
		
		view.setPrintable("hello");
		assertFalse(view.isPrintable());
	}
}
