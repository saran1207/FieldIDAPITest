package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.IsolationDeviceDescription;
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
    private List<IsolationPoint> isolationPoints = Lists.newArrayList();
    private final Component editor;
    private final IsolationPointListPanel list;
    private IsolationPoint newIsolationPoint = createIsolationPoint();
    private int index=1;

    public ContentPanel(String id) {
        super(id);

        setOutputMarkupId(true);

        isolationPoints.add(createIsolationPoint());
        isolationPoints.add(createIsolationPoint());
        isolationPoints.add(createIsolationPoint());
        isolationPoints.add(createIsolationPoint());

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

    private IsolationPoint createIsolationPoint() {
        IsolationDeviceDescription device = new IsolationDeviceDescription();
        device.setAssetType(AssetTypeBuilder.anAssetType().named("assetType"+index).build());
        IsolationPoint isolationPoint = new IsolationPoint();
        isolationPoint.setCheck("CHECK this is some very very very very very very very very very very very very very very very very  long text blah blah blah ");
        isolationPoint.setIdentifier("E-" + index++);
        isolationPoint.setDeviceDefinition(device);
        isolationPoint.setLocation("locsdfsd");
        isolationPoint.setMethod("isopMethod this is some very very very very very very very very very very very very very very very very  long text blah blah blah ");
        isolationPoint.setSource("electrical");
        return isolationPoint;
    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }


}
