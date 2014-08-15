package com.n4systems.fieldid.wicket.pages.setup.userregistration;

import com.n4systems.fieldid.service.user.UserRequestService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.UserRequest;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by tracyshi on 2014-08-15.
 */
public class UserRequestListPage extends FieldIDTemplatePage {

    @SpringBean
    private UserRequestService userRequestService;

    private ListView listView;
    private WebMarkupContainer noResults;

    public UserRequestListPage() {

        listView = new ListView<UserRequest>("userRequestListView", getUserRequests()) {
            @Override
            protected void populateItem(ListItem<UserRequest> item) {
                UserRequest userRequest = item.getModelObject();
                TimeZone timeZone = FieldIDSession.get().getSessionUser().getTimeZone();

                item.add(new Label("companyName", new PropertyModel<String>(userRequest, "companyName")));
                item.add(new Label("userId", new PropertyModel<String>(userRequest, "userAccount.userID")));
                item.add(new Label("firstName", new PropertyModel<String>(userRequest, "userAccount.firstName")));
                item.add(new Label("lastName", new PropertyModel<String>(userRequest, "userAccount.lastName")));
                item.add(new Label("comment", new PropertyModel<String>(userRequest, "comment")));
                item.add(new Label("created", new DayDisplayModel(new PropertyModel<Date>(userRequest, "created"), true, timeZone)));
                item.add(new BookmarkablePageLink<Void>("viewButton", ViewUserRequestPage.class, PageParametersBuilder.uniqueId(userRequest.getId())));
            }
        };

        add(listView);
        listView.setOutputMarkupId(true);

        add(noResults = new WebMarkupContainer("noResults"));
        noResults.setOutputMarkupPlaceholderTag(true);
        noResults.setVisible(isEmpty());
    }

    private LoadableDetachableModel<List<UserRequest>> getUserRequests() {
        return new LoadableDetachableModel<List<UserRequest>>() {
            @Override
            protected List<UserRequest> load() {
                return userRequestService.getAllUserRequests();
            }
        };
    }

    public boolean isEmpty() {
        return listView.getList().isEmpty();
    }

    @Override
    protected void addNavBar(String navBarId) {
        NavigationBar navBar = new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(UserRequestListPage.class).build());
        add(navBar);
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_user_registrations.plural"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

}
