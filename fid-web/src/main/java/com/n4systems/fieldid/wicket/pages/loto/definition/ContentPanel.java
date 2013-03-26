package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ContentPanel extends Panel {

    private @SpringBean PersistenceService persistenceService;

    private List<EditableImage> images;
    private IsolationPointEditor editor;
    private IsolationPointListPanel list;
    private IsolationPoint newIsolationPoint = createIsolationPoint(IsolationPointSourceType.W);
    private int index=1;

    public ContentPanel(String id, final IModel<ProcedureDefinition> model) {
        super(id,model);

        setOutputMarkupId(true);

        add(new AddIsolationPointButton("addButton") {
            @Override protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
                ContentPanel.this.doAdd(target, sourceType);
            }
        });

        add(new AttributeAppender("class", "content"));

        add(list = new IsolationPointListPanel("isolationPoints", new PropertyModel(model,"isolationPoints")) {

            @Override protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                editor.edit(isolationPoint);
                editor.openEditor(target);
            }

            @Override protected void doDelete(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                model.getObject().getIsolationPoints().remove(isolationPoint);
                target.add(list);
            }
        });

        add(editor = new IsolationPointEditor("isolationPointEditor") {
            @Override protected void doDone(AjaxRequestTarget target,Form<?> form) {
                IsolationPoint editedIsolationPoint = editor.getEditedIsolationPoint();
                if (!editedIsolationPoint.isNew()) {
                    updateIsolationPoint(editedIsolationPoint);
                } else {
                    addIsolationPoint(editedIsolationPoint);
                }
                target.add(list);
            }

            @Override protected void doCancel(AjaxRequestTarget target) {
            }
        });
    }

    private void updateIsolationPoint(IsolationPoint editedIsolationPoint) {
        for (IsolationPoint isolationPoint: getProcedureDefinition().getIsolationPoints()) {
           if (isolationPoint.getId().equals(editedIsolationPoint.getId())) {
               isolationPoint.setIdentifier(editedIsolationPoint.getIdentifier());
               isolationPoint.setCheck(editedIsolationPoint.getCheck());
               isolationPoint.setMethod(editedIsolationPoint.getMethod());
               isolationPoint.setSource(editedIsolationPoint.getSource());
               isolationPoint.setLocation(editedIsolationPoint.getLocation());
               return;
           }
       }
       throw new IllegalStateException("can't find isolation point with id '" + editedIsolationPoint.getId() + "'");
    }

    private void addIsolationPoint(IsolationPoint editedIsolationPoint) {
        getProcedureDefinition().getIsolationPoints().add(editedIsolationPoint);
    }

    private ProcedureDefinition getProcedureDefinition() {
        return (ProcedureDefinition) getDefaultModelObject();
    }

    protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
        editor.createNew(createIsolationPoint(sourceType));
        editor.openEditor(target);
    }

    private IsolationPoint createIsolationPoint(IsolationPointSourceType sourceType) {
//        IsolationDeviceDescription device = new IsolationDeviceDescription();
//        device.setAssetType(AssetTypeBuilder.anAssetType().named("assetType" + index).build());
        IsolationPoint isolationPoint = new IsolationPoint();
        // TODO : proper name generation!!!
        isolationPoint.setIdentifier(sourceType.name() + "-"+ index++);
//        isolationPoint.setCheck("CHECK this is some very very very very very very very very very very very very very very very very  long text blah blah blah ");
//        isolationPoint.setDeviceDefinition(device);
//        isolationPoint.setLocation("locsdfsd");
//        isolationPoint.setMethod("isopMethod this is some very very very very very very very very very very very very very very very very  long text blah blah blah ");
//        isolationPoint.setSource(sourceType.getIdentifier());
        return isolationPoint;
    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }



}
