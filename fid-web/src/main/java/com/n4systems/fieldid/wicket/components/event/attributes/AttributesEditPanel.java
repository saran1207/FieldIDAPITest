package com.n4systems.fieldid.wicket.components.event.attributes;

import com.n4systems.model.AbstractEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class AttributesEditPanel extends Panel {

    public AttributesEditPanel(String id, final IModel<AbstractEvent> eventModel) {
        super(id);
        
        add(new ListView<String>("attributes", new PropertyModel<List<? extends String>>(eventModel, "type.infoFieldNames")) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("attributeName", item.getModel()));
                item.add(new TextField<String>("attributeValue", new PropertyModel<String>(eventModel, "infoOptionMap["+item.getModelObject()+"]")));
            }
        });
    }

}
