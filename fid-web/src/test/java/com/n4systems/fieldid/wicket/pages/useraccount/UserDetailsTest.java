package com.n4systems.fieldid.wicket.pages.useraccount;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.useraccount.details.UserDetailsWithFormPage;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.*;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.Session;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.*;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;

import static com.n4systems.model.builders.TenantBuilder.n4;
import static com.n4systems.model.builders.UserBuilder.anEmployee;
import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertTrue;

public class UserDetailsTest {

    private WicketTester tester;
    public Session session;
    private IModel<User> userModel;
    private IModel<InternalOrg> primaryOrgIModel;
    private UserDetailsWithFormPage userDetailsPage;

    protected UserService userService;
    protected PersistenceService persistenceService;

    @Before
    public void setUp() {
        User  user = createUser(15811299L,"theoc1", "Theo", "Cociorba", "theo.cociorba@ecompliance.com");
        userModel = createUserModel(user);
        Tenant tenant = n4();
        User aUser = anEmployee().build();
        aUser.setFirstName("Theo");

        primaryOrgIModel = createInternalOrgModel(user.getOwner().getPrimaryOrg());
        WebApplication webApp = new MockApplication() {
            public Session newSession(Request request, Response resposne) {
                return new FieldIDSession(request,user);
            }
        };
        tester = new WicketTester(webApp);
        session = Session.get();

        assertTrue(session instanceof FieldIDSession);
         webApp.getResourceSettings().setLocalizer(new Localizer() {

            @Override
            public String getString(final String key, final Component component, final String defaultValue) throws MissingResourceException {
                String val = super.getString(key, component, defaultValue);
                if (val.equals(defaultValue))
                    val = new FIDLabelModel(key).getObject();
                return val;

            }
            @Override
            public String getString(final String key, final Component component, final IModel<?> model)
                    throws MissingResourceException {
                try {
                    return super.getString(key, component, model);
                }
                catch(MissingResourceException ex) {
                    return new FIDLabelModel(key).getObject();
                }
            }
        });
    }

    private IModel<User> createUserModel(User user) {
        return Model.of(user);
    }

    private IModel<InternalOrg> createInternalOrgModel(PrimaryOrg primaryOrg) {
        return Model.of(primaryOrg);
    }
    protected void insertUsernameMyAccount(String userName, String firstName, String lastName, String email) {

        this.userDetailsPage = createUserDetailsPage();
        userService = createMock(UserService.class);
        persistenceService = createMock(PersistenceService.class);
        userDetailsPage.setUserService(userService);
        userDetailsPage.setPersistenceService(persistenceService);

        //start and render the test page
        tester.startPage(userDetailsPage);
        FormTester formTester = tester.newFormTester("form");
        //set credentials
        formTester.setValue("userName", userName);
        formTester.setValue("firstName", firstName);
        formTester.setValue("lastName", lastName);
        formTester.setValue("email", email);
        //submit form
        formTester.submit();
    }

    protected User createUser(Long id, String userId, String firstName, String lastName, String email) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailAddress(email);
        user.setUserID(userId);
        PrimaryOrg primaryOrg = new PrimaryOrg();
        primaryOrg.setId(15511493L);
        primaryOrg.setDateFormat("MM/dd/yy");
        Tenant tenant = new Tenant();
        tenant.setId(15511493L);
        tenant.setName("n4");

        BaseOrg owner = new BaseOrg() {
            @Override
            public PrimaryOrg getPrimaryOrg() {
                return primaryOrg;
            }

            @Override
            public InternalOrg getInternalOrg() {
                return null;
            }

            @Override
            public SecondaryOrg getSecondaryOrg() {
                return null;
            }

            @Override
            public CustomerOrg getCustomerOrg() {
                return null;
            }

            @Override
            public DivisionOrg getDivisionOrg() {
                return null;
            }

            @Override
            public String getFilterPath() {
                return null;
            }

            @Override
            public BaseOrg getParent() {
                return null;
            }

            @Override
            public BaseOrg enhance(SecurityLevel level) {
                return null;
            }
        };
        owner.setId(15511493L);
        owner.getPrimaryOrg().setDateFormat("MM/dd/yy");
        user.setOwner(owner);
        user.setTimeZoneID("Canada:Ontario - Ottawa");
        user.setTenant(tenant);

        return user;
    }

    @Test
    public void testMessageForDuplicateUser(){
        insertUsernameMyAccount("theoc1", "Theo", "Cociorba", "theo.cociorba@ecompliance.com");
        tester.assertErrorMessages("userName.errors.data.userduplicate");
    }

    @Test
    public void testMessageForUserNameRequired(){
        insertUsernameMyAccount("", "Theo", "Cociorba", "theo.cociorba@ecompliance.com");
        tester.assertErrorMessages("User Name is required");
    }

    @Test
    public void testMessageForFirstNameRequired(){
        insertUsernameMyAccount("theoc2", "", "Cociorba", "theo.cociorba@ecompliance.com");
        tester.assertErrorMessages("First Name is required");
    }

    @Test
    public void testMessageForLastNameRequired(){
        insertUsernameMyAccount("theoc2", "Theo", "", "theo.cociorba@ecompliance.com");
        tester.assertErrorMessages("Last Name is required");
    }

    @Test
    public void testMessageForEmailRequired(){
        insertUsernameMyAccount("theoc2", "Theo", "Cociorba", "");
        //tester.assertInfoMessages("User Saved");
        //tester.assertModelValue("form:userName", "theoc1");
        tester.assertErrorMessages("Email Address is required");
    }

    private UserDetailsWithFormPage createUserDetailsPage() {
        UserDetailsWithFormPage userDetailsPage = new UserDetailsWithFormPage(userModel,primaryOrgIModel) {

        //UserDetailsPage userDetailsPage = new UserDetailsPage( new Model(null)) {
            public LoaderFactory getLoaderFactory() {
                LoaderFactory loaderFactory = new LoaderFactory(null) {
                    public UserListLoader createUserListLoader() {
                        return new UserListLoader(null) {

                            @Override
                            public List<User> load() {
                                return load((EntityManager) null, (SecurityFilter) null);
                            }
                            @Override
                            protected List<User> load(EntityManager em, SecurityFilter filter) {
                                User user = new User();
                                user.setId(15811299L);
                                user.setFirstName("Theo");
                                user.setLastName("Cociorba");
                                user.setEmailAddress("theo.cociorba@ecompliance.com");
                                user.setUserID("theoc1");

                                return Arrays.asList(user);
                            }
                        };
                    }
                };
                return loaderFactory;
            }
        };
        return userDetailsPage;
    }

}
