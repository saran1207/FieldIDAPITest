package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.IHeaderResponse;

public class NonWicketIframeLink extends NonWicketLink {

    private Boolean scrolling;
    private Integer width;
    private Integer height;
    private Boolean iframe;
    private String otherOptions;
    

    public NonWicketIframeLink(String id, String path, Boolean scrolling, Integer width, Integer height) {
        this(id, path, scrolling, width, height, true,  null);
    }

    public NonWicketIframeLink(String id, String path, Boolean scrolling, Integer width, Integer height, Boolean iframe, String otherOptions) {
        super(id, path);
        this.scrolling = scrolling;
        this.width = width;
        this.height = height;
        this.otherOptions=otherOptions;
        this.iframe = iframe;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        String options = "{";
        if(width != null)
            options+=" width: " + width + ",";
        if(height != null)
            options += " height: " + height + ",";
        if(scrolling)
            options += " scrolling: true,";
        if(iframe)
            options += " iframe: true,";
        if(otherOptions != null)
            options+= " " + otherOptions;
        options+=" }";
        response.renderOnDomReadyJavaScript("$('#"+getLinkMarkupId()+"').colorbox("+options+");");
    }

}
