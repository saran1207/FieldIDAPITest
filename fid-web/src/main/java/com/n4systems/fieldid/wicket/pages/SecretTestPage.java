package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.attachment.AttachmentService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

import java.util.List;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    private @SpringBean PersistenceService persistenceService;
    private @SpringBean AssetFullTextSearchService assetFullTextSearchService;
    private @SpringBean AssetIndexerService assetIndexerService;
    private @SpringBean AttachmentService attachmentService;

    private WebMarkupContainer selectedDeviceList;
    private WebMarkupContainer selectedLockList;

    private SearchRecord mySearch;

    private String text = null;
    private String tenant = "n4";
    private String language = "fr";
    private BaseOrg org;

    private List<SearchResult> docs = Lists.newArrayList();
    private List<EventType> testEntities = Lists.newArrayList();
    private AddressInfo address = new AddressInfo();
    private GoogleMap map;

    public SecretTestPage() {
//        address.setGpsLocation(new GpsLocation(178523L, 12728L));
//        address.setInput("111 Queen St East, Toronto");

        final Form form = new Form("form");
        add(form);
//        form.add(map=new GoogleMap("map").addLocation(43.548548, -96.987305).addLocation(3.548548, -56.987305).addLocation(49.548548, -6.987305));
        final FileUploadField upload = new FileUploadField("upload");
        upload.setOutputMarkupId(true);
        form.add(upload);
        upload.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload fileUpload = upload.getFileUpload();
                String fileName = fileUpload.getClientFileName();
//                S3ImageAttachment bogusImageAttachment = attachmentService.createBogusImageAttachment(getCurrentUser().getTenant(), fileUpload.getClientFileName(), fileUpload.getContentType(), fileUpload.getBytes());
//                attachmentService.save(bogusImageAttachment);
//
//                bogusImageAttachment = attachmentService.find(bogusImageAttachment.getId());
//                System.out.println(attachmentService.getAttachmentUrl(bogusImageAttachment));
//                System.out.println(attachmentService.getAttachmentUrl(bogusImageAttachment, S3ImageAttachmentHandler.MediumFlavour.class));
//                System.out.println(attachmentService.getAttachmentUrl(bogusImageAttachment, S3ImageAttachmentHandler.ThumbnailFlavour.class));



//                LocalFileAttachment fileAttachment = attachmentService.createBogusFileAttachment(getCurrentUser().getTenant(), fileUpload.getClientFileName(), fileUpload.getContentType(), fileUpload.getBytes());
//                attachmentService.save(fileAttachment);

            }
            @Override
            protected void onError(AjaxRequestTarget target) {
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
