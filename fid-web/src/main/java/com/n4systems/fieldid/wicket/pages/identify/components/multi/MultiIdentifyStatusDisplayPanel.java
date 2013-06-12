package com.n4systems.fieldid.wicket.pages.identify.components.multi;

import com.n4systems.model.MultipleAssetConfiguration;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class MultiIdentifyStatusDisplayPanel extends Panel {

    private static final int MAX_IDENTIFIERS_BEFORE_ELLIPSIS = 3;
    private static final String IDENTIFIER_SEPARATOR = ", ";

    public MultiIdentifyStatusDisplayPanel(String id, IModel<MultipleAssetConfiguration> config, final ModalWindow modalWindow) {
        super(id);
        setOutputMarkupPlaceholderTag(true);

        add(new TextField<String>("identifiersList", new MultipleAssetIdentifiersModel(config)));
        add(new AjaxLink("configureLink"){
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.show(target);
            }
        });
    }

    static class MultipleAssetIdentifiersModel extends LoadableDetachableModel<String> {

        private IModel<MultipleAssetConfiguration> config;

        public MultipleAssetIdentifiersModel(IModel<MultipleAssetConfiguration> config) {
            this.config = config;
        }

        @Override
        protected String load() {
            StringBuffer buffer = new StringBuffer();

            int numIdentifiersDisplayed = 0;
            boolean trimmed = false;
            for (MultipleAssetConfiguration.AssetConfiguration assetConfiguration : config.getObject().getAssetConfigs()) {
                buffer.append(assetConfiguration.getIdentifier()).append(IDENTIFIER_SEPARATOR);
                numIdentifiersDisplayed++;
                if (numIdentifiersDisplayed == MAX_IDENTIFIERS_BEFORE_ELLIPSIS) {
                    buffer.delete(buffer.length() - IDENTIFIER_SEPARATOR.length(), buffer.length());
                    trimmed = true;
                    if (numIdentifiersDisplayed < config.getObject().getAssetConfigs().size()) {
                        buffer.append(" ...");
                    }
                }
            }

            if (!trimmed) {
                buffer.delete(buffer.length() - IDENTIFIER_SEPARATOR.length(), buffer.length());
            }

            return buffer.toString();
        }

    }

}
