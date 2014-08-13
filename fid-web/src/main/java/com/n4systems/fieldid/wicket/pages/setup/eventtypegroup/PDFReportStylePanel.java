package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.PrintOut;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by rrana on 2014-08-08.
 */
public class PDFReportStylePanel extends Panel {

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    public PDFReportStylePanel(String id, final IModel<EventTypeGroup> eventTypeGroupModel, PrintOut.PrintOutType type) {
        super(id);

        if (type == PrintOut.PrintOutType.CERT) {
            final List<PrintOut> certs = eventTypeGroupService.findCertPrintOuts();

            //Create the Empty PrintOut option

            PrintOut emptyOption = new PrintOut();
            emptyOption.setName("No Thanks, I do not need a PDF Report for this Event Type Group.");
            certs.add(emptyOption);
            if(eventTypeGroupModel.getObject().getPrintOut() == null) {
                eventTypeGroupModel.getObject().setPrintOut(emptyOption);
            }

            RadioGroup<PrintOut> scoreRadioGroup = new RadioGroup<PrintOut>("pdfRadioGroup", new PropertyModel<PrintOut>(eventTypeGroupModel, "printOut"));
            scoreRadioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                }
            });
            add(scoreRadioGroup);

            scoreRadioGroup.add(new ListView<PrintOut>("pdfs", certs) {
                @Override
                protected void populateItem(ListItem<PrintOut> item) {

                    WebMarkupContainer container = new WebMarkupContainer("imageContainer");

                    //File file = new File(eventTypeGroupService.getUser(FieldIDSession.get().getSessionUser().getId()).getPrivateDir(), item.getModel().getObject().getThumbNailImage());
                    container.add(new ExternalImage("image", "/fieldid/file/downloadPrintOutThumb.action?uniqueID=" + item.getModel().getObject().getId()).setVisible(item.getModelObject().isThumbPreviewFileThere()));

                    if (item.getModelObject().isThumbPreviewFileThere()) {
                        container.add(new AttributeAppender("class", new Model<String>("printOutThumbnail"), ""));
                    } else {
                        container.add(new AttributeAppender("class", new Model<String>("noImageContainer"), " "));
                        container.add(new AttributeAppender("class", new Model<String>("noImage"), " "));
                    }

                    item.add(container);

                    Radio<PrintOut> radio = new Radio<PrintOut>("score", item.getModel());
                    item.add(radio);
                    radio.add(new Label("scoreLabel", new PropertyModel<String>(item.getModel(), "name")));

                    NonWicketLink link;
                    link = new NonWicketLink("downloadLink", "file/downloadPrintOutPreview.action?uniqueID=" + item.getModel().getObject().getId());
                    link.setVisible(item.getModelObject().isPrintOutFileThere());
                    //link.add(new Label("name", "label.preview"));
                    item.add(link);
                }
            });

        } else {
            final List<PrintOut> certs = eventTypeGroupService.findObservationPrintOuts();

            //Create the Empty PrintOut option
            PrintOut emptyOption = new PrintOut();
            emptyOption.setName("No Thanks, I do not need a PDF Observation Report for this Event Type Group.");
            certs.add(emptyOption);
            if(eventTypeGroupModel.getObject().getObservationPrintOut() == null) {
                eventTypeGroupModel.getObject().setObservationPrintOut(emptyOption);
            }

            RadioGroup<PrintOut> scoreRadioGroup = new RadioGroup<PrintOut>("pdfRadioGroup", new PropertyModel<PrintOut>(eventTypeGroupModel, "observationPrintOut"));
            scoreRadioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                }
            });
            add(scoreRadioGroup);

            scoreRadioGroup.add(new ListView<PrintOut>("pdfs", certs) {
                @Override
                protected void populateItem(ListItem<PrintOut> item) {

                    WebMarkupContainer container = new WebMarkupContainer("imageContainer");

                    //File file = new File(eventTypeGroupService.getUser(FieldIDSession.get().getSessionUser().getId()).getPrivateDir(), item.getModel().getObject().getThumbNailImage());
                    container.add(new ExternalImage("image", "/fieldid/file/downloadPrintOutThumb.action?uniqueID=" + item.getModel().getObject().getId()).setVisible(item.getModelObject().isThumbPreviewFileThere()));

                    if (item.getModelObject().isThumbPreviewFileThere()) {
                        container.add(new AttributeAppender("class", new Model<String>("printOutThumbnail"), ""));
                    } else {
                        container.add(new AttributeAppender("class", new Model<String>("noImageContainer"), " "));
                        container.add(new AttributeAppender("class", new Model<String>("noImage"), " "));
                    }

                    item.add(container);

                    Radio<PrintOut> radio = new Radio<PrintOut>("score", item.getModel());
                    item.add(radio);
                    radio.add(new Label("scoreLabel", new PropertyModel<String>(item.getModel(), "name")));

                    NonWicketLink link;
                    link = new NonWicketLink("downloadLink", "file/downloadPrintOutPreview.action?uniqueID=" + item.getModel().getObject().getId());
                    link.setVisible(item.getModelObject().isPrintOutFileThere());
                    //link.add(new Label("name", "label.preview"));
                    item.add(link);
                }
            });

        }
    }

}