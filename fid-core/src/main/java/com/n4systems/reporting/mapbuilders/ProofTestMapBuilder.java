package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.ProofTestInfo;
import com.n4systems.persistence.Transaction;

public class ProofTestMapBuilder extends AbstractMapBuilder<ProofTestInfo> {

	public ProofTestMapBuilder() {
		super(
			ReportField.PROOF_TEST_PEAK_LOAD,
			ReportField.PROOF_TEST_DURATION,
			ReportField.PROOF_TEST_PEAK_LOAD_DURATION
		);
	}
	
	@Override
	protected void setAllFields(ProofTestInfo entity, Transaction transaction) {
		setField(ReportField.PROOF_TEST_PEAK_LOAD,			entity.getPeakLoad());
		setField(ReportField.PROOF_TEST_DURATION,			entity.getDuration());
		setField(ReportField.PROOF_TEST_PEAK_LOAD_DURATION,	entity.getPeakLoadDuration());
	}

}
