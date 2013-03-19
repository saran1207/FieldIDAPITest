package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.image.EditableImageList;
import com.n4systems.model.common.EditableImage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ContentPanel extends Panel {

    private @SpringBean PersistenceService persistenceService;

    private List<EditableImage> images;

    public ContentPanel(String id) {
        super(id);

        // for debugging only....
       // images = persistenceService.findAll(EditableImage.class);
        //...............

        setOutputMarkupPlaceholderTag(true);
        add(new EditableImageList("images", new ImageModel() ));
        add(new AjaxLink("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });
        add(new AjaxLink("continue") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doContinue(target);
            }
        });

    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }


    class ImageModel extends LoadableDetachableModel<List<EditableImage>> {
        @Override
        protected List<EditableImage> load() {
            List<EditableImage> all = persistenceService.findAll(EditableImage.class);
            return all;
        }
    }

}
