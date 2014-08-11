package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.comment.CommentService;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * This is the data provider for the table in the Comment Templates setup page.
 *
 * Created by Jordan Heath on 08/08/14.
 */
public class CommentTemplateDataProvider extends FieldIDDataProvider<CommentTemplate> {

    @SpringBean
    private CommentService commentService;

    private List<CommentTemplate> results;

    private Long size;

    public CommentTemplateDataProvider(String order,
                                       SortOrder sortOrder) {
        setSort(order, sortOrder);
    }

    @Override
    public Iterator<CommentTemplate> iterator(int first,
                                              int count) {

        List<CommentTemplate> commentTemplateList = commentService.getPagedCommentTemplatesList(getSort().getProperty(),
                                                                                                getSort().isAscending(),
                                                                                                first,
                                                                                                count);

        return commentTemplateList.iterator();
    }

    @Override
    public int size() {
        return commentService.getCommentTemplateCount().intValue();
    }

    @Override
    public IModel<CommentTemplate> model(final CommentTemplate commentTemplate) {
        return new AbstractReadOnlyModel<CommentTemplate>() {
            @Override
            public CommentTemplate getObject() {
                return commentTemplate;
            }
        };
    }

    @Override
    public void detach() {
        results = null;
        size = null;
    }


}
