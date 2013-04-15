package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.job.JobService;
import com.n4systems.fieldid.wicket.*;
import com.n4systems.fieldid.wicket.FieldIdWicketTestRunner.WithUsers;
import com.n4systems.fieldid.wicket.components.dashboard.DashboardHeaderPanel;
import com.n4systems.fieldid.wicket.pages.DashboardPageTest.DashboardHarness;
import com.n4systems.fieldid.wicket.pages.widgets.JobsAssignedWidget;
import com.n4systems.fieldid.wicket.pages.widgets.NewsWidget;
import com.n4systems.fieldid.wicket.pages.widgets.Widget;
import com.n4systems.fieldid.wicket.pages.widgets.WidgetFactory;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.user.User;
import com.n4systems.services.dashboard.DashboardService;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(FieldIdWicketTestRunner.class)
public class DashboardPageTest extends FieldIdPageTest<DashboardHarness, DashboardPage> implements IFixtureFactory<DashboardPage> {

	private DashboardService dashboardService;
	private WidgetFactory widgetFactory;
	private JobService jobService;

	private DashboardLayout layout;
	private WidgetDefinition testWidgetDefinition;
	private TestWidget testWidget;
	private NewsWidget newsWidget;


    @Override
	@Before
    public void setUp() throws Exception {
    	super.setUp();
    	init();
        expectingUserService();
        expectingS3Service();
    }

	protected void init() {
		dashboardService = wire(DashboardService.class);
    	widgetFactory = wire(WidgetFactory.class);
		testWidgetDefinition = new WidgetDefinition(WidgetType.COMPLETED_EVENTS);  // use arbitrary bogus widget type for testWidget.
		testWidgetDefinition.setId(0L);
		jobService = wire(JobService.class);
    	layout = createNewDashboardLayout(testWidgetDefinition);
    	testWidget = new TestWidget(WidgetFactory.WIDGET_ID, new WidgetDefinition<WidgetConfiguration>(WidgetType.COMPLETED_EVENTS));
		newsWidget = new NewsWidget(WidgetFactory.WIDGET_ID, new WidgetDefinition(WidgetType.NEWS));
	}
    
	@Test
	@WithUsers({TestUser.ALL_PERMISSIONS_USER, TestUser.NO_PERMISSIONS_USER, TestUser.JOBS_USER})
	public void testRender() throws MalformedURLException {
        expectingConfig();
        expect(dashboardService.findLayout()).andReturn(layout);
		expectLastCall().times(3);	//extra invocation for assertion using getList().
        expect(dashboardService.findDashboardLayouts(true)).andReturn(Collections.singletonList(layout));
        expectLastCall().times(1);
        expect(dashboardService.countDashboardLayouts()).andReturn(1L);
        replay(dashboardService);
		expect(widgetFactory.createWidget(testWidgetDefinition)).andReturn(testWidget);
		replay(widgetFactory);

		renderFixture(this);
		
		assertVisible(getHarness().getHeaderPanel());
		assertVisible(getHarness().getSortableColumn(0));
		assertVisible(getHarness().getSortableColumn(1));
		
		getHarness().getSortableColumn(0).visitChildren(ListItem.class, new WidgetVisitor(TestWidget.class));
		assertEquals(0, getHarness().getSortableColumn(1).getList().size());
		assertVisible(getHarness().getGoogleAnalytics());

		verifyMocks(dashboardService, widgetFactory);
	}	
	

