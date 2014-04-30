package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureListPanel;
import com.n4systems.fieldid.wicket.components.loto.PublishedProcedureActionsColumn;
import com.n4systems.fieldid.wicket.data.ProcedureDefinitionDataProvider;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import java.util.List;

/**
 * Created by rrana on 2014-04-09.
 */
public class PublishedListAllPage extends ProceduresAllListPage implements IAjaxIndicatorAware{

    private ProcedureListPanel procedureDefinitionListPanel;
    private ProcedureDefinitionDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;

    private String procedureCodeString = null;
    private Asset asset = null;
    private boolean isProcedureCode = false;
    private boolean isAsset = false;

    private String searchTerm = "";
    private String textFilter = null;

    public PublishedListAllPage() {
        super(new IModel<PublishedState>() {
            @Override
            public PublishedState getObject() {
                return PublishedState.PUBLISHED;
            }

            @Override
            public void setObject(PublishedState object) {

            }

            @Override
            public void detach() {

            }
        });
    }

    public PublishedListAllPage(String procedureCodeString, Asset asset, boolean isProcedureCode, boolean isAsset){
        super(new IModel<PublishedState>() {
            @Override
            public PublishedState getObject() {
                return PublishedState.PUBLISHED;
            }

            @Override
            public void setObject(PublishedState object) {

            }

            @Override
            public void detach() {

            }
        });
        this.procedureCodeString = procedureCodeString;
        this.asset = asset;
        this.isProcedureCode = isProcedureCode;
        this.isAsset = isAsset;
        this.searchTerm = asset.getDisplayName();
    }

    @Override
    public String getAjaxIndicatorMarkupId(){
        return "loadingImage";
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        Form<Void> form = new Form<Void>("form");
        add(form);

        final TextField<String> field = new TextField<String>("field", new Model<String>(searchTerm));
        form.add(field);


        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
                PublishedListAllPage.this.dataProvider.setSearchTerm(field.getDefaultModelObjectAsString());
                PublishedListAllPage.this.dataProvider.resetProcedureCodeFlag();
                PublishedListAllPage.this.dataProvider.resetAssetCodeFlag();
                target.add(procedureDefinitionListPanel);
            }
        };
        onChangeAjaxBehavior.setThrottleDelay(Duration.milliseconds(new Long(500)));
        field.add(onChangeAjaxBehavior);

        dataProvider = new ProcedureDefinitionDataProvider("created", SortOrder.DESCENDING, PublishedState.PUBLISHED, procedureCodeString, asset, isProcedureCode, isAsset){
            @Override protected String getTextFilter() {
                return textFilter;
            }
        };

        add(new ContextImage("loadingImage", "images/loading-small.gif"));

        add(procedureDefinitionListPanel = new ProcedureListPanel("procedureDefinitionListPanel", dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends ProcedureDefinition>> columns) {
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends ProcedureDefinition>> columns) {
                addActionsColumn(columns);
            }

            @Override
            protected FIDFeedbackPanel getErrorFeedbackPanel() {
                return feedbackPanel;
            }
        });
        procedureDefinitionListPanel.setOutputMarkupPlaceholderTag(true);
    }


    protected void addActionsColumn(List<IColumn<? extends ProcedureDefinition>> columns) {
        columns.add(new PublishedProcedureActionsColumn(procedureDefinitionListPanel));
    }
}

