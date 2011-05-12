package com.n4systems.fieldid.wicket.components.navigation;

import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationBar extends Panel {

    public NavigationBar(String id, NavigationItem... items) {
        super(id);

        List<NavigationItem> navItemsList = filterVisibleItems(Arrays.asList(items));
        WebMarkupContainer optionsList = new WebMarkupContainer("optionsList");
        if (items.length == 0) {
            optionsList.add(new AttributeAppender("class", true, new Model<String>("emptyOptions"), " "));
        }

        optionsList.add(new ListView<NavigationItem>("navItems", navItemsList) {
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

                listItem.add(createSelectedAttributeModifier(navItem));
                if (navItem.isOnRight())
                    listItem.add(new SimpleAttributeModifier("class", "add"));

                Link link;
                if (navItem.getNonWicketUrl() != null) {
                    link = createExternalLinkTo("link", navItem.getNonWicketUrl(), navItem.getParameters());
                } else {
                    link = new BookmarkablePageLink<WebPage>("link", navItem.getPageClass(), navItem.getParameters());
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

        add(optionsList);
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

    private Link createExternalLinkTo(String linkId, final String nonWicketUrl, PageParameters parameters) {
        final String queryString = getQueryString(parameters);
        return new Link(linkId){
            @Override
            public void onClick() {
                getRequestCycle().setRequestTarget(new RedirectRequestTarget(nonWicketUrl + queryString));
            }
        };
    }

    private String getQueryString(PageParameters parameters) {
        StringBuffer queryString = new StringBuffer();
        for (String key : parameters.keySet()) {
            if (queryString.length() == 0) {
                queryString.append("?");
            } else {
                queryString.append("&");
            }
            queryString.append(key).append("=").append(parameters.get(key));
        }
        return queryString.toString();
    }

    private SimpleAttributeModifier createSelectedAttributeModifier(final NavigationItem navItem) {
        return new SimpleAttributeModifier("class", "selected") {
            @Override
            public boolean isEnabled(Component component) {
               return component.getPage().getClass() == navItem.getPageClass();
            }
        };
    }

}
