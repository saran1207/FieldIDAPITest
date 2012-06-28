package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;

public class NonWicketIframeLink extends NonWicketLink {

    private Boolean scrolling;
    private Integer width;
    private Integer height;
    private Boolean iframe;
    private String otherOptions;
    

    public NonWicketIframeLink(String id, String path, Boolean scrolling, Integer width, Integer height) {
        this(id, path, scrolling, width, height, true,  null, null);
    }

    public NonWicketIframeLink(String id, String path, Boolean scrolling, Integer width, Integer height, AttributeModifier attributeModifier) {
        this(id, path, scrolling, width, height, true,  null, attributeModifier);
    } 
    
    public NonWicketIframeLink(String id, String path, Boolean scrolling, Integer width, Integer height, Boolean iframe, String otherOptions) {
        this(id, path, scrolling, width, height, iframe, otherOptions, null);
    }

    public NonWicketIframeLink(String id, String path, Boolean scrolling, Integer width, Integer height, Boolean iframe, String otherOptions, AttributeModifier attributeModifier) {
        super(id, path, attributeModifier);
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
