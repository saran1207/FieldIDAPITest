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
}