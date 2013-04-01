package com.n4systems.fieldid.wicket.pages.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.fieldid.wicket.components.menuButton.MenuButton;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ProceduresPage extends LotoPage {

    private @SpringBean ProcedureSearchService procedureService;

    enum NewMode {
        COPY_EXISTING("label.copy_existing"), FROM_SCRATCH("label.start_blank");

        private String label;
        NewMode(String label) {this.label = label;}
        public String getLabel() {return label;}
    }

    public ProceduresPage(PageParameters params) {
        super(params);

        add(new BookmarkablePageLink<VersionsPage>("versionsLink", VersionsPage.class, PageParametersBuilder.uniqueId(getAssetId())));

        add(new MenuButton<NewMode>("newProcedure", new FIDLabelModel("label.new"), Lists.newArrayList(NewMode.values())) {
            @Override protected void buttonClicked(AjaxRequestTarget target) {
                doNewProcedureDef(NewMode.FROM_SCRATCH);
            }

            @Override protected Component populateLink(String linkId, String labelId, ListItem<NewMode> item) {
                final NewMode mode = item.getModelObject();
                return new Link(linkId) {
                    @Override public void onClick() {
                        doNewProcedureDef(mode);
                    }
                }.add(new Label(labelId, new FIDLabelModel(mode.getLabel())));
            }
        });

        add(new ListView<ProcedureDefinition>("list", new ProcedureDefinitionModel()) {

            @Override
            protected void populateItem(ListItem<ProcedureDefinition> item) {
                final ProcedureDefinition procedureDefinition = item.getModelObject();
                item.add(new Label("name",procedureDefinition.getProcedureCode()));
                // TODO : convert dates into friendly format.
                item.add(new Label("created", procedureDefinition.getCreated().toString()));
                item.add(new Label("lastModified", procedureDefinition.getModified().toString()));
                item.add(new Link("edit") {
                    @Override public void onClick() {
                        editProcedureDefinition(procedureDefinition);
                    }
                });
            }
        });
    }

    private void editProcedureDefinition(ProcedureDefinition procedureDefinition) {
        setResponsePage(new ProcedureDefinitionPage(new PageParameters().add("id",procedureDefinition.getId())));
    }

    private void doNewProcedureDef(NewMode mode) {
        switch (mode) {
            case COPY_EXISTING:
                // TODO : copy from existing page.  for now i'll just do brand new procDefs.
                setResponsePage(new ProcedureDefinitionPage(newProcedureDefinition()));
                break;
            case FROM_SCRATCH:
                setResponsePage(new ProcedureDefinitionPage(newProcedureDefinition()));
                break;
        }
    }

    private ProcedureDefinition newProcedureDefinition() {
        ProcedureDefinition pd = new ProcedureDefinition();
        pd.setAsset(assetModel.getObject());
        pd.setTenant(assetModel.getObject().getTenant());
        return pd;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/new_procedure_list.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/newCss/loto/procedures.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.procedures"));
    }


    class ProcedureDefinitionModel extends LoadableDetachableModel<List<ProcedureDefinition>> {

        @Override
        protected List<ProcedureDefinition> load() {
            return procedureService.getProceduresForAsset(assetModel.getObject());
        }
    }

}
