package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import java.util.List;

/**
 * A widget that takes a descriptor list and generates corresponding links in a Panel component.
 * Intended to be used where the number of links to create is only known at run time and thus is
 * best created by application code.
 *
 */
public class WicketLinkGeneratorComponent extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {

    private List<WicketLinkGeneratorDescriptor> descriptorList;

    public WicketLinkGeneratorComponent(String id, List<WicketLinkGeneratorDescriptor> descriptorList) {
        super(id);
        this.descriptorList = descriptorList;
        createComponents();
    }

    private void createComponents() {

        int i = 0;
        for (WicketLinkGeneratorDescriptor descriptor: descriptorList) {
            if (descriptor.getLinkOnClickHandler() instanceof WicketAjaxLinkGeneratorClickHandler) {
                AjaxLink link = new AjaxLink("link" + i) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        ((WicketAjaxLinkGeneratorClickHandler)descriptor.getLinkOnClickHandler()).onClick(target);
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
            if (descriptor.getLinkUrl() != null && descriptor.getLinkOnClickHandler() == null) {
                String absolutePath = ContextAbsolutizer.toContextAbsoluteUrl(descriptor.getLinkUrl());
                markup+="<a href=\"" + absolutePath + "\"";
                if (descriptor.getJsOnClickContent() != null) {
                    markup += " onclick=\"" + descriptor.getJsOnClickContent() + "\"";
                }
                markup+= ">" + (descriptor.getLabel() != null ? descriptor.getLabel() : "") + "</a>";
            }
            else
            if (descriptor.getLinkOnClickHandler() != null && descriptor.getLinkUrl() == null) {
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
