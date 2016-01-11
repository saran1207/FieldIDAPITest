package com.n4systems.fieldid.wicket.pages.massevent;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.IdentifierLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.stream.Collectors;

public class AssetSmartSearchPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetService assetService;

    @SpringBean
    private ConfigService configService;

    private Form<Void> form;
    private String query;

    private FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer resultList;
    private WebMarkupContainer selectedList;
    private WebMarkupContainer blankSlate;
    private Label numberOfAssetsSelected;
    private Link performEventLink;
    private TextField queryField;
    private List<Asset> selectedAssetsList;
    private Long massActionLimit;


    public AssetSmartSearchPage() {

        selectedAssetsList = Lists.newArrayList();

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(form = new Form<>("form"));

        form.add(queryField = new TextField<String>("query", new PropertyModel<>(this, "query")));
        queryField.setOutputMarkupId(true);
        form.add(new AjaxSubmitLink("submitLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                resultList.setVisible(true);
                target.add(resultList, feedbackPanel, getTopFeedbackPanel(), blankSlate);
                target.appendJavaScript("$('#" + queryField.getMarkupId() + "').val('');");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        });

        add(blankSlate = new WebMarkupContainer("blankSlate") {
            @Override
            public boolean isVisible() {
                return selectedAssetsList.isEmpty() && !resultList.isVisible();
            }
        });
        blankSlate.setOutputMarkupPlaceholderTag(true);

        add(resultList = new WebMarkupContainer("resultsList"));
        resultList.setOutputMarkupPlaceholderTag(true);
        resultList.setVisible(false);

        resultList.add(new IdentifierLabel("identifierLabel"));

        ListView resultListView;

        resultList.add(resultListView = new ListView<Asset>("asset", getSearchResults()) {
            @Override
            protected void populateItem(ListItem<Asset> item) {
                Asset asset = item.getModelObject();

                item.add(new Label("assetType", new PropertyModel<>(asset, "type.displayName")));
                item.add(new Label("identifier", new PropertyModel<>(asset, "identifier")));
                item.add(new Label("rfid", new PropertyModel<>(asset, "rfidNumber")));
                item.add(new Label("reference", new PropertyModel<>(asset, "customerRefNumber")));
                item.add(new Label("owner", new PropertyModel<>(asset, "owner.displayName")));
                item.add(new Label("assetStatus", new PropertyModel<>(asset, "assetStatus.displayName")));
                item.add(new Label("nextScheduledDate", getNextScheduledEvent(asset)));
                item.add(new AjaxLink<Void>("selectLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if (selectedAssetsList.size() < getMassActionLimit()) {
                            if (!selectedAssetsList.contains(asset)) {
                                selectedAssetsList.add(asset);
                                info(new FIDLabelModel("message.asset_added").getObject());
                                resultList.setVisible(false);
                                target.add(resultList, performEventLink, numberOfAssetsSelected, selectedList, getTopFeedbackPanel(), feedbackPanel, blankSlate);
                            } else {
                                error(new FIDLabelModel("message.asset_already_added").getObject());
                                target.add(feedbackPanel, getTopFeedbackPanel());
                            }
                        } else {
                            error(new FIDLabelModel("error.asset_limit_reached", getMassActionLimit()).getObject());
                            target.add(feedbackPanel, getTopFeedbackPanel());
                        }
                    }
                });
            }

            @Override
            public boolean isVisible() {
                return !getList().isEmpty();
            }
        });

        resultList.add(new WebMarkupContainer("noAssetsMessage") {
            @Override
            public boolean isVisible() {
                return resultListView.getList().isEmpty();
            }
        }.setOutputMarkupPlaceholderTag(true));

        add(performEventLink = new Link<Void>("performEventLink") {
            @Override
            public void onClick() {
                setResponsePage(new SelectMassEventPage(selectedAssetsList.stream().map(asset -> asset.getId()).collect(Collectors.toList())));
            }

            @Override
            public boolean isVisible() {
                return !selectedAssetsList.isEmpty();
            }
        });

        performEventLink.setOutputMarkupPlaceholderTag(true);

        add(numberOfAssetsSelected = new Label("numberOfAssetsSelected", getNumberSelected()) {
            @Override
            public boolean isVisible() {
                return !selectedAssetsList.isEmpty();
            }
        });
        numberOfAssetsSelected.setOutputMarkupPlaceholderTag(true);

        add(selectedList = new WebMarkupContainer("selectedList") {
            @Override
            public boolean isVisible() {
                return !selectedAssetsList.isEmpty();
            }
        });
        selectedList.setOutputMarkupPlaceholderTag(true);

        selectedList.add(new IdentifierLabel("identifierLabel"));

        selectedList.add(new ListView<Asset>("asset", getSelectedAssetsListModel()) {
            @Override
            protected void populateItem(ListItem<Asset> item) {
                Asset asset = item.getModelObject();

                item.add(new Label("assetType", new PropertyModel<>(asset, "type.displayName")));
                item.add(new Label("identifier", new PropertyModel<>(asset, "identifier")));
                item.add(new Label("rfid", new PropertyModel<>(asset, "rfidNumber")));
                item.add(new Label("reference", new PropertyModel<>(asset, "customerRefNumber")));
                item.add(new Label("owner", new PropertyModel<>(asset, "owner.displayName")));
                item.add(new Label("assetStatus", new PropertyModel<>(asset, "assetStatus.displayName")));
                item.add(new Label("nextScheduledDate", getNextScheduledEvent(asset)));
                item.add(new AjaxLink<Void>("removeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if (selectedAssetsList.contains(asset))
                            selectedAssetsList.remove(asset);
                        info(new FIDLabelModel("message.asset_remove").getObject());
                        target.add(performEventLink, numberOfAssetsSelected, selectedList, getTopFeedbackPanel(), blankSlate);
                    }
                });
            }
        });
    }

    private LoadableDetachableModel<String> getNumberSelected() {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return new FIDLabelModel("label.number_assets_selected", selectedAssetsList.size()).getObject();
            }
        };
    }

    private IModel<String> getNextScheduledEvent(Asset asset) {
        Event schedule = assetService.findNextScheduledEventByAsset(asset.getId());

        if(schedule == null)
            return Model.of(new String());

        return new DayDisplayModel(Model.of(schedule.getDueDate())).withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone());
    }

    private LoadableDetachableModel<List<Asset>> getSearchResults() {
        return new LoadableDetachableModel<List<Asset>>() {
            @Override
            protected List<Asset> load() {
                if (StringUtils.isNotEmpty(query))
                    return assetService.findExactAssetByIdentifiersForNewSmartSearch(query);
                else
                    return Lists.newArrayList();
            }
        };
    }

    private LoadableDetachableModel<List<Asset>> getSelectedAssetsListModel() {
        return new LoadableDetachableModel<List<Asset>>() {
            @Override
            protected List<Asset> load() {
                return selectedAssetsList;
            }
        };
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.select_assets"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                redirect("/startEvent.action");
            }
        }.add(new Label(linkLabelId, new FIDLabelModel("label.back_to_x", new FIDLabelModel("label.startevent").getObject())));
    }

    private Long getMassActionLimit() {
        if (massActionLimit == null) {
            massActionLimit = configService.getLong(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId());
        }
        return massActionLimit;
    }
}
