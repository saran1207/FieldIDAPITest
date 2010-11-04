package com.n4systems.api.model;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class EventViewTest {

	@Test
	public void test_get_next_event_date_as_date() {
		Date date = new Date();
		
		EventView view = new EventView();
		
		view.setNextEventDate(date);
		
		assertSame(date, view.getNextEventDateAsDate());
	}
	
	@Test
	public void test_get_next_event_date_as_date_allows_null() {
		EventView view = new EventView();
		assertNull(view.getNextEventDateAsDate());
	}
	
	@Test(expected=ClassCastException.class)
	public void test_get_next_event_date_as_date_throws_exception_on_wrong_type() {
		EventView view = new EventView();
		view.setNextEventDate("bad date type");
		
		view.getNextEventDateAsDate();
	}
	
	@Test
	public void test_get_event_date_as_date() {
		Date date = new Date();
		
		EventView view = new EventView();
		
		view.setDatePerformed(date);
		
		assertSame(date, view.getDatePerformedAsDate());
	}
	
	@Test
	public void test_get_event_date_as_date_allows_null() {
		EventView view = new EventView();
		assertNull(view.getDatePerformedAsDate());
	}
	
	@Test(expected=ClassCastException.class)
	public void test_get_event_date_as_date_throws_exception_on_wrong_type() {
		EventView view = new EventView();
		view.setDatePerformed("bad date type");
		
		view.getDatePerformedAsDate();
	}
	
	@Test
	public void set_printable_encodes_boolean() {
		EventView view = new EventView();
		
		view.setPrintable(true);
		assertEquals("Y", view.getPrintable());
		
		view.setPrintable(false);
		assertEquals("N", view.getPrintable());
	}
	
	@Test
	public void is_printable_allows_null() {
		EventView view = new EventView();
		assertNull(view.isPrintable());
	}
	
	@Test
	public void is_printable_is_true_on_Y() {
		EventView view = new EventView();
		view.setPrintable("Y");
		assertTrue(view.isPrintable());
	}
	
	@Test
	public void is_printable_defaults_false() {
		EventView view = new EventView();
		view.setPrintable("N");
		assertFalse(view.isPrintable());
		
		view.setPrintable("a");
		assertFalse(view.isPrintable());
		
		view.setPrintable("hello");
		assertFalse(view.isPrintable());
	}
}
