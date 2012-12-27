package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.action.ActionsListPage;
import com.n4systems.fieldid.wicket.components.event.criteria.CriteriaActionButton;
import com.n4systems.fieldid.wicket.components.event.criteria.factory.CriteriaEditorFactory;
import com.n4systems.fieldid.wicket.components.event.observations.DeficienciesEditPanel;
import com.n4systems.fieldid.wicket.components.event.observations.RecommendationsEditPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.components.popup.Popup;
import com.n4systems.fieldid.wicket.components.richText.RichText;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.event.criteriaimage.CriteriaImageListPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.Observation;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class CriteriaSectionEditPanel extends Panel {

    private static final String INSTRUCTIONS_JS_FORMAT = "richTextFactory.update('%s','%s');" +
            "$('#%s').appendTo('#%s');" +
            "$('#%s').css('visibility','visible').show();";

    private IModel<List<CriteriaResult>> results;
    private DialogModalWindow criteriaImagesModalWindow;
    private FIDModalWindow criteriaModalWindow;
    private DialogModalWindow actionsWindow;
    private TextArea instructionsContent;
    private WebMarkupContainer instructionsViewer;
    private RichText instructionsDialog;
    private Popup popup;

    public CriteriaSectionEditPanel(String id, IModel<List<CriteriaResult>> results) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new CriteriaEditForm("criteriaEditForm", results));

        add(criteriaImagesModalWindow = new DialogModalWindow("imagesWindow"));
        criteriaImagesModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                target.add(CriteriaSectionEditPanel.this);
                return true;
            }
        });

        add(criteriaModalWindow = new FIDModalWindow("observationWindow"));
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

        add(instructionsContent = new TextArea("instructionsContent", Model.of("")));
        instructionsContent.setOutputMarkupId(true);

        add(popup = new Popup("instructionsDialog") {
            @Override protected WebMarkupContainer createContent(String id) {
                return instructionsDialog = new RichText(id, Model.of("")).withButtonList(new ArrayList<String>()).withWidth("310px").disabled();
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/nicEdit/nicEdit-min.js");
    }

    class CriteriaEditForm extends Form<List<CriteriaResult>> {

        public CriteriaEditForm(String id, IModel<List<CriteriaResult>> results) {
            super(id, results);

            add(new ListView<CriteriaResult>("criteria", results) {
                @Override
                protected void populateItem(final ListItem<CriteriaResult> item) {
                    final CriteriaResult criteriaResult = item.getModel().getObject();
                    final Component image = new ContextImage("tooltip", "images/tooltip-icon.png").setOutputMarkupId(true).add(new AttributeAppender("class", Model.of("invisible"), " "));
                    image.setVisible(criteriaResult.getCriteria().getInstructions()!=null);
                    image.add(new AjaxEventBehavior("onclick") {
                        @Override protected void onEvent(AjaxRequestTarget target) {
                            instructionsContent.setDefaultModel(Model.of(criteriaResult.getCriteria().getInstructions()));
                            target.add(instructionsContent);
                            String javascript = String.format(INSTRUCTIONS_JS_FORMAT, instructionsDialog.getTextAreaMarkupId(), instructionsContent.getMarkupId(), popup.getMarkupId(), item.getMarkupId(), popup.getMarkupId() );
                            target.appendJavaScript(javascript);
                            target.appendJavaScript("putSpinnersOverImages();");
                        }
                        });
                    item.add(image);
                    item.add(new Label("criteriaName", new PropertyModel<String>(item.getModel(), "criteria.displayText")));
                    item.add(CriteriaEditorFactory.createEditorFor("criteriaEditor", item.getModel()));
                    final PropertyModel<List<? extends Observation>> recommendations = new PropertyModel<List<? extends Observation>>(item.getModel(), "recommendations");
                    item.add(new CriteriaActionButton("recommendationsButton", "images/rec-icon.png", criteriaResult.getRecommendations().size(), "label.recommendations", "mattButtonLeft") {
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
                    final PropertyModel<List<? extends Observation>> deficiencies = new PropertyModel<List<? extends Observation>>(item.getModel(), "deficiencies");
                    item.add(new CriteriaActionButton("deficienciesButton", "images/def-icon.png", criteriaResult.getDeficiencies().size(), "label.deficiencies", "mattButtonMiddle") {
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

                    item.add(new CriteriaActionButton("criteriaImageButton", "images/camera-icon.png", criteriaResult.getCriteriaImages().size(), "label.images", "mattButtonMiddle") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            criteriaImagesModalWindow.setTitle(new FIDLabelModel("label.images"));
                            criteriaImagesModalWindow.setPageCreator(new ModalWindow.PageCreator() {
                                @Override
                                public Page createPage() {
                                    return new CriteriaImageListPage(item.getModel()) {

                                        @Override
                                        protected void onClose(AjaxRequestTarget target) {
                                            criteriaImagesModalWindow.close(target);
                                        }
                                    };
                                }
                            });
                            criteriaImagesModalWindow.setInitialWidth(600);
                            criteriaImagesModalWindow.setInitialHeight(700);

                            criteriaImagesModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                                @Override
                                public void onClose(AjaxRequestTarget target) {
                                    CriteriaResult tempCriteriaResult = FieldIDSession.get().getPreviouslyStoredCriteriaResult();
                                    if (tempCriteriaResult != null) {
                                        item.getModelObject().getCriteriaImages().clear();
                                        for (CriteriaResultImage image : tempCriteriaResult.getCriteriaImages()) {
                                            image.setCriteriaResult(item.getModelObject());
                                            item.getModelObject().getCriteriaImages().add(image);
                                        }
                                        FieldIDSession.get().setPreviouslyStoredCriteriaResult(null);
                                    }
                                    target.add(CriteriaSectionEditPanel.this);
                                }
                            });
                            criteriaImagesModalWindow.show(target);
                        }

                    });

                    item.add(new CriteriaActionButton("actionsLink", "images/action-icon.png", criteriaResult.getActions().size(), "label.actions", "mattButtonRight") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            actionsWindow.setTitle(new Model<String>("Actions"));
                            FieldIDSession.get().setActionsForCriteria(item.getModelObject(), item.getModelObject().getActions());
                            actionsWindow.setPageCreator(new ModalWindow.PageCreator() {
                                @Override
                                public Page createPage() {
                                    return new ActionsListPage(item.getModel());
                                }

                                ;
                            });
                            actionsWindow.setCloseButtonCallback(createActionsCloseButtonCallback(item));
                            actionsWindow.show(target);
                        }
                    });

                    item.add(new CriteriaActionButton("showActionsButton", "images/icon-expand.png", null, "label.add_action", "mattButton") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            showActions(item);
                            target.add(item);
                            target.appendJavaScript("$('.tipsy').remove();");  // tipsy tooltips hang around after ajax. must manually clean them up.
                        }
                    });

                    showActionsIfAvailable(item);
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


                private ModalWindow.CloseButtonCallback createActionsCloseButtonCallback(final ListItem<CriteriaResult> item) {
                    return new ModalWindow.CloseButtonCallback() {
                        @Override
                        public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                            List<Event> actionsList = FieldIDSession.get().getActionsList(item.getModelObject());
                            item.getModelObject().setActions(actionsList);
                            target.add(CriteriaSectionEditPanel.this);
                            return true;
                        }
                    };
                }

            });
        }


    }
    
}
