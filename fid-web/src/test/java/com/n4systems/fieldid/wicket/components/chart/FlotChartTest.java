package com.n4systems.fieldid.wicket.components.chart;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.IFixtureFactory;
import com.n4systems.fieldid.wicket.IWicketTester;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.components.chart.FlotChart.ChartMarkup;
import com.n4systems.fieldid.wicket.components.chart.FlotChartTest.FlotChartHarness;
import com.n4systems.fieldid.wicket.components.reporting.results.FieldIdPanelTest;
import com.n4systems.util.chart.CalendarChartable;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.Chartable;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.LineGraphOptions;
import com.n4systems.util.json.JsonRenderer;


public class FlotChartTest extends FieldIdPanelTest<FlotChartHarness, FlotChart<Calendar>> implements IFixtureFactory<FlotChart<Calendar>> {

	private static final String TEST_CSS = "myCss";
	private static final String TEST_LABEL = "chartLabel";
	
	private JsonRenderer jsonRenderer;
	private Model<FlotOptions<Calendar>> optionsModel;
	private ArrayList<ChartSeries<Calendar>> newArrayList;
	private ArrayList<ChartSeries<Calendar>> chartList;

	
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		jsonRenderer = wire(JsonRenderer.class, "jsonRenderer");
	}
	
	@Test
	public void test_Render() {
		optionsModel = new Model<FlotOptions<Calendar>>(new LineGraphOptions<Calendar>());
		final ChartSeries<Calendar> chartSeries = new ChartSeries<Calendar>(TEST_LABEL, createData());
		chartList = Lists.newArrayList(chartSeries);		
		expect(jsonRenderer.render(optionsModel.getObject())).andReturn("{options}");
		expect(jsonRenderer.render(chartList)).andReturn("{chartSeries}");
		replay(jsonRenderer);
		
		renderFixture(this);
		
		assertVisible(getHarness().getChart());		
		assertClassContains(TEST_CSS, getHarness().getChart());
		
		// make sure javascript is rendered.
		assertInDocument(String.format("chartWidgetFactory.createWithData('%1$s',{chartSeries},{options});", getHarness().getChart().getMarkupId()));
		
	}
	
	
	@Override
	public FlotChart<Calendar> createFixture(String id) {
		IModel<List<ChartSeries<Calendar>>> model = new LoadableDetachableModel<List<ChartSeries<Calendar>>>() {
			@Override protected List<ChartSeries<Calendar>> load() {
				return chartList;
			}
		};
		return new FlotChart<Calendar>(id, model, optionsModel, TEST_CSS); 
	}

	private List<Chartable<Calendar>> createData() {
		List<Chartable<Calendar>> data = Lists.newArrayList();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1980);
		for (int i = 0; i < 100; i++) {
			Calendar x = Calendar.getInstance();
			x.setTimeInMillis(c.getTimeInMillis());
			data.add(new CalendarChartable(x,i));
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
