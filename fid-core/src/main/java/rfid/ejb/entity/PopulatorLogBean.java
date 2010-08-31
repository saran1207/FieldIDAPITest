package rfid.ejb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.model.Tenant;
import com.n4systems.model.parents.legacy.LegacyBeanTenant;

@Entity
@Table(name = "PopulatorLog")
public class PopulatorLogBean extends LegacyBeanTenant {
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeLogged;

	@Enumerated(EnumType.STRING)
	private PopulatorLog.logStatus logStatus;
	@Enumerated(EnumType.STRING)
	private PopulatorLog.logType logType;

	private String logMessage;

	public PopulatorLogBean() {
	}

	public PopulatorLogBean(Tenant tenant, String logMessage,
			PopulatorLog.logStatus logStatus, PopulatorLog.logType logType) {

		setTenant(tenant);
		setLogMessage(logMessage);
		setLogStatus(logStatus);
		setLogType(logType);
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public Date getTimeLogged() {
		return timeLogged;
	}

	public void setTimeLogged(Date timeLogged) {
		this.timeLogged = timeLogged;
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
