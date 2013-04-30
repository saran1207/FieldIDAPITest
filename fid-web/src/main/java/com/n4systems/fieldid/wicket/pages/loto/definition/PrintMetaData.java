package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.services.SecurityContext;
import com.n4systems.util.DateHelper;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintMetaData extends Panel {

    protected @SpringBean com.n4systems.fieldid.service.amazon.S3Service s3Service;
    private @SpringBean SecurityContext securityContext;


    public PrintMetaData(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        add(new AttributeAppender("class", Model.of("print-header")));

        // tenant image
        add(new ExternalImage("tenantLogo", s3Service.getBrandingLogoURL(model.getObject().getTenant().getId()).toString()).setEscapeModelStrings(false));
        // add(new ExternalS3Image("image", path));

        // developedby
        add(new Label("developedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getDevelopedBy().getDisplayName())));

        //reviewedBy
        add(new Label("reviewedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getApprovedBy().getDisplayName())));


        //revisedBy
        add(new Label("revisedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getModifiedBy().getDisplayName())));


        //date
        add(new Label("date", ProxyModel.of(model, on(ProcedureDefinition.class).getOriginDate())));

        boolean hasOrigin = false;
        Date dte = null;
        Date dte1 = null;
        Date dte2 = null;
        Date dte3 = null;
        User user = securityContext.getUserSecurityFilter().getUser();

        if (null != model.getObject().getOriginDate()) {

            hasOrigin = true;

            dte = model.getObject().getOriginDate();

            GregorianCalendar cal = (GregorianCalendar)Calendar.getInstance();
            cal.setTimeInMillis(dte.getTime());
            cal.add(Calendar.YEAR,1);
            dte1 = cal.getTime();

            cal.add(Calendar.YEAR,1);
            dte2 = cal.getTime();

            cal.add(Calendar.YEAR,1);
            dte3 = cal.getTime();
        }


        //modified date
        add(new Label("modifiedDate", ProxyModel.of(model, on(ProcedureDefinition.class).getModified())));

        //revision number
        add(new Label("revisionNumber", ProxyModel.of(model, on(ProcedureDefinition.class).getRevisionNumber())));


        if (hasOrigin) {
            //audit date one
            add(new Label("auditDateOne", Model.of( DateHelper.format(dte1, new DateTimeDefiner(user)) )));

            //audit date two
            add(new Label("auditDateTwo", Model.of( DateHelper.format(dte2, new DateTimeDefiner(user)) )));

            //audit date three
            add(new Label("auditDateThree", Model.of( DateHelper.format(dte3, new DateTimeDefiner(user)) )));
        } else {
        //audit date one
            add(new Label("auditDateOne", Model.of( "" )));

            //audit date two
            add(new Label("auditDateTwo", Model.of( "" )));

            //audit date three
            add(new Label("auditDateThree", Model.of( "" )));
        }

    }
}
