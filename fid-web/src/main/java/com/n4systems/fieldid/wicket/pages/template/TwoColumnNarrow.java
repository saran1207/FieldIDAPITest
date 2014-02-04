package com.n4systems.fieldid.wicket.pages.template;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

import java.util.List;

public class TwoColumnNarrow extends FieldIDTemplatePage {

    private static List LEVELS = Lists.newArrayList("Level 5", "Level 4", "Level 3", "Level 2", "Level 1");
    private static List WARNINGS = Lists.newArrayList("Use this for fall arrest only", "Use this for restraint only");

    // public TwoColumnNarrow() {
    //     add(new Image("featureImage", new ContextRelativeResource("/img/_temp/hoist.jpg")));

    //     Form simpleForm;
    //     add(simpleForm = new Form<Void>("form"));

    //     simpleForm.add(new FileUploadField("fileUploadField"));
    //     simpleForm.add(new TextField<String>("textField"));
    //     simpleForm.add(new FidDropDownChoice<String>("clearanceLevels", LEVELS));
    //     simpleForm.add(new FidDropDownChoice<String>("warnings", WARNINGS));
    // }

        @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Next page in hierarchy"), TemplatePage.class),
                new NavigationItem(new FIDLabelModel("label.current_page"), TwoColumnLeft.class)
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "Two Column: Narrow & Fixed Options");
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    class ActionGroup extends Fragment {

        public ActionGroup(String id) {
            super(id, "actions", TwoColumnNarrow.this);

            add(new Link<Void>("primaryLink") {
                @Override
                public void onClick() {
                }
            });

            add(new Link<Void>("secondaryLink") {
                @Override
                public void onClick() {
                }
            });
        }
    }
}
