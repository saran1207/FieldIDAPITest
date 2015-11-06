package com.n4systems.model.event;

import com.n4systems.model.user.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class AssignedToUpdate implements Serializable{
	
	public static final User UNASSIGNED = null;

	public static AssignedToUpdate assignAssetToUser(User assignedTo) {
		return new AssignedToUpdate(assignedTo, true);
	}
	
	public static AssignedToUpdate unassignAsset() {
		return new AssignedToUpdate(UNASSIGNED, true);
	}

	public static AssignedToUpdate ignoreAssignment() {
		return new AssignedToUpdate();
	}
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	private User assignedUser;
	
	private Boolean assignmentApplyed;

	protected AssignedToUpdate() {
		this(null, null);
	}
	
	private AssignedToUpdate(User assignedUser, Boolean assignmentApplyed) {
		super();
		this.assignedUser = assignedUser;
		this.assignmentApplyed = assignmentApplyed;
	}


	public User getAssignedUser() {
		return assignedUser;
	}

	public boolean isAssignmentApplyed() {
		return assignmentApplyed != null && assignmentApplyed;
	}

	public boolean isIgnoredAssignment() {
		return !isAssignmentApplyed();
	}

	@Override
	public String toString() {
		if (isIgnoredAssignment()) {
			return "ignored Assigned To Update";
		}
		return "AssignedToUpdate for [" + ((assignedUser != null) ? assignedUser.getUserLabel() : "-- NO ONE ---") + "]" ;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public boolean isUnassigned() {
		return assignedUser == UNASSIGNED;
	}
	
	
	
	
}
