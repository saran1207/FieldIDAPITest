package com.n4systems.fieldid.wicket.pages.customers;


import java.io.Serializable;

/**
 * Describes a link to be rendered by the widget. If the linkUrl is null then the label
 * value will be written out as is without any href. This can be used to place arbitrary
 * text beside a link.
 */
public class WicketLinkGeneratorDescriptor implements Serializable {

    private String linkUrl;
    private WicketLinkGeneratorClickHandler linkOnClickHandler;
    private String label;
    private String jsOnClickContent;

    /**
     * Values to be used to generate a link. If any value is not applicable set it to null.
     * @param linkUrl a url link to be placed in the generated html (can't specify both this and linkOnClickHandler)
     * @param linkOnClickHandler a callback to be called when the link is clicked (can't specify both this and linkUrl)
     * @param label the link label text. If linkUrl and linkOnClickHandler are not specified this is rendered as text
     * @param jsOnClickContent javascript to be placed in link's onClick handler - useful for confirm dialogs
     */
    public WicketLinkGeneratorDescriptor(String linkUrl, WicketLinkGeneratorClickHandler linkOnClickHandler, String label, String jsOnClickContent) {
        this.linkUrl = linkUrl;
        this.linkOnClickHandler = linkOnClickHandler;
        this.label = label;
        this.jsOnClickContent = jsOnClickContent;
    }

    public String getJsOnClickContent() {
        return jsOnClickContent;
    }

    public void setJsOnClickContent(String jsOnClickContent) {
        this.jsOnClickContent = jsOnClickContent;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public WicketLinkGeneratorClickHandler getLinkOnClickHandler() {
        return linkOnClickHandler;
    }

    public void setLinkOnClickHandler(WicketLinkGeneratorClickHandler linkOnClickHandler) {
        this.linkOnClickHandler = linkOnClickHandler;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
