package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.LotoPrintout;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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

        @SuppressWarnings("unchecked") //I hate those warnings...
        Link link = new Link("downloadLink", rowModel) {
            @Override
            public void onClick() {
                try {
                    String fileName = rowModel.getObject().getPrintoutName() + ".zip";

                    byte[] fileData = lotoReportService.getZipFile(rowModel.getObject());

                    File file = File.createTempFile("temp-file", ".tmp");
                    FileOutputStream fileAttachmentFos = new FileOutputStream(file);
                    fileAttachmentFos.write(fileData);

                    if(this.getPage() instanceof FieldIDFrontEndPage) {
                        ((FieldIDFrontEndPage) this.getPage()).handleDownload(file, fileName);
                    } else {
                        ((FieldIDTemplatePage) this.getPage()).handleDownload(file, fileName);
                    }
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
}

