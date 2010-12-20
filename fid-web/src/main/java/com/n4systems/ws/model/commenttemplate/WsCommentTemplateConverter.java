package com.n4systems.ws.model.commenttemplate;

import rfid.ejb.entity.CommentTempBean;

import com.n4systems.ws.model.WsModelConverter;

public class WsCommentTemplateConverter extends WsModelConverter<CommentTempBean, WsCommentTemplate> {

	@Override
	public WsCommentTemplate fromModel(CommentTempBean model) {
		WsCommentTemplate wsModel = new WsCommentTemplate();
		wsModel.setId(model.getUniqueID());
		wsModel.setName(model.getTemplateID());
		wsModel.setComment(model.getContents());
		return wsModel;
	}

}
