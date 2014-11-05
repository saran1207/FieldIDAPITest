package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.LotoPrintout;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class PrintoutTemplatePage extends FieldIDTemplatePage {

    private @SpringBean
    LotoReportService lotoReportService;

    FidDropDownChoice<LotoPrintout> longDropDown;
    FidDropDownChoice<LotoPrintout> shortDropDown;

    private LotoPrintout longFormPrintout;
    private LotoPrintout shortFormPrintout;

    List<LotoPrintout> longPrintouts;
    List<LotoPrintout> shortPrintouts;

    public PrintoutTemplatePage() {

        longPrintouts = generateLongFormPrintouts();
        shortPrintouts = generateShortFormPrintouts();
/*
        longFormPrintout = lotoReportService.getSelectedLongForm();
        shortFormPrintout = lotoReportService.getSelectedShortForm();


        if(longFormPrintout == null) {
            longFormPrintout = new LotoPrintout();
            longFormPrintout.setPrintoutName("Long Form - Default");
            longFormPrintout.setId(null);
            longFormPrintout.setSelected(true);
            longFormPrintout.setPrintoutType(LotoPrintoutType.LONG);
        }

        if(shortFormPrintout == null) {
            shortFormPrintout = new LotoPrintout();
            shortFormPrintout.setPrintoutName("Short Form - Default");
            shortFormPrintout.setId(null);
            shortFormPrintout.setSelected(true);
            shortFormPrintout.setPrintoutType(LotoPrintoutType.SHORT);
        }
*/

        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                //longDropDown.getModel().getObject().getPrintoutName();

                //Only update the database if they've selected something other than the DEFAULT.
               if(longFormPrintout.getId() != null) {
                   longFormPrintout.setSelected(true);
                   lotoReportService.updateSelectedLongForm(longFormPrintout);
               } else {
                   lotoReportService.resetSelectedLongForm();
               }

               if(shortFormPrintout.getId() != null) {
                   shortFormPrintout.setSelected(true);
                   lotoReportService.updateSelectedShortForm(shortFormPrintout);
               } else {
                   lotoReportService.resetSelectedShortForm();
               }

               info("Your changes have been updated!");
               setResponsePage(new PrintoutTemplatePage());
            }
        };

        /*FidDropDownChoice<LotoPrintout> */
        longDropDown = new FidDropDownChoice<>("longForm",
                new PropertyModel<LotoPrintout>(this, "longFormPrintout"),
                getLongFormPrintouts(),
                new ListableChoiceRenderer<LotoPrintout>());

        longDropDown.setNullValid(true).setRequired(true).setOutputMarkupId(true);
        longDropDown.setDefaultModelObject(longFormPrintout);

        form.add(longDropDown);

        /*form.add(new FidDropDownChoice<LotoPrintout>("longForm",
                                        new PropertyModel<>(this, "longFormPrintout"),
                                        getLongFormPrintouts(),
                                        new ListableChoiceRenderer<LotoPrintout>())
                );*/

        /*FidDropDownChoice<LotoPrintout> */
        shortDropDown = new FidDropDownChoice<>("shortForm",
                new PropertyModel<LotoPrintout>(this, "shortFormPrintout"),
                getShortFormPrintouts(),
                new ListableChoiceRenderer<LotoPrintout>());

        shortDropDown.setNullValid(true).setRequired(true).setOutputMarkupId(true);
        shortDropDown.setDefaultModelObject(shortFormPrintout);

        form.add(shortDropDown);

        /*form.add(new FidDropDownChoice<LotoPrintout>("shortForm",
                                        new PropertyModel<>(this, "shortFormPrintout"),
                                        getShortFormPrintouts(),
                                        new ListableChoiceRenderer<LotoPrintout>())
                );*/

        form.add(new SubmitLink("saveLink"));
        form.add(new BookmarkablePageLink<SettingsPage>("cancelLink", SettingsPage.class));
        add(form);

    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.printout_template"));
    }


    private LoadableDetachableModel<List<LotoPrintout>> getLongFormPrintouts() {
        return new LoadableDetachableModel<List<LotoPrintout>>() {
            @Override
            protected List<LotoPrintout> load() {

                return longPrintouts;
                /*List<LotoPrintout> printouts =  lotoReportService.getLongLotoPrintouts();
                for(LotoPrintout p:printouts) {
                    if(p.isSelected()) {
                        longFormPrintout = p;
                    }
                }

                if(longFormPrintout == null) {
                    longFormPrintout = printouts.get(printouts.size()-1);
                }
                //printouts.add(new LotoPrintout("Default Long Form"));
                //printouts.add(new LotoPrintout("Long Form 2"));
                //printouts.add(new LotoPrintout("Long Form 3"));
                return printouts;*/
            }
        };
    }

    private LoadableDetachableModel<List<LotoPrintout>> getShortFormPrintouts() {
        return new LoadableDetachableModel<List<LotoPrintout>>() {
            @Override
            protected List<LotoPrintout> load() {

                return shortPrintouts;
                /*List<LotoPrintout> printouts =  lotoReportService.getShortLotoPrintouts();

                for(LotoPrintout p:printouts) {
                    if(p.isSelected()) {
                        shortFormPrintout = p;
                    }
                }

                if(shortFormPrintout == null) {
                    shortFormPrintout = printouts.get(printouts.size()-1);
                }

                //printouts.add(new LotoPrintout("Default Short Form"));
                //printouts.add(new LotoPrintout("Short Form 2"));
                //printouts.add(new LotoPrintout("Short Form 3"));
                return printouts;*/
            }
        };
    }

    private List<LotoPrintout> generateLongFormPrintouts(){
        List<LotoPrintout> printouts =  lotoReportService.getLongLotoPrintouts();
        for(LotoPrintout p:printouts) {
            if(p.isSelected()) {
                longFormPrintout = p;
            }
        }

        if(longFormPrintout == null) {
            longFormPrintout = printouts.get(printouts.size()-1);
        }
        //printouts.add(new LotoPrintout("Default Long Form"));
        //printouts.add(new LotoPrintout("Long Form 2"));
        //printouts.add(new LotoPrintout("Long Form 3"));
        return printouts;
    }

    private List<LotoPrintout> generateShortFormPrintouts(){
        List<LotoPrintout> printouts =  lotoReportService.getShortLotoPrintouts();

        for(LotoPrintout p:printouts) {
            if(p.isSelected()) {
                shortFormPrintout = p;
            }
        }

        if(shortFormPrintout == null) {
            shortFormPrintout = printouts.get(printouts.size()-1);
        }

        //printouts.add(new LotoPrintout("Default Short Form"));
        //printouts.add(new LotoPrintout("Short Form 2"));
        //printouts.add(new LotoPrintout("Short Form 3"));
        return printouts;
    }

}
