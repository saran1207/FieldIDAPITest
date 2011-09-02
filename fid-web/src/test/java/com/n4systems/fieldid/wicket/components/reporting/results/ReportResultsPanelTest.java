package com.n4systems.fieldid.wicket.components.reporting.results;

import static org.easymock.EasyMock.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.fieldid.wicket.IWicketTester;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.components.reporting.results.ReportResultsPanelTest.ReportResultsPanelHarness;
import com.n4systems.model.Event;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.TableViewTransformer;
import com.n4systems.util.views.TableView;

public class ReportResultsPanelTest extends FieldIdPanelTest<ReportResultsPanelHarness, ReportResultsPanel> {

	private ReportService reportService;
	private EventReportCriteriaModel model = new EventReportCriteriaModel();
	private PageHolder<TableView> results = null;

	@Override
	@Before
	public void setUp() throws Exception { 
		model = new EventReportCriteriaModel();
		super.setUp();
		reportService = wire(ReportService.class, "reportService");
	}
	
	private PageHolder<TableView> makeResults() throws ParseException {
        List<Event> entities = new ArrayList<Event>();
        entities.add(EventBuilder.anEvent().build());		
        TableView pageResults = new TableViewTransformer("id", Lists.newArrayList("id")).transform(entities);
        return new PageHolder<TableView>(pageResults, 1);
	}

	@Test
	public void testRender() throws ParseException {
		results = makeResults();
		
		expect(reportService.countPages(model, 1L)).andReturn(1);
		expect(reportService.eventReport(eq(model), isA(ResultTransformer.class), eq(0), eq(20))).andReturn(results);
		replay(reportService);
		renderFixture(this);	
		assertVisible(getHarness().get("resultsTable"));
	}		
	
	@SuppressWarnings({"serial", "deprecation"})
	@Override
	public ReportResultsPanel createFixture(String id) {
		return new ReportResultsPanel(id, new Model<EventReportCriteriaModel>(model)) {
			@Override SerializableSecurityGuard getSecurityGuard() {
				return new SerializableSecurityGuard(TenantBuilder.n4(), PrimaryOrgBuilder.aPrimaryOrg().build());
			}
		};
	}
	
	@Override
	protected ReportResultsPanelHarness createHarness(String pathContext, IWicketTester wicketTester) {
		return new ReportResultsPanelHarness(pathContext, wicketTester);
	}	
	
	class ReportResultsPanelHarness extends WicketHarness {
		
		public ReportResultsPanelHarness(String pathContext, IWicketTester tester) {
			super(pathContext, tester);
		}
		
	}
}
