package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.action.ActionsPanel;
import com.n4systems.fieldid.wicket.components.event.criteria.edit.*;
import com.n4systems.fieldid.wicket.components.event.observations.DeficienciesEditPanel;
import com.n4systems.fieldid.wicket.components.event.observations.RecommendationsEditPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.popup.Popup;
import com.n4systems.fieldid.wicket.components.richText.RichTextDisplay;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.event.criteriaimage.CriteriaImageListPage;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.NumberFieldCriteriaRule;
import com.n4systems.model.criteriarules.OneClickCriteriaRule;
import com.n4systems.model.criteriarules.SelectCriteriaRule;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;
import java.util.Optional;

public class CriteriaSectionEditPanel extends Panel {

    private IModel<List<CriteriaResult>> results;
    private DialogModalWindow criteriaImagesModalWindow;
    private DialogModalWindow criteriaModalWindow;
    private DialogModalWindow actionsWindow;
    private WebMarkupContainer instructionsViewer;
    private Popup popup;
    private RichTextDisplay richTextDisplay;
    private boolean showActionButtons;
    private boolean showAttachmentsAndActions;
    private Class<? extends AbstractEvent> eventClass;

    public CriteriaSectionEditPanel(String id, final Class<? extends AbstractEvent> eventClass, IModel<List<CriteriaResult>> results, boolean showActionButtons) {
        this(id, eventClass, results, showActionButtons, true);
    }

