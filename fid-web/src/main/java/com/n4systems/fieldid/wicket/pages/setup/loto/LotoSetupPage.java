package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.util.DisplayEnumChoiceRenderer;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.AnnotationType;
import com.n4systems.model.procedure.LotoSettings;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

public class LotoSetupPage extends FieldIDTemplatePage {

    @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    private IModel<LotoSettings> lotoSettingsModel;


    public LotoSetupPage() {

        lotoSettingsModel = Model.of(getLotoSettings());

        add(new ListView<IsolationPointSourceType>("device", getIsolationPointSourceTypes()) {

            @Override
            protected void populateItem(ListItem<IsolationPointSourceType> item) {
                item.add(new Label("name", new PropertyModel<>(item.getModel(), "identifier")));
                item.add(new BookmarkablePageLink<ManageDevicePage>("editLink", ManageDevicePage.class, PageParametersBuilder.param("type", item.getModelObject().name())));
            }
        });

        add(new BookmarkablePageLink<ManageDevicePage>("editSharedLink", ManageDevicePage.class, PageParametersBuilder.param("type", "all")));

        add(new BookmarkablePageLink<LotoDetailsSetupPage>("editDetailsLink", LotoDetailsSetupPage.class));

        FidDropDownChoice<AnnotationType> annotationTypeSelect;
        add(annotationTypeSelect = new FidDropDownChoice<AnnotationType>("annotationType",
                new PropertyModel<>(lotoSettingsModel, "annotationType"),
                Lists.newArrayList(AnnotationType.values()),
                new DisplayEnumChoiceRenderer()));
        annotationTypeSelect.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                procedureDefinitionService.saveOrUpdateLotoSettings(lotoSettingsModel.getObject());
                info(new FIDLabelModel("message.annotation_style_updated").getObject());
                target.add(getTopFeedbackPanel());
            }
        });

        add(new BookmarkablePageLink<LockoutReasonsListPage>("lockoutReasonsLink", LockoutReasonsListPage.class));


    }

    private LotoSettings getLotoSettings() {
        LotoSettings lotoSettings =  procedureDefinitionService.getLotoSettings();
        return lotoSettings != null ? lotoSettings : new LotoSettings(getTenant());
    }

    private ArrayList<IsolationPointSourceType> getIsolationPointSourceTypes() {
        ArrayList<IsolationPointSourceType> isolationPointSourceTypes = Lists.newArrayList(IsolationPointSourceType.values());
        isolationPointSourceTypes.remove(IsolationPointSourceType.N);
        return isolationPointSourceTypes;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.loto_setup"));
    }
}
