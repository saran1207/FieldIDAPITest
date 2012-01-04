package com.n4systems.fieldid.wicket.components.chart;

import static org.easymock.EasyMock.*;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.IFixtureFactory;
import com.n4systems.fieldid.wicket.IWicketTester;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.components.chart.FlotChart.ChartMarkup;
import com.n4systems.fieldid.wicket.components.chart.FlotChartTest.FlotChartHarness;
import com.n4systems.fieldid.wicket.components.reporting.results.FieldIdPanelTest;
import com.n4systems.util.chart.DateChartable;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.Chartable;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.LineGraphOptions;
import com.n4systems.util.json.JsonRenderer;


public class FlotChartTest extends FieldIdPanelTest<FlotChartHarness, FlotChart<LocalDate>> implements IFixtureFactory<FlotChart<LocalDate>> {

	private static final String TEST_CSS = "myCss";
	private static final String TEST_LABEL = "chartLabel";
	
	private JsonRenderer jsonRenderer;
	private Model<FlotOptions<LocalDate>> optionsModel;
	private ChartData<LocalDate> chartList;

	
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		jsonRenderer = wire(JsonRenderer.class, "jsonRenderer");
	}
	
	@Test
	public void test_Render() {
		optionsModel = new Model<FlotOptions<LocalDate>>(new LineGraphOptions<LocalDate>());
		final ChartSeries<LocalDate> chartSeries = new ChartSeries<LocalDate>(TEST_LABEL, createData());
		chartList = new ChartData<LocalDate>(chartSeries);		
		expect(jsonRenderer.render(optionsModel.getObject())).andReturn("{options}");
		expect(jsonRenderer.render(chartList)).andReturn("{chartSeries}");
		replay(jsonRenderer);
		
		renderFixture(this);
		
		assertVisible(getHarness().getChart());		
		assertClassContains(TEST_CSS, getHarness().getChart());
		
		// make sure javascript is rendered.
//		assertInDocument(String.format("chartWidgetFactory.createWithData('%1$s',{chartSeries},{options});", getHarness().getChart().getMarkupId()));
        assertInDocument("chartWidgetFactory");
	}
	
	
	@Override
	public FlotChart<LocalDate> createFixture(String id) {
		IModel<ChartData<LocalDate>> model = new LoadableDetachableModel<ChartData<LocalDate>>() {
			@Override protected ChartData<LocalDate> load() {
				return chartList;
			}
		};
		return new FlotChart<LocalDate>(id, model, optionsModel, TEST_CSS); 
	}

	private List<Chartable<LocalDate>> createData() {
		List<Chartable<LocalDate>> data = Lists.newArrayList();
		Calendar c = Calendar.getInstance();
		for (int year = 1980; year < 2012; year++) {
			int value = year;
			data.add(new DateChartable(new LocalDate(year, 1, 1), value));
		}
		return data;
	}


	@Override
	protected FlotChartHarness createHarness(String pathContext, IWicketTester wicketTester) {
		return new FlotChartHarness(pathContext, wicketTester);
	}
	
	class FlotChartHarness extends WicketHarness {

		public FlotChartHarness(String pathContext, IWicketTester tester) {
			super(pathContext, tester);			
		}

		@SuppressWarnings("rawtypes")
		public ChartMarkup getChart() {
			return (ChartMarkup) get("flot");
		} 
		
	}

}

