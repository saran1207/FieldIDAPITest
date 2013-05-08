package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintProductSummary extends Panel {

    private @SpringBean SecurityContext securityContext;

    public PrintProductSummary(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        //equipmentNumber
        add(new Label("equipmentNumber", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentNumber())));

        //equipmentDescription
        add(new Label("equipmentLocation", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentLocation())));

        //building number
        add(new Label("building", ProxyModel.of(model, on(ProcedureDefinition.class).getBuilding())));



        //lockout points
        int lockoutPts = 0;
        int deviceCount = 0;

        List<IsolationPoint> isoPts =  on(ProcedureDefinition.class).getIsolationPoints();

        if (null != isoPts && isoPts.size() > 0) {
            lockoutPts = isoPts.size();

            //loop through device defns - sum iso points
            for(IsolationPoint iPt: isoPts) {

                if(iPt != null && StringUtils.isNotEmpty(iPt.getDeviceDefinition().getFreeformDescription())) {
                    deviceCount++;
            }
            }

        }

        //lockout points
        add(new Label("lockoutPoints", Model.of(String.valueOf(lockoutPts))));

        //device count
        add(new Label("deviceCount", Model.of(String.valueOf(deviceCount))));

        //warnings
        add(new Label("warnings", ProxyModel.of(model, on(ProcedureDefinition.class).getWarnings())));

        // audit dates
        boolean hasOrigin = false;

        if (null != model.getObject().getOriginDate()) {
            hasOrigin = true;
        }

        String css = hasOrigin ? "hasOrigin" : "noOrigin";
        add(new AttributeAppender("class",Model.of(css)," "));


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
            add(new Label("auditDateOne", Model.of(DateHelper.date2String("MMM yyyy", dte1))));

            //audit date two

            add(new Label("auditDateTwo", Model.of( DateHelper.date2String("MMM yyyy", dte2) )));

            //audit date three
            add(new Label("auditDateThree", Model.of( DateHelper.date2String("MMM yyyy", dte3) )));

            //audit date four
            add(new Label("auditDateFour", Model.of( DateHelper.date2String("MMM yyyy", dte4) )));



    }
}
