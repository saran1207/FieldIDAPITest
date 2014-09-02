package com.n4systems.fieldid.wicket.pages.template;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

public class FormComponents extends FieldIDTemplatePage {

    private WebMarkupContainer container;

    private boolean isHorizontal = false;

    private BaseOrg org;
    private Date date;
    private DateRange dateRange = new DateRange(RangeType.CUSTOM);
    private PredefinedLocation predefinedLocation;

    public FormComponents() {

        add(container = new WebMarkupContainer("formContainer"));
        container.setOutputMarkupId(true);
        container.add(new TextField<String>("textField"));
        container.add(new TextArea<String>("textArea"));
        container.add(new FidDropDownChoice<Country>("dropdownField", Lists.newArrayList(CountryList.getInstance().getCountries()), new ListableChoiceRenderer<Country>()));
        container.add(new OrgLocationPicker("orgPicker", new PropertyModel<BaseOrg>(this, "org")));
        container.add(new OrgLocationPicker("locationPicker",new PropertyModel<BaseOrg>(this, "org"), new PropertyModel<PredefinedLocation>(this, "predefinedLocation")));
        container.add(new DateTimePicker("datePicker", new PropertyModel<Date>(this, "date")));
        container.add(new DateRangePicker("dateRangePicker", new PropertyModel<DateRange>(this, "dateRange")));
        add(new AjaxLink<Void>("toggleLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                isHorizontal = !isHorizontal;

                if (isHorizontal) {
                    container.add(new AttributeModifier("class", "form-horizontal"));
                } else {
                    container.add(new AttributeModifier("class", ""));
                }
                target.add(container);

            }
        });
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new BookmarkablePageLink<TemplatePage>(linkId, TemplatePage.class)
                .add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Next page in hierarchy"), TemplatePage.class),
                new NavigationItem(new FIDLabelModel("label.current_page"), FormComponents.class)
        ));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                new NavigationItem(Model.of("TemplatePage"), TemplatePage.class),
                new NavigationItem(new FIDLabelModel("label.current_page"), FormComponents.class)
        ));
    }
}
