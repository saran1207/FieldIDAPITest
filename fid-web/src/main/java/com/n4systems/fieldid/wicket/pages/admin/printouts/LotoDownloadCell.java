package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.LotoPrintout;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.UrlEncoder;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rrana on 2014-11-10.
 */
public class LotoDownloadCell extends Panel {

    private @SpringBean
    LotoReportService lotoReportService;

    public LotoDownloadCell(String id, IModel<? extends LotoPrintout> rowModel) {
        super(id);

        Link link = new Link("downloadLink", rowModel) {
            @Override
            public void onClick() {
                try {
                    String fileName = rowModel.getObject().getPrintoutName() + ".zip";

                    byte[] fileData = lotoReportService.getZipFile(rowModel.getObject());

                    File file = File.createTempFile("temp-file", ".tmp");
                    FileOutputStream fileAttachmentFos = new FileOutputStream(file);
                    fileAttachmentFos.write(fileData);

                    handleDownload(file, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        FlatLabel label = new FlatLabel("name", new FIDLabelModel("label.download"));
        label.setEnabled(true);
        label.setVisible(true);
        link.add(label);

        add(link);
    }

    private void handleDownload(File tempReport, String fileName) {
        String encodedFileName = UrlEncoder.QUERY_INSTANCE.encode(fileName, getRequest().getCharset());

        IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(tempReport));

        Duration cacheDuration = Duration.hours(2);
        //Duration cacheDuration = Duration.minutes(5);

        getRequestCycle().scheduleRequestHandlerAfterCurrent(
                new ResourceStreamRequestHandler(resourceStream) {
                    @Override
                    public void respond(IRequestCycle requestCycle) {
                        super.respond(requestCycle);
                    }
                }.setFileName(encodedFileName).setContentDisposition(ContentDisposition.ATTACHMENT).setCacheDuration(cacheDuration)
        );
    }
}

