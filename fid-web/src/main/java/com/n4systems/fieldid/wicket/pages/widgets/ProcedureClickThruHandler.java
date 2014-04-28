package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunProceduresPage;
import org.apache.wicket.Component;

public class ProcedureClickThruHandler extends SimpleClickThruHandler{

    public ProcedureClickThruHandler(Component component, Long widgetDefinitionId) {
        super(component, widgetDefinitionId);
    }

    @Override
    protected Class<? extends FieldIDFrontEndPage> getClickThruPage() {
        return RunProceduresPage.class;
    }
}
