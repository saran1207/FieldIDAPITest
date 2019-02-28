package com.n4systems.fieldid.service.user;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.services.config.ConfigService;
import com.n4systems.services.config.MutableRootConfig;
import com.n4systems.services.config.MutableSystemConfig;
import com.n4systems.services.config.RootConfig;
import com.n4systems.util.persistence.QueryBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Security;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private UserService userService;
    private PersistenceService persistenceServiceMock;
    private AdminUserService adminUserServiceMock;
    private ConfigService configServiceMock;

    @Before
    public void setUp() {
        userService = new UserService();
        persistenceServiceMock = mock(PersistenceService.class);
        adminUserServiceMock = mock(AdminUserService.class);
        configServiceMock = mock(ConfigService.class);
        userService.setPersistenceService(persistenceServiceMock);
        ReflectionTestUtils.setField(userService, "adminUserService", adminUserServiceMock);
        ReflectionTestUtils.setField(userService, "configService", configServiceMock);
    }

    @Test
    public void userIdIsUnique_When_User_Is_Unique_True() {
        when(persistenceServiceMock.exists(any(QueryBuilder.class))).thenReturn(false);
        assertTrue(userService.userIdIsUnique(123L,"user_id_Unique",4321L));
    }

    @Test
    public void userIdIsUnique_When_User_Is_Not_Unique_True() {
        when(persistenceServiceMock.exists(any(QueryBuilder.class))).thenReturn(true);
        assertFalse(userService.userIdIsUnique(123L,"user_id_Duplicate",4321L));
    }

    @Test
    public void userIdIsUnique_When_User_Is_Null_True() {
        when(persistenceServiceMock.exists(any(QueryBuilder.class))).thenReturn(true);
        assertTrue(userService.userIdIsUnique(123L,null,4321L));
    }

    @Test(expected=InvalidQueryException.class)
    public void userIdIsUnique_Persistence_Has_InvalidQueryException_True() {
        when(persistenceServiceMock.exists(any(QueryBuilder.class))).thenThrow(new InvalidQueryException("The expected message for InvalidQueryException"));
        userService.userIdIsUnique(123L,"user_id",4321L);
    }

    @Test
    public void authenticateUserByPassword_For_User_With_Correct_Password_True() {
        User currentUser = UserBuilder.aFullUser().build();
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","password")).thenReturn(null);
        assertNotNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    @Test
    public void authenticateUserByPassword_For_User_With_Wrong_Password_True() {
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(null);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","password")).thenReturn(null);
        assertNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    @Test
    public void authenticateUserByPassword_For_System_User_With_Wrong_Password_True() {
        User currentUser = UserBuilder.aSystemUser().build();
        MutableRootConfig mutableRootConfig = new MutableRootConfig();
        MutableSystemConfig mutableSystemConfig = new MutableSystemConfig();
        mutableSystemConfig.setSystemUserPassword("b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc55");
        mutableRootConfig.setSystem(mutableSystemConfig);
        RootConfig rootConfig = new RootConfig(mutableRootConfig);
        when(configServiceMock.getConfig(any(Long.class))).thenReturn(rootConfig);
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","password")).thenReturn(null);
        assertNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    @Test
    public void authenticateUserByPassword_For_System_User_With_Correct_Password_True() {
        User currentUser = UserBuilder.aSystemUser().build();
        MutableRootConfig mutableRootConfig = new MutableRootConfig();
        MutableSystemConfig mutableSystemConfig = new MutableSystemConfig();
        mutableSystemConfig.setSystemUserPassword("b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86");
        mutableRootConfig.setSystem(mutableSystemConfig);
        RootConfig rootConfig = new RootConfig(mutableRootConfig);
        when(configServiceMock.getConfig(any(Long.class))).thenReturn(rootConfig);
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","password")).thenReturn(null);
        assertNotNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    @Test
    public void authenticateUserByPassword_For_Admin_User_With_Correct_Password_True() {
        User currentUser = UserBuilder.aFullUser().build();
        User adminUser = UserBuilder.anAdminUser().build();
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","passwordSpecial")).thenReturn(adminUser);
        assertNotNull(userService.authenticateUserByPassword("n4","user_id","passwordSpecial"));
    }

    @Test
    public void authenticateUserByPassword_For_Admin_User_With_Wrong_Password_True() {
        User currentUser = UserBuilder.aFullUser().build();
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","passwordWrong")).thenReturn(null);
        assertNull(userService.authenticateUserByPassword("n4","user_id","passwordWrong"));
    }

    @Test(expected=InvalidQueryException.class)
    public void authenticateUserByPassword_Persistence_Has_InvalidQueryException_True() {

        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenThrow(new InvalidQueryException("The expected message for InvalidQueryException"));
        userService.authenticateUserByPassword("n4","user_id","password");
    }

}