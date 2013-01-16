package com.n4systems.fieldid.wicket.components.dashboard.subcomponents;

import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunReportPage;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.services.reporting.EventKpiRecord;
import com.n4systems.services.reporting.KpiType;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class EventKpiTable extends Panel {

    @SpringBean
	private DashboardReportingService reportingService;
	private EventKPIWidgetConfiguration config;
    private WidgetDefinition<EventKPIWidgetConfiguration> widgetDefinition;


    public EventKpiTable(String id, List<BaseOrg> orgList, EventKPIWidgetConfiguration eventKPIWidgetConfiguration, final WidgetDefinition<EventKPIWidgetConfiguration> widgetDefinition) {
		super(id);
		this.config = eventKPIWidgetConfiguration;
        this.widgetDefinition = widgetDefinition;

		add(new ListView<EventKpiRecord>("customerEventKpiList", getCustomerEventKPIs(orgList)) {
			@Override protected void populateItem(ListItem<EventKpiRecord> item) {
				EventKpiRecord eventKpiRecord = item.getModelObject();
                item.add(new Label("customerName", eventKpiRecord.getCustomer().getDisplayName()));
                final KpiWrapper kpiWrapper = new KpiWrapper(eventKpiRecord);
                item.add(new Label("total", new StringResourceModel("label.kpi_total", Model.of(kpiWrapper), null).getString() ));
                item.add(new Kpi("kpiBarChart", item.getModel(), eventKpiRecord.getTotalScheduledEvents()));
                item.add(new KpiLabel("kpiLabel", Model.of(kpiWrapper)));
                item.add(new WebMarkupContainer("blankSlate") {
                    @Override public boolean isVisible() {
                        return kpiWrapper.getTotalScheduledEvents() == 0L;
                    }
                });
            }
		});
	}

    private Long getPercentage(Long value, Long total) {
        if (value==0) return 0L;
        return Math.max(1L,value * 100 / total);
    }

    private long getRoundedPercentage(KpiWrapper kpi) {
        // note : i need to account for rounding.
        // e.g. say total = 99.   x = 32, y = 30, z = 12.  (31.??%, 29.??%, 11.??%)    rounded percentage = 31+29+11=71%
        // as opposed to (x+y+z)/total -> (32+30+12)/99 = 73+%
        return getPercentage(kpi.getFailed(), kpi.getTotalScheduledEvents()) +
                getPercentage(kpi.getClosed(), kpi.getTotalScheduledEvents()) +
                getPercentage(kpi.getNa(), kpi.getTotalScheduledEvents()) +
                getPercentage(kpi.getPassed(), kpi.getTotalScheduledEvents());
    }

    private PageParameters getParams(KpiWrapper eventKpiRecord, String series) {
        PageParameters params = new PageParameters().add(AbstractSearchPage.SOURCE_PARAMETER, AbstractSearchPage.WIDGET_SOURCE).add(AbstractSearchPage.WIDGET_DEFINITION_PARAMETER, widgetDefinition.getId());
        params.add(AbstractSearchPage.SERIES_PARAMETER, series);
        int orgIndex = widgetDefinition.getConfig().getOrgs().indexOf(eventKpiRecord.getCustomer());
        params.add(AbstractSearchPage.X_PARAMETER, orgIndex);
        return params;
    }
	
	private List<EventKpiRecord> getCustomerEventKPIs(List<BaseOrg> orgs) {
		List<EventKpiRecord> eventKpis = new ArrayList<EventKpiRecord>();
		for(BaseOrg org: orgs) {
			eventKpis.add(reportingService.getEventKpi(org, getDateRange()));
		}
		return eventKpis;
	}
	
	protected DateRange getDateRange() {
		return new DateRange(config.getRangeType());
	}


    // -----------------------------------------------------------------------------------------------------------------

    class Kpi extends WebMarkupContainer {

        protected KpiWrapper kpi;
        protected Long total;


        public Kpi(String id, final IModel<EventKpiRecord> model, Long total) {
            super(id, model);
            this.total = total;

            kpi = new KpiWrapper(model.getObject());

            add(new KpiBar(kpi,KpiType.PASSED) {
                @Override protected Long getValue() {
                    return kpi.getPassed();
                }
            });
            add(new KpiBar(kpi,KpiType.FAILED) {
                @Override protected Long getValue() {
                    return kpi.getFailed();
                }
            });
            add(new KpiBar(kpi,KpiType.CLOSED) {
                @Override protected Long getValue() {
                    return kpi.getClosed();
                }
            });
            add(new KpiBar(kpi,KpiType.NA) {
                @Override protected Long getValue() {
                    return kpi.getNa();
                }
            });
            add(new KpiBar(kpi, KpiType.INCOMPLETE) {
                @Override protected Long getValue() {
                    return kpi.getIncomplete();
                }
            }.withWidth(100 - getRoundedPercentage(kpi)));
        }

        @Override
        public boolean isVisible() {
            return total>0L;
        }


        class KpiBar extends WebMarkupContainer {

            private Long width;

            public KpiBar(final KpiWrapper kpi, final KpiType type) {
                super(type.getLabel());
                add(new AjaxEventBehavior("onclick") {
                    @Override protected void onEvent(AjaxRequestTarget target) {
                        EventKpiTable.this.setResponsePage(RunReportPage.class, getParams(kpi, type.getLabel()));
                    }
                });
            }

            @Override
            public boolean isVisible() {
                return getValue()>0;
            }

            @Override
            protected void onInitialize() {
                super.onInitialize();
            }

            protected Long getValue() {
                return 0L;
            }

            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                Long w = width==null ? getPercentage(getValue(),total) : width;
                String style = "width:" + w + "%;";
                if (width !=null) {
                    style+=" left:"+(100-width)+"%;";
                }
                tag.put("style",style);
            }

            public KpiBar withWidth(Long width) {
                this.width = width;
                return this;
            }

        }

    }

    class KpiLabel extends WebMarkupContainer {

        private final IModel<KpiWrapper> model;

        public KpiLabel(String id, IModel<KpiWrapper> model) {
            super(id, model);
            this.model = model;
            addLabel(KpiType.FAILED);
            addLabel(KpiType.CLOSED);
            addLabel(KpiType.NA);
            addLabel(KpiType.PASSED);
        }

        private void addLabel(final KpiType type) {
            String name=type.name().toLowerCase();
            Label label = new Label(name,new StringResourceModel("label.kpi_type."+name,this,model));
            add(label);
            label.add(new AjaxEventBehavior("onclick") {
                @Override protected void onEvent(AjaxRequestTarget target) {
                    EventKpiTable.this.setResponsePage(RunReportPage.class, getParams(model.getObject(), type.getLabel()));
                }
            });
        }
    }

}
