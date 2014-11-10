package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.LotoPrintout;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-11-10.
 */
public class LotoDeleteCell extends Panel {

    private @SpringBean
    LotoReportService lotoReportService;

    public LotoDeleteCell(String id, IModel<? extends LotoPrintout> rowModel, WebMarkupContainer container) {
        super(id);


        AjaxLink link = new AjaxLink("deleteLink", rowModel) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                lotoReportService.deleteLotoPrintout(rowModel.getObject());
                target.add(container);
            }
        };

        //link.add(new SimpleAttributeModifier("onclick", "return confirm('Are you sure you want to remove this procedure?  If this is the selected procedure, it will be reverted to default.');"));

        FlatLabel label = new FlatLabel("name", new FIDLabelModel("label.delete"));
        label.setEnabled(true);
        label.setVisible(true);

        link.add(label);
        add(link);
    }


}
