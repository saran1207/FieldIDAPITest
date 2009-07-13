package com.n4systems.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
 *  This class is used to tie legacy ButtonState id's to our new
 *  Criteria and States.  It exists really only for hand-held service
 *  and should be deleted as soon as all hand-helds in the field upgrade 
 *  to protocol version 7.
 *  
 *  Also note, this object should be READ-ONLY.  All mappings have been created
 *  in migration 200809091230_migrate_product_types.rb (specifically by file state_set.rb).
 *  There should be no need to create any new mappings.
 *  
 *  TODO: Delete me ASAP
 */
@Deprecated
@Entity
@Table(name = "legacybuttonstatemappings")
public class LegacyButtonStateMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    private Long buttonStateId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "r_tenant")
    private TenantOrganization tenant;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Criteria criteria;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private State state;

	public LegacyButtonStateMapping() {	}

	public Long getButtonStateId() {
		return buttonStateId;
	}

	public TenantOrganization getTenant() {
		return tenant;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public State getState() {
		return state;
	}
}
