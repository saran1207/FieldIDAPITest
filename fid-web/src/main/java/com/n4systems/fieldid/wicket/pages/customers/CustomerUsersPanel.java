package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.components.table.StyledAjaxPagingNavigator;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.user.AddCustomerUserPage;
import com.n4systems.fieldid.wicket.pages.setup.user.ViewUserPage;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserPaginatedLoader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.UserType;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by agrabovskis on 2018-02-08.
 */
public class CustomerUsersPanel extends Panel {

    static final private Logger logger = Logger.getLogger(CustomerUsersPanel.class);

    @SpringBean
    private UserLimitService userLimitService;

    @SpringBean
    private OrgService orgService;

    private IModel<Long> customerSelectedForEditModel;
    private IModel<SessionUser> sessionUserModel;
    private LoaderFactory loaderFactory;
    private int currentResultCount = 0;

    public CustomerUsersPanel(String id, IModel<Long> customerSelectedForEditModel, IModel<SessionUser> sessionUserModel, LoaderFactory loaderFactory) {
        super(id);
        this.customerSelectedForEditModel = customerSelectedForEditModel;
        this.sessionUserModel = sessionUserModel;
        this.loaderFactory = loaderFactory;
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer addUserSection = new WebMarkupContainer("addUserSection");
        add(addUserSection);
        addUserSection.setVisible(userLimitService.isReadOnlyUsersEnabled());

        addUserSection.add(new Link("addUser") {
            @Override
            public void onClick() {
                setResponsePage(AddCustomerUserPage.class, PageParametersBuilder.param("customerId", customerSelectedForEditModel.getObject()));
                // '/w/setup/addCustomerUser?customerId=${customer.id}'
            }
        });

        /* Create the loader for the user list */
        final Integer pageSize = ConfigService.getInstance().getInteger(ConfigEntry.WEB_PAGINATION_PAGE_SIZE);

        /* Create loaders within models to avoid serializing them */
        final IModel<UserPaginatedLoader> userLoaderModel =
                new LoadableDetachableModel<UserPaginatedLoader>() {

                    protected UserPaginatedLoader load() {
                        UserPaginatedLoader loader = getLoaderFactory().createUserPaginatedLoader();
                        loader.setPageSize(pageSize);
                        loader.withCustomer((CustomerOrg) orgService.findById(customerSelectedForEditModel.getObject()));
                        return loader;
                    }
                };
        currentResultCount = (int) userLoaderModel.getObject().load().getTotalResults();


        /* Create the user list top level containers */

        final WebMarkupContainer resultList = new WebMarkupContainer("userList") {
            @Override
            public boolean isVisible() {
                return currentResultCount > 0;
            }
        };
        resultList.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(resultList);

        final WebMarkupContainer noResults = new WebMarkupContainer("noResults") {
            @Override
            public boolean isVisible() {
                return currentResultCount == 0;
            }
        };
        noResults.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(noResults);

        final IDataProvider userDataProvider = new UserDataProvider(userLoaderModel, currentResultCount, pageSize);

        final DataView<User> lineItemsView =
                new DataView<User>("result.list", userDataProvider) {
                    @Override
                    public void populateItem(final Item<User> item) {

                        final User user = item.getModelObject();
                        item.add(new Link("viewUserLink") {
                            @Override
                            public void onClick() {
                                System.out.println("view link clicked");
                                //<a href="<@s.url action="viewUser" uniqueID="${user.id!}" includeParams="get"/>" >${user.userID?html!}</a>
                                setResponsePage(ViewUserPage.class, PageParametersBuilder.param("uniqueID", user.getId()));
                            }
                            @Override
                            public boolean isVisible() {
                                return user.getUserType() != UserType.PERSON;
                            }
                        }.add(new Label("label", Model.of(user.getUserID()))));
                        item.add(new Label("userName", Model.of(user.getUserLabel())));
                        DivisionOrg userDivisionOrg = user.getOwner().getDivisionOrg();
                        item.add(new Label("userDivision", Model.of(userDivisionOrg != null ? userDivisionOrg.getName() : "")));
                        item.add(new Label("userEmail", Model.of(user.getEmailAddress())));
                        // ${(action.dateCreated(user)??)?string(action.formatDateTime(action.dateCreated(user)), "--")}
                        /* DateCreated */
                        String lastLoginValue = "--";
                        Date lastLoginDate = user.getLastLogin();
                        if (lastLoginDate != null) {
                            lastLoginValue = new FieldIdDateFormatter(lastLoginDate, getSessionUser(), true, true).format();
                        }
                        item.add(new Label("lastLogin", Model.of(lastLoginValue)));

                        // <a href="<@s.url action="customersUserArchive" uniqueID="${(user.id)!}" includeParams="get"/>"><@s.text name="label.archive" /></a>
                        item.add(new AjaxLink("archiveLink") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                doArchive(user);
                                target.addChildren(getPage(), FeedbackPanel.class);
                                userLoaderModel.detach();
                                target.add(resultList);
                                target.add(noResults);
                            }
                        });
                    }
                };

        lineItemsView.setOutputMarkupId(true);
        lineItemsView.setItemsPerPage(pageSize);


        resultList.add(lineItemsView);
        resultList.add(new StyledAjaxPagingNavigator("resultTableNavigatorTop", lineItemsView, resultList) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultList);
            }
        });
        resultList.add(new StyledAjaxPagingNavigator("resultTableNavigatorBottom", lineItemsView, resultList) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultList);
            }
        });
    }

    private void doArchive(User user) {
        if (user == null || user.getId() == null) {
            error(getString("error.no_user"));
            throw new MissingEntityException("user is required.");
        }
        try {
            user.archiveUser();
            new UserSaver().update(user);
            logger.info("Archived user " + user.getUserID());
            info(getString("message.userarchived"));
        } catch (Exception e) {
            error(getString("error.failedtoarchive"));
            logger.error("failed to archive user " + user.getUserID(), e);
        }
    }

    private LoaderFactory getLoaderFactory() {
        return loaderFactory;
    }

    private SessionUser getSessionUser() {
        return sessionUserModel.getObject();
    }

    private class UserDataProvider implements IDataProvider<User> {

        private IModel<UserPaginatedLoader> loader;
        private int currentCount;
        private int pageSize;

        public UserDataProvider(IModel<UserPaginatedLoader> loader, int currentCount, int pageSize) {
            this.loader = loader;
            this.currentCount = currentCount;
            this.pageSize = pageSize;
        }

        @Override
        public void detach() {
        }

        /* Gets an iterator for the subset of total data */
        @Override
        public Iterator iterator(int first, int count) {
            int currentPage = first / pageSize + 1;
            loader.getObject().setPage(currentPage);
            return loader.getObject().load().getList().iterator();
        }

        /* Gets total number of items in the collection represented by the DataProvider */
        @Override
        public int size() {
            return currentResultCount;
        }

        @Override
        public IModel model(User object) {
            return new AbstractReadOnlyModel<User>() {
                @Override
                public User getObject() {
                    return object;
                }
            };
        }
    }
}
