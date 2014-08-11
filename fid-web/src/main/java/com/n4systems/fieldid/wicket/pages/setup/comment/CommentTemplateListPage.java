package com.n4systems.fieldid.wicket.pages.setup.comment;

import com.n4systems.fieldid.service.comment.CommentService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.setup.comment.CommentTemplateActionColumn;
import com.n4systems.fieldid.wicket.components.setup.comment.CommentTemplateListPanel;
import com.n4systems.fieldid.wicket.data.CommentTemplateDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.TemplatesPage;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page lists all Comment Templates and allows the user to remove/delete or edit the templates.
 *
 * The user may also add a new Comment Template from this page.
 *
 *
 * Created by Jordan Heath on 08/08/14.
 */
public class CommentTemplateListPage extends FieldIDTemplatePage {

    protected FIDFeedbackPanel feedbackPanel;

    @SpringBean
    private CommentService commentService;

    public CommentTemplateListPage() {
        //We don't actually do anything here... but it's good to specify that this
        //is the constructor.  We don't give a crap about PageParameters here.
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        //Another single-parter.
        CommentTemplateListPanel listPanel;
        add(listPanel = new CommentTemplateListPanel("commentTemplateList",
                                                     getDataProvider()) {

            @Override
            protected FIDFeedbackPanel getFeedbackPanel() {
                return feedbackPanel;
            }

            @Override
            protected void addActionColumn(List<IColumn<CommentTemplate>> columnList) {
                columnList.add(new CommentTemplateActionColumn(this));
            }
        });

        listPanel.setOutputMarkupId(true);
    }


    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_comment_templates"));
    }


    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, TemplatesPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                        aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                         commentService.getCommentTemplateCount()))
                                  .page(CommentTemplateListPage.class)
                                  .build(),

                        aNavItem().label(new FIDLabelModel("nav.add"))
                                  .page(AddCommentTemplatePage.class)
                                  .onRight()
                                  .build()
                )
        );
    }

    private FieldIDDataProvider<CommentTemplate> getDataProvider() {
        return new CommentTemplateDataProvider("name", SortOrder.ASCENDING);
    }
}
