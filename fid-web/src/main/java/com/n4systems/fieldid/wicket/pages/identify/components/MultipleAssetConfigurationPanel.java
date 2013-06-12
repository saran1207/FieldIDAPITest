package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.asset.AssetIdentifierService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.identify.components.multi.AutoGenerateConfigurationPanel;
import com.n4systems.fieldid.wicket.pages.identify.components.multi.BatchIdentifierConfigurationPanel;
import com.n4systems.fieldid.wicket.pages.identify.components.multi.ManualConfigurationPanel;
import com.n4systems.fieldid.wicket.pages.identify.components.multi.RangeConfigurationPanel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.MultipleAssetConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class MultipleAssetConfigurationPanel extends Panel {

    @SpringBean
    private AssetIdentifierService assetIdentifierService;

    @SpringBean
    private AssetService assetService;

    private Integer numAssets;
    private GenerationMethod generationMethod = GenerationMethod.RANGE;
    private IModel<AssetType> assetTypeModel;
    private IModel<MultipleAssetConfiguration> model;

    private WebMarkupContainer[] stepPanels = new WebMarkupContainer[3];
    private FIDFeedbackPanel feedbackPanel;

    public MultipleAssetConfigurationPanel(String id, IModel<AssetType> assetTypeModel, IModel<MultipleAssetConfiguration> model) {
        super(id);
        this.assetTypeModel = assetTypeModel;
        this.model = model;

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        add(stepPanels[0] = new Step1Panel("step1Panel"));
        add(stepPanels[1] = new Step2Panel("step2Panel"));
        add(stepPanels[2] = new Step3Panel("step3Panel"));
        stepPanels[1].setVisible(false); stepPanels[2].setVisible(false);
    }

    class Step1Panel extends WebMarkupContainer {

        public Step1Panel(String id) {
            super(id);
            setOutputMarkupPlaceholderTag(true);
            Form form = new Form("step1Form");
            add(form);

            RequiredTextField<Integer> howManyField = new RequiredTextField<Integer>("howMany", new PropertyModel<Integer>(MultipleAssetConfigurationPanel.this, "numAssets"));
            form.add(howManyField);
            howManyField.add(new RangeValidator<Integer>(1, 250));

            form.add(new AjaxButton("continueButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    gotoStep(1, target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
        }

    }

    class Step2Panel extends WebMarkupContainer {
        public Step2Panel(String id) {
            super(id);
            setOutputMarkupPlaceholderTag(true);

            Form form = new Form("step2Form");
            add(form);

            RadioGroup<GenerationMethod> radioGroup = new RadioGroup<GenerationMethod>("identifierMethodGroup", new PropertyModel<GenerationMethod>(MultipleAssetConfigurationPanel.this, "generationMethod"));
            form.add(radioGroup);
            radioGroup.add(new ListView<GenerationMethod>("identifierMethod", Arrays.asList(GenerationMethod.values())) {
                @Override
                protected void populateItem(ListItem<GenerationMethod> item) {
                    item.add(new Radio<GenerationMethod>("methodRadio", item.getModel()));
                    if (item.getModelObject() == GenerationMethod.BATCH) {
                        item.add(new BatchIdentifierConfigurationPanel("methodConfigurationPanel", ProxyModel.of(model, on(MultipleAssetConfiguration.class).getBatchIdentifier())));
                    } else if (item.getModelObject() == GenerationMethod.MANUAL) {
                        item.add(new ManualConfigurationPanel("methodConfigurationPanel"));
                    } else if (item.getModelObject() == GenerationMethod.NON_SERIALIZED) {
                        item.add(new AutoGenerateConfigurationPanel("methodConfigurationPanel"));
                    } else if (item.getModelObject() == GenerationMethod.RANGE) {
                        item.add(new RangeConfigurationPanel("methodConfigurationPanel", ProxyModel.of(model, on(MultipleAssetConfiguration.class).getRangeConfiguration())));
                    }
                }
            });

            form.add(new AjaxButton("continueButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    MultipleAssetConfiguration config = model.getObject();
                    if (generationMethod == GenerationMethod.BATCH && StringUtils.isBlank(config.getBatchIdentifier())) {
                        error(getString("error.enter_an_identifier"));
                        target.add(feedbackPanel);
                        return;
                    }
                    if (generationMethod == GenerationMethod.RANGE && config.getRangeConfiguration().getStart() == null) {
                        error(getString("error.enter_a_start"));
                        target.add(feedbackPanel);
                        return;
                    }
                    List<MultipleAssetConfiguration.AssetConfiguration> assetConfigs = new ArrayList<MultipleAssetConfiguration.AssetConfiguration>(numAssets);
                    for (int i = 0; i < numAssets; i++) {
                        assetConfigs.add(new MultipleAssetConfiguration.AssetConfiguration());
                    }
                    config.setAssetConfigs(assetConfigs);
                    populateIdentifierGrid(model.getObject());
                    gotoStep(2, target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            form.add(new AjaxLink("backLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    gotoStep(0, target);
                }
            });

        }
    }

    private void populateIdentifierGrid(MultipleAssetConfiguration multipleAssetConfig) {
        if (generationMethod == GenerationMethod.RANGE) {
            Integer start = multipleAssetConfig.getRangeConfiguration().getStart();
            for (MultipleAssetConfiguration.AssetConfiguration assetConfig : multipleAssetConfig.getAssetConfigs()) {
                String prefix = multipleAssetConfig.getRangeConfiguration().getPrefix();
                String suffix = multipleAssetConfig.getRangeConfiguration().getSuffix();
                prefix = prefix == null ? "" : prefix;
                suffix = suffix == null ? "" : suffix;
                assetConfig.setIdentifier(prefix+start+suffix);
                start+=1;
            }
        } else if (generationMethod == GenerationMethod.BATCH) {
            for (MultipleAssetConfiguration.AssetConfiguration assetConfig : multipleAssetConfig.getAssetConfigs()) {
                assetConfig.setIdentifier(multipleAssetConfig.getBatchIdentifier());
            }
        } else if (generationMethod == GenerationMethod.NON_SERIALIZED) {
            for (MultipleAssetConfiguration.AssetConfiguration assetConfig : multipleAssetConfig.getAssetConfigs()) {
                assetConfig.setIdentifier(assetIdentifierService.generateIdentifier(assetTypeModel.getObject()));
            }
        }
    }

    class Step3Panel extends WebMarkupContainer {
        public Step3Panel(String id) {
            super(id);
            setOutputMarkupPlaceholderTag(true);

            Form form = new Form("step3Form");
            add(form);

            form.add(new ListView<MultipleAssetConfiguration.AssetConfiguration>("identifierGrid", ProxyModel.of(model, on(MultipleAssetConfiguration.class).getAssetConfigs())) {
                @Override
                protected void populateItem(final ListItem<MultipleAssetConfiguration.AssetConfiguration> item) {
                    item.setOutputMarkupId(true);
                    final PropertyModel<String> rfidNumModel = ProxyModel.of(item.getModel(), on(MultipleAssetConfiguration.AssetConfiguration.class).getRfidNumber());
                    final TextField<String> rfidField = new TextField<String>("rfidNumber", rfidNumModel);

                    item.add(createDuplicateWarningBehavior(rfidNumModel));

                    item.add(new RequiredTextField<String>("identifier", ProxyModel.of(item.getModel(), on(MultipleAssetConfiguration.AssetConfiguration.class).getIdentifier())));
                    item.add(rfidField);
                    item.add(new TextField<String>("customerRefNumber", ProxyModel.of(item.getModel(), on(MultipleAssetConfiguration.AssetConfiguration.class).getCustomerRefNumber())));

                    rfidField.add(new AjaxFormComponentUpdatingBehavior("onblur") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            target.add(item);
                        }
                    });
                }
            });

            form.add(new AjaxButton("confirmButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    model.getObject().setConfigurationComplete(true);
                    ModalWindow.closeCurrent(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            form.add(new AjaxLink("backLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    gotoStep(1, target);
                }
            });
        }
    }

    private Behavior createDuplicateWarningBehavior(final PropertyModel<String> rfidNumModel) {
        return new AttributeAppender("class", Model.of("duplicate-field"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return assetService.rfidExists(rfidNumModel.getObject());
            }
        };
    }

    private void gotoStep(int stepNumber, AjaxRequestTarget target) {
        for (int i=0; i < stepPanels.length; i++) {
            stepPanels[i].setVisible(i == stepNumber);
        }
        target.add(stepPanels);
        target.add(feedbackPanel);
    }

    private enum GenerationMethod { RANGE, NON_SERIALIZED, BATCH, MANUAL };

}
