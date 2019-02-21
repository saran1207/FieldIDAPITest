package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import org.junit.Before;
import org.junit.Test;

import static com.n4systems.model.builders.TenantBuilder.n4;
import static junit.framework.Assert.assertTrue;

public class UserServiceTest extends FieldIdServiceTest {

    private @TestTarget UserService userService;
    private @TestMock OrgService orgService;
    private @TestMock UserGroupService userGroupService;
    private @TestMock PersistenceService persistenceService;
    private @TestMock SecurityContext securityContext;
    private User user;
    private User currentUser;
    private BaseOrg org;
    private UserSecurityFilter securityFilter;
    private Tenant tenant;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        tenant = n4();
        org = OrgBuilder.aPrimaryOrg().build();
        user = UserBuilder.aFullUser().build();
        securityFilter = new UserSecurityFilter(user);
        user.setTenant(tenant);
        user.setOwner(org);

        currentUser = UserBuilder.aFullUser().build();
        currentUser.setTenant(tenant);
        currentUser.setOwner(org);
    }

    @Test
    public void test_UserSecurityCardNumberIsUnique() {
        boolean isUserSecurityCardNumberUnique = userService.userSecurityCardNumberIsUnique(tenant.getId(),null,currentUser.getId());
        assertTrue(isUserSecurityCardNumberUnique);
        isUserSecurityCardNumberUnique = userService.userSecurityCardNumberIsUnique(tenant.getId(),"",currentUser.getId());
        assertTrue(isUserSecurityCardNumberUnique);
    }

}