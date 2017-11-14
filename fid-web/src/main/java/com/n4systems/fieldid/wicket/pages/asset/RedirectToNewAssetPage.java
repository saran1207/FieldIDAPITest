package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.xalan.lib.Redirect;

/**
 * Created by agrabovskis on 2017-10-31.
 */
public class RedirectToNewAssetPage extends Panel {

    public RedirectToNewAssetPage(String id) {
        super(id);
        RequestCycle requestCycle= RequestCycle.get();
        requestCycle.setResponsePage(IdentifyOrEditAssetPage.class);

    }
}
