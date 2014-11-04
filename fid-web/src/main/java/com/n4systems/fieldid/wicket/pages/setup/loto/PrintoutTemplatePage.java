package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.google.common.collect.Lists;
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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class PrintoutTemplatePage extends FieldIDTemplatePage {

    private LotoPrintout longFormPrintout;
    private LotoPrintout shortFormPrintout;

    public PrintoutTemplatePage() {

        Form form = new Form("form") {
            @Override
            protected void onSubmit() {

            }
        };

        form.add(new FidDropDownChoice<LotoPrintout>("longForm",
                                        new PropertyModel<>(this, "longFormPrintout"),
                                        getLongFormPrintouts(),
                                        new ListableChoiceRenderer<LotoPrintout>())
                );

        form.add(new FidDropDownChoice<LotoPrintout>("shortForm",
                                        new PropertyModel<>(this, "shortFormPrintout"),
                                        getShortFormPrintouts(),
                                        new ListableChoiceRenderer<LotoPrintout>())
                );

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
                List<LotoPrintout> printouts =  Lists.newArrayList();
                printouts.add(new LotoPrintout("Long Form 1"));
                printouts.add(new LotoPrintout("Long Form 2"));
                printouts.add(new LotoPrintout("Long Form 3"));
                return printouts;
            }
        };
    }

    private LoadableDetachableModel<List<LotoPrintout>> getShortFormPrintouts() {
        return new LoadableDetachableModel<List<LotoPrintout>>() {
            @Override
            protected List<LotoPrintout> load() {
                List<LotoPrintout> printouts =  Lists.newArrayList();
                printouts.add(new LotoPrintout("Short Form 1"));
                printouts.add(new LotoPrintout("Short Form 2"));
                printouts.add(new LotoPrintout("Short Form 3"));
                return printouts;
            }
        };
    }

}
