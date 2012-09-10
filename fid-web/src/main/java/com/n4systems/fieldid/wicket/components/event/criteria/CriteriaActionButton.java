package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public abstract class CriteriaActionButton extends Panel {
    private String image;

    public CriteriaActionButton(String id, String image, Integer count, String label, String style) {
		super(id);
        setRenderBodyOnly(true);
        this.image = image;
        setOutputMarkupId(true);

        AjaxLink link;
        add(link = new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                CriteriaActionButton.this.onClick(target);
            }
        });
        link.add(new AttributeAppender("class", Model.of(style), " "));
        link.add(new ContextImage("icon", new PropertyModel<String>(this, "image")));
        if (count!=null && count>0) {
            link.add(new Label("count",Model.of(count+"")));
        } else {
            link.add(new WebMarkupContainer("count").setVisible(false));
        }
        String tooltip = getTooltip(label, count);
        if (tooltip!=null) {
            link.add(new TipsyBehavior(tooltip, TipsyBehavior.Gravity.N));
        }

    }

    protected String getTooltip(String label, Integer count) {
        if (count==null) {
            return new FIDLabelModel(label).getObject() ;
        } else {
            return count + " " + new FIDLabelModel(label).getObject();
        }
    }

    protected abstract void onClick(AjaxRequestTarget target);

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }
}
