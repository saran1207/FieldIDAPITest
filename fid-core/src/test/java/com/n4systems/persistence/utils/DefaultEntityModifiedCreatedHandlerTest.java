package com.n4systems.persistence.utils;

import com.n4systems.fieldid.context.InteractionContext;
import com.n4systems.model.eula.EULA;
import com.n4systems.model.user.User;
import com.n4systems.util.time.StoppedClock;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class DefaultEntityModifiedCreatedHandlerTest {

    private static final DateFormat TEST_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final String TEST_DATE = "02/05/2011";

    private InteractionContext mockInteractionContext;
    private DefaultEntityModifiedCreatedHandler modifiedCreatedHandler;
    private Date testDate;

    @Before
    public void setup() throws Exception {
        mockInteractionContext = createMock(InteractionContext.class);
        testDate = TEST_DATE_FORMAT.parse(TEST_DATE);
        modifiedCreatedHandler = new DefaultEntityModifiedCreatedHandler(mockInteractionContext, new StoppedClock(testDate));
    }

    @Test
    public void insert_abstract_entity_should_set_value_from_context() {
        User user = new User();
        EULA eula = new EULA();

        setupMockContextForGetUser(user);

        modifiedCreatedHandler.onCreate(eula);


        assertSame(user, eula.getCreatedBy());
        assertSame(user, eula.getModifiedBy());

        assertEquals(testDate, eula.getCreated());
        assertEquals(testDate, eula.getModified());
    }

    @Test
    public void update_abstract_entity_should_set_value_from_context() {
        User user = new User();
        EULA eula = new EULA();

        setupMockContextForGetUser(user);

        modifiedCreatedHandler.onUpdate(eula);

        assertSame(user, eula.getModifiedBy());
        assertEquals(testDate, eula.getModified());
    }

    @Test
    public void update_abstract_entity_should_set_modified_to_null_when_no_user_in_context() {
        EULA eula = new EULA();

        setupMockContextForGetUser(null);

        modifiedCreatedHandler.onUpdate(eula);

        assertNull(eula.getModifiedBy());
        assertEquals(testDate, eula.getModified());
    }

    @Test
    public void insert_entity_with_created_already_set_isnt_overwritten() throws Exception {
        Date preExistingDate = TEST_DATE_FORMAT.parse("01/01/2001");

        User user = new User();
        EULA eula = new EULA();
        eula.setCreated(preExistingDate);

        setupMockContextForGetUser(user);
        modifiedCreatedHandler.onCreate(eula);

        assertSame(user, eula.getCreatedBy());
        assertEquals(preExistingDate, eula.getCreated());
    }

    private void setupMockContextForGetUser(User user) {
        if (user != null)
            user.setUserID("testuser");

        expect(mockInteractionContext.getCurrentUser()).andReturn(user).anyTimes();
        expect(mockInteractionContext.isEnabled()).andReturn(Boolean.TRUE).anyTimes();
        replay(mockInteractionContext);
    }

}
