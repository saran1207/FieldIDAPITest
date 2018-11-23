package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.service.asset.AutoAttributeService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.widgets.attributes.*;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.web.helper.SessionUser;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by agrabovskis on 2018-11-12.
 */
abstract public class AutoAttributeDefinitionAddPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeDefinitionAddPanel.class);

    @SpringBean
    private AutoAttributeService autoAttributeService;

    private IModel<Long> autoAttributeCriteriaProvidedIdModel;
    private IModel<Long> autoAttributeDefinitionProvidedIdModel;

    private IModel<AutoAttributeCriteria> autoAttributeCriteriaModel;
    private IModel<AutoAttributeDefinition> autoAttributeDefinitionModel;

    private LoadableDetachableModel<List<InfoFieldBean>> inputInfoFieldBeansModel;
    private LoadableDetachableModel<List<InfoOptionInput>> inputInfoOptionsModel;

    private LoadableDetachableModel<List<InfoFieldBean>> outputInfoFieldBeanModel;
    private LoadableDetachableModel<List<InfoOptionInput>> outputInfoOptionsModel;
    private IModel<SessionUser> sessionUserModel;

    public AutoAttributeDefinitionAddPanel(String id,
                                           IModel<Long> autoAttributeCriteriaProvidedIdModel,
                                           IModel<Long> autoAttributeDefinitionProvidedIdModel,
                                           IModel<SessionUser> sessionUserModel) {
        super(id);
        this.autoAttributeCriteriaProvidedIdModel = autoAttributeCriteriaProvidedIdModel;
        this.autoAttributeDefinitionProvidedIdModel = autoAttributeDefinitionProvidedIdModel;
        this.sessionUserModel = sessionUserModel;
        createDataModels();
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/site_wide.css");
    }

    public void handleSelectionChange() {
        System.out.println("AutoAttributeDefinitionAddPanel.handleSelectionChanged");
        autoAttributeDefinitionProvidedIdModel.setObject(null);
        autoAttributeCriteriaModel.detach();
        autoAttributeDefinitionModel.detach();
        inputInfoFieldBeansModel.detach();
        inputInfoOptionsModel.detach();
        outputInfoFieldBeanModel.detach();
        outputInfoOptionsModel.detach();
    }

    private void addComponents() {

        final Form addDefinitionForm = new Form("addDefinitionForm");
        add(addDefinitionForm);

        final DataView<InfoFieldBean> inputFieldsList =
                new DataView<InfoFieldBean>("inputFieldsList", new ListDataProvider<InfoFieldBean>() {
                    @Override
                    protected List<InfoFieldBean> getData() {
                        return inputInfoFieldBeansModel.getObject();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<InfoFieldBean> item) {
                        item.add(getWidgetForAttribute("inputAttribute", false, item.getModelObject(), findMatchingInput(item.getModelObject(), inputInfoOptionsModel), inputInfoOptionsModel));
                    }
                };
        addDefinitionForm.add(inputFieldsList);

        final DataView<InfoFieldBean> outputFieldsList =
                new DataView<InfoFieldBean>("outputFieldsList", new ListDataProvider<InfoFieldBean>() {
                    @Override
                    protected List<InfoFieldBean> getData() {
                        System.out.println("AutoAttributeDefinitionAddPanel, outputFieldsList, getData");
                        return outputInfoFieldBeanModel.getObject();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<InfoFieldBean> item) {
                        System.out.println("AutoAttributeDefinitionAddPanel, outputFieldsList, populateItem");
                        item.add(getWidgetForAttribute("outputAttribute", true, item.getModelObject(), findMatchingInput(item.getModelObject(), outputInfoOptionsModel), outputInfoOptionsModel));
                    }
                };
        addDefinitionForm.add(outputFieldsList);

        AjaxButton saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println("save button clicked");
                if (saveChanges(false))
                    addActionCompleted(target);
                else
                    target.addChildren(getPage(), FeedbackPanel.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }

        };
        addDefinitionForm.add(saveButton);

        Button saveAndAddButton = new Button("saveAndAdd") {
            @Override
            public void onSubmit() {
                System.out.println("save and add button clicked");
                saveChanges(true);
            }

            @Override
            public boolean isVisible() {
                return autoAttributeDefinitionModel.getObject().getId() == null;
            }
        };
        saveAndAddButton.setDefaultFormProcessing(false);
        addDefinitionForm.add(saveAndAddButton);

        AjaxSubmitLink cancelLink = new AjaxSubmitLink("cancel") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println("Cancel on add page clicked");
                cancelActionCompleted(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        };
        addDefinitionForm.add(cancelLink);
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

        autoAttributeDefinitionModel = new LoadableDetachableModel<AutoAttributeDefinition>() {
            @Override
            protected AutoAttributeDefinition load() {
                System.out.println("Loading AutoAttributeDefinitionAddPanel.autoAttributeDefinition");
                Long definitionId = autoAttributeDefinitionProvidedIdModel.getObject();
                if (definitionId != null)
                    return autoAttributeService.getAutoAttributeDefinitionWithPostFetches(definitionId);
                else {
                    System.out.println("... definition id is null");
                    return new AutoAttributeDefinition();
                }
            }
        };

        inputInfoFieldBeansModel = new LoadableDetachableModel<List<InfoFieldBean>>() {
            @Override
            protected List<InfoFieldBean> load() {
                List<InfoFieldBean> criteriaInputs = autoAttributeCriteriaModel.getObject().getInputs();
                return criteriaInputs;
            }
        };

        inputInfoOptionsModel = new LoadableDetachableModel<List<InfoOptionInput>>() {
            @Override
            protected List<InfoOptionInput> load() {

                System.out.println("AutoAttributeDefinitionAddPanel.getInputInfoOptions, autoAttributeDefinition has " + autoAttributeDefinitionModel.getObject().getInputs().size() + " inputs");

                List<InfoFieldBean> criteriaInputs = autoAttributeCriteriaModel.getObject().getInputs();

                List<InfoOptionInput> inputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(
                        autoAttributeDefinitionModel.getObject().getInputs(),
                        criteriaInputs,
                        sessionUserModel.getObject());

                return inputInfoOptions;
            }
        };

        outputInfoFieldBeanModel = new LoadableDetachableModel<List<InfoFieldBean>>() {
            @Override
            protected List<InfoFieldBean> load() {
                System.out.println("AutoAttributeDefinitionAddPanel loading autoAttributeCriteria outputs");
                List<InfoFieldBean> criteriaOutputs = autoAttributeCriteriaModel.getObject().getOutputs();
                return criteriaOutputs;
            }
        };
        outputInfoOptionsModel = new LoadableDetachableModel<List<InfoOptionInput>>() {
            @Override
            protected List<InfoOptionInput> load() {
                List<InfoFieldBean> criteriaOutputs = autoAttributeCriteriaModel.getObject().getOutputs();

                List<InfoOptionInput> outputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(
                        autoAttributeDefinitionModel.getObject().getOutputs(),
                        criteriaOutputs,
                        sessionUserModel.getObject());

                for (InfoOptionInput info : outputInfoOptions) {
                    System.out.println("... loaded model: " + info.getUniqueIDString() + ":" + info.getName() + ":" + info.getUniqueIDString());
                }
                return outputInfoOptions;
            }
        };
    }

    private Component getWidgetForAttribute(String id, boolean appearsInOutputSection, InfoFieldBean infoFieldBean, InfoOptionInput infoOptionInput, IModel<List<InfoOptionInput>> optionsModel) {

        Component widget;

        if (infoFieldBean.isRetired()) {
            widget = new AttributeRetiredFieldWidget(id, infoFieldBean, infoOptionInput);
        } else if (infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.SelectBox)) {
            widget = new AttributeSelectFieldWidget(id, appearsInOutputSection, infoFieldBean, infoOptionInput, optionsModel);
        } else if (infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.ComboBox)) {
            if (appearsInOutputSection) {
                widget = new AttributeComboBoxFieldWidget(id, infoFieldBean, infoOptionInput, optionsModel);
            }
            else {
                /* In the 'input' section combo box is not supported, only select */
                widget = new AttributeSelectFieldWidget(id, appearsInOutputSection, infoFieldBean, infoOptionInput, optionsModel);
            }
        } else if (infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.TextField) && infoFieldBean.isUsingUnitOfMeasure()) {
            widget = new AttributeUomFieldPanel(id, infoFieldBean, infoOptionInput);
        } else if (infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.TextField)) {
            widget = new AttributeTextFieldWidget(id, infoFieldBean, infoOptionInput);
        } else if (infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.DateField)) {
            widget = new AttributeDateFieldWidget(id, infoFieldBean, infoOptionInput, sessionUserModel);
        } else {
            widget = new Label(id, infoFieldBean.getName());
        }
        return widget;
    }

    public boolean saveChanges(boolean withAdd) {

        /*System.out.println("AutoAttributeDefinitionCrud.doSave, autoAttributeDefinition " + autoAttributeDefinition);
        if( autoAttributeCriteria == null || autoAttributeDefinition == null ) {
            addFlashMessage( getText( "error.noautoattributecriteria" ) );
            return ERROR;
        }*/

       /* updateInfoOptions(inputInfoFieldBeansModel, inputInfoOptionsModel);
        updateInfoOptions(outputInfoFieldBeanModel, outputInfoOptionsModel);*/

        autoAttributeDefinitionModel.getObject().setCriteria(autoAttributeCriteriaModel.getObject());
        autoAttributeDefinitionModel.getObject().setTenant(sessionUserModel.getObject().getTenant());

        String error = validateOutputs(autoAttributeCriteriaModel.getObject(), outputInfoOptionsModel.getObject());
        if (error != null) {
            System.out.println("Validate outputs encountered '" + error + "'");
            Session.get().error(error);
            return false;
        }

        convertOutputsToInfoOptionsWithValidation(
                autoAttributeCriteriaModel.getObject(),
                outputInfoOptionsModel.getObject(),
                sessionUserModel.getObject());

        convertInputsToInfoOptions(
                autoAttributeCriteriaModel.getObject(),
                inputInfoOptionsModel.getObject(),
                sessionUserModel.getObject());

        System.out.println("Wicket - autoAttributeDefinition inputs " + autoAttributeDefinitionModel.getObject().getInputs().size());
        for (InfoOptionBean bean : autoAttributeDefinitionModel.getObject().getInputs()) {
            System.out.println("Wicket: definition input " + bean.getUniqueID() + ":" + bean.getName());
            InfoFieldBean infoBean = bean.getInfoField();
            if (infoBean != null)
                System.out.println("... its infoFieldBean is " + infoBean.getUniqueID()+ ":" + infoBean.getName());
            else
                System.out.println("... its infoFieldBean is null");
        }
        AutoAttributeDefinition existingDefinition = autoAttributeService.findTemplateToApply(autoAttributeCriteriaModel.getObject(), autoAttributeDefinitionModel.getObject().getInputs());
        System.out.println("Wicket existing definition " + existingDefinition);
        if (existingDefinition != null)
            System.out.println("... existing definition id is " + existingDefinition.getId() + ", " + autoAttributeDefinitionModel.getObject().getId());
        //if( existingDefinition != null && !existingDefinition.getId().equals( uniqueID ) ) {
        System.out.println("existing definition: " + (existingDefinition != null));
        if (autoAttributeDefinitionModel.getObject() != null)
            System.out.println("... editing definition " + autoAttributeDefinitionModel.getObject().getId());
        if (existingDefinition != null && autoAttributeDefinitionModel.getObject() != null &&
                !existingDefinition.getId().equals(autoAttributeDefinitionModel.getObject().getId())) {
            Session.get().error("These inputs have already been used on an auto attribute definition.");
            return false;
        }

        try {
            autoAttributeDefinitionModel.setObject(autoAttributeService.saveDefinition(autoAttributeDefinitionModel.getObject()));
        } catch (Exception e) {
            String errorMsg = new FIDLabelModel("error.definition_save_failed").getObject();
            logger.error(errorMsg);
            Session.get().error("error.definition_save_failed");
            return false;
        }

        /*uniqueID = autoAttributeDefinition.getId();*/
        Session.get().info(new FIDLabelModel("message.definition_saved").getObject());
        /*if( saveandadd != null ) {
            setSavedInputs();
            return "saveandadd";
        }
        return "saved";*/
        if (withAdd) {
           /* public String doEdit() {

                if( autoAttributeCriteria == null || autoAttributeDefinition == null ) {
                    addFlashMessage( getText( "error.noautoattributecriteria" ) );
                    return ERROR;
                }
                if( autoAttributeDefinition.getId() == null ) {
                    getSavedInputs();
                }

                return SUCCESS;
            }*/
            return true;
        }
        else
            return true;
    }



    // WEB-2583 NOTE : previously, no validation was done because the field types didn't require it.
    //  this was added to handle date fields.  if more and more field types are added this should be refactored
    //  into a separate class.
    private String validateOutputs(AutoAttributeCriteria autoAttributeCriteria, List<InfoOptionInput> outputInfoOptions) {
        SessionUser user = sessionUserModel.getObject();
        for (InfoOptionInput input : outputInfoOptions) {
            for (InfoFieldBean field : autoAttributeCriteria.getOutputs()) {
                if (field.getUniqueID().equals(input.getInfoFieldId()) && input != null) {
                    String error = validateField(field, user, input.getName());
                    if (error != null) {
                        return error;
                    }
                }
            }
        }
        return null;
    }

    private String validateField(InfoFieldBean field, SessionUser user, String name) {
        if (field.getFieldType().equals(InfoFieldBean.DATEFIELD_FIELD_TYPE)) {
            System.out.println("Validating field '" + name + "'");
            SessionUserDateConverter dateConverter = user.createUserDateConverter();
            Date date = dateConverter.convertDate(name, field.isIncludeTime());
            return date == null ? getString("error.invalidate_date_value") : null;
        }
        return null;
    }

    private void convertOutputsToInfoOptionsWithValidation(AutoAttributeCriteria autoAttributeCriteria, List<InfoOptionInput> outputInfoOptions, SessionUser sessionUser) {
        autoAttributeDefinitionModel.getObject().setOutputs(InfoOptionInput.convertInputInfoOptionsToInfoOptions(outputInfoOptions, autoAttributeCriteria.getOutputs(), sessionUser));
    }

    private void convertInputsToInfoOptions(AutoAttributeCriteria autoAttributeCriteria, List<InfoOptionInput> inputInfoOptions, SessionUser sessionUser) {
        System.out.println("AutoAttributeDefinitionAddPanel.convertInputsToInfoOptions, " + inputInfoOptions.size() + " inputInfoOptions, " + autoAttributeCriteria.getInputs() + " criteria inputs");
       /* for (InfoOptionInput input : inputInfoOptions) {
            System.out.println("AutoAttributeDefinitionAddPanel.convertInputsToInfoOptions, " + input.getUniqueID() + ":" + input.getInfoFieldId() + ":" + input.getUniqueIDString());
        }
        for (InfoFieldBean info : autoAttributeCriteria.getInputs()) {
            System.out.println("AutoAttributeDefinitionAddPanel.convertInputsToInfoOptions, " + info.getUniqueID() + ":" + info.getName());
        }*/
        autoAttributeDefinitionModel.getObject().setInputs(InfoOptionInput.convertInputInfoOptionsToInfoOptions(inputInfoOptions, autoAttributeCriteria.getInputs(), sessionUser));
      /*  for (InfoOptionBean info : autoAttributeDefinition.getInputs()) {
            System.out.println("AutoAttributeDefinitionAddPanel.convertInputsToInfoOptions, input InfoOptionBean " + info.getUniqueID() + ":" + info.getName() + ":" + info.getInfoField().getName());
        }*/
    }

    /**
     * Set each Input/Output InfoField with its corresponding infoFieldID value, based on this line
     * in the struts code:
     * <@s.hidden name="${prefix}InfoOptions[${stat.index}].infoFieldId"  value="${infoField.uniqueID}"/>
     **/
    private void updateInfoOptions(IModel<List<InfoFieldBean>> infoFieldBeans, IModel<List<InfoOptionInput>> infoOptionInputs) {
        Iterator<InfoFieldBean> inputBeanIterator = infoFieldBeans.getObject().iterator();
        Iterator<InfoOptionInput> inputInfoOptionIterator = infoOptionInputs.getObject().iterator();
        while (inputBeanIterator.hasNext() && inputInfoOptionIterator.hasNext()) {
            inputInfoOptionIterator.next().setInfoFieldId(inputBeanIterator.next().getUniqueID());
        }
    }

    private InfoOptionInput findMatchingInput(InfoFieldBean infoFieldBean, IModel<List<InfoOptionInput>> optionsModel) {
        for (InfoOptionInput infoOptionInput : optionsModel.getObject()) {
            if (infoFieldBean.getUniqueID().equals(infoOptionInput.getInfoFieldId())) {
                System.out.println("Matching input " + infoOptionInput);
                return infoOptionInput;
            }
        }
        return null;
    }


    abstract protected void addActionCompleted(AjaxRequestTarget target);
    abstract protected void cancelActionCompleted(AjaxRequestTarget target);
}
