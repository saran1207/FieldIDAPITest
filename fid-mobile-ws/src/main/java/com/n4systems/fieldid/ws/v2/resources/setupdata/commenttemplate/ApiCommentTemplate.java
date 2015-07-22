package com.n4systems.fieldid.ws.v2.resources.setupdata.commenttemplate;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel2;

public class ApiCommentTemplate extends ApiReadOnlyModel2 {
	private String name;
	private String comment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
