package com.n4systems.fieldid.wicket.pages.admin.tenants;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.admin.tenant.TenantUserListPanel;
import com.n4systems.fieldid.wicket.data.AdminDataProviderWrapper;
import com.n4systems.fieldid.wicket.data.PlaceUsersDataProvider;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.fieldid.wicket.components.renderer.EnumPropertyChoiceRenderer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.UserType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.util.Arrays;

public class TenantUserListPage extends FieldIDAdminPage {

	@SpringBean private OrgService orgService;

	private Long tenantId;
	private IModel<String> searchTextModel;
	private IModel<UserType> userTypeModel;
	private TenantUserListPanel listPanel;

	public TenantUserListPage(PageParameters params) {
		super(params);
		tenantId = params.get("id").toLong();

		searchTextModel = new Model<String>();
		add(new TextField<String>("searchBox", searchTextModel).add(new FilterAjaxUpdateBehaviour()));

		userTypeModel = new Model<UserType>();
		add(new DropDownChoice<UserType>("type", userTypeModel, Arrays.asList(UserType.values()), new EnumPropertyChoiceRenderer<UserType>()).add(new FilterAjaxUpdateBehaviour()));

		// note the dummy Org model as we are not org filtering right now
		PlaceUsersDataProvider dataProvider = new PlaceUsersDataProvider(new Model<BaseOrg>(), "lastName, firstName", SortOrder.ASCENDING)
				.setSearchFilterModel(searchTextModel)
				.setTypeFilterModel(userTypeModel);

		add(listPanel = new TenantUserListPanel("userListPanel", new AdminDataProviderWrapper(tenantId, dataProvider)));
	}

	private void onAjaxFilterUpdate(AjaxRequestTarget target) {
		listPanel.detach();
		target.add(listPanel);
	}

	private class FilterAjaxUpdateBehaviour extends OnChangeAjaxBehavior {
		public FilterAjaxUpdateBehaviour() {
			setThrottleDelay(Duration.seconds(1));
		}

		@Override
		protected void onUpdate(final AjaxRequestTarget target) {
			onAjaxFilterUpdate(target);
		}
	}


}
