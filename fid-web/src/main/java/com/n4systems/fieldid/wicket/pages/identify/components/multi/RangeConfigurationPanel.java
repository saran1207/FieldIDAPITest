package com.n4systems.fieldid.wicket.pages.identify.components.multi;

import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.MultipleAssetConfiguration;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

public class RangeConfigurationPanel extends Panel {

    public RangeConfigurationPanel(String id, IModel<MultipleAssetConfiguration.RangeConfiguration> rangeConfigModel) {
        super(id);

        add(new TextField<String>("prefix", ProxyModel.of(rangeConfigModel, on(MultipleAssetConfiguration.RangeConfiguration.class).getPrefix())));
        add(new TextField<Integer>("start", ProxyModel.of(rangeConfigModel, on(MultipleAssetConfiguration.RangeConfiguration.class).getStart())));
        add(new TextField<String>("suffix", ProxyModel.of(rangeConfigModel, on(MultipleAssetConfiguration.RangeConfiguration.class).getSuffix())));
    }

}
