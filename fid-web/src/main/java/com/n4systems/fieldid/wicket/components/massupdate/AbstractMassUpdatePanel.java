package com.n4systems.fieldid.wicket.components.massupdate;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class AbstractMassUpdatePanel extends Panel {

	protected AbstractMassUpdatePanel previousPanel;

    @SpringBean
    protected UserService userService;

    public AbstractMassUpdatePanel(String id) {
		super(id);
	}

	public AbstractMassUpdatePanel(String id, IModel<?> model) {
		super(id, model);
	}

	public AbstractMassUpdatePanel getPreviousPanel() {
		return previousPanel;
	}
	
	protected void onCancel() {}

	public boolean isDetailsPanel() {
		return false;
	}

	public boolean isConfirmPanel() {
		return false;
	}

    protected User getCurrentUser() {
        return userService.getUser( FieldIDSession.get().getSessionUser().getId());
    }
}