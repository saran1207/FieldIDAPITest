package com.n4systems.ejb.legacy;

import com.n4systems.tools.Pager;
import rfid.ejb.PopulatorCriteria;
import rfid.ejb.entity.PopulatorLogBean;

public interface PopulatorLog {

	public enum logStatus {
		error, success;

		public String getName() {
			return name();
		}
	};

	public enum logType {
		mobile, datapopulator, prooftest;

		public String getName() {
			return name();
		}
	};

	public Pager<PopulatorLogBean> findPopulatorLogBySearch(Long tenantId, PopulatorCriteria criteria, int pageNumber, int pageSize);


	public Long createPopulatorLog(PopulatorLogBean populatorLogBean);


}
