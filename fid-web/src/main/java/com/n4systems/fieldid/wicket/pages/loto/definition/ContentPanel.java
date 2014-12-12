package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.services.date.DateService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContentPanel extends Panel {

    private @SpringBean ProcedureSearchService procedureSearchService;
    private @SpringBean DateService dateService;

    private final Pattern numberPattern = Pattern.compile(".*(\\d+)");

    private IsolationPointEditor editor;
    private IsolationPointListPanel lockList;
    private IsolationPointListPanel unlockList;

    private Map<String, Boolean> isolationPointMap;

    private IModel<ProcedureDefinition> procedureDefinitionModel;

    public ContentPanel(String id, final IModel<ProcedureDefinition> model) {
        super(id, model);
        setOutputMarkupId(true);
        this.procedureDefinitionModel = model;

        add(new AddIsolationPointButton("addButton") {
            @Override protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
                ContentPanel.this.doAdd(target, sourceType);
            }
        });

        add(new AttributeAppender("class", "content"));

        add(lockList = new IsolationPointListPanel("lockIsolationPoints", model, true) {

            @Override
            protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                editor.edit(isolationPoint, target);
                editor.openEditor(target);
            }

            @Override
            protected void doDelete(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                getProcedureDefinition().removeIsolationPoint(isolationPoint);
                target.add(lockList, unlockList);
            }

            @Override
            protected void reorderIsolationPoint(AjaxRequestTarget target, IsolationPoint isolationPoint, int index) {
                List<IsolationPoint> isolationPointList = getProcedureDefinition().getLockIsolationPoints();

                if(isolationPointList.size() > 1) {
                    if(index == 0) {
                        index = 1;
                    }

                    isolationPointList.remove(isolationPoint);
                    isolationPointList.add(index - 1, isolationPoint);

                    getProcedureDefinition().reindexLockIsolationPoints(isolationPointList);
                }
            }

            @Override
            protected void toggleList(AjaxRequestTarget target, boolean isLockDirection) {
                ContentPanel.this.toggleList(target, isLockDirection);
            }
        });

        lockList.setOutputMarkupPlaceholderTag(true);

        add(unlockList = new IsolationPointListPanel("unlockIsolationPoints", model, false) {

            @Override
            protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) {
                editor.edit(isolationPoint, target);
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

                if(isolationPointList.size() > 1) {
                    if(index == 0) {
                        index = 1;
                    }

                    isolationPointList.remove(isolationPoint);
                    isolationPointList.add(index - 1, isolationPoint);

                    getProcedureDefinition().reindexUnlockIsolationPoints(isolationPointList);
                }
            }

            @Override
            protected void toggleList(AjaxRequestTarget target, boolean isLockDirection) {
                ContentPanel.this.toggleList(target, isLockDirection);
            }
        });
        unlockList.setOutputMarkupPlaceholderTag(true);
        unlockList.setVisible(false);

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
                cleanUpAfterCancel(); //The user cancelled, so we need to clean up any resulting mess...
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

    /**
     * This method performs a quick cleanup to remove unused images and annotations after the user cancels editing an
     * Isolation Point.  Previously, this lead to extra images and annotations being added to the ProcedureDefinition.
     */
    private void cleanUpAfterCancel() {
        collectIsolationPointIdentifiers();

        getProcedureDefinition().getImages()
                                .stream()
                                .forEach(this::lookForAnnotations);
        //Above, I could have passed the list in as a parameter in the method call... but I wouldn't have gotten to use
        //that really cool looking syntax sugar.

        removeCancelledImages();
    }

    /**
     * This method gathers up a map of all isolation points and whether or not they have an attached Annotation.  This
     * information is then used to remove unused annotations and unused images.
     */
    private void collectIsolationPointIdentifiers() {
        isolationPointMap = getProcedureDefinition().getLockIsolationPoints()
                                                    .stream()
                                                    //Here, we're grabbing all of the Isolation Points and making a map
                                                    //with their identifiers and whether or not they have an Annotation.
                                                    .collect(Collectors.toMap(IsolationPoint::getIdentifier,
                                                                              a -> a.getAnnotation() != null));
    }

    /**
     * This method Claws out any unused annotations.  These annotations don't live in the DB yet and are associated with
     * the Isolation Point the user is about to back out of creating.
     *
     * @param image - A ProcedureDefinitionImage representing an image from the ProcedureDefinition which may not be used.
     */
    private void lookForAnnotations(ProcedureDefinitionImage image) {
        List<ImageAnnotation> removeUs =
                image.getAnnotations()
                     .stream()
                     //Filter out any ImageAnnotation that is either not found in the map, or relates back to an
                     //IsolationPoint WITHOUT an Annotation... that means it's an Annotation we don't want.
                     .filter(annotation -> isolationPointMap.get(annotation.getText()) == null ||
                                          !isolationPointMap.get(annotation.getText()))
                     //Put all of these annotations into a list.
                     .collect(Collectors.toList());

        //Now remove those from the list.  We have to do this after we're done processing.
        image.getAnnotations().removeAll(removeUs);
    }

    /**
     * This method is used to claw out any unused images.  This is determined by looking for images with no annotations.
     * Since those would already have been removed by this point, any images without annotations are unneeded.
     */
    private void removeCancelledImages() {
        List<ProcedureDefinitionImage> removeUs =
                getProcedureDefinition().getImages()
                                        .stream()
                                        //Filter out all images that have no annotations... we need to remove them.
                                        .filter(image -> image.getAnnotations() == null ||
                                                image.getAnnotations().size() == 0)
                                        //Put those images into a List...
                                        .collect(Collectors.toList());

        //Then use that list to remove the unwanted images.
        getProcedureDefinition().getImages().removeAll(removeUs);
    }

    protected void doContinue(AjaxRequestTarget target) { }

    protected void doCancel(AjaxRequestTarget target) { }

    public void onAnnotationStyleSelected(AjaxRequestTarget target) {
        lockList.reloadImageList(target, procedureDefinitionModel);
        unlockList.reloadImageList(target, procedureDefinitionModel);
    }

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

    private void toggleList(AjaxRequestTarget target, boolean isLockDirection) {
        lockList.setVisible(!isLockDirection);
        unlockList.setVisible(isLockDirection);
        target.add(lockList, unlockList);
    }

    private IsolationPoint createIsolationPoint(IsolationPointSourceType sourceType) {
        IsolationPoint isolationPoint = new IsolationPoint();
        isolationPoint.setIdentifier(getNextIdentifier(sourceType));
        isolationPoint.setSourceType(sourceType);
        isolationPoint.setSourceText(sourceType.getIdentifier());
        isolationPoint.setTenant(getProcedureDefinition().getTenant());
        isolationPoint.setFwdIdx(getProcedureDefinition().getLockIsolationPoints().size());
        isolationPoint.setRevIdx(0);
        return isolationPoint;
    }

    private String getNextIdentifier(IsolationPointSourceType sourceType) {
        TreeMap<Long, IsolationPoint> reservedSourceNumbers=new TreeMap<>();
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
}
