package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.services.date.DateService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentPanel extends Panel {

    private @SpringBean ProcedureSearchService procedureSearchService;
    private @SpringBean DateService dateService;

    private final Pattern numberPattern = Pattern.compile(".*(\\d+)");

    private IsolationPointEditor editor;
    private IsolationPointListPanel lockList;
    private IsolationPointListPanel unlockList;

    private boolean openEditor = true;

    public ContentPanel(String id, final IModel<ProcedureDefinition> model) {
        super(id, model);
        setOutputMarkupId(true);

        add(new AddIsolationPointButton("addButton") {
            @Override protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
                ContentPanel.this.doAdd(target, sourceType);
            }
        });

        add(new AttributeAppender("class", "content"));

        add(lockList = new IsolationPointListPanel("lockIsolationPoints", model, true) {

            @Override protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) {
               editor.edit(isolationPoint);
               editor.openEditor(target);
            }

            @Override protected void doDelete(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                getProcedureDefinition().removeIsolationPoint(isolationPoint);
                target.add(lockList, unlockList);
            }

            @Override protected void reorderIsolationPoint(AjaxRequestTarget target, IsolationPoint isolationPoint, int index) {
                List<IsolationPoint> isolationPointList = getProcedureDefinition().getLockIsolationPoints();

                isolationPointList.remove(isolationPoint);
                isolationPointList.add(index - 1, isolationPoint);

                getProcedureDefinition().reindexLockIsolationPoints(isolationPointList);
            }
        });
        lockList.setOutputMarkupPlaceholderTag(true);

        add(unlockList = new IsolationPointListPanel("unlockIsolationPoints", model, false) {

            @Override
            protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                editor.edit(isolationPoint);
                editor.openEditor(target);
            }

            @Override
            protected void doDelete(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                getProcedureDefinition().removeIsolationPoint(isolationPoint);
                target.add(lockList, unlockList);
            }

            @Override
            protected void reorderIsolationPoint(AjaxRequestTarget target, IsolationPoint isolationPoint, int index) {
                List<IsolationPoint> isolationPointList = getProcedureDefinition().getUnlockIsolationPoints();

                isolationPointList.remove(isolationPoint);
                isolationPointList.add(index - 1, isolationPoint);

                getProcedureDefinition().reindexUnlockIsolationPoints(isolationPointList);
            }
        });
        unlockList.setOutputMarkupPlaceholderTag(true);
        unlockList.setVisible(false);

        add(new AjaxLink<Void>("showLockOrder") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                lockList.setVisible(true);
                unlockList.setVisible(false);
                target.add(lockList, unlockList);
                target.appendJavaScript("$('.show-unlock-order').removeClass('mattButtonPressed');$('.show-lock-order').addClass('mattButtonPressed');");
            }
        });

        add(new AjaxLink<Void>("showUnlockOrder") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                lockList.setVisible(false);
                unlockList.setVisible(true);
                target.add(lockList, unlockList);
                target.appendJavaScript("$('.show-lock-order').removeClass('mattButtonPressed');$('.show-unlock-order').addClass('mattButtonPressed');");
            }
        });

        add(editor = new IsolationPointEditor("isolationPointEditor", getProcedureDefinition()) {
            @Override
            protected void doDone(AjaxRequestTarget target, Form<?> form) {
                if (editor.isEditing()) {
                    updateIsolationPoint();
                } else {
                    addIsolationPoint();
                }
                target.add(lockList, unlockList);
            }

            @Override
            protected void doCancel(AjaxRequestTarget target) {
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
        getProcedureDefinition().addIsolationPoint(editor.getEditedIsolationPoint());
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
        isolationPoint.setTenant(getProcedureDefinition().getTenant());
        isolationPoint.setFwdIdx(getProcedureDefinition().getLockIsolationPoints().size());
        isolationPoint.setRevIdx(0);
        return isolationPoint;
    }

    private String getNextIdentifier(IsolationPointSourceType sourceType) {
        TreeMap<Long, IsolationPoint> reservedSourceNumbers=new TreeMap<Long,IsolationPoint>();
        for (IsolationPoint isolationPoint:getProcedureDefinition().getLockIsolationPoints()) {
            if (isolationPoint.getSourceType().equals(sourceType)) {
                Long number = parseNumber(isolationPoint.getIdentifier());
                if (number!=null) {
                    reservedSourceNumbers.put(number,isolationPoint);
                }
            }
        }
        Long next = reservedSourceNumbers.size()>0 ? reservedSourceNumbers.lastEntry().getKey()+1L : 1L;
        return sourceType.name() + "-" +  next;
    }

    private Long parseNumber(String text) {
        Matcher matcher = numberPattern.matcher(text);
        if (matcher.matches() && matcher.groupCount()>=1) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

    public boolean isOpenEditor() {
        return openEditor;
    }

    public void setOpenEditor(boolean openEditor) {
        this.openEditor = openEditor;
    }
}
