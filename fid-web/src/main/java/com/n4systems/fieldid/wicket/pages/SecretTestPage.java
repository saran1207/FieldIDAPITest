package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.attachment.AttachmentService;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.EventType;
import com.n4systems.model.LotoPrintout;
import com.n4systems.model.LotoPrintoutType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.AssetIndexerService;
import com.n4systems.services.search.SearchResult;
import com.n4systems.util.SearchRecord;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    public SecretTestPage() {
        ProcedureDefinition procedureDefinition = procedureDefinitionService.getProcedureDefinitionById(183L);

        add(new ListView<ProcedureDefinitionImage>("image", procedureDefinition.getImages()) {
            @Override
            protected void populateItem(ListItem<ProcedureDefinitionImage> item) {
                item.add(new ExternalLink("link", s3Service.getProcedureDefinitionImageSvgURL(item.getModelObject()).toString()));
            }
        });

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
        response.renderCSSReference("style/legacy/reset.css");
        response.renderCSSReference("style/legacy/site_wide.css");
        response.renderCSSReference("style/legacy/fieldid.css");
    }

}
