package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.chart.FlotChart;
import com.n4systems.fieldid.wicket.components.chart.FlotOptions;
import com.n4systems.fieldid.wicket.components.chart.LineGraphFlotOptions;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public abstract class ChartWidget<X> extends Widget {

	protected Component flotChart;
	protected BaseOrg owner;

	public ChartWidget(String id, IModel<WidgetDefinition> model) {
		super(id, model);
		add(flotChart = createFlotChart(model.getObject().getWidgetType().getCamelCase()+"Chart"));
	}

	protected Component createFlotChart(final String css) {
		return new AjaxLazyLoadPanel("chart") {
			@Override
			public Component getLazyLoadComponent(String markupId) {
				return createFlotChartImpl(markupId, css);
			}
		};		
	}

	private final Component createFlotChartImpl(String id, String css) {		
		LoadableDetachableModel<List<ChartData<X>>> model = new LoadableDetachableModel<List<ChartData<X>>>() {
			@Override protected List<ChartData<X>> load() {
				return getChartData();
			}			
		};		
		return new FlotChart<X>(id, model, createOptions(), css);
	}
	
	protected abstract List<ChartData<X>> getChartData();
	
	protected FlotOptions<X> createOptions() {
		return new LineGraphFlotOptions<X>();
	}
	
	
	class OrgForm extends Form {

		public OrgForm(String id) {
			super(id);
			add(new OrgPicker("ownerPicker", new PropertyModel<BaseOrg>(ChartWidget.this, "owner")) { 
				@Override protected void closePicker(AjaxRequestTarget target) {
					super.closePicker(target);
					target.addComponent(ChartWidget.this);
				}
				@Override protected void cancelPicker(AjaxRequestTarget target) {
					super.cancelPicker(target);
					target.addComponent(ChartWidget.this);
				}
			});
		}
		
	}	
	
	
}
