package com.n4systems.model.commenttemplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "comment_templates")
public class CommentTemplate extends EntityWithTenant implements Listable<Long> {
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false, length=2500)
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

}
