package com.n4systems.fieldid.wicket.pages.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 *
 * This is the base class for the "Procedures" page.  The three different tabbed pages under this will extend it.
 *
 * Created by rrana on 2014-04-09.
 */
public abstract class ProceduresAllListPage extends FieldIDTemplatePage {

    @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    protected IModel<PublishedState> procedureDefinitionIModel;

    public ProceduresAllListPage(IModel<PublishedState> model) {
        procedureDefinitionIModel = model;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.procedures"));
    }


    @Override
    protected void addNavBar(String navBarId) {

        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.procedures_published_count", procedureDefinitionService.getPublishedCount(""))).page(PublishedListAllPage.class).build(),
                aNavItem().label(new FIDLabelModel("label.procedures_drafts_count", procedureDefinitionService.getDraftCount(""))).page(DraftListAllPage.class).build(),
                aNavItem().label(new FIDLabelModel("label.procedures_previously_published")).page(PreviouslyPublishedListAllPage.class).build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/procedureDefinition.css");
        response.renderJavaScriptReference("javascript/procedureDefinitionPage.js");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        PublishedState publishedState = procedureDefinitionIModel.getObject();
        List<NavigationItem> navItems = createBreadCrumbs(publishedState);
        add(new BreadCrumbBar(breadCrumbBarId, navItems.toArray(new NavigationItem[0])));
    }

    protected List<NavigationItem> createBreadCrumbs(PublishedState procedureDefinition) {
        List<NavigationItem> navItems = Lists.newArrayList();

        navItems.add(aNavItem().label("title.procedures").page(PublishedListAllPage.class).build());

        if(procedureDefinition.equals(PublishedState.PUBLISHED)) {
            navItems.add(aNavItem().label(new PropertyModel<String>(procedureDefinition, "label")).page(PublishedListAllPage.class).build());
        } else if(procedureDefinition.equals(PublishedState.DRAFT)) {
            navItems.add(aNavItem().label(new PropertyModel<String>(procedureDefinition, "label")).page(DraftListAllPage.class).build());
        } else if(procedureDefinition.equals(PublishedState.PREVIOUSLY_PUBLISHED)) {
            navItems.add(aNavItem().label(new PropertyModel<String>(procedureDefinition, "label")).page(PreviouslyPublishedListAllPage.class).build());
        }

        return navItems;
    }

}