package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.user.User;
import com.n4systems.services.date.DateService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentPanel extends Panel {

    private @SpringBean ProcedureSearchService procedureSearchService;
    private @SpringBean DateService dateService;

    private final Pattern numberPattern = Pattern.compile(".*(\\d+)");

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

            @Override protected void reorderIsolationPoint(AjaxRequestTarget target, IsolationPoint isolationPoint, int index) {
                List<IsolationPoint> isolationPoints = model.getObject().getIsolationPoints();
                isolationPoints.remove(isolationPoint);
                isolationPoints.add(index,isolationPoint);
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
        isolationPoint.setSourceType(sourceType);
        User user = FieldIDSession.get().getSessionUser().getSecurityFilter().getUser();
        isolationPoint.setCreatedBy(user);
        isolationPoint.setModifiedBy(user);
        isolationPoint.setTenant(getProcedureDefinition().getTenant());
        Date now = dateService.now().toDate();
        isolationPoint.setCreated(now);
        isolationPoint.setModified(now);
        return isolationPoint;
    }

    private String getNextIdentifier(IsolationPointSourceType sourceType) {
        TreeMap<Long, IsolationPoint> reservedSourceNumbers=new TreeMap<Long,IsolationPoint>();
        for (IsolationPoint isolationPoint:getProcedureDefinition().getIsolationPoints()) {
            if (isolationPoint.getSourceType().equals(sourceType)) {
                Long number = parseNumber(isolationPoint.getIdentifier());
                if (number!=null) {
                    reservedSourceNumbers.put(number,isolationPoint);
                }
            }
        }
        Long next = reservedSourceNumbers.size()>0 ? reservedSourceNumbers.lastEntry().getKey()+1L : 1L;
        return sourceType.name() + " -" +  next;
    }

    private Long parseNumber(String text) {
        Matcher matcher = numberPattern.matcher(text);
        if (matcher.matches() && matcher.groupCount()>=1) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

}
