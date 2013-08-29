package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class WarningsAndInstructionsPanel extends Panel {
    
    private Label warning;
    private Label instructions;
    private ExternalLink cautionLink;

    public WarningsAndInstructionsPanel(String id, IModel<Asset> model) {
        super(id, model);

        AssetType type = model.getObject().getType();
        
        add(warning = new Label("warnings", new PropertyModel<AssetType>(type, "warnings")));
        warning.setVisible(type.getWarnings() != null && !type.getWarnings().isEmpty());

        add(instructions = new Label("instructions", new PropertyModel<AssetType>(type, "instructions")));
        instructions.setVisible(type.getInstructions() != null && !type.getInstructions().isEmpty());

        String cautionUrl = type.getCautionUrl();
        add(cautionLink = new ExternalLink("moreFromTheWebLink", cautionUrl, cautionUrl));
        cautionLink.setVisible(cautionUrl != null && !cautionUrl.isEmpty());
    }
}
