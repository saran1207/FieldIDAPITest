package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.image.IsolationPointImageGallery;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    private @SpringBean PersistenceService persistenceService;

    private WebMarkupContainer selectedDeviceList;
    private WebMarkupContainer selectedLockList;

    private ProcedureDefinition procedureDefinition;

    public SecretTestPage() {

        procedureDefinition=persistenceService.findAll(ProcedureDefinition.class).get(0);

        procedureDefinition.setImages(Lists.<ProcedureDefinitionImage>newArrayList());

        add(new IsolationPointImageGallery("gallery", procedureDefinition, null).withNoDoneButton());
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/site_wide.css");
        response.renderCSSReference("style/fieldid.css");
    }

}
