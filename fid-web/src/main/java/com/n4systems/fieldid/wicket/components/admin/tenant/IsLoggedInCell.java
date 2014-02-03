package com.n4systems.fieldid.wicket.components.admin.tenant;

import com.n4systems.fieldid.servlets.ConcurrentLoginSessionListener;
import com.n4systems.fieldid.wicket.model.BooleanModel;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class IsLoggedInCell extends Panel {

	public IsLoggedInCell(String id, IModel<User> model) {
		super(id, model);
		add(new Label("isLoggedIn", new BooleanModel(new Model<Boolean>(isUserLoggedIn(model)))));
	}

	private boolean isUserLoggedIn(IModel<User> model) {
		return ConcurrentLoginSessionListener.getValidSessionsForUser(model.getObject().getId()).size() > 0;
	}

}