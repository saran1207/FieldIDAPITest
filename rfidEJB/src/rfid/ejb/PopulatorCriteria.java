package rfid.ejb;

import java.io.Serializable;
import java.util.Date;

import rfid.ejb.session.PopulatorLog;

public class PopulatorCriteria implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date fromDate;
	private Date toDate;
	private PopulatorLog.logStatus logStatus;
	private PopulatorLog.logType logType;

	
	public PopulatorCriteria() {
	}
	
	public Date getFromDate() {
		return fromDate;
	}


	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public PopulatorLog.logStatus getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(PopulatorLog.logStatus logStatus) {
		this.logStatus = logStatus;
	}


	public PopulatorLog.logType getLogType() {
		return logType;
	}

	public void setLogType(PopulatorLog.logType logType) {
		this.logType = logType;
	}


}
