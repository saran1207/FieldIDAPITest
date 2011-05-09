package com.n4systems.fieldid.wicket.components.navigation;

import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationBar extends Panel {

    public NavigationBar(String id, NavigationItem... items) {
        super(id);

        List<NavigationItem> navItemsList = filterVisibleItems(Arrays.asList(items));

        add(new ListView<NavigationItem>("navItems", navItemsList) {
            @Override
            protected void populateItem(ListItem<NavigationItem> listItem) {
                final NavigationItem navItem = listItem.getModelObject();

                WebMarkupContainer linkContainer = new WebMarkupContainer("linkContainer") {
                    @Override
                    public boolean isVisible() {
                        return getPage().getClass() != navItem.getPageClass();
                    }
                };
                linkContainer.setRenderBodyOnly(true);

                listItem.add(createAttributeModifier(navItem));
                Link link;
                if (navItem.getNonWicketUrl() != null) {
                    link = createExternalLinkTo("link", navItem.getNonWicketUrl());
                } else {
                    link = new BookmarkablePageLink<WebPage>("link", navItem.getPageClass());
                }

                linkContainer.add(link);
                link.add(new Label("linkLabel", navItem.getLabelModel()));

                listItem.add(linkContainer);
                listItem.add(new Label("currentPageLabel", navItem.getLabelModel()) {
                    @Override
                    public boolean isVisible() {
                        return getPage().getClass() == navItem.getPageClass();
                    }
                });
            }
        });
    }

    private List<NavigationItem> filterVisibleItems(List<NavigationItem> navigationItems) {
        List<NavigationItem> visibleItems = new ArrayList<NavigationItem>(navigationItems.size());
        for (NavigationItem item : navigationItems) {
            if (item.display()) {
                visibleItems.add(item);
            }
        }
        return visibleItems;
    }

    private Link createExternalLinkTo(String linkId, final String nonWicketUrl) {
        return new Link(linkId){
            @Override
            public void onClick() {
                getRequestCycle().setRequestTarget(new RedirectRequestTarget(nonWicketUrl));
            }
        };
    }

    private SimpleAttributeModifier createAttributeModifier(final NavigationItem navItem) {
        return new SimpleAttributeModifier("class", "selected") {
            @Override
            public boolean isEnabled(Component component) {
               return component.getPage().getClass() == navItem.getPageClass();
            }
        };
    }

}
