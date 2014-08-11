package com.n4systems.fieldid.wicket.pages.setup.comment;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page allows the user to edit an existing Comment Template.
 *
 * Created by Jordan Heath on 08/08/14.
 */
public class EditCommentTemplatePage extends AddCommentTemplatePage {

    public EditCommentTemplatePage(PageParameters params) {
        CommentTemplate loadedTemplate =
                params.get("commentTemplateId") != null ?
                commentService.getCommentTemplateById(params.get("commentTemplateId").toLong()) :
                new CommentTemplate();

        this.thisTemplate = Model.of(loadedTemplate);
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                        aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                commentService.getCommentTemplateCount()))
                                .page(CommentTemplateListPage.class)
                                .build(),

                        aNavItem().label(new FIDLabelModel("nav.edit"))
                                  .page(EditCommentTemplatePage.class)
                                  .params(PageParametersBuilder.param("commentTemplateId",
                                          this.thisTemplate
                                                .getObject()
                                                .getId()))
                                  .build(),

                        aNavItem().label(new FIDLabelModel("nav.add"))
                                .page(AddCommentTemplatePage.class)
                                .onRight()
                                .build()
                )
        );
    }

    /**
     * This method has been overridden to return a static value of true, indicating that the form is an Edit form.
     *
     * @return True, all the time.
     */
    @Override
    protected boolean isEdit() {
        return true;
    }
}
