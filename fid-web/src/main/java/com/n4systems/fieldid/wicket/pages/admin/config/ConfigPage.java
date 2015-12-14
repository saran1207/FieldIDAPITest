package com.n4systems.fieldid.wicket.pages.admin.config;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.config.ConfigService;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ConfigPage extends FieldIDAdminPage {

	@SpringBean private ConfigService configService;
	@SpringBean private OrgService orgService;

	private IModel<PrimaryOrg> selectedOrg = new Model<>();

	public ConfigPage() {
		final Component feedbackPanel;
		add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

		final Component config;
		add(config = new Label("config", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				Long tenantId = selectedOrg.getObject() != null ? selectedOrg.getObject().getTenant().getId() : null;
				return configService.getConfig(tenantId).toString();
			}
		}).setOutputMarkupId(true));

		add(new DropDownChoice<>("primaryOrgs", selectedOrg, getActivePrimaryOrgs())
			.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(config);
				}
			})
		);

		add(new Button("reload")
			.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
					try {
						configService.reloadConfigurations();
						FieldIDSession.get().cleanupFeedbackMessages();
					} catch (Exception e) {
						FieldIDSession.get().error(e.getMessage());
					} finally {
						target.add(config);
						target.add(feedbackPanel);
					}
				}
			})
		);
	}

	private LoadableDetachableModel<List<PrimaryOrg>> getActivePrimaryOrgs() {
		return new LoadableDetachableModel<List<PrimaryOrg>>() {
			@Override
			protected List<PrimaryOrg> load() {
				return orgService.getActivePrimaryOrgs();
			}
		};
	}
}
