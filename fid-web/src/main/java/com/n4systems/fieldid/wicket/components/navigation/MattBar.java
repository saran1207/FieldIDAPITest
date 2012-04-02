package com.n4systems.fieldid.wicket.components.navigation;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.FlatLabel;
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

    private Serializable currentState;
    
    
    public MattBar(String id) {
        super(id);

        setOutputMarkupId(true);

        add(links = new ListView<IModel<String>>("links",new PropertyModel<List<IModel<String>>>(this, "linkTitles")) {
            @Override
            protected void populateItem(final ListItem<IModel<String>> item) {
                AjaxLink link = new AjaxLink("link") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        Serializable newState = linkStates.get(item.getIndex());
                        currentState = newState;
                        onEnterState(target, newState);
                        target.add(MattBar.this);
                    }
                };
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
                item.add(link);
            }
        });
    }

    public void addLink(IModel<String> linkTitle, Serializable linkState) {
        linkTitles.add(linkTitle);
        linkStates.add(linkState);
        linkImages.add(null);
    }

    public void addLinkWithImage(IModel<String> linkTitle, Serializable linkState, String url) {
        linkTitles.add(linkTitle);
        linkStates.add(linkState);
        linkImages.add(url);
    }

    public void setCurrentState(Serializable currentState) {
        this.currentState = currentState;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");

        for (Component component : links.visitChildren(AjaxLink.class)) {
            response.renderOnDomReadyJavaScript("jQuery('#"+component.getMarkupId()+"').click(function(e) { jQuery(e.target).addClass('mattButtonPressed'); } );");
        }
    }

    protected void onEnterState(AjaxRequestTarget target, Object state) { }

}
