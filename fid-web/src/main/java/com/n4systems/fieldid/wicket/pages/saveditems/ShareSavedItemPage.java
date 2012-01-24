package com.n4systems.fieldid.wicket.pages.saveditems;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.fieldid.wicket.components.saveditems.ShareWithUsersFromOrgPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareSavedItemPage extends FieldIDFrontEndPage {

    @SpringBean
    private UserService userService;

    private Long savedItemId;

    public ShareSavedItemPage(PageParameters params) {
        super(params);
        savedItemId = params.get("id").toLong();
        add(new ShareItemForm("shareItemForm"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/share_saved_item.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.share_saved_item"));
    }

    class ShareItemForm extends Form {

        Map<String,Boolean> sharedUsersMap = new HashMap<String,Boolean>();

        public ShareItemForm(String id) {
            super(id);

            add(new FIDFeedbackPanel("feedbackPanel"));

            final Map<BaseOrg, List<User>> orgList = userService.getUsersByOrg();

            add(new ListView<BaseOrg>("orgList", new ArrayList<BaseOrg>(orgList.keySet())) {
                @Override
                protected void populateItem(ListItem<BaseOrg> orgItem) {
                    boolean initiallyExpanded = orgItem.getIndex() == 0;

                    final SlidingCollapsibleContainer slidingCollapsibleContainer = new SlidingCollapsibleContainer("orgContainer", new PropertyModel<String>(orgItem.getModel(), "name"), initiallyExpanded);
                    orgItem.add(slidingCollapsibleContainer);

                    slidingCollapsibleContainer.addContainedPanel(new ShareWithUsersFromOrgPanel("usersPanel", orgList.get(orgItem.getModelObject()), sharedUsersMap));
                }
            });

            add(new Button("submitButton"));
            add(new BookmarkablePageLink<Void>("cancelLink", ManageSavedItemsPage.class));
        }

        @Override
        protected void onSubmit() {
            List<Long> userIdsToShareWith = getSelectedUserIds();
            if (userIdsToShareWith.isEmpty()) {
                error(new FIDLabelModel("error.select_at_least_one_user_to_share_with").getObject());
            } else {
                userService.shareSavedItemWithUsers(savedItemId, userIdsToShareWith);
                FieldIDSession.get().info(new FIDLabelModel("message.item_successfully_shared").getObject());
                setResponsePage(ManageSavedItemsPage.class);
            }
        }

        private List<Long> getSelectedUserIds() {
            List<Long> selectedUserIds = new ArrayList<Long>();
            for (String s : sharedUsersMap.keySet()) {
                if (sharedUsersMap.get(s)) {
                    selectedUserIds.add(Long.parseLong(s));
                }
            }
            return  selectedUserIds;
        }

    }

}
