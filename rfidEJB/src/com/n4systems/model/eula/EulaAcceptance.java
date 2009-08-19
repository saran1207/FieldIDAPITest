package com.n4systems.model.eula;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.exceptions.NotUpdatableException;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "eulaacceptances")
public class EulaAcceptance extends EntityWithTenant implements FilteredEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	private UserBean acceptor;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(fetch = FetchType.EAGER)
	private EULA eula;

	public EulaAcceptance() {
		super();
		date = new Date();
	}

	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD, null, null);
	}
	
	
	@Override
	protected void onUpdate() {
		throw new NotUpdatableException("you can not update this entity.");
	}
	
	
	

	public EulaAcceptance(Tenant tenant) {
		super(tenant);
	}

	public UserBean getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(UserBean acceptor) {
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
