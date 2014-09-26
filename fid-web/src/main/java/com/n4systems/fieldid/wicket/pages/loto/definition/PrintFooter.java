package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@ComponentWithExternalHtml
public class PrintFooter extends Panel {


    protected @SpringBean
    com.n4systems.fieldid.service.amazon.S3Service s3Service;

    private @SpringBean
    SecurityContext securityContext;

    public PrintFooter(String id, IModel<ProcedureDefinition> model) {
        super(id,model);


        // audit dates
        boolean hasOrigin = false;

        if (null != model.getObject().getOriginDate()) {
            hasOrigin = true;
        }

        String css = hasOrigin ? "hasOrigin" : "noOrigin";
        add(new AttributeAppender("class", Model.of(css)," "));


        Date dte = null;
        Date dte1 = null;
        Date dte2 = null;
        Date dte3 = null;
        Date dte4 = null;

        if (hasOrigin) {

            User user = securityContext.getUserSecurityFilter().getUser();

            dte = model.getObject().getOriginDate();
            //dte = model.getObject().getModified();


            GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
            cal.setTimeInMillis(dte.getTime());
            cal.add(Calendar.YEAR,1);
            dte1 = cal.getTime();

            cal.add(Calendar.YEAR,1);
            dte2 = cal.getTime();

            cal.add(Calendar.YEAR,1);
            dte3 = cal.getTime();

            cal.add(Calendar.YEAR,1);
            dte4 = cal.getTime();

        }


        // add(new Label("auditDateOne", Model.of( DateHelper.format(dte1, new DateTimeDefiner(user)) )));
        //add(new Label("auditDateOne", Model.of(DateHelper.date2String("MMM yyyy", dte1))));

        //audit date two

        //add(new Label("auditDateTwo", Model.of( DateHelper.date2String("MMM yyyy", dte2) )));

        //audit date three
        //add(new Label("auditDateThree", Model.of( DateHelper.date2String("MMM yyyy", dte3) )));

        //audit date four
        //add(new Label("auditDateFour", Model.of( DateHelper.date2String("MMM yyyy", dte4) )));

        //tennant logo
        add(new ExternalImage("tenantLogo", s3Service.getBrandingLogoURL(model.getObject().getTenant().getId()).toString()).setEscapeModelStrings(false));

    }
}
