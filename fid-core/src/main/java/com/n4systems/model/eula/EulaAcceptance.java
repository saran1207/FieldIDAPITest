package com.n4systems.model.eula;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.exceptions.NotUpdatableException;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "eulaacceptances")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EulaAcceptance extends EntityWithTenant {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	private User acceptor;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(fetch = FetchType.EAGER)
	private EULA eula;

	public EulaAcceptance() {
		super();
		date = new Date();
	}
	
	@Override
	protected void onUpdate() {
		throw new NotUpdatableException("you can not update this entity.");
	}

	public EulaAcceptance(Tenant tenant) {
		super(tenant);
	}

	public User getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(User acceptor) {
		if (!acceptor.isAdmin()) {
			throw new InvalidArgumentException("must be an admin user");
		}
		this.acceptor = acceptor;
	}

	public Date getDate() {
		return date;
	}

	public EULA getEula() {
		return eula;
	}

	public void setEula(EULA eula) {
		this.eula = eula;
	}

}
