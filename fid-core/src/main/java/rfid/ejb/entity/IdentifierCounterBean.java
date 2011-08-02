package rfid.ejb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.legacy.LegacyBeanTenant;

@Entity
@Table(name = "serialnumbercounter")
public class IdentifierCounterBean extends LegacyBeanTenant implements Saveable {
	private static final long serialVersionUID = 1L;
	
	private Long counter;
	private String decimalFormat;
	private Long daysToReset;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastReset;

	public Long getCounter() {
		return counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}

	public String getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(String decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	public Long getDaysToReset() {
		return daysToReset;
	}

	public void setDaysToReset(Long daysToReset) {
		this.daysToReset = daysToReset;
	}

	public Date getLastReset() {
		return lastReset;
	}

	public void setLastReset(Date lastReset) {
		this.lastReset = lastReset;
	}
}
