package com.n4systems.fieldid.service.user;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userServiceTest;
    private PersistenceService persistenceServiceTest;

    @Before
    public void setUp() {
        userServiceTest = mock(UserService.class);
        persistenceServiceTest = mock(PersistenceService.class);
    }

    @Test
    public void userIdIsUnique_When_User_Is_Unique_True() {
        when(userServiceTest.userIdIsUnique(123L,"testUnique",4321L)).thenReturn(true);
        assertTrue(userServiceTest.userIdIsUnique(123L,"testUnique",4321L));
        verify(userServiceTest,atLeastOnce()).userIdIsUnique(123L,"testUnique",4321L);
    }

    @Test
    public void userIdIsUnique_When_User_Is_Not_Unique_True() {
        when(userServiceTest.userIdIsUnique(123L,"testFalse",4321L)).thenReturn(false);
        assertFalse(userServiceTest.userIdIsUnique(123L,"testFalse",4321L));
        verify(userServiceTest,atLeastOnce()).userIdIsUnique(123L,"testFalse",4321L);
    }

    @Test
    public void userIdIsUnique_Persistence_Returns_A_Result_True() {

        QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(UserGroup.class, new TenantOnlySecurityFilter(123L)).setCountSelect();
        queryBuilder.addWhere(WhereParameter.Comparator.EQ, "userID", "useID", "testUnique");
        queryBuilder.addWhere(WhereParameter.Comparator.NE, "id", "id", 4321L);

        when(persistenceServiceTest.exists(queryBuilder)).thenReturn(true);
        assertTrue(persistenceServiceTest.exists(queryBuilder));
        verify(persistenceServiceTest,atLeastOnce()).exists(queryBuilder);
    }

    @Test
    public void userIdIsUnique_Persistence_Returns_No_Results_True() {

        QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(UserGroup.class, new TenantOnlySecurityFilter(123L)).setCountSelect();
        queryBuilder.addWhere(WhereParameter.Comparator.EQ, "userID", "useID", "testFalse");
        queryBuilder.addWhere(WhereParameter.Comparator.NE, "id", "id", 4321L);

        when(persistenceServiceTest.exists(queryBuilder)).thenReturn(false);
        assertFalse(persistenceServiceTest.exists(queryBuilder));
        verify(persistenceServiceTest,atLeastOnce()).exists(queryBuilder);
    }

    @Test(expected=InvalidQueryException.class)
    public void userIdIsUnique_Persistence_Has_InvalidQueryException_True() {

        QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(UserGroup.class, new TenantOnlySecurityFilter(123L)).setCountSelect();
        queryBuilder.addWhere(WhereParameter.Comparator.EQ, "userIDTest", "useIDTest", "testException");
        queryBuilder.addWhere(WhereParameter.Comparator.NE, "id", "id", 4321L);

        when(persistenceServiceTest.exists(queryBuilder)).thenThrow(new InvalidQueryException("The expected message for InvalidQueryException"));
        persistenceServiceTest.exists(queryBuilder);
    }
}