    public CriteriaSectionEditPanel(String id, final Class<? extends AbstractEvent> eventClass, IModel<List<CriteriaResult>> results, boolean showActionButtons, boolean showAttachmentsAndActions) {
        super(id);

        this.eventClass = eventClass;

        add(new AttributeAppender("class", "form-horizontal"));
        this.showActionButtons = showActionButtons;
        this.showAttachmentsAndActions = showAttachmentsAndActions;
        setOutputMarkupPlaceholderTag(true);
        add(new CriteriaEditForm("criteriaEditForm", eventClass, results));

        add(criteriaImagesModalWindow = new DialogModalWindow("imagesWindow"));
        criteriaImagesModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                target.add(CriteriaSectionEditPanel.this);
                return true;
            }
        });

        add(criteriaModalWindow = new DialogModalWindow("observationWindow"));
        criteriaModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                target.add(CriteriaSectionEditPanel.this);
                return true;
            }
        });

        add(actionsWindow = new DialogModalWindow("actionsWindow"));
        actionsWindow.setInitialWidth(350);
        actionsWindow.setInitialHeight(600);

        add(popup = new Popup("instructionsDialog") {
            { setVisible(false); }
            @Override protected WebMarkupContainer createContent(String id) {
                return richTextDisplay =  new RichTextDisplay(id, Model.of(""));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/nicEdit/nicEdit-min.js");
    }

    class CriteriaEditForm extends Form<List<CriteriaResult>> {

        public CriteriaEditForm(String id, final Class<? extends AbstractEvent> eventClass, IModel<List<CriteriaResult>> results) {
            super(id, results);
            setMultiPart(true);

            add(new ListView<CriteriaResult>("criteria", results) {
                @Override
                protected void populateItem(final ListItem<CriteriaResult> item) {
                    final CriteriaResult criteriaResult = item.getModel().getObject();

                    item.add(createCriteriaHelpTooltip(item, criteriaResult));

                    if (criteriaResult.getCriteria().isRequired()) {
                        item.add(new FlatLabel("requiredIndicatior", "*"));
                    } else {
                        item.add(new FlatLabel("requiredIndicatior"));
                    }
                    item.add(new FlatLabel("criteriaName", new PropertyModel<String>(item.getModel(), "criteria.displayText")));
                    item.add(createEditorFor("criteriaEditor", item.getModel()));
                    item.add(new CriteriaActionButton("recommendationsButton", "images/rec-icon.png", criteriaResult.getRecommendations().size(), "label.recommendations", "btn-secondary") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            criteriaModalWindow.setTitle(new FIDLabelModel("label.recommendations"));
                            criteriaModalWindow.setContent(new RecommendationsEditPanel(criteriaModalWindow.getContentId(), item.getModel()) {
                                @Override
                                protected void onClose(AjaxRequestTarget target) {
                                    target.add(CriteriaSectionEditPanel.this);
                                    criteriaModalWindow.close(target);
                                }
                            });
                            criteriaModalWindow.show(target);
                        }
                    });
                    item.add(new CriteriaActionButton("deficienciesButton", "images/def-icon.png", criteriaResult.getDeficiencies().size(), "label.deficiencies", "btn-secondary") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            criteriaModalWindow.setTitle(new FIDLabelModel("label.deficiencies"));
                            criteriaModalWindow.setContent(new DeficienciesEditPanel(criteriaModalWindow.getContentId(), item.getModel()) {
                                @Override
                                protected void onClose(AjaxRequestTarget target) {
                                    target.add(CriteriaSectionEditPanel.this);
                                    criteriaModalWindow.close(target);
                                }
                            });
                            criteriaModalWindow.show(target);
                        }
                    });

                    item.add(new CriteriaActionButton("criteriaImageButton", "images/camera-icon.png", criteriaResult.getCriteriaImages().size(), "label.images", "btn-secondary") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            showImagesModal(target, item.getModel());
                        }

                    }.setVisible(showAttachmentsAndActions));

                    item.add(new CriteriaActionButton("actionsLink", "images/action-icon.png", criteriaResult.getActions().size(), "label.actions", "btn-secondary") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            actionsWindow.setTitle(new FIDLabelModel("label.actions"));
                            Class<? extends Event> actionEventClass = eventClass.equals(SubEvent.class) ? ThingEvent.class : (Class<? extends Event>) eventClass;
                            actionsWindow.setContent(new ActionsPanel(actionsWindow.getContentId(), item.getModel(), actionEventClass, null, false, false));
                            actionsWindow.setCloseButtonCallback(createActionsCloseButtonCallback());
                            actionsWindow.show(target);
                        }
                    }.setVisible(showAttachmentsAndActions));

                    item.add(new CriteriaActionButton("showActionsButton", "images/icon-expand.png", null, "label.add_action", "btn-secondary") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            showActions(item);
                            target.add(item);
                            target.appendJavaScript("$('.tipsy').remove();");  // tipsy tooltips hang around after ajax. must manually clean them up.
                        }
                    }.setVisible(showActionButtons));

                    showActionsIfAvailable(item);
                }

                private Component createCriteriaHelpTooltip(final ListItem<CriteriaResult> item, final CriteriaResult criteriaResult) {
                    Component image = new ContextImage("tooltip", "images/tooltip-icon.png").setOutputMarkupId(true).add(new AttributeAppender("class", Model.of("invisible"), " "));
                    image.setVisible(criteriaResult.getCriteria().hasNonEmptyInstructions());
                    image.add(new AjaxEventBehavior("onclick") {
                        @Override protected void onEvent(AjaxRequestTarget target) {
                            richTextDisplay.setText(Model.of(criteriaResult.getCriteria().getInstructions()));
                            popup.setVisible(true);
                            target.add(popup);
                            target.appendJavaScript(String.format("$('#%s').appendTo('#%s')", popup.getMarkupId(), item.getMarkupId()));
                            target.appendJavaScript("putSpinnersOverImages();");
                        }
                        });
                    return image;
                }

                private void showActionsIfAvailable(ListItem<CriteriaResult> item) {
                    item.setOutputMarkupId(true);
                    CriteriaResult result = item.getModel().getObject();
                    if (result.getDeficiencies().isEmpty() && result.getCriteriaImages().isEmpty() && result.getActions().isEmpty() && result.getRecommendations().isEmpty()) {
                        return;
                    }
                    showActions(item);
                }


                private void showActions(ListItem<CriteriaResult> item) {
                    item.add(new AttributeAppender("class", Model.of("expand-actions"), " "));
                }

            });
        }


    }

    @SuppressWarnings("unchecked")
    private Component createEditorFor(String componentId, IModel<? extends CriteriaResult> resultModel) {
        CriteriaResult result = resultModel.getObject();
        if (result instanceof TextFieldCriteriaResult) {
            return new TextCriteriaEditPanel(componentId, (IModel<TextFieldCriteriaResult>) resultModel);
        } else if (result instanceof SelectCriteriaResult) {
            return new SelectCriteriaEditPanel(componentId, (IModel<SelectCriteriaResult>) resultModel) {
                @Override
                protected void doUpdateAction(AjaxRequestTarget target, IModel<SelectCriteriaResult> result) {
                    Optional<SelectCriteriaRule> optionalRule =
                            result.getObject()
                                    .getCriteria()
                                    .getRules()
                                    .stream()
                                    //Filter out any rules that apply to the given criteria value...
                                    .filter(rule -> ((SelectCriteriaRule)rule).getSelectValue().equals(result.getObject().getValue()))
                                    //Convert them to SelectCriteriaRules, because they're CriteriaRules otherwise...
                                    .map(rule -> (SelectCriteriaRule)rule)
                                    .findAny();

                    if(optionalRule.isPresent()) {
                        handleModalSelection(optionalRule.get().getAction(), target, Model.of((CriteriaResult)result.getObject()));
                    }
                }
            };
        } else if (result instanceof OneClickCriteriaResult) {
            return new OneClickCriteriaEditPanel(componentId, (IModel<OneClickCriteriaResult>) resultModel) {
                @Override
                protected void doUpdateAction(AjaxRequestTarget target, IModel<OneClickCriteriaResult> result) {
                    Optional<OneClickCriteriaRule> optionalRule =
                            result.getObject()
                                    .getCriteria()
                                    .getRules()
                                    .stream()
                                    .filter(rule -> ((OneClickCriteriaRule) rule).getButton().getId().equals(result.getObject().getButton().getId()))
                                    .map(rule -> (OneClickCriteriaRule)rule)
                                    .findAny();

                    if(optionalRule.isPresent()) {
                        handleModalSelection(optionalRule.get().getAction(), target, Model.of((CriteriaResult)result.getObject()));
                    }
                }
            };
        } else if (result instanceof NumberFieldCriteriaResult) {
            return new NumberFieldCriteriaEditPanel(componentId, (IModel<NumberFieldCriteriaResult>) resultModel) {
                @Override
                protected void doUpdateAction(AjaxRequestTarget target, IModel<NumberFieldCriteriaResult> result) {
                    Optional<NumberFieldCriteriaRule> optionalRule =
                            result.getObject()
                                    .getCriteria()
                                    .getRules()
                                    .stream()
                                    .filter(rule -> {
                                        //These rules are slightly more complex than the others...
                                        double value1 = ((NumberFieldCriteriaRule)rule).getValue1();
                                        Double value2 = ((NumberFieldCriteriaRule)rule).getValue2();
                                        switch (((NumberFieldCriteriaRule)rule).getComparisonType()) {
                                            case LE:
                                                return result.getObject().getValue() <= value1;
                                            case GE:
                                                return result.getObject().getValue() >= value1;
                                            case EQ:
                                                return result.getObject().getValue() == value1;
                                            case BT:
                                                return result.getObject().getValue() >= value1 &&
                                                        result.getObject().getValue() <= value2;

                                            default:
                                                return false;
                                        }
                                    })
                                    .map(rule -> (NumberFieldCriteriaRule)rule)
                                    .findFirst();

                    if(optionalRule.isPresent()) {
                        handleModalSelection(optionalRule.get().getAction(), target, Model.of((CriteriaResult)result.getObject()));
                    }
                }
            };
        } else if (result instanceof ScoreCriteriaResult) {
            return new ScoreCriteriaEditPanel(componentId, (IModel<ScoreCriteriaResult>) resultModel);
        } else if (result instanceof ObservationCountCriteriaResult) {
            return new ObservationCountCriteriaEditPanel(componentId, (IModel<ObservationCountCriteriaResult>) resultModel);
        } else if (result instanceof DateFieldCriteriaResult) {
            return new DateCriteriaEditPanel(componentId, (IModel<DateFieldCriteriaResult>) resultModel);
        } else if (result instanceof UnitOfMeasureCriteriaResult) {
            return new UnitOfMeasureCriteriaEditPanel(componentId, (IModel<UnitOfMeasureCriteriaResult>) resultModel);
        } else if (result instanceof SignatureCriteriaResult) {
            return new SignatureCriteriaResultEditPanel(componentId, (IModel<SignatureCriteriaResult>) resultModel);
        } else if (result instanceof ComboBoxCriteriaResult) {
            return new ComboBoxCriteriaEditPanel(componentId, (IModel<ComboBoxCriteriaResult>) resultModel);
        } else {
            return new WebMarkupContainer(componentId);
        }
    }

    private void handleModalSelection(CriteriaRule.ActionType action, AjaxRequestTarget target, IModel<CriteriaResult> model) {
        switch(action) {
            case IMAGE:
                showImagesModal(target, model);
                break;

            case ACTION:
                showActionsModal(target, model);
                break;

            default:
                //Do nothing, look pretty...
        }
    }

    private void showImagesModal(AjaxRequestTarget ajaxRequestTarget, IModel<CriteriaResult> resultModel) {
        criteriaImagesModalWindow.setTitle(new FIDLabelModel("label.images"));
        criteriaImagesModalWindow.setPageCreator((ModalWindow.PageCreator) () -> new CriteriaImageListPage(resultModel) {

            @Override
            protected void onClose(AjaxRequestTarget target) {
                criteriaImagesModalWindow.close(target);
            }
        });
        criteriaImagesModalWindow.setInitialWidth(600);
        criteriaImagesModalWindow.setInitialHeight(700);

        criteriaImagesModalWindow.setWindowClosedCallback((ModalWindow.WindowClosedCallback) target -> {
            CriteriaResult tempCriteriaResult = FieldIDSession.get().getPreviouslyStoredCriteriaResult();
            if (tempCriteriaResult != null) {
                resultModel.getObject().getCriteriaImages().clear();
                for (CriteriaResultImage image : tempCriteriaResult.getCriteriaImages()) {
                    image.setCriteriaResult(resultModel.getObject());
                    resultModel.getObject().getCriteriaImages().add(image);
                }
                FieldIDSession.get().setPreviouslyStoredCriteriaResult(null);
            }
            target.add(CriteriaSectionEditPanel.this);
        });
        criteriaImagesModalWindow.show(ajaxRequestTarget);
    }

    private void showActionsModal(AjaxRequestTarget target, IModel<CriteriaResult> resultModel) {
        actionsWindow.setTitle(new FIDLabelModel("label.actions"));

        @SuppressWarnings("unchecked") //Sssshhhhh... it's okay!
        Class<? extends Event> actionEventClass = eventClass.equals(SubEvent.class) ? ThingEvent.class : (Class<? extends Event>) eventClass;
        actionsWindow.setContent(new ActionsPanel(actionsWindow.getContentId(), resultModel, actionEventClass, null, false, false));
        actionsWindow.setCloseButtonCallback(createActionsCloseButtonCallback());
        actionsWindow.show(target);
    }

    private ModalWindow.CloseButtonCallback createActionsCloseButtonCallback() {
        return (ModalWindow.CloseButtonCallback) target -> {
            target.add(CriteriaSectionEditPanel.this);
            return true;
        };
    }
}
