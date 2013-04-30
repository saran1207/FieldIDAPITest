package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.loto.ProcedureTitleLabel;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class PreviouslyPublishedListPage extends LotoPage{

    private @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    public PreviouslyPublishedListPage(PageParameters params) {
        super(params);

        add(new BookmarkablePageLink<PreviouslyPublishedListPage>("activeLink", ProcedureDefinitionListPage.class, PageParametersBuilder.uniqueId(getAssetId())));
        add(new BookmarkablePageLink<PreviouslyPublishedListPage>("previouslyPublishedListLink", PreviouslyPublishedListPage.class, PageParametersBuilder.uniqueId(getAssetId())));


        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        ListView listView;

        listContainer.add(listView = new ListView<ProcedureDefinition>("list", new ProcedureDefinitionModel()) {

            @Override
            protected void populateItem(ListItem<ProcedureDefinition> item) {
                final ProcedureDefinition procedureDefinition = item.getModelObject();
                item.add(new Label("name", new PropertyModel<String>(procedureDefinition, "procedureCode")));
                item.add(new Label("revisionNumber", new PropertyModel<String>(procedureDefinition, "revisionNumber")));
                item.add(new Label("developedBy", new PropertyModel<String>(procedureDefinition, "developedBy.displayName")));
                item.add(new Label("created", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "created"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("approvedBy", new PropertyModel<String>(procedureDefinition, "approvedBy")));
                item.add(new Label("originDate", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "originDate"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("retireDate", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "retireDate"), true, getCurrentUser().getTimeZone())));
            }
        });
        listContainer.setVisible(!listView.getList().isEmpty());
        add(listContainer);

        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        blankSlate.setVisible(listView.getList().isEmpty());
        add(blankSlate);
    }

    class ProcedureDefinitionModel extends LoadableDetachableModel<List<ProcedureDefinition>> {

        @Override
        protected List<ProcedureDefinition> load() {
            return procedureDefinitionService.getPreviouslyPublishedProceduresForAsset(assetModel.getObject());
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/newCss/loto/procedures.css");
    }

    @Override
    protected Component createTitleLabel(String labelId, boolean isTopTitle) {
        if(isTopTitle)
            return new Label(labelId, new FIDLabelModel("label.procedures"));
        else
            return new ProcedureTitleLabel(labelId, assetModel);
    }

}
