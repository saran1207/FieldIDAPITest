package com.n4systems.fieldid.wicket.pages.setup.eventtype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.event.EventTypeRulesService;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeRule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class RulesPage extends EventTypePage {

    @SpringBean
    private EventTypeRulesService rulesService;
    @SpringBean
    private AssetStatusService assetStatusService;

    private FIDFeedbackPanel feedbackPanel;

    public RulesPage(PageParameters params) {
        super(params);

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(new ListView<EventTypeRule>("result", getRules(eventTypeModel.getObject())) {
            @Override
            protected void populateItem(ListItem<EventTypeRule> item) {

                item.add(new FlatLabel("resultLabel", new PropertyModel<String>(item.getModel(), "result.displayName")));

                Label statusLabel;
                item.add(statusLabel = new Label("statusLabel", new PropertyModel<String>(item.getModel(), "assetStatus.displayName")));
                statusLabel.setOutputMarkupPlaceholderTag(true);

                Form form;

                item.add(form = new Form<Void>("form"));
                form.setOutputMarkupPlaceholderTag(true);
                form.add(new FidDropDownChoice<AssetStatus>("selectStatus",
                        new PropertyModel<AssetStatus>(item.getModel(), "assetStatus"),
                        getAssetStatuses(),
                        new ListableChoiceRenderer<AssetStatus>()).setNullValid(true).add(new UpdateComponentOnChange()));

                AjaxLink editLink;

                item.add(editLink = new AjaxLink<Void>("editLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        statusLabel.setVisible(false);
                        form.setVisible(true);
                        this.setVisible(false);
                        target.add(statusLabel, form, this);
                    }
                });
                editLink.setOutputMarkupPlaceholderTag(true);

                form.add(new AjaxSubmitLink("saveLink") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                        EventTypeRule rule = item.getModelObject();

                        if (rule.getAssetStatus()!= null && rule.getAssetStatus().getId() == -1 && rulesService.exists(rule.getEventType(), rule.getResult())) {
                            rulesService.deleteRule(rule.getEventType(), rule.getResult());
                        } else if (rule.getAssetStatus()!= null && rule.getAssetStatus().getId() != -1){
                            if (rule.isNew()) {
                                rule.setTenant(getTenant());
                            }
                            rulesService.saveOrUpdateRule(rule);
                        }

                        statusLabel.setVisible(true);
                        form.setVisible(false);
                        editLink.setVisible(true);
                        target.add(statusLabel, form, editLink);
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form<?> form) {

                    }
                });

                form.setVisible(false);

            }
        });


    }

    private LoadableDetachableModel<List<EventTypeRule>> getRules(EventType eventType) {
        return new LoadableDetachableModel<List<EventTypeRule>>() {
            @Override
            protected List<EventTypeRule> load() {
                return rulesService.getAllRules(eventType);
            }
        };
    }

    private List<AssetStatus> getAssetStatuses() {
        List assetStatusList = Lists.newArrayList();
        assetStatusList.add(rulesService.getNoChangeStatus());
        assetStatusList.addAll(assetStatusService.getActiveStatuses());
        return assetStatusList;
    }
}
