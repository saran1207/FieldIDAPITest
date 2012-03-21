package com.n4systems.services.dashboard;

import com.n4systems.fieldid.FieldIdServicesUnitTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.persistence.QueryBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;


public class DashboardServiceTest extends FieldIdServicesUnitTest {
	
	@TestTarget private DashboardService dashboardService;
	
	@TestMock private PersistenceService persistenceService;
	@TestMock private SecurityContext securityContext;
	
	private SecurityFilter securityFilter;
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		User user = UserBuilder.aDivisionUser().build();
		securityFilter = new UserSecurityFilter(user);
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void test_findLayout_firstTime() {
		expect(persistenceService.find(anyObject(QueryBuilder.class))).andReturn(null);
		replay(persistenceService);
		expect(securityContext.getUserSecurityFilter()).andReturn(securityFilter);
		expectLastCall().times(2);
		replay(securityContext);
		
		DashboardLayout actual = dashboardService.findLayout();
		
		assertEquals(0, actual.getWidgetCount());
		assertEquals(0, actual.getColumns().get(0).getWidgets().size());
		assertEquals(0, actual.getColumns().get(1).getWidgets().size());		
	}

	@SuppressWarnings("unchecked")
	@Test 
	public void test_findLayout_existing() {
		DashboardLayout existingLayout = createNewDashboardLayout(new WidgetDefinition(WidgetType.COMMON_LINKS), new WidgetDefinition(WidgetType.ASSETS_STATUS));
		expect(persistenceService.find(anyObject(QueryBuilder.class))).andReturn(existingLayout);
		replay(persistenceService);
		expect(securityContext.getUserSecurityFilter()).andReturn(securityFilter);
		expectLastCall().times(2);
		replay(securityContext);
		
		DashboardLayout actual = dashboardService.findLayout();
		
		assertEquals(2, actual.getWidgetCount());
		assertEquals(2, actual.getColumns().get(0).getWidgets().size());
		assertEquals(0, actual.getColumns().get(1).getWidgets().size());		
	}

	
	
	private DashboardLayout createNewDashboardLayout(WidgetDefinition<?>... widgetDefinitions) {
		DashboardLayout layout = new DashboardLayout();
		
		DashboardColumn firstColumn = new DashboardColumn();
		for (WidgetDefinition<?> widgetDefinition:widgetDefinitions) { 
			firstColumn.getWidgets().add(widgetDefinition);
		}
		
		DashboardColumn secondColumn = new DashboardColumn();   // leave this one empty.
		
		layout.getColumns().add(firstColumn);
		layout.getColumns().add(secondColumn);
		
		return layout;
	}
	
	
	
}
