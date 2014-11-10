package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.attachment.AttachmentService;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.EventType;
import com.n4systems.model.LotoPrintout;
import com.n4systems.model.LotoPrintoutType;
import com.n4systems.model.orgs.BaseOrg;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    private @SpringBean LotoReportService lotoReportService;
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
                try {
                    LotoPrintout lotoPrintOut = new LotoPrintout();
                    lotoPrintOut.setPrintoutName("Ruppi's Test Default Short");
                    lotoPrintOut.setPrintoutType(LotoPrintoutType.LONG);
                    lotoPrintOut.setTenant(FieldIDSession.get().getTenant());
                    lotoPrintOut.setCreatedBy(getCurrentUser());
                    lotoPrintOut.setCreated(new Date());



                    File file = fileUpload.writeToTempFile();
                    String fileName = fileUpload.getClientFileName();

                    unZipIt(file.getAbsolutePath(), lotoPrintOut);

                    lotoReportService.saveLotoReport(file, lotoPrintOut);

                } catch (IOException e) {
                    e.printStackTrace();
                }

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



    public void unZipIt(String zipFile, LotoPrintout lotoPrintout) throws IOException{

        boolean created;

        byte[] buffer = new byte[1024];

        //try{
            //create output directory is not exists
            File folder = PathHandler.getAbsoluteLotoDefaultPath(lotoPrintout);
            if(!folder.exists()){
                created = folder.mkdirs();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(folder + File.separator + fileName);

                System.out.println("file unzip : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

        //} catch(IOException ex){
        //    ex.printStackTrace();
        //}
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
