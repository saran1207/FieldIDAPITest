package com.n4systems.fieldid.service.user;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.util.persistence.QueryBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    private UserService userService;
    private PersistenceService persistenceServiceMock;

    @Before
    public void setUp() {
        userService = new UserService();
        persistenceServiceMock = mock(PersistenceService.class);
        userService.setPersistenceService(persistenceServiceMock);
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
        assertTrue(userService.userSecurityCardNumberIsUnique(123L,"",4321L));
    }

}