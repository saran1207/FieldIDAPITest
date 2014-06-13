package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.event.ProcedureAuditEventService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by rrana on 2014-06-10.
 */
public class ProcedureAuditPage extends FieldIDTemplatePage {

    @SpringBean
    private ProcedureAuditEventService procedureAuditEventService;

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.upcoming_audits"));
    }

    @Override
    protected void addNavBar(String navBarId) {

        Long auditCount = procedureAuditEventService.getAllAuditCount("");
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.upcoming_audits", auditCount)).page(ProcedureAuditListPage.class).build()
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