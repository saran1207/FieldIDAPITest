package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.IsolationPoint;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ContentPanel extends Panel {

    private @SpringBean PersistenceService persistenceService;

    private List<EditableImage> images;
    private List<IsolationPoint> isolationPoints;
    private final Component editor;
    private final IsolationPointListPanel list;
    private IsolationPoint newIsolationPoint = new IsolationPoint();

    public ContentPanel(String id) {
        super(id);

        setOutputMarkupId(true);

        newIsolationPoint.setCheck("check");
        newIsolationPoint.setIdentifier("E-1");
        newIsolationPoint.setLocation("locsdfsd");
        newIsolationPoint.setMethod("isopMethod");
        newIsolationPoint.setSource("electrical");

        add(new AttributeAppender("class", "content"));

        add(list = new IsolationPointListPanel("isolationPoints", new PropertyModel(this,"isolationPoints")) {
            @Override
            protected void doAdd(AjaxRequestTarget target) {
                target.appendJavaScript("procedureDefinitionPage.openIsolationPointEditor('"+ContentPanel.this.getMarkupId()+"');");
            }
        });

        add(editor = new IsolationPointEditor("isolationPointEditor", new PropertyModel(this,"newIsolationPoint")) {
            @Override protected void doDone(AjaxRequestTarget target, Model<IsolationPoint> isolationPoint) {
                // update list....add new isolation point
                target.appendJavaScript("procedureDefinitionPage.closeIsolationPointEditor('"+ContentPanel.this.getMarkupId()+"');");
            }
            @Override protected void doCancel(AjaxRequestTarget target) {
                // do NOT update model. cancel out.
                target.appendJavaScript("procedureDefinitionPage.closeIsolationPointEditor('"+ContentPanel.this.getMarkupId()+"');");
            }

        });

    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }


}
