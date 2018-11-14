package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.service.asset.AutoAttributeService;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.web.helper.SessionUser;

import java.util.List;

/**
 * Created by agrabovskis on 2018-11-12.
 */
public class AutoAttributeDefinitionAddPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeDefinitionAddPanel.class);

    @SpringBean
    private AutoAttributeService autoAttributeService;

    private IModel<Long> autoAttributeCriteriaProvidedIdModel;

    private LoadableDetachableModel<List<InfoFieldBean>> inputInfoFieldBeansModel;
    private LoadableDetachableModel<List<InfoOptionInput>> inputInfoOptionsModel;

    //private LoadableDetachableModel<List<InfoFieldOption>> inputFieldOptionsModel;
    private LoadableDetachableModel<List<InfoFieldBean>> outputInfoFieldBeanModel;
    private LoadableDetachableModel<List<InfoOptionInput>> outputInfoOptionsModel;
    private IModel<SessionUser> sessionUserModel;
    private AutoAttributeDefinition autoAttributeDefinition;

    public AutoAttributeDefinitionAddPanel(String id, IModel<Long> autoAttributeCriteriaProvidedIdModel,
                                           IModel<SessionUser> sessionUserModel) {
        super(id);
        this.autoAttributeCriteriaProvidedIdModel = autoAttributeCriteriaProvidedIdModel;
        this.sessionUserModel = sessionUserModel;
        autoAttributeDefinition = new AutoAttributeDefinition();
        createDataModels();
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/site_wide.css");
    }

    private void addComponents() {

        final DataView<InfoFieldBean> inputFieldsList =
                new DataView<InfoFieldBean>("inputFieldsList", new ListDataProvider<InfoFieldBean>() {
                    @Override
                    protected List<InfoFieldBean> getData() {
                        return inputInfoFieldBeansModel.getObject();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<InfoFieldBean> item) {
                        item.add(getWidgetForAttribute("inputAttribute", false, item.getModelObject(), inputInfoOptionsModel));
                    }
                };
        add(inputFieldsList);

        final DataView<InfoFieldBean> outputFieldsList =
                new DataView<InfoFieldBean>("outputFieldsList", new ListDataProvider<InfoFieldBean>() {
                    @Override
                    protected List<InfoFieldBean> getData() {
                        return outputInfoFieldBeanModel.getObject();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<InfoFieldBean> item) {
                        item.add(getWidgetForAttribute("outputAttribute", true, item.getModelObject(), outputInfoOptionsModel));
                    }
                };
        add(outputFieldsList);

    }

    private void createDataModels() {

        /*inputFieldOptionsModel = new LoadableDetachableModel<List<InfoFieldOption>>() {
            @Override
            protected List<InfoFieldOption> load() {
                AutoAttributeCriteria autoAttributeCriteria = autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());

          *//*      List<InfoFieldBean> criteriaInputs = autoAttributeCriteria.getInputs().stream().filter(
                        field -> !field.isRetired() && field.getName() != null).collect(Collectors.toList());*//*
                List<InfoFieldBean> criteriaInputs = autoAttributeCriteria.getInputs();

                List<InfoOptionInput> inputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(
                            autoAttributeDefinition.getInputs(),
                            criteriaInputs,
                            sessionUserModel.getObject() );

                Iterator<InfoFieldBean> criteriaIter = criteriaInputs.iterator();
                Iterator<InfoOptionInput> optionIter = inputInfoOptions.iterator();

                List<InfoFieldOption> options = new ArrayList<>();
                while (criteriaIter.hasNext() && optionIter.hasNext()) {
                    options.add(new InfoFieldOption(criteriaIter.next(), optionIter.next()));
                }
                return options;
            }
        };*/

        inputInfoFieldBeansModel = new LoadableDetachableModel<List<InfoFieldBean>>() {
            @Override
            protected List<InfoFieldBean> load() {
                AutoAttributeCriteria autoAttributeCriteria = autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
                List<InfoFieldBean> criteriaInputs = autoAttributeCriteria.getInputs();
                return criteriaInputs;
            }
        };

        inputInfoOptionsModel = new LoadableDetachableModel<List<InfoOptionInput>>() {
            @Override
            protected List<InfoOptionInput> load() {
                AutoAttributeCriteria autoAttributeCriteria = autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
                List<InfoFieldBean> criteriaInputs = autoAttributeCriteria.getInputs();

                List<InfoOptionInput> inputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(
                        autoAttributeDefinition.getInputs(),
                        criteriaInputs,
                        sessionUserModel.getObject() );

                return inputInfoOptions;
            }
        };
        /*outputInfoOptionsModel = new LoadableDetachableModel<List<InfoOptionInput>>() {
            @Override
            protected List<InfoOptionInput> load() {
                AutoAttributeCriteria autoAttributeCriteria = autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
                List<InfoOptionInput> outputInfoOptions = new ArrayList<InfoOptionInput>();
                if( autoAttributeCriteria.getOutputs() != null ) {
                    outputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(
                            autoAttributeDefinition.getOutputs(),
                            autoAttributeCriteria.getOutputs(),
                            sessionUserModel.getObject());
                }

                return outputInfoOptions;
            }
        };*/
        outputInfoFieldBeanModel = new LoadableDetachableModel<List<InfoFieldBean>>() {
            @Override
            protected List<InfoFieldBean> load() {
                AutoAttributeCriteria autoAttributeCriteria = autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
                List<InfoFieldBean> criteriaOutputs = autoAttributeCriteria.getOutputs();
                return criteriaOutputs;
            }
        };
        outputInfoOptionsModel = new LoadableDetachableModel<List<InfoOptionInput>>() {
            @Override
            protected List<InfoOptionInput> load() {
                AutoAttributeCriteria autoAttributeCriteria = autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
                List<InfoFieldBean> criteriaOutputs = autoAttributeCriteria.getOutputs();

                System.out.println("wicket:autoAttributeCriteria has " + autoAttributeCriteria.getOutputs().size() + " outputs, autoAttributeDefinitions has " + autoAttributeDefinition.getOutputs().size() + " outputs");

                List<InfoOptionInput> outputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(
                        autoAttributeDefinition.getOutputs(),
                        criteriaOutputs,
                        sessionUserModel.getObject() );

                System.out.println("wicket:autoAttributeCriteria created " + outputInfoOptions.size() + " options");
                return outputInfoOptions;
            }
        };
    }

    private Component getWidgetForAttribute(String id, boolean appearsInOutputSection, InfoFieldBean infoFieldBean, IModel<List<InfoOptionInput>> optionsModel) {

        Component widget;

        System.out.println("Getting widget for attribute " + infoFieldBean.getName() + ", retired: " + infoFieldBean.isRetired() + ", required " + infoFieldBean.isRequired());
        if (infoFieldBean.isRetired()) {
            widget = new AutoAttributeDisplayRetiredFieldPanel(id, infoFieldBean, optionsModel);
        }
        else
        if (infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.SelectBox) ||
                infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.ComboBox)) {
            widget = new AutoAttributeDisplaySelectFieldPanel(id, appearsInOutputSection, infoFieldBean, optionsModel);
        }
        else
        if (infoFieldBean.getType().equals(InfoFieldBean.InfoFieldType.TextField) && infoFieldBean.isUsingUnitOfMeasure()) {
            widget = new AutoAttributeDisplaySimpleTextFieldPanel(id, infoFieldBean, optionsModel);
        }
        else {
            widget = new Label(id, infoFieldBean.getName());
        }
        return widget;
    }

    /*private class InfoFieldOption implements Serializable {

        private InfoFieldBean infoFieldBean;
        private InfoOptionInput infoOptionInput;

        public InfoFieldOption(InfoFieldBean infoFieldBean, InfoOptionInput infoOptionInput) {
            this.infoFieldBean = infoFieldBean;
            this.infoOptionInput = infoOptionInput;
        }

        public InfoFieldBean getInfoFieldBean() {
            return infoFieldBean;
        }

        public InfoOptionInput getInfoOptionInput() {
            return infoOptionInput;
        }
    }*/
}
