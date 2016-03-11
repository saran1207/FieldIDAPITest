package com.n4systems.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertTrue;

@RunWith(value = Parameterized.class)
public class RangeOverLapTest {

	@Parameters
	public static Collection<Object[]> data() {
		Collection<Object[]> data = new ArrayList<Object[]>();
		
		data.add(new Object[] {new Range<Integer>(1, 10), new Range<Integer>(8, 20), TRUE});
		data.add(new Object[] {new Range<Integer>(1, 10),new Range<Integer>(-4, 5), TRUE});
		data.add(new Object[] {new Range<Integer>(1, 10),new Range<Integer>(2, 5), TRUE});
		data.add(new Object[] {new Range<Integer>(1, 10),new Range<Integer>(-2, 20), TRUE});
		data.add(new Object[] {new Range<Integer>(1, 10),new Range<Integer>(1, 10), TRUE});
		
		data.add(new Object[] {new Range<Integer>(5, 10),new Range<Integer>(1, 5), TRUE});
		
		data.add(new Object[] {new Range<Integer>(1, 10),new Range<Integer>(11, 20), FALSE});
		data.add(new Object[] {new Range<Integer>(1, 10),new Range<Integer>(-11, 0), FALSE});
		return data;
	}
	
	
	Range<Integer> range1;
	Range<Integer> range2;
	
	Boolean doOverLap;
	
	
	public RangeOverLapTest(Range<Integer> range1, Range<Integer> range2, Boolean doOverlap) {
		super();
		this.range1 = range1;
		this.range2 = range2;
		doOverLap = doOverlap;
	}
	
	@Test
	public void should_find_correct_overlap_value() throws Exception {
		assertTrue(doOverLap == range1.overlap(range2));
		assertTrue(doOverLap == range2.overlap(range1));
	}

	
}
