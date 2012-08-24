package com.n4systems.fieldid.wicket.components.navigation;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.List;

public class MattBar extends Panel {

    private ListView<IModel<String>> links;
    private List<IModel<String>> linkTitles = Lists.newArrayList();
    private List<Serializable> linkStates = Lists.newArrayList();
    private List<String> linkImages = Lists.newArrayList();
    private List<String> linkTooltips = Lists.newArrayList();
    
    private Serializable currentState;


    public MattBar(String id) {
        super(id);

        setOutputMarkupId(true);

        add(links = new ListView<IModel<String>>("links",new PropertyModel<List<IModel<String>>>(this, "linkTitles")) {
            @Override
            protected void populateItem(final ListItem<IModel<String>> item) {
                AjaxLink link = createLink(item);
                link.setOutputMarkupId(true);

                item.setRenderBodyOnly(true);
                
                if (item.getIndex() == 0) {
                    link.add(new AttributeAppender("class", new Model<String>("mattButtonLeft"), " "));
                } else if (item.getIndex() == linkStates.size() - 1) {
                    link.add(new AttributeAppender("class", new Model<String>("mattButtonRight"), " "));
                } else {
                    link.add(new AttributeAppender("class", new Model<String>("mattButtonMiddle"), " "));
                }
                

                link.add(new AttributeAppender("class", new Model<String>("mattButtonPressed"), " ") {
                    @Override
                    public boolean isEnabled(Component component) {
                        return currentState != null && currentState.equals(linkStates.get(item.getIndex()));
                    }
                });

                link.add(new FlatLabel("linkLabel", item.getModelObject()));

                String url = linkImages.get(item.getIndex());
                if (url!=null) {
                    link.add(new ContextImage("img", url));
                } else {
                    link.add(new WebComponent("img").setVisible(false));
                }
                link.add(new AttributeAppender("class", new Model<String>("tipsy-tooltip"), " "));
                item.add(link);
                String tooltipKey = linkTooltips.get(item.getIndex());
                link.add(new AttributeModifier("title", tooltipKey == null ? "" : getString(tooltipKey)));
            }
        });
    }

    protected AjaxLink createLink(final ListItem<IModel<String>> item) {
        return new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Serializable newState = linkStates.get(item.getIndex());
                currentState = newState;
                onEnterState(target, newState);
                target.add(MattBar.this);
            }
        };
    }

    public void addLink(IModel<String> linkTitle, Serializable linkState) {
        linkTitles.add(linkTitle);
        linkStates.add(linkState);
        linkImages.add(null);
        linkTooltips.add(null);
    }


    public void addLink(IModel<String> linkTitle, Serializable linkState, String url, String tooltip) {
        linkTitles.add(linkTitle);
        linkStates.add(linkState);
        linkImages.add(url);
        linkTooltips.add(tooltip);
    }


    public MattBar setCurrentState(Serializable currentState) {
        this.currentState = currentState;
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");

        for (Component component : links.visitChildren(AjaxLink.class)) {
            response.renderOnDomReadyJavaScript("jQuery('#"+component.getMarkupId()+"').click(function(e) { jQuery(e.target).addClass('mattButtonPressed'); } );");
        }

        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        // CAVEAT : https://github.com/jaz303/tipsy/issues/19
        // after ajax call, tipsy tooltips will remain around so need to remove them explicitly.
        response.renderOnDomReadyJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150, live:true})");
    }

    protected void onEnterState(AjaxRequestTarget target, Object state) { }

    

}
