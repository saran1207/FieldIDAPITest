package com.n4systems.ejb.impl;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.FieldIdServiceTest;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.PredefinedLocationBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.location.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.test.TestTarget;
import com.n4systems.testutils.DummyEntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.easymock.EasyMock.*;

public class PredefinedLocationManagerTest extends FieldIdServiceTest {

    @TestTarget private PredefinedLocationManagerImpl predefinedLocationManager;
    private PredefinedLocationListLoader predefinedLocationListLoader;

    private UserSecurityFilter filter;
    private Tenant tenant;
    private BaseOrg org;
    private DummyEntityManager em;
    private PredefinedLocationTreeLoader predefinedLocationTreeLoader;
    private PredefinedLocationSaver predefinedLocationSaver;

    @Before
    public void setup() {
        super.setUp();
        //TestConfigContext.newContext().setEntry(ConfigEntry.MAX_SERIALS_PER_PROOFTEST, MAX_IDENTIFIERS);
        User user = UserBuilder.aFullUser().build();
        filter = new UserSecurityFilter(user);
        tenant = TenantBuilder.aTenant().build();
        org = OrgBuilder.aPrimaryOrg().build();
    }

    @After
    public void tearDown() {
        //TestConfigContext.resetToDefaultContext();
    }

    protected Object createSut(Field sutField) throws Exception {
        predefinedLocationTreeLoader = createMock(PredefinedLocationTreeLoader.class);
        predefinedLocationSaver = createMock(PredefinedLocationSaver.class);
        return new PredefinedLocationManagerImpl(em = new DummyEntityManager()) {
            @Override protected PredefinedLocationTreeLoader createPredefinedLocationTreeLoader(SecurityFilter securityFilter) {
                return predefinedLocationTreeLoader;
            }
            @Override protected PredefinedLocationSaver createSaver() {
                return predefinedLocationSaver;
            }
        };
    }

    @Test
    public void test_updateChildrenOwner() {
        PredefinedLocation node = new PredefinedLocation(tenant, null, org);
        node.setId(10L);
        List<PredefinedLocation> nodes = Lists.newArrayList();
        PredefinedLocationTree tree = new PredefinedLocationTree();

        PredefinedLocationTreeNode n1 = new PredefinedLocationTreeNode(PredefinedLocationBuilder.aRootPredefinedLocation().withId(10L).build());
        tree.addNode(n1);
        PredefinedLocationTreeNode n2 = new PredefinedLocationTreeNode(PredefinedLocationBuilder.aRootPredefinedLocation().withId(15L).build());
        tree.addNode(n2);
        PredefinedLocationTreeNode n11 = new PredefinedLocationTreeNode(PredefinedLocationBuilder.aPredefinedLocation().withId(20L).withParent(n1.getNodeValue()).build());
        n1.addChild(n11);
        PredefinedLocationTreeNode n111 = new PredefinedLocationTreeNode(PredefinedLocationBuilder.aPredefinedLocation().withId(30L).withParent(n11.getNodeValue()).build());
        n11.addChild(n111);
        PredefinedLocationTreeNode n112 = new PredefinedLocationTreeNode(PredefinedLocationBuilder.aPredefinedLocation().withId(35L).withParent(n11.getNodeValue()).build());
        n11.addChild(n112);

        expect(predefinedLocationTreeLoader.load(em)).andReturn(tree);
        replay(predefinedLocationTreeLoader);
        expect(predefinedLocationSaver.update(n1.getNodeValue())).andReturn(n1.getNodeValue());
        expect(predefinedLocationSaver.update(n11.getNodeValue())).andReturn(n11.getNodeValue());
        expect(predefinedLocationSaver.update(n111.getNodeValue())).andReturn(n111.getNodeValue());
        expect(predefinedLocationSaver.update(n112.getNodeValue())).andReturn(n112.getNodeValue());
        replay(predefinedLocationSaver);

        // expect it to update n1, n11, n111, n112 but NOT n2 because it isn't a child of n1.
        predefinedLocationManager.updateChildrenOwner(filter, node);
    }

}