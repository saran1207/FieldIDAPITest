package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import static ch.lambdaj.Lambda.on;


/**
 * This is the markup class for a panel which shows information on a "Note" isolation point in the
 * LOTO Procedure Editor.
 *
 * Created by jheath on 30/06/2014.
 */
public class NoteListItem extends Panel {
    public NoteListItem(String id, IModel<IsolationPoint> model) {
        super(id, model);
        final IsolationPoint isolationPoint = model.getObject();

        add(new Label("notes", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getMethod())));
        add(new WebMarkupContainer("label").add(new AttributeAppender("class", getLabelCss(model)," ")));
    }

    private IModel<String> getLabelCss(final IModel<IsolationPoint> model) {
        return new Model<String>() {
            @Override public String getObject() {
                return model.getObject().getAnnotation()!=null ? "labelled" : "unlabelled";
            }
        };
    }
}
