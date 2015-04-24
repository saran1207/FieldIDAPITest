package rfid.ejb.entity;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "identifier_counters")
public class IdentifierCounter extends EntityWithTenant implements Saveable {
	private static final long serialVersionUID = 1L;
	
	private Long counter;

	private String decimalFormat;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastReset;

	private Boolean resetAnnually;

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

	public Date getLastReset() {
		return lastReset;
	}

	public void setLastReset(Date lastReset) {
		this.lastReset = lastReset;
	}

	public Boolean isResetAnnually() { return resetAnnually; }

	public void setResetAnnually(Boolean resetAnnually) { this.resetAnnually = resetAnnually; }
}
