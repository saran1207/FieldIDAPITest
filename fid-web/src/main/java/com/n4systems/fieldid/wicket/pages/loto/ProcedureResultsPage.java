package com.n4systems.fieldid.wicket.pages.loto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.components.timeline.TimePointInfoProvider;
import com.n4systems.fieldid.wicket.components.timeline.TimelinePanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.IsolationPointResult;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.services.date.DateService;
import com.n4systems.util.DateHelper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class ProcedureResultsPage extends FieldIDFrontEndPage {

    @SpringBean private S3Service s3Service;
    @SpringBean private DateService dateService;

    private IModel<Procedure> procedureModel;
    private ProcedureWorkflowState currentTimelineDisplay;
    private Long procedureId;

    public ProcedureResultsPage(PageParameters params) {
        procedureId = params.get("id").toLong();
        currentTimelineDisplay = params.get("unlocking").isEmpty() ? ProcedureWorkflowState.LOCKED : ProcedureWorkflowState.UNLOCKED;

        procedureModel = new EntityModel<Procedure>(Procedure.class, procedureId);

        PropertyModel<List<IsolationPointResult>> lockResults = ProxyModel.of(procedureModel, on(Procedure.class).getLockResults());
        PropertyModel<List<IsolationPointResult>> unlockResults = ProxyModel.of(procedureModel, on(Procedure.class).getUnlockResults());

        WebMarkupContainer timelinesContainer = new WebMarkupContainer("timelinesContainer");
        timelinesContainer.setOutputMarkupPlaceholderTag(true);
        timelinesContainer.add(createLockingResultsSelector());

        if (currentTimelineDisplay == ProcedureWorkflowState.LOCKED) {
            timelinesContainer.add(createTimelinePanel("resultsTimeline", lockResults, ProcedureWorkflowState.LOCKED));
        } else {
            timelinesContainer.add(createTimelinePanel("resultsTimeline", unlockResults, ProcedureWorkflowState.UNLOCKED));
        }

        BookmarkablePageLink assetSummaryLink = new BookmarkablePageLink("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(procedureModel.getObject().getAsset().getId()));
        add(assetSummaryLink);
        assetSummaryLink.add(new Label("assetTypeName", ProxyModel.of(procedureModel, on(Procedure.class).getAsset().getType().getName())));
        assetSummaryLink.add(new Label("assetIdentifier", ProxyModel.of(procedureModel, on(Procedure.class).getAsset().getIdentifier())));
        add(new Label("procedureCode", ProxyModel.of(procedureModel, on(Procedure.class).getType().getProcedureCode())));

        add(createLocationDetailsPanel("locationDetailsPanel"));
        add(createWorkflowStateLabel("workflowState"));

        addProcedureDetails();

        add(timelinesContainer);
    }

    private void addProcedureDetails() {
        add(new DateTimeLabel("scheduledForLabel", ProxyModel.of(procedureModel, on(Procedure.class).getDueDate())));
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
        return new TimelinePanel<IsolationPointResult>(id, results, new IsolationPointResultTimePointProvider(state));
    }

    private Component createLockingResultsSelector() {
        return new MattBar("selectLockingOrUnlockingResults") {
            {
                addLink(new FIDLabelModel("label.locking"), ProcedureWorkflowState.LOCKED);
                addLink(new FIDLabelModel("label.unlocking"), ProcedureWorkflowState.UNLOCKED);
                setCurrentState(currentTimelineDisplay);
                setVisible(procedureModel.getObject().getWorkflowState()==ProcedureWorkflowState.UNLOCKED);
            }
            @Override
            protected void onEnterState(AjaxRequestTarget target, Object state) {
                PageParameters params = new PageParameters();
                params.add("id", procedureId);
                if (state == ProcedureWorkflowState.UNLOCKED) {
                    params.add("unlocking", true);
                }
                setResponsePage(ProcedureResultsPage.class, params);
            }
        };
    }

    class IsolationPointResultTimePointProvider implements TimePointInfoProvider<IsolationPointResult> {

        private final ProcedureWorkflowState state;
        IsolationPointResultTimePointProvider(ProcedureWorkflowState state) { this.state=state; }

        @Override
        public Date getDate(IsolationPointResult item) {
            Date checkTimeInUserTimeZone = DateHelper.convertToUserTimeZone(item.getCheckCheckTime(), FieldIDSession.get().getSessionUser().getTimeZone());
            return checkTimeInUserTimeZone;
        }

        @Override
        public String getTitle(IsolationPointResult item) {
            StringBuffer sb = new StringBuffer();
            if (item.getIsolationPoint().getIdentifier() != null) {
                sb.append(item.getIsolationPoint().getIdentifier()).append(" ");
            }
            if (item.getIsolationPoint().getSourceText() != null) {
                sb.append(item.getIsolationPoint().getSourceText()).append(" ");
            }
            sb.append(state==ProcedureWorkflowState.LOCKED ? getString("label.locked_out") : getString("label.unlocked"));
            return sb.toString();
        }

        @Override
        public String getText(IsolationPointResult item) {
            return item.getIsolationPoint().getMethod();
        }

        @Override
        public String getMediaUrl(IsolationPointResult item) {
            ImageAnnotation annotation = item.getIsolationPoint().getAnnotation();
            if (annotation != null) {
                return s3Service.getProcedureDefinitionImageMediumURL((ProcedureDefinitionImage) annotation.getImage()).toString();
            } else {
                return null;
            }
        }

        @Override
        public String getThumbnailUrl(IsolationPointResult item) {
            ImageAnnotation annotation = item.getIsolationPoint().getAnnotation();
            if (annotation != null) {
                return s3Service.getProcedureDefinitionImageMediumURL((ProcedureDefinitionImage) annotation.getImage()).toString();
            } else {
                return null;
            }
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/procedureResults.css");
        response.renderCSSReference("style/legacy/newCss/asset/header.css");

        response.renderCSSReference("style/legacy/component/annotated-image.css");
        response.renderJavaScriptReference("javascript/jquery.annotate.js");
        response.renderJavaScriptReference("javascript/displayAnnotations.js");

        JsonElement convertedIsolationAnnotations = serializeImageAnnotations(procedureModel.getObject().getLockResults());
        response.renderJavaScript("var isolationAnnotations = " + convertedIsolationAnnotations.toString()+";", null);
        response.renderJavaScript("unlockingState = " + currentTimelineDisplay.equals(ProcedureWorkflowState.UNLOCKED) + ";", null);
    }

    @Override
    protected void renderJqueryJavaScriptReference(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/jquery-1.7.2.js");
    }

    protected JsonElement serializeImageAnnotations(List<IsolationPointResult> results) {
        JsonArray points = new JsonArray();

        for (IsolationPointResult result : results) {
            ImageAnnotation annotation = result.getIsolationPoint().getAnnotation();
            if (annotation != null && annotation.getImage() != null) {
                JsonObject point = new JsonObject();
                point.addProperty("x", annotation.getX());
                point.addProperty("y", annotation.getY());
                String escapedText = StringEscapeUtils.escapeJavaScript(annotation.getText());
                point.addProperty("text", escapedText);
                point.addProperty("cssStyle", annotation.getType().getCssClass());
                points.add(point);
            }
        }

        return points;
    }
}
