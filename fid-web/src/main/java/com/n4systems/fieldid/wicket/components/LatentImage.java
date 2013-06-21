package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.util.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import static ch.lambdaj.Lambda.on;

public class LatentImage extends WebComponent {

    public static final String NOT_INITIALIZED = "NOT_INITIALIZED";

    private String src=NOT_INITIALIZED;

    public LatentImage(String id) {
        super(id);

        add(new AbstractDefaultAjaxBehavior() {
            @Override public void renderHead(Component component, IHeaderResponse response) {
                if (NOT_INITIALIZED.equals(src)) {
                    StringBuilder javascript = new StringBuilder();
                    javascript.append("wicketAjaxGet('" + getCallbackUrl() + "', function() {}, function() { });\n");
                    response.renderOnLoadJavaScript(javascript.toString());
                }
            }

            @Override protected void respond(AjaxRequestTarget target) {
                updateImage(target);
                target.add(LatentImage.this);
            }
        });

        setOutputMarkupId(true);
    }

    protected void updateImage(AjaxRequestTarget target) {
        src = updateSrc();
    }

    @Override
    public boolean isVisible() {
        return !(NOT_INITIALIZED.equals(src) || StringUtils.isEmpty(src));
    }

    protected String updateSrc() {
        return null;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new AttributeModifier("src", getImageUrl()));
        add(new AttributeModifier("class", getCssClass()));
    }

    private IModel<String> getCssClass() {
        return new Model<String>() {
            @Override public String getObject() {
                return NOT_INITIALIZED.equals(src) || StringUtils.isEmpty(src)? "not-loaded" : "loaded";
            }
        };
    }

    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        checkComponentTag(tag, "img");
    }

    private IModel<String> getImageUrl() {
        return ProxyModel.of(this,on(LatentImage.class).getSrc());
    }

    public String getSrc() {
        return src;
    }

}
