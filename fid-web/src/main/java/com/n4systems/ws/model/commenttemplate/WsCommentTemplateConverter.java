package com.n4systems.ws.model.commenttemplate;


import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.ws.model.WsModelConverter;

public class WsCommentTemplateConverter extends WsModelConverter<CommentTemplate, WsCommentTemplate> {

	@Override
	public WsCommentTemplate fromModel(CommentTemplate model) {
		WsCommentTemplate wsModel = new WsCommentTemplate();
		wsModel.setId(model.getId());
		wsModel.setName(model.getName());
		wsModel.setComment(model.getComment());
		return wsModel;
	}

}
