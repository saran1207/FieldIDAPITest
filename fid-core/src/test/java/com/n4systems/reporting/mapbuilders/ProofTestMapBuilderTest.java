package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.ProofTestInfo;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ReportMap;

public class ProofTestMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ProofTestInfo ptInfo = new ProofTestInfo();
		ptInfo.setPeakLoad(TestHelper.randomString());
		ptInfo.setDuration(TestHelper.randomString());
		ptInfo.setPeakLoadDuration(TestHelper.randomString());
		
		ProofTestMapBuilder builder = new ProofTestMapBuilder();

		ReportMap<Object> params = new ReportMap<Object>();
		builder.addParams(params, ptInfo, null);
		
		assertEquals(ptInfo.getPeakLoad(), params.get(ReportField.PROOF_TEST_PEAK_LOAD.getParamKey()));
		assertEquals(ptInfo.getDuration(), params.get(ReportField.PROOF_TEST_DURATION.getParamKey()));
		assertEquals(ptInfo.getPeakLoadDuration(), params.get(ReportField.PROOF_TEST_PEAK_LOAD_DURATION.getParamKey()));
		
	}
	
}
