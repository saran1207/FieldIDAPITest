package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.asset.AutoAttributeService;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.draggable.DraggableAjaxBehavior;
import org.odlabs.wiquery.ui.draggable.DraggableContainment;
import org.odlabs.wiquery.ui.draggable.DraggableRevert;
import org.odlabs.wiquery.ui.droppable.DroppableAccept;
import org.odlabs.wiquery.ui.droppable.DroppableAjaxBehavior;
import rfid.ejb.entity.InfoFieldBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agrabovskis on 2018-11-27.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
abstract public class AutoAttributeEditPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeEditPanel.class);

    @SpringBean
    private AssetTypeService assetTypeService;

    @SpringBean
    private AutoAttributeService autoAttributeService;

    private IModel<User> currentUserModel;
    private IModel<Long> assetTypeSelectedForEditModel;
    private IModel<Long> autoAttributeCriteriaProvidedIdModel;
    private IModel<AssetType> assetTypeModel;
    private IModel<AutoAttributeCriteria> autoAttributeCriteriaModel;
    private IModel<List<InfoFieldBean>> availableFieldsModel;
    private IModel<List<InfoFieldBean>> inputFieldsModel;
    private IModel<List<InfoFieldBean>> outputFieldsModel;

    public AutoAttributeEditPanel(
            String id,
            IModel<User> currentUserModel,
            IModel<Long> assetTypeSelectedForEditModel,
            IModel<Long> autoAttributeCriteriaProvidedIdModel) {
        super(id);
        this.currentUserModel = currentUserModel;
        this.assetTypeSelectedForEditModel = assetTypeSelectedForEditModel;
        this.autoAttributeCriteriaProvidedIdModel = autoAttributeCriteriaProvidedIdModel;
        createDataModels();
        addComponents();
    }

    public void handleSelectionChange() {
        assetTypeModel.detach();
        autoAttributeCriteriaModel.detach();
        availableFieldsModel.detach();
    }

    private void addComponents() {

        Form form = new Form("attributeEditForm");
        add(form);

        form.add(new Label("assetTypeName", new PropertyModel(assetTypeModel, "name")));

        FieldListPanel availableFieldsList = new FieldListPanel(
                "availableFieldsList",
                new FIDLabelModel("label.availablefields").getObject(),
                false,
                availableFieldsModel);
        availableFieldsList.setOutputMarkupId(true);
        availableFieldsList.setOutputMarkupPlaceholderTag(true);
        form.add(availableFieldsList);

        FieldListPanel inputFieldsList = new FieldListPanel(
                "inputFieldsList",
                new FIDLabelModel("label.inputfields").getObject(),
                true,
                inputFieldsModel);
        inputFieldsList.setOutputMarkupId(true);
        inputFieldsList.setOutputMarkupPlaceholderTag(true);
        form.add(inputFieldsList);

        FieldListPanel outputFieldsList = new FieldListPanel(
                "outputFieldsList",
                new FIDLabelModel("label.outputfields").getObject(),
                false,
                outputFieldsModel);
        outputFieldsList.setOutputMarkupId(true);
        outputFieldsList.setOutputMarkupPlaceholderTag(true);
        form.add(outputFieldsList);

        AjaxButton saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println("save button clicked");
                doSave(target);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        };
        saveButton.add(new ConfirmBehavior(new Model(getString("message.save_prompt"))));

        form.add(saveButton);

        WebMarkupContainer deleteButtonSection = new WebMarkupContainer("deleteButtonSection") {
            @Override
            public boolean isVisible() {
                return autoAttributeCriteriaModel.getObject() != null &&
                        autoAttributeCriteriaModel.getObject().getId() != null;
            }
        };
        form.add(deleteButtonSection);

        AjaxButton deleteButton = new AjaxButton("delete") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println("delete button clicked");
                doDelete(target);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };
        deleteButton.add(new ConfirmBehavior(new Model(getString("message.delete_prompt"))));
        deleteButtonSection.add(deleteButton);

        AjaxSubmitLink cancelLink = new AjaxSubmitLink("cancel") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                cancelActionCompleted(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        };
        form.add(cancelLink);

        AjaxSubmitLink switchToDefinitionListLink = new AjaxSubmitLink("switchToDefinitionList") {
            @Override
            public boolean isVisible() {
                return autoAttributeCriteriaModel.getObject() != null &&
                        autoAttributeCriteriaModel.getObject().getId() != null;
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                switchToDefinitionList(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        };
        form.add(switchToDefinitionListLink);
    }

    private void createDataModels() {

        assetTypeModel = new LoadableDetachableModel<AssetType>() {
            @Override
            protected AssetType load() {
                AssetType assetType = assetTypeService.getAssetTypeWithPostFetches(assetTypeSelectedForEditModel.getObject());
                return assetType;
            }
        };

        autoAttributeCriteriaModel = new LoadableDetachableModel<AutoAttributeCriteria>() {
            @Override
            protected AutoAttributeCriteria load() {
                Long criteriaId = autoAttributeCriteriaProvidedIdModel.getObject();
                if (criteriaId == null)
                    return new AutoAttributeCriteria();
                else
                    return autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                            autoAttributeCriteriaProvidedIdModel.getObject());
            }
        };

        availableFieldsModel = new LoadableDetachableModel<List<InfoFieldBean>>() {
            @Override
            protected List<InfoFieldBean> load() {
                List<InfoFieldBean> available = new ArrayList(assetTypeModel.getObject().getAvailableInfoFields());
                // remove the ones that are used in inputs or outputs.
                available.removeAll(autoAttributeCriteriaModel.getObject().getInputs());
                available.removeAll(autoAttributeCriteriaModel.getObject().getOutputs());
                return available;
            }
        };

        inputFieldsModel = new LoadableDetachableModel<List<InfoFieldBean>>() {
            @Override
            protected List<InfoFieldBean> load() {
                List<InfoFieldBean> inputs = autoAttributeCriteriaModel.getObject().getInputs();
                return inputs;
            }
        };

        outputFieldsModel = new LoadableDetachableModel<List<InfoFieldBean>>() {
            @Override
            protected List<InfoFieldBean> load() {
                return autoAttributeCriteriaModel.getObject().getOutputs();
            }
        };
    }

    private void doSave(AjaxRequestTarget target) {

        List<InfoFieldBean> inputFields = inputFieldsModel.getObject();
        List<InfoFieldBean> outputFields = outputFieldsModel.getObject();

        AutoAttributeCriteria autoAttributeCriteria = autoAttributeCriteriaModel.getObject();

        autoAttributeCriteria.setInputs(inputFields);
        autoAttributeCriteria.setOutputs(outputFields);
        autoAttributeCriteria.setTenant(currentUserModel.getObject().getTenant());
        autoAttributeCriteria.setAssetType(assetTypeModel.getObject());
        autoAttributeCriteria.setDefinitions(new ArrayList<AutoAttributeDefinition>());

        if (autoAttributeCriteria.getId() != null) {
            autoAttributeCriteria = autoAttributeService.update(autoAttributeCriteria);
        } else {
            autoAttributeCriteria = autoAttributeService.save(autoAttributeCriteria, currentUserModel.getObject());
        }
        Session.get().info(getString("message.save_success"));
        target.addChildren(getPage(), FeedbackPanel.class);
        saveActionCompleted(target, autoAttributeCriteria);
    }

    private void doDelete(AjaxRequestTarget target) {

        autoAttributeService.delete(autoAttributeCriteriaModel.getObject());
        Session.get().info(getString("message.delete_success"));
        target.addChildren(getPage(), FeedbackPanel.class);
        deleteActionCompleted(target);
    }

    abstract void saveActionCompleted(AjaxRequestTarget target, AutoAttributeCriteria createdCriteria);
    abstract void deleteActionCompleted(AjaxRequestTarget target);
    abstract void cancelActionCompleted(AjaxRequestTarget target);
    abstract void switchToDefinitionList(AjaxRequestTarget target);


    private class FieldListPanel extends Panel {

        private IModel<String> STATIC_OPTION_CSS_CLASS = Model.of("staticOption");
        private IModel<String> DYNAMIC_OPTION_CSS_CLASS = Model.of("dynamicOption");
        private IModel<List<InfoFieldBean>> infoFieldBeanModel;

        /**
         *
         * @param id
         * @param listTitle
         * @param acceptOnlyStaticOptions true if this panel only accepts drop of static fields.
         * @param infoFieldBeanModel
         */
        public FieldListPanel(String id, String listTitle, boolean acceptOnlyStaticOptions, IModel<List<InfoFieldBean>> infoFieldBeanModel) {
            super(id);
            this.infoFieldBeanModel = infoFieldBeanModel;
            addComponents(listTitle, acceptOnlyStaticOptions);
        }

        private void addComponents(String listTitle, boolean acceptOnlyStaticOptions) {
            WebMarkupContainer dropBox = new WebMarkupContainer("dropBox");
            add(dropBox);
            Label listTitleLabel = new Label("listTitle", listTitle);
            dropBox.add(listTitleLabel);
            final DataView<InfoFieldBean> infoFieldsList =
                    new DataView<InfoFieldBean>("infoFieldsList", new ListDataProvider<InfoFieldBean>() {
                        @Override
                        protected List<InfoFieldBean> getData() {
                            return infoFieldBeanModel.getObject();
                        }
                    }) {
                        @Override
                        protected void populateItem(Item<InfoFieldBean> item) {
                            System.out.println("populateItem " + item.getModelObject().getName());
                            WebMarkupContainer container = new WebMarkupContainer("infoFieldContainer");
                            container.add(new AttributeAppender("class",DYNAMIC_OPTION_CSS_CLASS, " "));
                            container.setOutputMarkupId(true);
                            container.setOutputMarkupPlaceholderTag(true);
                            container.setDefaultModel(
                                    new Model(
                                            new FieldSourceLocation(
                                                    item.getModelObject(),
                                                    FieldListPanel.this,
                                                    infoFieldBeanModel))); // Make model available for drop target
                            item.add(container);
                            if (item.getModelObject().hasStaticInfoOption())
                                container.add(new AttributeAppender("class", STATIC_OPTION_CSS_CLASS, " "));
                            container.add(new Label("infoFieldName", item.getModelObject().getName()));
                            String displayType;
                            InfoFieldBean.InfoFieldType type = item.getModelObject().getType();
                            if (type.equals(InfoFieldBean.InfoFieldType.TextField))
                                displayType = "T";
                            else
                            if (type.equals(InfoFieldBean.InfoFieldType.ComboBox)) {
                                displayType = "C";
                            }
                            else
                            if (type.equals(InfoFieldBean.InfoFieldType.SelectBox)) {
                                displayType = "S";
                            }
                            else
                            if (type.equals(InfoFieldBean.InfoFieldType.DateField))
                                displayType = "D";
                            else
                                displayType = "";
                            container.add(new Label("infoFieldType", displayType));

                            DraggableAjaxBehavior draggableAjaxBehavior = new DraggableAjaxBehavior() {
                                @Override
                                public void onDrag(Component draggedComponent, AjaxRequestTarget ajaxRequestTarget) {
                                }

                                @Override
                                public void onStart(Component draggedComponent, AjaxRequestTarget ajaxRequestTarget) {
                                }

                                @Override
                                public void onStop(Component draggedComponent, AjaxRequestTarget ajaxRequestTarget) {
                                    // Redraw dropped component
                                    ajaxRequestTarget.add(((FieldSourceLocation) draggedComponent.getDefaultModelObject()).getFieldListPanel());
                                }
                            };
                            draggableAjaxBehavior.setContainment(new DraggableContainment(DraggableContainment.ContainmentEnum.WINDOW));
                            draggableAjaxBehavior.setRevert(new DraggableRevert(DraggableRevert.RevertEnum.INVALID));
                            container.add(draggableAjaxBehavior);
                        }
                    };
            dropBox.add(infoFieldsList);


           DroppableAjaxBehavior droppableAjaxBehavior = new DroppableAjaxBehavior() {
                @Override
                public void onDrop(Component droppedComponent, AjaxRequestTarget ajaxRequestTarget) {
                    /* Remove dropped component from source model and add it to target model. */
                    InfoFieldBean infoFieldBean = ((FieldSourceLocation) droppedComponent.getDefaultModelObject()).getInfoFieldBean();
                    infoFieldBeanModel.getObject().add(infoFieldBean);
                    ajaxRequestTarget.add(droppedComponent);
                    ajaxRequestTarget.add(FieldListPanel.this);

                    FieldSourceLocation sourceLocation = (FieldSourceLocation) droppedComponent.getDefaultModelObject();
                    sourceLocation.getInfoFieldBeanModel().getObject().remove(infoFieldBean);
                }
            };

            droppableAjaxBehavior.setAccept(
                    new DroppableAccept(acceptOnlyStaticOptions ?
                            ("." + STATIC_OPTION_CSS_CLASS.getObject()) :
                            ("." + DYNAMIC_OPTION_CSS_CLASS.getObject())));
            dropBox.add(droppableAjaxBehavior);
        }

    }

    private class FieldSourceLocation implements Serializable {

        private InfoFieldBean infoFieldBean;
        private FieldListPanel fieldListPanel;
        private IModel<List<InfoFieldBean>> infoFieldBeanModel;

        private FieldSourceLocation(InfoFieldBean infoFieldBean, FieldListPanel fieldListPanel, IModel<List<InfoFieldBean>> infoFieldBeanModel) {
            this.infoFieldBean = infoFieldBean;
            this.fieldListPanel = fieldListPanel;
            this.infoFieldBeanModel = infoFieldBeanModel;
        }

        private InfoFieldBean getInfoFieldBean() {
            return infoFieldBean;
        }

        private FieldListPanel getFieldListPanel() {
            return fieldListPanel;
        }

        private IModel<List<InfoFieldBean>> getInfoFieldBeanModel() {
            return infoFieldBeanModel;
        }
    }
}
