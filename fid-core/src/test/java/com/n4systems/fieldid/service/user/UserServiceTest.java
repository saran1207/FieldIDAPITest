package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.services.config.ConfigService;
import com.n4systems.services.config.MutableRootConfig;
import com.n4systems.services.config.MutableSystemConfig;
import com.n4systems.services.config.RootConfig;
import com.n4systems.tools.EncryptionUtility;
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

    @Test
    public void authenticateUserByPassword_For_User_With_Correct_Password_True() {
        User currentUser = UserBuilder.aFullUser().build();
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        assertNotNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    @Test
    public void authenticateUserByPassword_For_User_With_Wrong_Password_True() {
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(null);
        assertNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    /**
     * Expected SystemUserPassword not null or length > 128 - line #107
     * Otherwise SecurityException is thrown
     * Password in line #107 should be different from the String "password" produced by algorithm
     * from BouncyCastleProvider() - line #26
     */
    @Test
    public void authenticateUserByPassword_For_System_User_With_Wrong_Password_True() {
        User currentUser = UserBuilder.aSystemUser().build();
        MutableRootConfig mutableRootConfig = new MutableRootConfig();
        MutableSystemConfig mutableSystemConfig = new MutableSystemConfig();
        mutableSystemConfig.setSystemUserPassword(EncryptionUtility.getSHA512HexHash("passwordWrong"));
        mutableRootConfig.setSystem(mutableSystemConfig);
        RootConfig rootConfig = new RootConfig(mutableRootConfig);
        when(configServiceMock.getConfig(any(Long.class))).thenReturn(rootConfig);
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        assertNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    /**
     * Expected SystemUserPassword not null or length > 128 - line #107
     * Otherwise SecurityException is thrown
     * Password in line #107 is the String "password" produced by algorithm
     * from BouncyCastleProvider() - line #26
     */
    @Test
    public void authenticateUserByPassword_For_System_User_With_Correct_Password_True() {
        User currentUser = UserBuilder.aSystemUser().build();
        MutableRootConfig mutableRootConfig = new MutableRootConfig();
        MutableSystemConfig mutableSystemConfig = new MutableSystemConfig();
        mutableSystemConfig.setSystemUserPassword(EncryptionUtility.getSHA512HexHash("password"));
        mutableRootConfig.setSystem(mutableSystemConfig);
        RootConfig rootConfig = new RootConfig(mutableRootConfig);
        when(configServiceMock.getConfig(any(Long.class))).thenReturn(rootConfig);
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        assertNotNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    /**
     * Expected SystemUserPassword not null or length > 128
     * Otherwise SecurityException is thrown
     * with the message "System password not configured correctly"
     * at com.n4systems.services.config.SystemConfig.getSystemUserPassword(SystemConfig.java:66)
     */
    @Test(expected=SecurityException.class)
    public void authenticateUserByPassword_For_System_User_With_SecurityException_True() {
        User currentUser = UserBuilder.aSystemUser().build();
        MutableRootConfig mutableRootConfig = new MutableRootConfig();
        MutableSystemConfig mutableSystemConfig = new MutableSystemConfig();
        mutableSystemConfig.setSystemUserPassword("password");
        mutableRootConfig.setSystem(mutableSystemConfig);
        RootConfig rootConfig = new RootConfig(mutableRootConfig);
        when(configServiceMock.getConfig(any(Long.class))).thenReturn(rootConfig);
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        userService.authenticateUserByPassword("n4","user_id","password");
    }
    @Test
    public void authenticateUserByPassword_For_Admin_User_With_Correct_Password_True() {
        User currentUser = UserBuilder.aFullUser().build();
        User adminUser = UserBuilder.anAdminUser().build();
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","password")).thenReturn(adminUser);
        assertNotNull(userService.authenticateUserByPassword("n4","user_id","password"));
    }

    @Test
    public void authenticateUserByPassword_For_Admin_User_With_Wrong_Password_True() {
        User currentUser = UserBuilder.aFullUser().build();
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(currentUser);
        when(adminUserServiceMock.attemptSudoAuthentication("n4","user_id","passwordWrong")).thenReturn(null);
        assertNull(userService.authenticateUserByPassword("n4","user_id","passwordWrong"));
    }

    @Test
    public void userSecurityCardNumberIsUnique_When_CardNumber_Is_Unique_True() {
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(0L);
        assertTrue(userService.userSecurityCardNumberIsUnique(123L,"card_Number_Unique",4321L));
    }

    @Test
    public void userSecurityCardNumberIsUnique_When_CardNumber_Is_Not_Unique_True() {
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(1L);
        assertFalse(userService.userSecurityCardNumberIsUnique(123L,"card_Number_Duplicate",4321L));
    }

    @Test
    public void userSecurityCardNumberIsUnique_When_CardNumber_Is_Null_True() {
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(1L);
        assertTrue(userService.userSecurityCardNumberIsUnique(123L,null,4321L));
    }

    @Test
    public void userSecurityCardNumberIsUnique_When_CardNumber_Is_Empty_True() {
        when(persistenceServiceMock.find(any(QueryBuilder.class))).thenReturn(1L);
        assertTrue(userService.userSecurityCardNumberIsUnique(123L, "", 4321L));
    }

}