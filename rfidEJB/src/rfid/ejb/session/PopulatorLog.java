package rfid.ejb.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import rfid.ejb.PopulatorCriteria;
import rfid.ejb.entity.PopulatorLogBean;

import com.n4systems.tools.Pager;

@Local
public interface PopulatorLog {
	
	public enum logStatus{
		error ,
		success;
		
		public String getName() {
			return name();
		}
	};
	
	public enum logType{
		mobile,
		datapopulator,
		prooftest;
		
		public String getName() {
			return name();
		}
	};
	
	
	public Pager<PopulatorLogBean> findPopulatorLogBySearch(Long tenantId, PopulatorCriteria criteria, int pageNumber, int pageSize);
	public List<PopulatorLogBean> findAllPopulatorLog();
	public Long createPopulatorLog(PopulatorLogBean populatorLogBean);
	public void removePopulatorLog(Long uniqueID);
	
	/**
	 * @deprecated
	 */
	public void removeAllMessages();
	
	public void removeAllLogs();
	
	public ArrayList<String> findAllLogTypes(long tenantId);
	public ArrayList<String> findAllLogStatuses(long tenantId);

}
