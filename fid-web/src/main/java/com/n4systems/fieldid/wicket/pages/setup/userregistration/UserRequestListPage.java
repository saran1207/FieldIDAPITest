package com.n4systems.fieldid.wicket.pages.setup.userregistration;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.UserRequest;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by tracyshi on 2014-08-15.
 */
public class UserRequestListPage extends FieldIDTemplatePage {

    @SpringBean
    private UserService userService;

    private ListView listView;

    public UserRequestListPage() {

        listView = new ListView<UserRequest>("userRequestListView", getUserRequesets()) {
            @Override
            protected void populateItem(ListItem<UserRequest> item) {
                UserRequest userRequest = item.getModelObject();
                User userAccount = userRequest.getUserAccount();

                item.add(new Label("companyName", new PropertyModel<String>(userRequest, "companyName")));
                item.add(new Label("userId", new PropertyModel<String>(userAccount, "userID")));
                item.add(new Label("firstName", new PropertyModel<String>(userAccount, "firstName")));
                item.add(new Label("lastName", new PropertyModel<String>(userAccount, "lastName")));
                item.add(new Label("comment", new PropertyModel<String>(userRequest, "comment")));
                item.add(new Label("created", new PropertyModel<String>(userRequest, "created")));
                item.add(new BookmarkablePageLink<Void>("view", ViewUserRequestPage.class));
            }
        };

        add(listView);

    }

    private LoadableDetachableModel<List<UserRequest>> getUserRequesets() {
        return new LoadableDetachableModel<List<UserRequest>>() {
            @Override
            protected List<UserRequest> load() {
                return userService.getUserRequests(getTenant());
            }
        };
    }

    public boolean isEmpty() {
        return listView.getList().isEmpty();
    }

}
