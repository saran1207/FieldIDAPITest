package com.n4systems.fieldid.wicket.pages.admin.tenants;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.admin.tenant.TenantUserListPanel;
import com.n4systems.fieldid.wicket.data.AdminDataProviderWrapper;
import com.n4systems.fieldid.wicket.data.PlaceUsersDataProvider;
import com.n4systems.fieldid.wicket.model.admin.AdminModelWrapper;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class TenantUserListPage extends FieldIDAdminPage {

	@SpringBean private OrgService orgService;

	private Long tenantId;

	public TenantUserListPage(PageParameters params) {
		super(params);
		tenantId = params.get("id").toLong();

		IModel<BaseOrg> orgModel = new AdminModelWrapper<BaseOrg>(tenantId, new LoadableDetachableModel<BaseOrg>() {
			@Override
			protected BaseOrg load() {
				return orgService.getPrimaryOrgForTenant(tenantId);
			}
		});

		add(new TenantUserListPanel("userListPanel", new AdminDataProviderWrapper(tenantId, new PlaceUsersDataProvider(orgModel, "lastName, firstName", SortOrder.ASCENDING))));
	}

}
