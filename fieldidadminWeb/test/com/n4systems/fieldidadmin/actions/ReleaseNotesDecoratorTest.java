package com.n4systems.fieldidadmin.actions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.model.ui.releasenotes.BulletPoint;
import com.n4systems.model.ui.releasenotes.ReleaseNotes;
import com.n4systems.test.helpers.FluentArrayList;


public class ReleaseNotesDecoratorTest {

	
	@Test
	public void should_do_nothing_to_the_list_when_there_are_no_blank_bullets() throws Exception {
		List<BulletPoint> expectedList = new FluentArrayList<BulletPoint>(new BulletPoint("hello"), new BulletPoint("hello"));
		
		ReleaseNotesDecorator sut = new ReleaseNotesDecorator(new ReleaseNotes());
		sut.getBullets().addAll(expectedList);
		
		sut.cleanInputs();
		
		assertEquals(expectedList, sut.getBullets());
	}
	
	
	@Test
	public void should_remove_all_the_elements_when_they_are_all_blank() throws Exception {
		List<BulletPoint> expectedList = new ArrayList<BulletPoint>();
		
		ReleaseNotesDecorator sut = new ReleaseNotesDecorator(new ReleaseNotes());
		sut.getBullets().addAll(new FluentArrayList<BulletPoint>(BulletPoint.emptyBulletPoint(), BulletPoint.emptyBulletPoint()));
		
		sut.cleanInputs();
		
		assertEquals(expectedList, sut.getBullets());
	}
	
	
	@Test
	public void should_remove_all_the_blank_elements_even_when_they_are_not_continuous() throws Exception {
		List<BulletPoint> expectedList = new FluentArrayList<BulletPoint>(new BulletPoint("hello"), new BulletPoint("hello"));
		
		ReleaseNotesDecorator sut = new ReleaseNotesDecorator(new ReleaseNotes());
		sut.getBullets().addAll(new FluentArrayList<BulletPoint>(BulletPoint.emptyBulletPoint(), new BulletPoint("hello"), BulletPoint.emptyBulletPoint(), BulletPoint.emptyBulletPoint(), new BulletPoint("hello")));
		
		sut.cleanInputs();
		
		assertEquals(expectedList, sut.getBullets());
	}
}
