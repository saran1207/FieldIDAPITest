package com.n4systems.fieldid.wicket.components.setup.comment;

import com.n4systems.fieldid.service.comment.CommentService;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.comment.CommentTemplateListPage;
import com.n4systems.fieldid.wicket.pages.setup.comment.EditCommentTemplatePage;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This is the Action Cell for the Comment Template List page.
 *
 * This allows users to either Edit or Remove a Comment Template.
 *
 *
 * Created by Jordan Heath on 08/08/14.
 */
public class CommentTemplateActionCell extends Panel {

    @SpringBean
    private CommentService commentService;

    private CommentTemplate thisTemplate;

    public CommentTemplateActionCell(String id,
                                     IModel<CommentTemplate> model,
                                     final CommentTemplateListPanel listPanel) {
        super(id);

        this.thisTemplate = model.getObject();

        //Create your links and add the necessary mechanics here.
        //There are only two and they're both static!!  No maintaining visibility here.
        add(new BookmarkablePageLink("editLink",
                                     EditCommentTemplatePage.class,
                                     PageParametersBuilder.param("commentTemplateId", thisTemplate.getId())));

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        optionsContainer.add(new Link("removeLink") {
            @Override
            public void onClick() {
                removeCommentTemplate();
                setResponsePage(CommentTemplateListPage.class);
            }
        });

        add(optionsContainer);

    }

    private void removeCommentTemplate() {
        CommentTemplate deleteMe = commentService.getCommentTemplateById(thisTemplate.getId());
        commentService.delete(deleteMe);
    }
}
