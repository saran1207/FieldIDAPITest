package com.n4systems.fieldid.ws.v1.resources.commenttemplate;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiCommentTemplate extends ApiReadonlyModel {
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
