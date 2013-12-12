package com.n4systems.fieldid.wicket.components.admin.tenant;

import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.user.User;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class LoginAsCell extends Panel {

	@SpringBean private AdminUserService adminUserService;
	@SpringBean private ConfigService configService;

	public LoginAsCell(String id, final IModel<User> model) {
		super(id, model);

		add(new AjaxLink<User>("loginLink", model) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				adminUserService.createSudoPermission(FieldIDSession.get().getAdminUser(), model.getObject());
				target.appendJavaScript("window.open('" + formatLoginURL(model.getObject()) + "', '_blank')");
			}
		});
	}

	private String formatLoginURL(User user) {
		String systemProtocol = configService.getString(ConfigEntry.SYSTEM_PROTOCOL);
		String systemDomain = configService.getString(ConfigEntry.SYSTEM_DOMAIN);

		String url = String.format("%s://%s.%s/fieldid/login.action?userName=%s", systemProtocol, user.getTenant().getName(), systemDomain, user.getUserID());
		return url;
	}

}
