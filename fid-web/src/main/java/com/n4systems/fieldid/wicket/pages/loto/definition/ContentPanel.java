package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ContentPanel extends Panel {

    private @SpringBean
    ProcedureSearchService procedureSearchService;

    private List<EditableImage> images;
    private IsolationPointEditor editor;
    private IsolationPointListPanel list;
    private IsolationPoint newIsolationPoint = createIsolationPoint(IsolationPointSourceType.W);
    private int index=1;

    public ContentPanel(String id, final IModel<ProcedureDefinition> model) {
        super(id, model);
        setOutputMarkupId(true);

        add(new AddIsolationPointButton("addButton") {
            @Override protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
                ContentPanel.this.doAdd(target, sourceType);
            }
        });

        add(new AttributeAppender("class", "content"));

        add(list = new IsolationPointListPanel("isolationPoints", model) {

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
                if (editor.isEditing()) {
                    updateIsolationPoint();
                } else {
                    addIsolationPoint();
                }
                target.add(list);
            }

            @Override protected void doCancel(AjaxRequestTarget target) {
            }
        });

        add(new AjaxLink("cancel") {
            @Override public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });
        add(new AjaxLink("continue") {
            @Override public void onClick(AjaxRequestTarget target) {
                doContinue(target);
            }
        });

    }

    protected void doContinue(AjaxRequestTarget target) { }

    protected void doCancel(AjaxRequestTarget target) { }

    private IsolationPoint updateIsolationPoint() {
        return editor.getEditedIsolationPoint();
    }

    private void addIsolationPoint() {
        getProcedureDefinition().getIsolationPoints().add(editor.getEditedIsolationPoint());
    }

    private ProcedureDefinition getProcedureDefinition() {
        return (ProcedureDefinition) getDefaultModelObject();
    }

    protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
        editor.editNew(createIsolationPoint(sourceType));
        editor.openEditor(target);
    }

    private IsolationPoint createIsolationPoint(IsolationPointSourceType sourceType) {
        IsolationPoint isolationPoint = new IsolationPoint();
        isolationPoint.setIdentifier(getNextIdentifier(sourceType));
        return isolationPoint;
    }

    private String getNextIdentifier(IsolationPointSourceType sourceType) {
        // TODO : proper name generation!!!
        return sourceType.name() + "-"+ index++;
    }

}
