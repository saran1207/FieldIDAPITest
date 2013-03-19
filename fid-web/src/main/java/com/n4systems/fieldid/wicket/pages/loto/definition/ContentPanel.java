package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.image.EditableImageList;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ContentPanel extends Panel {

    private @SpringBean PersistenceService persistenceService;

    private List<EditableImage> images;
    List<ProcedureDefinition> procedures = Lists.newArrayList();

    public ContentPanel(String id) {
        super(id);

        // for debugging only...
       // images = persistenceService.findAll(EditableImage.class);
        //...............

        setOutputMarkupPlaceholderTag(true);
        add(new EditableImageList("images", new ImageModel() ));

        //add(new ProcedureList("procedures", new PropertyModel(this,"procedures")));

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
//            return Lists.newArrayList();
            List<EditableImage> all = persistenceService.findAll(EditableImage.class);
            return all;
        }
    }

    class ProcedureList extends ListView<ProcedureDefinition> {

        public ProcedureList(String id, IModel<List<? extends ProcedureDefinition>> model) {
            super(id, model);
        }

        @Override
        protected void populateItem(ListItem<ProcedureDefinition> item) {

        }
    }

}
