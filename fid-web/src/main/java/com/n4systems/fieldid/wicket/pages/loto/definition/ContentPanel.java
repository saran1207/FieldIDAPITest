package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ContentPanel extends Panel {

    private @SpringBean PersistenceService persistenceService;

    private List<EditableImage> images;
    private List<IsolationPoint> isolationPoints = Lists.newArrayList();
    private final IsolationPointEditor editor;
    private final IsolationPointListPanel list;
    private IsolationPoint newIsolationPoint = createIsolationPoint(IsolationPointSourceType.W);
    private int index=1;

    public ContentPanel(String id, IModel<ProcedureDefinition> model) {
        super(id);

        setOutputMarkupId(true);

        for (IsolationPointSourceType type:IsolationPointSourceType.values()) {
            isolationPoints.add(createIsolationPoint(type));
        }

        add(new AddIsolationPointButton("addButton") {
            @Override protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
                ContentPanel.this.doAdd(target, sourceType);
            }
        });

        add(new AttributeAppender("class", "content"));

        add(list = new IsolationPointListPanel("isolationPoints", new PropertyModel(model,"isolationPoints")) {

            @Override protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                newIsolationPoint = isolationPoint;
                editor.openEditor(target);
            }

            @Override protected void doDelete(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                isolationPoints.remove(isolationPoint);
                target.add(list);
            }
        });

        add(editor = new IsolationPointEditor("isolationPointEditor", new PropertyModel(this,"newIsolationPoint")) {
            @Override protected void doDone(AjaxRequestTarget target, Model<IsolationPoint> isolationPoint) {
                // detect if new or not?
                IsolationPoint editedIsolationPoint = (IsolationPoint) editor.getDefaultModelObject();
                if (!isolationPoints.contains(editedIsolationPoint)) {
                    isolationPoints.add(editedIsolationPoint);
                } else {
                    // do i need to persist the new one?  or should that be done later???  (yes, handle that in service imo).
                }
                target.add(list);
            }
            @Override protected void doCancel(AjaxRequestTarget target) {
            }
        });

    }

    protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
        newIsolationPoint = createIsolationPoint(sourceType);
        editor.openEditor(target);
    }

    private IsolationPoint createIsolationPoint(IsolationPointSourceType sourceType) {
        IsolationDeviceDescription device = new IsolationDeviceDescription();
        device.setAssetType(AssetTypeBuilder.anAssetType().named("assetType" + index).build());
        IsolationPoint isolationPoint = new IsolationPoint();
        isolationPoint.setCheck("CHECK this is some very very very very very very very very very very very very very very very very  long text blah blah blah ");
        isolationPoint.setIdentifier("E-" + index++);
        isolationPoint.setDeviceDefinition(device);
        isolationPoint.setLocation("locsdfsd");
        isolationPoint.setMethod("isopMethod this is some very very very very very very very very very very very very very very very very  long text blah blah blah ");
        isolationPoint.setSource(sourceType.getIdentifier());
        return isolationPoint;
    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }

}
