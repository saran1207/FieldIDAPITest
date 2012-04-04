package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public abstract class SaveMenu extends Panel {

    private Link saveLink;
    private Link saveAsDownArrow;
    private WebMarkupContainer saveAsMenu;

    public SaveMenu(String id) {
        super(id);
        setRenderBodyOnly(true);

        saveLink = createSaveLink("save");
        add(saveLink);

        saveAsDownArrow = createSaveAsLink("saveAs");
        add(saveAsDownArrow);

        saveAsMenu = new WebMarkupContainer("saveAsMenu");
        saveAsMenu.add(new AttributeAppender("class", new PropertyModel<String>(this, "saveAsCssClass")));
        add(saveAsMenu);

        saveLink.add(new AttributeAppender("class", new PropertyModel<String>(this,"saveLinkCssClass")) );
    }

    protected abstract Link createSaveAsLink(String id);

    protected abstract Link createSaveLink(String id);

    public String getSaveAsCssClass() {
        return saveAsDownArrow.isVisible() ? " " : " hide";
    }

    public String getSaveLinkCssClass() {
        return saveAsDownArrow.isVisible() ? " mattButtonLeft" : " ";
    }
    
}
