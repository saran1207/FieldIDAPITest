package com.n4systems.fieldid.utils;

import com.n4systems.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class CopyEventFactoryTest {
	private static final Long STATE_ID = 1L;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCopyCriteriaResult() {

        ThingEvent i = new ThingEvent();
		
		Set<CriteriaResult> results = new HashSet<CriteriaResult>();
		OneClickCriteriaResult criteriaResult = new OneClickCriteriaResult();
		Button button = new Button();
		button.setId( STATE_ID );
		criteriaResult.setButton(button);
		criteriaResult.setId( 2L );
		results.add( criteriaResult );
		
		
		Recommendation r = new Recommendation();
		r.setText( "woot" );
		r.setId( 1L );
		criteriaResult.getRecommendations().add( r );
		
		Set<CriteriaResult> copiedResults = CopyEventFactory.copyCriteriaResults( results, i );
		
		assertEquals( results.size(), copiedResults.size() );
		CriteriaResult copiedResult = copiedResults.iterator().next();
		assertEquals( criteriaResult.getButton(), ((OneClickCriteriaResult)copiedResult).getButton() );
		assertEquals( criteriaResult, copiedResult );
		assertEquals( criteriaResult.getRecommendations().size(), copiedResult.getRecommendations().size() );
		
		criteriaResult.getRecommendations().clear();
		assertFalse( criteriaResult.getRecommendations().size() == copiedResult.getRecommendations().size() );
		
		copiedResult.setId( 4L );
		assertFalse( criteriaResult.equals( copiedResult ) );
	}
	
	@Test
	public void testCopyRecommendations() {
		List<Recommendation> recommendations = new ArrayList<Recommendation>();
		Recommendation r = new Recommendation();
		r.setText( "woot" );
		r.setId(1L);
		recommendations.add(r);
		
		List<Recommendation> copiedRecommendations = CopyEventFactory.copyRecommendations( recommendations );
		
		assertEquals( recommendations.size(), copiedRecommendations.size() );
		Recommendation copiedRecommendation = copiedRecommendations.iterator().next();
		assertEquals(r.getText(), copiedRecommendation.getText());
		assertEquals(r, copiedRecommendation);
		
		copiedRecommendation.setId( 4L );
		assertFalse( r.equals( copiedRecommendation ) );
	}

}