	@Test
	public void testAddWidget() throws MalformedURLException {
        expectingConfig();
		expect(dashboardService.findLayout()).andReturn(layout);
		expectLastCall().times(4);  // have to add some expectations because our asserts actually trigger calls...yecccch.
        expect(dashboardService.findDashboardLayouts(true)).andReturn(Collections.singletonList(layout));
        expectLastCall().times(1);
        expect(dashboardService.countDashboardLayouts()).andReturn(1L);
        dashboardService.saveLayout(layout);
        expect(dashboardService.createWidgetDefinition(WidgetType.NEWS)).andReturn(new WidgetDefinition(WidgetType.NEWS));
		replay(dashboardService);
		expect(widgetFactory.createWidget(testWidgetDefinition)).andReturn(testWidget);
		expectLastCall().times(2);
		expect(widgetFactory.createWidget(WidgetDefinitionMatcher.eq(WidgetType.NEWS))).andReturn(newsWidget);
		replay(widgetFactory);	

		renderFixture(this);

		// -1 because news is already added,   -1 because Jobs isn't shown for this user = -2

/*
		int available = WidgetType.values().length-2;
		assertEquals(available, getHarness().getAddWidgetsDropDown().getChoices().size());
*/

		getHarness().addWidget(WidgetType.NEWS);
		
		IVisitor<ListItem<WidgetDefinition<?>>, Void> visitor = new WidgetVisitor(NewsWidget.class, TestWidget.class);
		
		getHarness().getSortableColumn(0).visitChildren(ListItem.class, visitor);
		
		// should be one less available after adding. 
/*
		assertEquals(available-1, getHarness().getAddWidgetsDropDown().getChoices().size());
*/

		verifyMocks(dashboardService, widgetFactory, userLimitService);
	}	
	
//	@Test
	public void testAddWidgetWithJobsUser() throws MalformedURLException {
		User user = UserBuilder.aFullUser().build();
		user.getOwner().getPrimaryOrg().setExtendedFeatures(Sets.newHashSet(ExtendedFeature.Projects));
		setSessionUser(user);

        expectingConfig();
		expect(dashboardService.findLayout()).andReturn(layout);
		expectLastCall().times(4);
        expect(dashboardService.findDashboardLayouts(true)).andReturn(Collections.singletonList(layout));
        expectLastCall().times(1);
        expect(dashboardService.countDashboardLayouts()).andReturn(1L);
        expect(dashboardService.createWidgetDefinition(WidgetType.JOBS_ASSIGNED)).andReturn(new WidgetDefinition(WidgetType.JOBS_ASSIGNED));
        dashboardService.saveLayout(layout);
		replay(dashboardService);
		
		expect(jobService.countAssignedToMe(true)).andReturn(new Long(0));
		expectLastCall().anyTimes();
		replay(jobService);	
		
		testWidget = new TestWidget(WidgetFactory.WIDGET_ID, testWidgetDefinition);
		expect(widgetFactory.createWidget(testWidgetDefinition)).andReturn(testWidget);
		expectLastCall().times(2);
		final JobsAssignedWidget jobsWidget = new JobsAssignedWidget(WidgetFactory.WIDGET_ID, new WidgetDefinition(WidgetType.JOBS_ASSIGNED));
		expect(widgetFactory.createWidget(WidgetDefinitionMatcher.eq(WidgetType.JOBS_ASSIGNED))).andReturn(jobsWidget);
		replay(widgetFactory);

		renderFixture(this);

		getHarness().addWidget(WidgetType.JOBS_ASSIGNED);
		
		getHarness().getSortableColumn(0).visitChildren(ListItem.class, new WidgetVisitor(JobsAssignedWidget.class, TestWidget.class));

		verifyMocks(dashboardService, widgetFactory, jobService);
	}	
	
	@Test
	public void testRemoveWidget() throws MalformedURLException {
        expectingConfig();
		expect(dashboardService.findLayout()).andReturn(layout);
		expectLastCall().times(3);
        expect(dashboardService.findDashboardLayouts(true)).andReturn(Collections.singletonList(layout));
        expectLastCall().times(1);
        expect(dashboardService.countDashboardLayouts()).andReturn(1L);
        dashboardService.saveLayout(layout);
		replay(dashboardService);
		expect(widgetFactory.createWidget(testWidgetDefinition)).andReturn(testWidget);
		replay(widgetFactory);

		renderFixture(this);	

		List<DashboardColumn> columns = layout.getColumns();    
		assertEquals(1, columns.get(0).getWidgets().size());
		assertEquals(0, columns.get(1).getWidgets().size());
		
		getHarness().removeWidget(0,0);	// remove first and only widget.

		assertEquals(0, columns.get(0).getWidgets().size());
		assertEquals(0, columns.get(1).getWidgets().size());

		verifyMocks(dashboardService, widgetFactory);
	}	
	
	@Test
	public void test_BlankSlate() throws MalformedURLException {
		layout = createNewDashboardLayout();
		expectingConfig();
		expect(dashboardService.findLayout()).andReturn(layout);
		expectLastCall().times(4);
        expect(dashboardService.findDashboardLayouts(true)).andReturn(Collections.singletonList(layout));
        expectLastCall().times(1);
        expect(dashboardService.countDashboardLayouts()).andReturn(1L);
        dashboardService.saveLayout(layout);
        expect(dashboardService.createWidgetDefinition(WidgetType.NEWS)).andReturn(new WidgetDefinition(WidgetType.NEWS));
        replay(dashboardService);

		expect(widgetFactory.createWidget(WidgetDefinitionMatcher.eq(WidgetType.NEWS))).andReturn(newsWidget);
		replay(widgetFactory);	

		renderFixture(this);
		
		assertEquals(0, layout.getWidgetCount());
		assertVisible(getHarness().getBlankSlatePanel());
		assertInvisible(getHarness().getSortableColumn(0));
		assertInvisible(getHarness().getSortableColumn(1));
				
		getHarness().addWidget(WidgetType.NEWS);
		
		assertEquals(1, layout.getWidgetCount());
		assertInvisible(getHarness().getBlankSlatePanel());
		assertVisible(getHarness().getSortableColumn(0));
		assertVisible(getHarness().getSortableColumn(1));
		
		verifyMocks(dashboardService, widgetFactory, userLimitService);
	}
	
