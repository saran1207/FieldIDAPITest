package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.org.people.AddPlaceUserPanel;
import com.n4systems.fieldid.wicket.components.org.people.PeopleListPanel;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class PlacePeoplePage extends PlacePage {

    @SpringBean
    private UserService userService;

    private PeopleListPanel peopleListPanel;
    private AddPlaceUserPanel addPlaceUserPanel;
    private AjaxLink addUserLink;
    private AjaxLink viewAllUsersLink;

    public PlacePeoplePage(PageParameters params) {
        super(params);

        add(peopleListPanel = new PeopleListPanel("peopleListPanel", new PlaceUsersDataProvider("lastName, firstName", SortOrder.ASCENDING)));
        peopleListPanel.setOutputMarkupPlaceholderTag(true);

        add(addPlaceUserPanel = new AddPlaceUserPanel("addPlaceUserPanel", orgModel));
        addPlaceUserPanel.setOutputMarkupPlaceholderTag(true);
        addPlaceUserPanel.setVisible(false);

        add(addUserLink = new AjaxLink<Void>("addUserLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addUserLink.setVisible(false);
                viewAllUsersLink.setVisible(true);
                peopleListPanel.setVisible(false);
                addPlaceUserPanel.setVisible(true);
                target.add(addUserLink, viewAllUsersLink, peopleListPanel, addPlaceUserPanel);
            }
        });
        addUserLink.setOutputMarkupPlaceholderTag(true);

        add(viewAllUsersLink = new AjaxLink<Void>("viewAllUsersLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addUserLink.setVisible(true);
                viewAllUsersLink.setVisible(false);
                peopleListPanel.setVisible(true);
                addPlaceUserPanel.setVisible(false);
                target.add(addUserLink, viewAllUsersLink, peopleListPanel, addPlaceUserPanel);
            }
        });
        viewAllUsersLink.setOutputMarkupPlaceholderTag(true);
        viewAllUsersLink.setVisible(false);
    }

    private class PlaceUsersDataProvider extends FieldIDDataProvider<User> {

        private PlaceUsersDataProvider(String order, SortOrder sortOrder) {
            setSort(order, sortOrder);
        }

        @Override
        public Iterator<? extends User> iterator(int first, int count) {
            List<User> users = userService.getOrgUsers(orgModel.getObject(), getSort().getProperty(), getSort().isAscending());
            return users.subList(first, first+count).iterator();
        }

        @Override
        public int size() {
            return userService.countOrgUsers(orgModel.getObject()).intValue();
        }

        @Override
        public IModel<User> model(final User object) {
            return new AbstractReadOnlyModel<User>() {
                @Override
                public User getObject() {
                    return object;
                }
            };
        }
    }
}
