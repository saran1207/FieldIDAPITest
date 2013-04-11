package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.DisplayNoneIfCondition;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.components.timeline.TimePointInfoProvider;
import com.n4systems.fieldid.wicket.components.timeline.TimelinePanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.IsolationPointResult;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class ProcedureResultsPage extends FieldIDFrontEndPage {

    private IModel<Procedure> procedureModel;
    private ProcedureWorkflowState currentTimelineDisplay = ProcedureWorkflowState.LOCKED;
    private Component lockResultsPanel;
    private Component unlockResultsPanel;

    public ProcedureResultsPage(PageParameters params) {
        Long procedureId = params.get("id").toLong();
        procedureModel = new EntityModel<Procedure>(Procedure.class, procedureId);

        PropertyModel<List<IsolationPointResult>> lockResults = ProxyModel.of(procedureModel, on(Procedure.class).getLockResults());
        PropertyModel<List<IsolationPointResult>> unlockResults = ProxyModel.of(procedureModel, on(Procedure.class).getUnlockResults());

        WebMarkupContainer timelinesContainer = new WebMarkupContainer("timelinesContainer");
        timelinesContainer.setOutputMarkupPlaceholderTag(true);
        timelinesContainer.add(createLockingResultsSelector());
        timelinesContainer.add(lockResultsPanel = createTimelinePanel("lockResults", lockResults, ProcedureWorkflowState.LOCKED));
        timelinesContainer.add(unlockResultsPanel = createTimelinePanel("unlockResults", unlockResults, ProcedureWorkflowState.UNLOCKED));

        add(new Label("assetTypeName", ProxyModel.of(procedureModel, on(Procedure.class).getAsset().getType().getName())));
        add(new Label("procedureCode", ProxyModel.of(procedureModel, on(Procedure.class).getType().getProcedureCode())));

        add(createLocationDetailsPanel("locationDetailsPanel"));
        add(createWorkflowStateLabel("workflowState"));

        addProcedureDetails();

        add(timelinesContainer);
    }

    private void addProcedureDetails() {
        add(new DateTimeLabel("scheduledForLabel", new UserToUTCDateModel(ProxyModel.of(procedureModel, on(Procedure.class).getDueDate()))));
        add(new DateTimeLabel("lockedOnLabel", new UserToUTCDateModel(ProxyModel.of(procedureModel, on(Procedure.class).getLockDate()))));
        add(new DateTimeLabel("unlockedOnLabel", new UserToUTCDateModel(ProxyModel.of(procedureModel, on(Procedure.class).getUnlockDate()))).setVisible(procedureModel.getObject().getUnlockDate() != null));
        add(new Label("lockedByLabel", ProxyModel.of(procedureModel, on(Procedure.class).getLockedBy().getFullName())));
        add(new Label("unlockedByLabel", ProxyModel.of(procedureModel, on(Procedure.class).getUnlockedBy().getFullName())).setEscapeModelStrings(procedureModel.getObject().getUnlockedBy() != null));
    }

    private Component createWorkflowStateLabel(String id) {
        Label workflowStateLabel;
        if (procedureModel.getObject().getWorkflowState() == ProcedureWorkflowState.LOCKED) {
            workflowStateLabel = new Label(id, new FIDLabelModel("label.locked_out"));
        } else {
            workflowStateLabel = new Label(id, new FIDLabelModel("label.unlocked"));
        }
        return workflowStateLabel.add(new AttributeAppender("class", Model.of(procedureModel.getObject().getWorkflowState().name()), " "));
    }

    private WebMarkupContainer createLocationDetailsPanel(String id) {
        WebMarkupContainer locationContainer = new WebMarkupContainer(id);

        if (procedureModel.getObject().getGpsLocation() != null && procedureModel.getObject().getGpsLocation().isValid()) {
            locationContainer.add(new FlatLabel("latitude", ProxyModel.of(procedureModel, on(Procedure.class).getGpsLocation().getLatitude())));
            locationContainer.add(new FlatLabel("longitude", ProxyModel.of(procedureModel, on(Procedure.class).getGpsLocation().getLongitude())));
            locationContainer.add(new GoogleMap("googleMap", procedureModel.getObject().getGpsLocation().getLatitude().doubleValue(), procedureModel.getObject().getGpsLocation().getLongitude().doubleValue()));
        } else {
            locationContainer.setVisible(false);
        }

        return locationContainer;
    }

    private Component createTimelinePanel(String id, IModel<List<IsolationPointResult>> results, final ProcedureWorkflowState state) {
        return new TimelinePanel<IsolationPointResult>(id, results, new IsolationPointResultTimePointProvider(state)) {
            {
                add(new DisplayNoneIfCondition(new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return state != currentTimelineDisplay;
                    }
                }));
            }
        };
    }

    private Component createLockingResultsSelector() {
        return new MattBar("selectLockingOrUnlockingResults") {
            {
                addLink(new FIDLabelModel("label.locking"), ProcedureWorkflowState.LOCKED);
                addLink(new FIDLabelModel("label.unlocking"), ProcedureWorkflowState.UNLOCKED);
                setCurrentState(ProcedureWorkflowState.LOCKED);
                setVisible(procedureModel.getObject().getWorkflowState()==ProcedureWorkflowState.UNLOCKED);
            }
            @Override
            protected void onEnterState(AjaxRequestTarget target, Object state) {
                if (state == ProcedureWorkflowState.LOCKED) {
                    target.appendJavaScript("$('#"+unlockResultsPanel.getMarkupId()+"').hide();$('#"+lockResultsPanel.getMarkupId()+"').show();");
                } else {
                    target.appendJavaScript("$('#"+lockResultsPanel.getMarkupId()+"').hide();$('#"+unlockResultsPanel.getMarkupId()+"').show();");
                }
            }
        };
    }

    class IsolationPointResultTimePointProvider implements TimePointInfoProvider<IsolationPointResult> {

        private final ProcedureWorkflowState state;
        IsolationPointResultTimePointProvider(ProcedureWorkflowState state) { this.state=state; }

        @Override
        public Date getDate(IsolationPointResult item) {
            return item.getCheckCheckTime();
        }

        @Override
        public String getTitle(IsolationPointResult item) {
            return item.getIsolationPoint().getIdentifier() + " " + (state==ProcedureWorkflowState.LOCKED ? getString("label.locked_out") : getString("label.unlocked"));
        }

        @Override
        public String getText(IsolationPointResult item) {
            return item.getIsolationPoint().getMethod();
        }

        @Override
        public String getUrl(IsolationPointResult item) {
            return "http://www.fieldid.com/assets/images/video/fieldid-overview-base-large.png";
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/timeline/storyjs-embed.js");
        response.renderCSSReference("style/pageStyles/procedureResults.css");
        response.renderCSSReference("style/newCss/asset/header.css");
    }
}
