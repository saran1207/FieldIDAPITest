package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class PrintIdentifiedCertPage extends FieldIDFrontEndPage {

    public PrintIdentifiedCertPage(Long assetId) {
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("/file/downloadManufacturerCert.action?uniqueID="+assetId);
        add(new WebMarkupContainer("downloadCertIframe").add(new AttributeModifier("src", linkUrl)));
        add(new NonWicketLink("downloadLink", "file/downloaddManufacturerCert.action?uniqueID="+assetId));
    }

}
