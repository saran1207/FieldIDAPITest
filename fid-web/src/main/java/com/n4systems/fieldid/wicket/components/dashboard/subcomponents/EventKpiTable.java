package com.n4systems.fieldid.wicket.components.dashboard.subcomponents;

import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunReportPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.services.reporting.EventKpiRecord;
import com.n4systems.services.reporting.KpiType;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
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

import static ch.lambdaj.Lambda.on;

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
				item.add(new Kpi("kpi", item.getModel()));
                item.add(new Label("total", eventKpiRecord.getTotalScheduledEvents()+""));
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

        public final Long COZY_THRESHOLD=20L;

        private final KpiBar passed;
        private final KpiBar failed;
        private final KpiBar closed;
        private final KpiBar incomplete;
        private final KpiBar na;
        private KpiWrapper kpi;

        public Kpi(String id, final IModel<EventKpiRecord> model) {
            super(id, model);

            add(new Label("customerName", model.getObject().getCustomer().getDisplayName()));

            kpi = new KpiWrapper(model.getObject());

            add(passed = new KpiBar(kpi, KpiType.PASSED));
            add(failed = new KpiBar(kpi, KpiType.FAILED));
            add(closed = new KpiBar(kpi, KpiType.CLOSED));
            add(na = new KpiBar(kpi, KpiType.NA));
            add(incomplete = new KpiBar(kpi, KpiType.INCOMPLETE).withWidth(100 - getRoundedPercentage(kpi)));
            incomplete.add(new KpiLabel("incompleteLabel", KpiType.INCOMPLETE, ProxyModel.of(kpi, on(KpiWrapper.class).getIncomplete())));

            WebMarkupContainer allCompleted;
            add(allCompleted = new WebMarkupContainer("allCompleted"));
            allCompleted.add(new KpiLabel("completeLabel", KpiType.PASSED, ProxyModel.of(kpi, on(KpiWrapper.class).getCompletedAndClosed()), getRoundedPercentage(kpi)) {
                @Override protected String getTooltip() {
                    return new StringResourceModel("label.kpi_type.completed", Model.of(kpi), null).getString();
                }
            });

            add(passed.createTickMark());
            add(closed.createTickMark());
            add(failed.createTickMark());
            add(na.createTickMark());

            if (kpi.getCompletedPercentage()< COZY_THRESHOLD) {
                 add(new AttributeAppender("class", Model.of("cozy"), " "));
            }

        }

        class KpiBar extends WebMarkupContainer {

            private final KpiType type;
            private final KpiWrapper kpi;
            private Long width;
            private final String tooltip;

            public KpiBar(final KpiWrapper kpi, final KpiType type) {
                super(type.getLabel());
                this.type = type;
                this.kpi = kpi;
                this.tooltip = new StringResourceModel("label.kpi_type."+type.getLabel(), Model.of(kpi), null).getString();
                add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
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

            private Long getValue() {
                switch (type) {
                    case FAILED:
                        return kpi.getFailed();
                    case INCOMPLETE:
                        return kpi.getIncomplete();
                    case PASSED:
                        return kpi.getPassed();
                    case CLOSED:
                        return kpi.getClosed();
                    case TOTAL:
                        return kpi.getTotalScheduledEvents();
                    case NA:
                        return kpi.getNa();
                    default:
                        throw new IllegalStateException("invalid kpi type " + type);
                }
            }

            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                Long w = width==null ? getPercentage(getValue(), kpi.getTotalScheduledEvents()) : width;
                String style = "width:" + w + "%;";
                if (width !=null) {
                    style+=" left:"+(100-width)+"%;";
                }
                tag.put("style",style);
                tag.put("title", tooltip);
            }

            public KpiBar withWidth(Long width) {
                this.width = width;
                return this;
            }

            public KpiTickMark createTickMark() {
                return new KpiTickMark(getId()+"TickMark", Model.of(getValue()));
            }


            class KpiTickMark extends Label {

                public KpiTickMark(String id, IModel<Long> model) {
                    super(id, model);
                }

                @Override
                public boolean isVisible() {
                    return ((Long)getDefaultModelObject())>0;
                }

                @Override protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                }

            }

        }


        class KpiLabel extends Label {

            private final long percentage;

            public KpiLabel(String id, KpiType type,IModel<Long> model) {
                this(id,type,model,100L);
            }

            public KpiLabel(String id, KpiType type,IModel<Long> model, Long percentage) {
                super(id, model);
                this.percentage = percentage;
            }

            @Override protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("title", getTooltip());
            }

            protected String getTooltip() {
                return null;
            }

            @Override public boolean isVisible() {
                return percentage>0;
            }

        }


    }








}
