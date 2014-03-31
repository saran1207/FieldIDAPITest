package com.n4systems.fieldid.wicket.pages.trends;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.CriteriaTrendsService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.chart.FlotChart;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.JavascriptPackageResourceIE;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.EventType;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.reporting.CriteriaTrendsResultCountByCriteriaRecord;
import com.n4systems.services.reporting.CriteriaTrendsResultCountRecord;
import com.n4systems.util.chart.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class CriteriaTrendsPage extends FieldIDFrontEndPage {

    @SpringBean
    private CriteriaTrendsService criteriaTrendsService;

    @SpringBean
    private PersistenceService persistenceService;


    private EventType eventType;
    private RangeType rangeType;
    private FIDFeedbackPanel feedbackPanel;
    private String selectedResultText;

    private WebMarkupContainer trendsByResultContainer;
    private WebMarkupContainer noResultsContainer;

    public CriteriaTrendsPage() {
        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        add(feedbackPanel);

        Form form = new Form("form");
        add(form);
        form.add(new FidDropDownChoice<EventType>("eventType", new PropertyModel<EventType>(this, "eventType"), new EventTypesForTenantModel(), new ListableChoiceRenderer<EventType>()).setRequired(true));
        EnumSet<RangeType> rangeTypes = RangeType.allFloatingButFutureTypes();
        rangeTypes.remove(RangeType.CUSTOM);
        RangeType[] rangeTypesArray = new RangeType[rangeTypes.size()];
        form.add(new FidDropDownChoice<RangeType>("datePicker", new PropertyModel<RangeType>(this, "rangeType"), Arrays.asList(rangeTypes.toArray(rangeTypesArray)), new ListableChoiceRenderer<RangeType>()).setRequired(true));

        final LoadableDetachableModel<List<CriteriaTrendsResultCountRecord>> trendsResultModel = createTrendsResultModel();

        form.add(new AjaxSubmitLink("submitLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (trendsResultModel.getObject().isEmpty()) {
                    target.add(feedbackPanel, trendsByResultContainer.setVisible(false), noResultsContainer.setVisible(true));
                } else {
                    CriteriaTrendsResultCountRecord record = trendsResultModel.getObject().iterator().next();
                    selectedResultText = record.getResultText();
                    trendsByResultContainer.addOrReplace(createChart());
                    target.add(feedbackPanel, trendsByResultContainer.setVisible(true), noResultsContainer.setVisible(false));
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel, trendsByResultContainer.setVisible(false), noResultsContainer.setVisible(false));
            }
        });

        add(trendsByResultContainer = new WebMarkupContainer("trendsByResultContainer"));
        trendsByResultContainer.setVisible(false).setOutputMarkupPlaceholderTag(true);
        add(noResultsContainer = new WebMarkupContainer("noResultsContainer"));
        noResultsContainer.setVisible(false).setOutputMarkupPlaceholderTag(true);

        trendsByResultContainer.add(new FlatLabel("eventTypeLabel", new PropertyModel<String>(this, "eventType.name")));
        trendsByResultContainer.add(new Label("dateRangeLabel", new PropertyModel<String>(this, "rangeType.displayName")));

        trendsByResultContainer.add(new ListView<CriteriaTrendsResultCountRecord>("trendsByResult", trendsResultModel) {
            @Override
            protected void populateItem(final ListItem<CriteriaTrendsResultCountRecord> item) {
                item.add(new AttributeModifier("class", "active") {
                    @Override
                    public boolean isEnabled(Component component) {
                        return item.getModelObject().getResultText() != null && item.getModelObject().getResultText().equals(selectedResultText);
                    }
                });
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        selectedResultText = item.getModelObject().getResultText();
                        trendsByResultContainer.addOrReplace(createChart());
                        target.add(trendsByResultContainer);
                    }
                });
                item.add(new Label("resultText", ProxyModel.of(item.getModel(), on(CriteriaTrendsResultCountRecord.class).getResultText())));
                item.add(new Label("count", ProxyModel.of(item.getModel(), on(CriteriaTrendsResultCountRecord.class).getCount())));
            }
        });
    }

    private FlotChart<String> createChart() {
        FlotChart<String> chart = new FlotChart<String>("chart", createChartDataModel(), Model.of(new BarChartOptions<String>()), "");
        chart.setTooltipFunction("plainTrendsTooltip");
        return chart;
    }

    private IModel<ChartData<String>> createChartDataModel() {
        return new LoadableDetachableModel<ChartData<String>>() {
            @Override
            protected ChartData<String> load() {
                final List<CriteriaTrendsResultCountByCriteriaRecord> criteriaTrendsByCriteria = criteriaTrendsService.findCriteriaTrendsByCriteria(eventType, new DateRange(rangeType), selectedResultText);
                ChartSeries<String> series = new ChartSeries<String>(null, null, criteriaTrendsByCriteria, new CriteriaTrendsSortedByCountChartMap(criteriaTrendsByCriteria));
                return new ChartData<String>(new CriteriaTrendsChartManager(), series);
            }
        };
    }

    private LoadableDetachableModel<List<CriteriaTrendsResultCountRecord>> createTrendsResultModel() {
        return new LoadableDetachableModel<List<CriteriaTrendsResultCountRecord>>() {
            @Override
            protected List<CriteriaTrendsResultCountRecord> load() {
                return criteriaTrendsService.findCriteriaTrends(eventType, new DateRange(rangeType));
            }
        };
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.trending"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        JavascriptPackageResourceIE.renderJavaScriptReference(response, "javascript/flot/excanvas.min.js");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/legacy/newCss/trends/trending.css");
        response.renderJavaScriptReference("javascript/flot/jquery.flot.min.js");
        response.renderJavaScriptReference("javascript/flot/jquery.flot.stack.min.js");
        response.renderJavaScriptReference("javascript/flot/jquery.flot.navigate.min.js");
        response.renderJavaScriptReference("javascript/flot/jquery.flot.symbol.min.js");
        response.renderJavaScriptReference("javascript/dashboard.js");
        response.renderJavaScriptReference("javascript/trends/trending.js");
    }
}
