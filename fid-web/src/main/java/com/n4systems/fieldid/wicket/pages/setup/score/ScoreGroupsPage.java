package com.n4systems.fieldid.wicket.pages.setup.score;

import com.n4systems.fieldid.service.event.ScoreService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class ScoreGroupsPage extends FieldIDLoggedInPage {

    @SpringBean
    private ScoreService scoreService;

    public ScoreGroupsPage() {
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new NewScoreGroupForm("newScoreGroupForm"));
        IModel<ArrayList<ScoreGroup>> scoreGroups = new Model<ArrayList<ScoreGroup>>(new ArrayList<ScoreGroup>(getScoreGroups()));
        add(new ListView<ScoreGroup>("scoreGroups", scoreGroups) {
            @Override
            protected void populateItem(ListItem<ScoreGroup> item) {
                item.add(new ScoreGroupPanel("group", item.getModel()));
            }
        });
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.score_groups"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                NavigationItemBuilder.aNavItem().page("eventTypes.action").label("nav.view_all").build(),
                NavigationItemBuilder.aNavItem().page("buttonGroups.action").label("nav.button_groups").build(),
                NavigationItemBuilder.aNavItem().page(ScoreGroupsPage.class).label("nav.score_groups").build(),
                NavigationItemBuilder.aNavItem().page("eventTypesAdd.action").label("nav.add").onRight().build()));
    }


    class NewScoreGroupForm extends Form {

        private String newGroupName;

        public NewScoreGroupForm(String id) {
            super(id);
            add(new TextField<String>("scoreGroupName", new PropertyModel<String>(this, "newGroupName")));
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            ScoreGroup scoreGroup = new ScoreGroup();
            scoreGroup.setTenant(getTenant());
            scoreGroup.setName(newGroupName);
            scoreService.saveScoreGroup(scoreGroup);
            FieldIDSession.get().info("Successfully saved group!");
            setResponsePage(ScoreGroupsPage.class);
        }

        @Override
        protected void onError() {
            System.out.println("Form error yo");
        }
    }

    public List<ScoreGroup> getScoreGroups() {
        return scoreService.getScoreGroups();
    }

}
