package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.service.asset.AutoAttributeService;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.List;

/**
 * Created by agrabovskis on 2018-11-12.
 */
abstract public class AutoAttributeDefinitionsListPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeDefinitionsListPanel.class);

    @SpringBean
    private AutoAttributeService autoAttributeService;

    private IModel<Long> autoAttributeCriteriaProvidedIdModel;
    private IModel<Long> autoAttributeDefinitionIdModel;
    private IModel<AutoAttributeCriteria> autoAttributeCriteriaModel;
    private IModel<List<AutoAttributeDefinition>> autoAttributeDefinitionModel;
    private WebMarkupContainer resultsPanel;
    private WebMarkupContainer noResultsPanel;

    public AutoAttributeDefinitionsListPanel(
            String id,
            IModel<Long> autoAttributeCriteriaProvidedIdModel,
            IModel<Long> autoAttributeDefinitionIdModel) {
        super(id);
        this.autoAttributeCriteriaProvidedIdModel = autoAttributeCriteriaProvidedIdModel;
        this.autoAttributeDefinitionIdModel = autoAttributeDefinitionIdModel;
        createDataModels();
        addComponents();
    }

    @Override
    protected void onBeforeRender() {
        System.out.println("AutoAttributeDefinitionsListPanel.onBeforeRender");
        //autoAttributeDefinitionModel.detach(); // force reload in case criteria has changed
        super.onBeforeRender();
    }

    public void handleSelectionChange() {
        Session.get().cleanupFeedbackMessages();
        autoAttributeCriteriaModel.detach();
        autoAttributeDefinitionModel.detach();
    }

    private void addComponents() {

        /* Create the result section, hidden if no results were found */
        resultsPanel = new WebMarkupContainer("resultList") {
            @Override
            public boolean isVisible() {
                return showResultSection();
            }
        };
        resultsPanel.setOutputMarkupId(true);
        resultsPanel.setOutputMarkupPlaceholderTag(true);

        final DataView<InfoFieldBean> criteriaInputsList =
                new DataView<InfoFieldBean>("criteriaList", new ListDataProvider<InfoFieldBean>() {
                    @Override
                    protected List<InfoFieldBean> getData() {
                        return autoAttributeCriteriaModel.getObject().getInputs();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<InfoFieldBean> item) {
                        item.add(new Label("inputName", item.getModelObject().getName()));
                    }
                };
        resultsPanel.add(criteriaInputsList);

        final DataView<AutoAttributeDefinition> definitionList =
                new DataView<AutoAttributeDefinition>("definitionList", new ListDataProvider<AutoAttributeDefinition>() {
                    @Override
                    protected List<AutoAttributeDefinition> getData() {
                        return autoAttributeDefinitionModel.getObject();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<AutoAttributeDefinition> item) {
                        final AutoAttributeDefinition definition = item.getModelObject();
                        final DataView<InfoOptionBean> definitionInputList =
                                new DataView<InfoOptionBean>("definitionInputList", new ListDataProvider<InfoOptionBean>() {
                                    @Override
                                    protected List<InfoOptionBean> getData() {
                                        return definition.getInputs();
                                    }
                                }) {
                                    @Override
                                    protected void populateItem(Item<InfoOptionBean> item) {
                                        item.add(new Label("inputName", item.getModelObject().getName()));
                                    }
                                };
                        item.add(definitionInputList);
                        AjaxLink autoAttributeDefinitionEditLink = new AjaxLink("autoAttributeDefinitionEdit") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                // templates/html/autoAttributeDefinition/form.ftl
                                System.out.println("Edit definition link clicked for " + definition.getId());
                                autoAttributeDefinitionIdModel.setObject(definition.getId());
                                editActionInvoked(target);
                            }
                        };
                        item.add(autoAttributeDefinitionEditLink);

                        AjaxLink autoAttributeDefinitionRemoveLink = new AjaxLink("autoAttributeDefinitionRemove") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                              System.out.println("Remove definition link clicked");
                                try {
                                    //autoAttributeManager.removeDefinition( autoAttributeDefinition );
                                    autoAttributeService.removeDefinition(definition);
                                    logger.info("AutoAttributesDefinition ");
                                    Session.get().info("message.definition_removed");
                                    autoAttributeDefinitionModel.detach();
                                    target.add(resultsPanel);
                                    target.add(noResultsPanel);
                                } catch (Exception e) {
                                    logger.error("Removal of AutoAttributesDefinition failed", e);
                                    Session.get().error("error.remove_definition_failed");
                                }
                            }
                        };
                        item.add(autoAttributeDefinitionRemoveLink);
                    }
                };


        resultsPanel.add(definitionList);

        /* Create the section to display a message if no result */
        noResultsPanel = new WebMarkupContainer("emptyListResult"){
            @Override
            public boolean isVisible() {
                return !showResultSection();
            }
        };
        noResultsPanel.setOutputMarkupId(true);
        noResultsPanel.setOutputMarkupPlaceholderTag(true);

        add(resultsPanel);
        add(noResultsPanel);
    }

    protected boolean showResultSection() {
        return autoAttributeCriteriaModel.getObject().getInputs().size() > 0 &&
                autoAttributeDefinitionModel.getObject().size() > 0;
    }

    private void createDataModels() {

        autoAttributeCriteriaModel = new LoadableDetachableModel<AutoAttributeCriteria>() {

            protected AutoAttributeCriteria load() {
                System.out.println("Loading autoAttributeCriteria");
                AutoAttributeCriteria criteria = autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
                System.out.println("AUtoAttributeCriteria loaded with " + criteria.getInputs().size() + " inputs and " + criteria.getOutputs().size() + " outputs");
                return criteria;
            }
        };

        autoAttributeDefinitionModel = new LoadableDetachableModel<List<AutoAttributeDefinition>>() {
            @Override
            protected List<AutoAttributeDefinition> load() {
                System.out.println("Loading autoAttributeDefinition");
                return autoAttributeService.getAutoAttributeDefinitionsWithPostFetches(
                        autoAttributeCriteriaModel.getObject().getId());
            }
        };
    }

    abstract protected void editActionInvoked(AjaxRequestTarget target);
}
