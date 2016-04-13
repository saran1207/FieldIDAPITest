package com.n4systems.fieldid.wicket.pages.admin;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDWicketPage;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.model.admin.AdminUserType;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.UrlEncoder;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.File;

public class FieldIDAdminPage extends FieldIDWicketPage {
	
	public FieldIDAdminPage() {
		this(null);
	}
	
	public FieldIDAdminPage(PageParameters params) {
        super(params);
        
        if (!FieldIDSession.get().isAdminConsoleAuthenticated()) {
            throw new RedirectToUrlException("/admin/signIn.action");
        }

        setupNavigation();
    }

	private void setupNavigation() {
		AdminUser adminUser = FieldIDSession.get().getAdminUser();
		boolean isSuperUser = adminUser.getType().equals(AdminUserType.SUPER);

		NavigationItemBuilder navItemBuilder = NavigationItemBuilder.aNavItem();
		add(new NavigationBar("navBar",
			navItemBuilder.page("/admin/organizations.action").label("Tenants").build(),
			navItemBuilder.page("/w/admin/users").label("Users").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/eulas.action").label("EULA").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/orderMappingList.action").label("Order Mappings").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/mailTest.action").label("Mail").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/certSelection.action").label("Certs").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/taskCrud.action").label("Tasks").cond(isSuperUser).build(),
			navItemBuilder.page("/w/admin/config").label("Config").cond(isSuperUser).build(),
            navItemBuilder.page("/w/admin/configureLanguages").label("Languages").cond(isSuperUser).build()
		));
	}

	/**
	 * Theoretically, we can call-back to this method from any Component/Panel as long as the base page is, in fact,
	 * this Page class.
	 *
	 * This method was ripped out of some other Wicket components and allows the developer to send a file downstream
	 * to the user by directly manipulating the request cycle.  This functionality is offered by pre-canned components
	 * starting in Wicket 6, so the use of this method may be rather short-lived.
	 *
	 * The File which the user will download is represented by fileToDownload, and the name that will be given to
	 * the download is described by fileName.
	 *
	 * @param fileToDownload - A File object that will be downloaded by the user.
	 * @param fileName - A String representing the desired name for the File object.
	 */
	public void handleDownload(File fileToDownload, String fileName) {
		fileName = UrlEncoder.QUERY_INSTANCE.encode(fileName, getRequest().getCharset());

		IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(fileToDownload));

		getRequestCycle().scheduleRequestHandlerAfterCurrent(
				new ResourceStreamRequestHandler(resourceStream) {
					@Override
					public void respond(IRequestCycle requestCycle) {
						super.respond(requestCycle);

						Files.remove(fileToDownload);
					}
				}.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT)
		);
	}

}
