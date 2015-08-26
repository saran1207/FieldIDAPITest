package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.action.ActionsPanel;
import com.n4systems.fieldid.wicket.components.event.criteria.factory.CriteriaResultFactory;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.pages.event.criteriaimage.CriteriaImageViewListPage;
import com.n4systems.model.*;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class CriteriaSectionViewPanel extends Panel {

    private DialogModalWindow criteriaImagesModalWindow;
    private DialogModalWindow actionsWindow;

    public CriteriaSectionViewPanel(String id, IModel<List<CriteriaResult>> results, Class<? extends AbstractEvent> eventClass) {
        super(id);

        add(new AttributeAppender("class", "display-horizontal"));
        add(criteriaImagesModalWindow = new DialogModalWindow("imagesModal"));
        add(actionsWindow = new DialogModalWindow("actionsModal"));
        actionsWindow.setInitialWidth(350);
        actionsWindow.setInitialHeight(600);

        add(new ListView<CriteriaResult>("criteria", results) {

            @Override
            protected void populateItem(final ListItem<CriteriaResult> item) {
                final IModel<CriteriaResult> criteriaResultModel = item.getModel();

                final WebMarkupContainer deficienciesDialog;
                final WebMarkupContainer recommendationsDialog;

                item.add(new Label("criteriaName", new PropertyModel<String>(criteriaResultModel, "criteria.displayText")));
                item.add(CriteriaResultFactory.createResultPanelFor("criteriaResult", criteriaResultModel));

                item.add(deficienciesDialog = new WebMarkupContainer("deficienciesDialog"));
                item.add(recommendationsDialog = new WebMarkupContainer("recommendationsDialog"));

                deficienciesDialog.setOutputMarkupId(true);
                recommendationsDialog.setOutputMarkupId(true);

                deficienciesDialog.add(new ListView<Deficiency>("deficiency", new PropertyModel<List<Deficiency>>(criteriaResultModel, "deficiencies")) {

                    @Override
                    protected void populateItem(ListItem<Deficiency> item) {
                        item.add(new Label("text", new PropertyModel<String>(item.getModel(), "text")));
                        if(item.getModelObject().getState() == Observation.State.OUTSTANDING) {
                            item.add(new AttributeAppender("class", "def-selected"));
                        } else if(item.getModelObject().getState() == Observation.State.REPAIREDONSITE) {
                            item.add(new AttributeAppender("class", "def-repaired-on-site"));
                        } else if(item.getModelObject().getState() == Observation.State.REPAIRED) {
                            item.add(new AttributeAppender("class", "def-repaired"));
                        }
                    }
                });

                recommendationsDialog.add(new ListView<Recommendation>("recommendation", new PropertyModel<List<Recommendation>>(criteriaResultModel, "recommendations")) {

                    @Override
                    protected void populateItem(ListItem<Recommendation> item) {
                        item.add(new Label("text", new PropertyModel<String>(item.getModel(), "text")));
                        if(item.getModelObject().getState() == Observation.State.OUTSTANDING) {
                            item.add(new AttributeAppender("class", "rec-selected"));
                        }
                    }
                });

                item.add(new AjaxLink<Void>("deficiencies") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        target.appendJavaScript("$('#"+ deficienciesDialog.getMarkupId()+"').dialog('option',  'position', { my:'top', at:'left', of: $('#"+getMarkupId()+"')});");
                        target.appendJavaScript("$('#"+ deficienciesDialog.getMarkupId()+"').dialog('open');");
                    }

                    @Override
                    public boolean isVisible() {
                        return !item.getModelObject().getDeficiencies().isEmpty();
                    }
                });

                item.add(new AjaxLink<Void>("recommendations") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        target.appendJavaScript("$('#"+ recommendationsDialog.getMarkupId()+"').dialog('option',  'position', { my:'top', at:'left', of: $('#"+getMarkupId()+"')});");
                        target.appendJavaScript("$('#"+ recommendationsDialog.getMarkupId()+"').dialog('open');");
                    }

                    @Override
                    public boolean isVisible() {
                        return !item.getModelObject().getRecommendations().isEmpty();
                    }
                });

                item.add(new AjaxLink<Void>("criteriaImages") {
                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        criteriaImagesModalWindow.setPageCreator(new ModalWindow.PageCreator() {
                            @Override
                            public Page createPage() {
                                return new CriteriaImageViewListPage(criteriaResultModel);
                            }
                        });
                        criteriaImagesModalWindow.setInitialWidth(600);
                        criteriaImagesModalWindow.setInitialHeight(700);
                        criteriaImagesModalWindow.show(target);
                    }

                    @Override
                    public boolean isVisible() {
                        return !item.getModelObject().getCriteriaImages().isEmpty();
                    }
                });

                item.add(new AjaxLink<Void>("actions") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        actionsWindow.setTitle(new Model<String>("Actions"));
                        Class<? extends Event> actionEventClass = eventClass.equals(SubEvent.class) ? ThingEvent.class : (Class<? extends Event>) eventClass;
                        actionsWindow.setContent(new ActionsPanel(actionsWindow.getContentId(), item.getModel(), actionEventClass, null, true, false));
                        actionsWindow.show(target);
                    }

                    @Override
                    public boolean isVisible() {
                        return !item.getModelObject().getActions().isEmpty();
                    }
                });
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");
        response.renderCSSReference("style/plugins/jquery-ui-dialog/jquery-ui.custom.css");
        response.renderOnDomReadyJavaScript("$('.observation-dialog').dialog({ autoOpen: false });");
        response.renderCSSReference("style/legacy/modal/fid_modal.css");

    }
}
