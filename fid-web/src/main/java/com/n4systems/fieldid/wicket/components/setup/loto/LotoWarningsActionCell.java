package com.n4systems.fieldid.wicket.components.setup.loto;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This is the Action Cell for the LOTO Warnings List Panel.
 *
 * Created by Jordan Heath on 14-11-20.
 */
public class LotoWarningsActionCell extends Panel {

    @SpringBean
    private WarningTemplateService warningTemplateService;

    protected WarningTemplate thisTemplate;

    public LotoWarningsActionCell(String id,
                                  IModel<WarningTemplate> model) {
        super(id);

        this.thisTemplate = model.getObject();

        //Create the necessary Links and other pretty components here
        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        optionsContainer.add(new AjaxLink<Void>("editLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doEdit(target);
            }
        }.setOutputMarkupId(true));

        optionsContainer.add(new AjaxLink<Void>("deleteLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doDelete(target);
            }
        }.setOutputMarkupId(true));

        add(optionsContainer);
    }

    protected void doDelete(AjaxRequestTarget target) {}

    protected void doEdit(AjaxRequestTarget target) {}
}
