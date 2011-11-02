package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.components.chart.FlotChart;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.LineGraphOptions;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

@SuppressWarnings("serial")
public abstract class ChartWidget<X> extends Widget {

	protected Component flotChart;
	protected BaseOrg owner;
	protected ChartGranularity granularity = ChartGranularity.ALL;
	protected Integer period = 30;
	
	public ChartWidget(String id, IModel<WidgetDefinition> model) {
		super(id, model);
		setOutputMarkupId(true);		
		add(flotChart = createFlotChart(model.getObject().getWidgetType().getCamelCase()+"Chart"));
	}

	protected Component createFlotChart(final String css) {
		return new AjaxLazyLoadPanel("chart") {
			@Override
			public Component getLazyLoadComponent(String markupId) {
				return createFlotChartImpl(markupId, css);
			}
            public Component getLoadingComponent(final String markupId) {
                return new Label(markupId, "<div class='loadingText'>" + new FIDLabelModel("label.loading_ellipsis").getObject() +
                    "</div>").setEscapeModelStrings(false);
            }
		};		
	}
	
    private void setGranularity(ChartGranularity period) {
    	this.granularity = period;
    }        	
    
	public void setPeriod(Integer period) {
		this.period = period;
	}

	@SuppressWarnings("rawtypes")
	protected void addGranularityButton(String id, final ChartGranularity granularity) {
        AjaxLink granularityButton = new AjaxLink(id) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setGranularity(granularity);
                target.addComponent(ChartWidget.this);
            }
        };
        granularityButton.add(new AttributeAppender("class", true, new Model<String>("selected"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return granularity == ChartWidget.this.granularity;
            }
        });
        add(granularityButton);
	}

	protected void addPeriodButton(String id, final int period) {
        AjaxLink periodButton = new AjaxLink(id) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setPeriod(period);
                target.addComponent(ChartWidget.this);
            }
        };
        periodButton.add(new AttributeAppender("class", true, new Model<String>("selected"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return period == ChartWidget.this.period;
            }
        });
        add(periodButton);
	}

	private Component createFlotChartImpl(String id, String css) {
		LoadableDetachableModel<List<ChartSeries<X>>> model = new LoadableDetachableModel<List<ChartSeries<X>>>() {
			@Override protected List<ChartSeries<X>> load() {
				return getChartSeries();
			}
		};		
		return new FlotChart<X>(id, model, createOptions(), css);
	}
	
	protected abstract List<ChartSeries<X>> getChartSeries();
	
	protected FlotOptions<X> createOptions() {
		return new LineGraphOptions<X>();
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
