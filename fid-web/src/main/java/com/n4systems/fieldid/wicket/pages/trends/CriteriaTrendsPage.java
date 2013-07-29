package com.n4systems.fieldid.wicket.pages.trends;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.CriteriaTrendsService;
import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.chart.FlotChart;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.EventType;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.reporting.CriteriaTrendsResultCountByCriteriaRecord;
import com.n4systems.services.reporting.CriteriaTrendsResultCountRecord;
import com.n4systems.util.chart.*;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class CriteriaTrendsPage extends FieldIDFrontEndPage {

    @SpringBean
    private CriteriaTrendsService criteriaTrendsService;

    @SpringBean
    private PersistenceService persistenceService;


    private EventType eventType;
    private DateRange dateRange = new DateRange(RangeType.THIS_WEEK);
    private FIDFeedbackPanel feedbackPanel;
    private String selectedResultText;

    private WebMarkupContainer trendsByResultContainer;
    private WebMarkupContainer trendsForResultByCriteriaContainer;

    public CriteriaTrendsPage() {
        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        add(feedbackPanel);

        Form form = new Form("form");
        add(form);
        form.add(new FidDropDownChoice<EventType>("eventType", new PropertyModel<EventType>(this, "eventType"), new EventTypesForTenantModel(), new ListableChoiceRenderer<EventType>()).setRequired(true));
        form.add(new DateRangePicker("datePicker", new PropertyModel<DateRange>(this, "dateRange")));

        form.add(new AjaxSubmitLink("submitLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel, trendsByResultContainer.setVisible(true), trendsForResultByCriteriaContainer.setVisible(false));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        add(trendsByResultContainer = new WebMarkupContainer("trendsByResultContainer"));
        trendsByResultContainer.setVisible(false).setOutputMarkupPlaceholderTag(true);

        trendsByResultContainer.add(new ListView<CriteriaTrendsResultCountRecord>("trendsByResult", createTrendsResultModel()) {
            @Override
            protected void populateItem(final ListItem<CriteriaTrendsResultCountRecord> item) {
                AjaxLink selectResultLink = new AjaxLink("selectResultLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        selectedResultText = item.getModelObject().getResultText();
                        trendsForResultByCriteriaContainer.addOrReplace(new FlotChart<String>("chart", createChartDataModel(), Model.of(new BarChartOptions<String>()), ""));
                        target.add(trendsForResultByCriteriaContainer.setVisible(true));

                    }
                };
                item.add(selectResultLink);
                selectResultLink.add(new Label("resultText", ProxyModel.of(item.getModel(), on(CriteriaTrendsResultCountRecord.class).getResultText())));
                item.add(new Label("count", ProxyModel.of(item.getModel(), on(CriteriaTrendsResultCountRecord.class).getCount())));
            }
        });

        add(trendsForResultByCriteriaContainer = new WebMarkupContainer("trendsForResultByCriteriaContainer"));
        trendsForResultByCriteriaContainer.setOutputMarkupPlaceholderTag(true).setVisible(false);
    }

    private IModel<ChartData<String>> createChartDataModel() {
        return new LoadableDetachableModel<ChartData<String>>() {
            @Override
            protected ChartData<String> load() {
                ChartSeries<String> series = new ChartSeries<String>(criteriaTrendsService.findCriteriaTrendsByCriteria(eventType, dateRange, selectedResultText));
                return new ChartData<String>(new CriteriaTrendsChartManager(), series);
            }
        };
    }

    private LoadableDetachableModel<List<CriteriaTrendsResultCountRecord>> createTrendsResultModel() {
        return new LoadableDetachableModel<List<CriteriaTrendsResultCountRecord>>() {
            @Override
            protected List<CriteriaTrendsResultCountRecord> load() {
                return criteriaTrendsService.findCriteriaTrends(eventType, dateRange);
            }
        };
    }

    private LoadableDetachableModel<List<CriteriaTrendsResultCountByCriteriaRecord>> createTrendsByCriteriaModel() {
        return new LoadableDetachableModel<List<CriteriaTrendsResultCountByCriteriaRecord>>() {
            @Override
            protected List<CriteriaTrendsResultCountByCriteriaRecord> load() {
                return criteriaTrendsService.findCriteriaTrendsByCriteria(eventType, dateRange, selectedResultText);
            }
        };
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.criteria_trends"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/flot/jquery.flot.min.js");
        response.renderJavaScriptReference("javascript/flot/jquery.flot.stack.min.js");
        response.renderJavaScriptReference("javascript/flot/jquery.flot.navigate.min.js");
        response.renderJavaScriptReference("javascript/flot/jquery.flot.symbol.min.js");
        response.renderJavaScriptReference("javascript/dashboard.js");
    }
}
