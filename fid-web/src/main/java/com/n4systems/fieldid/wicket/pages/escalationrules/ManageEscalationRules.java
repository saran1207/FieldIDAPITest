package com.n4systems.fieldid.wicket.pages.escalationrules;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.escalationrule.AssignmentEscalationRuleService;
import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.sendsearch.SendSearchService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssignmentEscalationRule;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

/**
 * Created by rrana on 2015-08-17.
 */
public class ManageEscalationRules extends FieldIDFrontEndPage {

    private static final String HIGHLIGHT_REORDER_JS = "$('.list').delegate('tr', 'mousedown', function(event) { if(event.which==1) $(this).css('background-color','#eee'); } );";
    private static final String UNHIGHLIGHT_REORDER_JS = "$('.list').delegate('tr', 'mouseup', function(event) { if(event.which==1) $(this).css('background-color', '#fff' ); } );";


    @SpringBean
    private UserService userService;

    @SpringBean
    private PersistenceService persistenceService;

    @SpringBean
    private SavedAssetSearchService savedAssetService;

    @SpringBean
    private SavedReportService savedReportService;

    @SpringBean
    private SendSearchService sendSearchService;

    @SpringBean
    private AssignmentEscalationRuleService assignmentEscalationRuleService;

    private WebMarkupContainer itemsListContainer;

    private boolean reorderState = false;


    private List<AssignmentEscalationRule> savedItems;
    IModel<List<AssignmentEscalationRule>> assignmentEscalationRuleModel;

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.manage_rules"));
    }

    public ManageEscalationRules() {

        // if in "reorder" mode, then turn off row highlighting.
        IModel<String> reorderModel = new AbstractReadOnlyModel<String>() {
            @Override public String getObject() {
                return reorderState ? "no-highlight" : "";
            }
        };
        itemsListContainer = new WebMarkupContainer("itemsListContainer");
        itemsListContainer.add(new AttributeAppender("class", reorderModel, " "));
        itemsListContainer.setOutputMarkupId(true);
        add(itemsListContainer);

        assignmentEscalationRuleModel = createSavedItemsModel();
        itemsListContainer.add(new ListView<AssignmentEscalationRule>("itemsList", assignmentEscalationRuleModel) {
            @Override
            protected void populateItem(final ListItem<AssignmentEscalationRule> item) {
                item.setOutputMarkupId(true);

                WebMarkupContainer firstColumn = new WebMarkupContainer("firstColumn");
                firstColumn.add(createColspanModifier());
                firstColumn.add(new Label("ruleName", new FIDLabelModel(item.getModel(), "ruleName")));
                item.add(firstColumn);

                item.add(new Label("modifiedDate", new DayDisplayModel(new PropertyModel<Date>(item.getModelObject(), "modified"), true, FieldIDSession.get().getSessionUser().getTimeZone())) {
                    @Override
                    public boolean isVisible() {
                        return !reorderState;
                    }
                });

                //This will be hard-coded for now as there are no other types of rules.
                item.add(new Label("type", new FIDLabelModel("Escalation Rule")));

                item.add(new BookmarkablePageLink<Void>("editLink", EditRulePage.class, PageParametersBuilder.id(item.getModelObject().getId())));

                item.add(new AjaxLink("deleteLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        assignmentEscalationRuleService.archiveRule(item.getModelObject());
                        updateAssignmentEscalationRuleModel();
                        setResponsePage(new ManageEscalationRules());
                    }
                });
            }
        });

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/component/manage_saved_items.css");
        response.renderCSSReference("style/legacy/newCss/component/header_reorder_link_button.css");
        response.renderOnDomReadyJavaScript(HIGHLIGHT_REORDER_JS);
        response.renderOnDomReadyJavaScript(UNHIGHLIGHT_REORDER_JS);
    }

    private void updateAssignmentEscalationRuleModel() {
        assignmentEscalationRuleModel = createSavedItemsModel();
    }

    private LoadableDetachableModel<List<AssignmentEscalationRule>> createSavedItemsModel() {
        return new LoadableDetachableModel<List<AssignmentEscalationRule>>() {
            @Override
            protected List<AssignmentEscalationRule> load() {
                return assignmentEscalationRuleService.getAllActiveRules();
            }
        };
    }

    private Behavior createColspanModifier() {
        return new AttributeModifier("colspan", "4") {
            @Override
            public boolean isEnabled(Component component) {
                return reorderState;
            }
        };
    }

}
