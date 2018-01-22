package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import java.util.List;

/**
 * A widget that takes a descriptor list and generates a series of links.
 */
public class WicketLinkGeneratorWidget extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {

    private List<WicketLinkGeneratorDescriptor> descriptorList;

    public WicketLinkGeneratorWidget(String id, List<WicketLinkGeneratorDescriptor> descriptorList) {
        super(id);
        this.descriptorList = descriptorList;
        createComponents();
    }

    private void createComponents() {

        int i = 0;
        for (WicketLinkGeneratorDescriptor descriptor: descriptorList) {
            if (descriptor.getLinkOnClickHandler() != null) {
                Link link = new Link("link" + i) {
                    @Override
                    public void onClick() {
                        descriptor.getLinkOnClickHandler().onClick();
                    }
                };
                add(link);
                if (descriptor.getJsOnClickContent() != null) {
                    link.add(new AttributeModifier("onclick", descriptor.getJsOnClickContent()));
                }
            }
            i++;
        }
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container,
                                                   Class<?> containerClass) {
        String markup = "<wicket:panel><div>";
        int i = 0;
        for (WicketLinkGeneratorDescriptor descriptor: descriptorList) {
            if (descriptor.getLinkUrl() != null) {
                //System.out.println("Generating markup for link 'link" + i + "'");
                String absolutePath = ContextAbsolutizer.toContextAbsoluteUrl(descriptor.getLinkUrl());
                markup+="<a href=\"" + absolutePath + "\"";
                if (descriptor.getJsOnClickContent() != null) {
                    //System.out.println(" has onClick content " + descriptor.getJsOnClickContent());
                    markup += " onclick=\"" + descriptor.getJsOnClickContent() + "\"";
                }
                markup+= ">" + (descriptor.getLabel() != null ? descriptor.getLabel() : "") + "</a>";
            }
            else
            if (descriptor.getLinkOnClickHandler() != null) {
                markup+="<a wicket:id=\"link" + i + "\">" +
                        (descriptor.getLabel() != null ? descriptor.getLabel() : "") + "</a>";
            }
            else
            if (descriptor.getLabel() != null) {
                markup+=descriptor.getLabel();
            }
            i++;
        }
        markup+="</div></wicket:panel>";
        //System.out.println("Writing markup '" + markup + "'");
        StringResourceStream resourceStream = new StringResourceStream(markup);

        return resourceStream;
    }

    /**
     * Avoid markup caching for this component
     */
    @Override
    public String getCacheKey(MarkupContainer arg0, Class<?> arg1) {
        return null;
    }

}