	@Override
	public DashboardPage createFixture(String id) {
		return new DashboardPage(configurationProvider) {
			@Override protected void redirectToSetupWizardIfNecessary() {
				// do nothing...skip this when testing.
			}
		};
	}

	@Override
	protected DashboardHarness createHarness(String pathContext, IWicketTester wicketTester) {
		return new DashboardHarness(pathContext, wicketTester);
	}
	
	private DashboardLayout createNewDashboardLayout(WidgetDefinition... widgetDefinitions) {
		DashboardLayout layout = new DashboardLayout();
		
		DashboardColumn firstColumn = new DashboardColumn();
		for (WidgetDefinition widgetDefinition:widgetDefinitions) { 
			firstColumn.getWidgets().add(widgetDefinition);
		}
		
		DashboardColumn secondColumn = new DashboardColumn();   // leave this one empty.
		
		layout.getColumns().add(firstColumn);
		layout.getColumns().add(secondColumn);
		
		return layout;
	}
	
	class WidgetVisitor implements IVisitor<ListItem<WidgetDefinition<?>>, Void> {

		private Class<? extends Widget<?>>[] widgets;
		public WidgetVisitor(Class<? extends Widget<?>>... widgets) { 
			this.widgets = widgets;
		}
		
		@Override public void component(ListItem<WidgetDefinition<?>> item, final IVisit<Void> visit) {
			if (item.getIndex()>=widgets.length) {
				fail("there should only be " + widgets.length + " widgets");
			}
			Class<? extends Widget> clazz = widgets[item.getIndex()];
			assertEquals(clazz, item.get("widget").getClass());
		}
		
	}
	
	static protected class WidgetDefinitionMatcher implements IArgumentMatcher {

		private WidgetType type;

		public WidgetDefinitionMatcher(WidgetType type) {
			this.type = type;
		}
		
		public static final WidgetDefinition eq(WidgetType type) {
			EasyMock.reportMatcher(new WidgetDefinitionMatcher(type));
			return null;
		}
		
		@Override
		public void appendTo(StringBuffer buffer) {
			buffer.append("'").append(new WidgetDefinition(type)).append("'");
		}

		@Override
		public boolean matches(Object argument) {
			if (!(argument instanceof WidgetDefinition) ) { 
				return false;
			}
			WidgetDefinition actual = (WidgetDefinition) argument;
			return actual.getWidgetType().equals(type);
		} 
		
	}
	
	
	@SuppressWarnings("unchecked")
	class DashboardHarness extends WicketHarness {
		
		public DashboardHarness(String pathContext, IWicketTester tester) {
			super(pathContext, tester);	
			//appendPathContext("content");
		}
		
		public Component getGoogleAnalytics() {
			return getWicketTester().getComponentFromLastRenderedPage("googleAnalyticsScripts");
		}

		public WebMarkupContainer getBlankSlatePanel() {
			return (WebMarkupContainer) get("content", "blankSlate");
		}

		public void addWidget(WidgetType widgetType) {
            getWicketTester().executeAjaxEvent(get("headerPanel", "addWidgetsLink"), "onclick");
            assertVisible(get("configWindow"));
            getWicketTester().executeAjaxEvent(getAddWidgetLink(widgetType), "onclick");

        }

		public DashboardHeaderPanel getHeaderPanel() {
			return (DashboardHeaderPanel) get("headerPanel");
		}

		public ListView<?> getSortableColumn(int i) {
			MarkupContainer container = (MarkupContainer) get("content", "columnsContainer", "sortableColumn" + ((i==0)?"":"2"));
			return container==null? null : (ListView<?>) container.get("widgets");
		}

        private AjaxLink getAddWidgetLink(WidgetType widgetType) {
            ListView<WidgetType> widgets = (ListView<WidgetType>) get("configWindow", "content", "widgets", "widgetList");
            return (AjaxLink) widgets.get(widgetType.ordinal()).get("add");
        }

		private FormTester getAddWidgetFormTester() {
			return getFormTester("addWidgetPanel","addWidgetForm");
		}
		
		private Widget getWidget(int col, int index) {
			String sortableColumnId = "sortableColumn" + (col==0?"":"2");
			WebMarkupContainer sortableColumns = (WebMarkupContainer) get("content", "columnsContainer", sortableColumnId);
			ListView<WidgetDefinition> listView = (ListView<WidgetDefinition>) sortableColumns.get("widgets");
			ListItem<WidgetDefinition> c = (ListItem<WidgetDefinition>) listView.get(index);
			return (Widget) c.get("widget");
		}

		public void removeWidget(int col, int index) {
			getWicketTester().executeAjaxEvent(getWidget(col,index).get("removeButton"), "onclick");
		}
		
	}



    class TestWidget extends Widget<WidgetConfiguration> {
        public TestWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
            super(id, Model.of(widgetDefinition));
        }

        @Override
        public Component createConfigPanel(String id) {
            return null;
        }
    }



}
