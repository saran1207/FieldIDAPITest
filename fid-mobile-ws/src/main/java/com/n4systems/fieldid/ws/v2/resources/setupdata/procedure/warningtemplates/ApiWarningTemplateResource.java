package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.warningtemplates;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("warningTemplate")
public class ApiWarningTemplateResource extends SetupDataResourceReadOnly<ApiWarningTemplate, WarningTemplate> {

    public ApiWarningTemplateResource() {
        super(WarningTemplate.class, true);
    }

    @Override
    protected ApiWarningTemplate convertEntityToApiModel(WarningTemplate template) {
        ApiWarningTemplate apiTemplate = new ApiWarningTemplate();
        apiTemplate.setSid(template.getId());
        apiTemplate.setActive(true);
        apiTemplate.setModified(template.getModified());
		apiTemplate.setName(template.getName());
		apiTemplate.setWarning(template.getWarning());
        return apiTemplate;
    }
}
