package com.n4systems.model.security;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.terms.completedordue.WithinUserGroupTerm;

import javax.persistence.Query;
import java.util.TimeZone;

/**
 * CAVEAT : this filter behaves differently if the owner is a primary org.
 * it doesn't really do anything and you will need to make sure you add tenant security as well.
 * i.e. if you use    new OwnerAndDownFilter(aPrimaryOrg),   you might get results from another tenant! yikes.
 */
public class UserSecurityFilter extends AbstractSecurityFilter {
	
	private final BaseOrg filterOrg;
    private User user;
    private TimeZone timeZone;
    private boolean showArchived = false; //TODO May be we should consider moving this up the class hierarchy as it is also used by TenantOnlySecurityFilter - Kumana.


    public UserSecurityFilter(BaseOrg filterOrg, User user, TimeZone timeZone) {
		this.filterOrg = filterOrg;
		this.user = user;
        this.timeZone = timeZone;
	}
	
	public UserSecurityFilter(User user) {
		this(user.getOwner(), user, user.getTimeZone());
	}

	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered() && !showArchived) {
			addFilterParameter(builder, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isTenantFiltered()) {
			// The tenant filter is almost always applied along with the user/owner filter as a safety precaution
			addFilterParameter(builder, definer.getTenantPath(), getTenantId());
			
			if (definer.isUserFiltered() && user != null) {
				// we check to see if this is user filtered first since you can't get more specific then a user filter
				addFilterParameter(builder, definer.getUserPath(), user.getId());
				
			} else if (definer.isOwnerFiltered()) {
				// we don't need to add an owner filter for a PrimaryOrg since it's 1 to 1 with the tenant
				if (filterOrg.isSecondary()) {
					addEqualOrNullFilterParameter(builder, prepareFullOwnerPath(definer, filterOrg), filterOrg.getId());
				} else if (filterOrg.isExternal()) {
					addFilterParameter(builder, prepareFullOwnerPath(definer, filterOrg), filterOrg.getId());
				}
			}
		}

        if (filterOrg.getPrimaryOrg().hasExtendedFeature(ExtendedFeature.UserGroupFiltering)
                && definer.isEventUserGroupFiltered() && user != null && user.getGroups().size() > 0) {
            new WithinUserGroupTerm(null, user)
                    .applyToQuery(builder);
        }

	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered() && !showArchived) {
			setParameter(query, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isTenantFiltered()) {
			setParameter(query, definer.getTenantPath(), getTenantId());
			
			if (definer.isUserFiltered() && user != null) {
				setParameter(query, definer.getUserPath(), user.getId());
				
			} else if (definer.isOwnerFiltered() && !filterOrg.isPrimary()) {
				setParameter(query, prepareFullOwnerPath(definer, filterOrg), filterOrg.getId());
			}
		}
	}

	@Override
	protected String produceWhereClause(String alias, SecurityDefiner definer) throws SecurityException {
		StringBuilder clauses = new StringBuilder();
		boolean firstArgument = true;
		
		if (definer.isStateFiltered()) {
			addFilterClause(clauses, definer.getStatePath(), alias, false);
			firstArgument = false;
		}
		
		if (definer.isTenantFiltered()) {
			// The tenant filter is almost always applied along with the user/owner filter as a safety precaution
			// If this is the first argument, do not prepend an AND
			addFilterClause(clauses, definer.getTenantPath(), alias, !firstArgument);
			
			if (definer.isUserFiltered()) {
				// we check to see if this is user filtered first since you can't get more specific then a user filter
				addFilterClause(clauses, definer.getUserPath(), alias, true);
				
			} else if (definer.isOwnerFiltered()) {
				// we don't need to add an owner filter for a PrimaryOrg since it's 1 to 1 with the tenant
				if (filterOrg.isSecondary()) {
					addEqualOrNullFilterClause(clauses, prepareFullOwnerPath(definer, filterOrg), alias, true);
				} else if (filterOrg.isExternal()) {
					addFilterClause(clauses, prepareFullOwnerPath(definer, filterOrg), alias, true);
				}
				
			}
		}
		
		return clauses.toString();
	}

	public Long getTenantId() {
		return filterOrg.getTenant().getId();
	}
	
	public BaseOrg getOwner() {
		return filterOrg;
	}
	
	public Long getUserId() {
		return user == null ? null : user.getId();
	}

	public boolean hasOwner() {
		return filterOrg != null;
	}

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public User getUser() {
        return user;
    }
    
    public UserSecurityFilter setShowArchived(boolean showArchived) {
		this.showArchived = showArchived;
		return this;
	}
}
