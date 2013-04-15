package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.image.EditableImageGallery;
import com.n4systems.model.Tenant;
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

        add(new EditableImageGallery<ProcedureDefinitionImage>("gallery", persistenceService.findAll(ProcedureDefinitionImage.class)) {
            @Override protected ProcedureDefinitionImage createImage(S3Service.S3ImagePath path, Tenant tenant) {
                ProcedureDefinitionImage image = new ProcedureDefinitionImage();
                image.setTenant(tenant);
                image.setFileName(path.getOrigPath());
                image.setProcedureDefinition(procedureDefinition);
                return image;
            }
        });
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
