package com.n4systems.fieldid.wicket.pages.setup;

import java.text.SimpleDateFormat;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.progressbar.ProgressBar;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.orgs.PrimaryOrg;

public class YourPlanPage extends FieldIDFrontEndPage {
	
	@SpringBean
	private UserLimitService userLimitService;
	
	public YourPlanPage() {
		
        add(CSSPackageResource.getHeaderContribution("style/setup/your_plan.css"));

		SessionUser sessionUser = getSessionUser();
		PrimaryOrg primaryOrg = getSecurityGuard().getPrimaryOrg();
		
		
		add(new Label("companyId", sessionUser.getTenant().getDisplayName()));
		add(new Label("planName", primaryOrg.getSignUpPackage().getDisplayName()));
		add(new Label("createDate", new SimpleDateFormat(sessionUser.getDateFormat()).format(primaryOrg.getCreated())));
		
		add(new ProgressBar("fullUsersUsage").setValue(getUsage(userLimitService.getEmployeeUsersCount(), userLimitService.getMaxEmployeeUsers())));
		add(new Label("fullUsersCount", userLimitService.getEmployeeUsersCount().toString()));
		add(getUserMax("fullUsersMax", userLimitService.getMaxEmployeeUsers()));
		
		WebMarkupContainer liteUsersStatus = new WebMarkupContainer("liteUsersStatusContainer");
		liteUsersStatus.add(new ProgressBar("liteUsersUsage").setValue(getUsage(userLimitService.getLiteUsersCount(), userLimitService.getMaxLiteUsers())));
		liteUsersStatus.add(new Label("liteUsersCount", userLimitService.getLiteUsersCount().toString()));
		liteUsersStatus.add(getUserMax("liteUsersMax", userLimitService.getMaxLiteUsers()));
		add(liteUsersStatus.setVisible(userLimitService.isLiteUsersEnabled()));
		
		WebMarkupContainer readonlyUsersStatus = new WebMarkupContainer("readonlyUsersStatusContainer");
		readonlyUsersStatus.add(new ProgressBar("readonlyUsersUsage").setValue(getUsage(userLimitService.getReadOnlyUserCount(), userLimitService.getMaxReadOnlyUsers())));
		readonlyUsersStatus.add(new Label("readonlyUsersCount", userLimitService.getReadOnlyUserCount().toString()));
		readonlyUsersStatus.add(getUserMax("readonlyUsersMax", userLimitService.getMaxReadOnlyUsers()));
		add(readonlyUsersStatus.setVisible(userLimitService.isReadOnlyUsersEnabled()));
		
	}

	private Label getUserMax(String id, Integer maxUsers) {
		if(maxUsers == -1)
			return new Label(id, new FIDLabelModel("label.unlimited"));
		else
			return new Label(id, maxUsers.toString());
	}

	private int getUsage(Integer count, Integer maximum) {
		Long usage = 0L;
		if(maximum > 0) 
			usage = Math.round((count.doubleValue() * 100)/maximum.doubleValue());
		return usage.intValue();
	}

	@Override
	protected void addNavBar(String navBarId) {
		add(new NavigationBar(navBarId, 
				NavigationItemBuilder.aNavItem().label("nav.about_your_system").page(YourPlanPage.class).build()));
	}
	
    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, SettingsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }
    
    @Override
    protected Label createTitleLabel(String labelId) {
        return new FlatLabel(labelId, new FIDLabelModel("title.manage_field_id_plan.plural"));
    }
	
}
