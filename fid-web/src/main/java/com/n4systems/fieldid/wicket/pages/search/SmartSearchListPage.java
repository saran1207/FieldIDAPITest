package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.search.results.SmartSearchListPanel;
import com.n4systems.fieldid.wicket.data.SmartSearchDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by rrana on 2015-06-22.
 */
public class SmartSearchListPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetService assetService;

    private SmartSearchListPanel smartSearchListPanel;
    private SmartSearchDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;

    private String searchTerm = "";

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.view_assets"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/procedureDefinition.css");
    }

    public SmartSearchListPage(PageParameters params) {
        super();
        this.searchTerm = params.get("searchTerm").toString();

        if(assetService.findExactAssetSizeByIdentifiersForNewSmartSearch(searchTerm) == 1) {
            Asset smartSearchAsset = assetService.findExactAssetByIdentifiersForNewSmartSearch(searchTerm).get(0);
            setResponsePage(new AssetSummaryPage(smartSearchAsset));
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        dataProvider = new SmartSearchDataProvider(searchTerm);

        //add(new ContextImage("loadingImage", "images/loading-small.gif"));

        add(smartSearchListPanel = new SmartSearchListPanel("smartSearchListPanel", dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends Asset>> columns) {
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends Asset>> columns) {
            }

            @Override
            protected FIDFeedbackPanel getErrorFeedbackPanel() {
                return feedbackPanel;
            }
        });
        smartSearchListPanel.setOutputMarkupPlaceholderTag(true);
    }

}