package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 *
 * This is the base class for the "Procedures" page.  The three different tabbed pages under this will extend it.
 *
 * Created by rrana on 2014-04-09.
 */
public abstract class ProceduresAllListPage extends FieldIDTemplatePage {

    @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.procedures"));
    }


    @Override
    protected void addNavBar(String navBarId) {

        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.procedures_published")).page(PublishedListAllPage.class).build(),
                aNavItem().label(new FIDLabelModel("label.procedures_drafts_count", procedureDefinitionService.getDraftCount())).page(DraftListAllPage.class).build(),
                aNavItem().label(new FIDLabelModel("label.procedures_previously_published")).page(PreviouslyPublishedListAllPage.class).build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/procedureDefinition.css");
        response.renderJavaScriptReference("javascript/procedureDefinitionPage.js");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
    }
}