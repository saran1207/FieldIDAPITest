package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by rrana on 2014-08-15.
 */
public class ObservationReportStylePanel extends Panel {

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    private List<PrintOut> observationPrintOutList;

    private PrintOut selection;
    private RadioGroup<PrintOut> scoreRadioGroup;

    public ObservationReportStylePanel(String id, PrintOut printOut) {
        super(id);

        observationPrintOutList = eventTypeGroupService.findObservationPrintOuts();
        //final List<PrintOut> certs = eventTypeGroupService.findObservationPrintOuts();

        //Create the Empty PrintOut option
        PrintOut emptyOption = new PrintOut();
        emptyOption.setId(null);
        emptyOption.setName("No Thanks, I do not need a PDF Observation Report for this Event Type Group.");
        observationPrintOutList.add(emptyOption);

        if(printOut == null) {
            selection = emptyOption;
        } else {
            selection = printOut;
        }

        scoreRadioGroup = new RadioGroup<PrintOut>("pdfRadioGroup", Model.of(selection));
        scoreRadioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        add(scoreRadioGroup);

        scoreRadioGroup.add(new ListView<PrintOut>("pdfs", observationPrintOutList) {
            @Override
            protected void populateItem(ListItem<PrintOut> item) {

                WebMarkupContainer container = new WebMarkupContainer("imageContainer");

                if (item.getModelObject().isThumbPreviewFileThere()) {
                    container.add(new ExternalImage("image", "/fieldid/file/downloadPrintOutThumb.action?uniqueID=" + item.getModel().getObject().getId()).setVisible(item.getModelObject().isThumbPreviewFileThere()));
                    container.add(new AttributeAppender("class", new Model<String>("printOutThumbnail"), ""));
                } else {
                    container.add(new ExternalImage("image", "/fieldid/images/nopreview.png"));
                    container.add(new AttributeAppender("class", new Model<String>("noImageContainer"), " "));
                }

                item.add(container);

                Radio<PrintOut> radio = new Radio<PrintOut>("score", item.getModel());
                item.add(radio);
                radio.add(new Label("scoreLabel", new PropertyModel<String>(item.getModel(), "name")));

                NonWicketLink link;
                link = new NonWicketLink("downloadLink", "file/downloadPrintOutPreview.action?uniqueID=" + item.getModel().getObject().getId());
                link.setVisible(item.getModelObject().isPrintOutFileThere());
                item.add(link);
            }
        });
    }

    public PrintOut getPrintOut() {
        if(scoreRadioGroup.getModel().getObject() == null || scoreRadioGroup.getModel().getObject().getId() == null) {
            return null;
        } else {
            return scoreRadioGroup.getModel().getObject();
        }
    }

}
