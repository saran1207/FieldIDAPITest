package com.n4systems.fieldid.wicket.pages.loto;

import com.google.common.collect.Lists;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ProceduresPage extends LotoPage {

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

    }

    private void doNewProcedureDef(NewMode mode) {
        switch (mode) {
            case COPY_EXISTING:
                // TODO : copy from existing page.  for now i'll just do brand new procDefs.
                setResponsePage(new ProcedureDefinitionPage(Model.of(new ProcedureDefinition())));
                break;
            case FROM_SCRATCH:
                setResponsePage(new ProcedureDefinitionPage(Model.of(new ProcedureDefinition())));
                break;
        }
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

}
