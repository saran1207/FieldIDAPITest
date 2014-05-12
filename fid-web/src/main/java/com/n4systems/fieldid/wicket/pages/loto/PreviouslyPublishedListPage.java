package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.loto.ViewPrintProcedureDefMenuButton;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
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

        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        ListView listView;

        listContainer.add(listView = new ListView<ProcedureDefinition>("list", new ProcedureDefinitionModel()) {

            @Override
            protected void populateItem(final ListItem<ProcedureDefinition> item) {
                final IModel<ProcedureDefinition> procedureDefinition = item.getModel();
                item.add(new Label("name", new PropertyModel<String>(procedureDefinition, "procedureCode")));
                item.add(new Label("revisionNumber", new PropertyModel<String>(procedureDefinition, "revisionNumber")));
                item.add(new Label("developedBy", new PropertyModel<String>(procedureDefinition, "developedBy.displayName")));
                item.add(new Label("created", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "created"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("approvedBy", new PropertyModel<String>(procedureDefinition, "approvedBy")));
                item.add(new Label("originDate", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "originDate"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("retireDate", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "retireDate"), true, getCurrentUser().getTimeZone())));
                item.add(new ViewPrintProcedureDefMenuButton("print", procedureDefinition));
                item.add(new AjaxLink<Void>("restoreLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        ProcedureDefinition newRevision = procedureDefinitionService.cloneProcedureDefinition(item.getModelObject());
                        newRevision.setPublishedState(PublishedState.DRAFT);
                        setResponsePage(new ProcedureDefinitionPage(Model.of(newRevision)));
                    }
                });
            }
        });
        listContainer.setVisible(!listView.getList().isEmpty());
        add(listContainer);

        blankSlate.setVisible(listView.getList().isEmpty());
        add(blankSlate);
    }

    class ProcedureDefinitionModel extends LoadableDetachableModel<List<ProcedureDefinition>> {

        @Override
        protected List<ProcedureDefinition> load() {
            return procedureDefinitionService.getPreviouslyPublishedProceduresForAsset(assetModel.getObject());
        }
    }

}
