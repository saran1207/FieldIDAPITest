package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.util.ConfigEntry;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.Random;

public class OopsPage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(OopsPage.class);

    public OopsPage(PageParameters params) {
        super(params);
        addComponents(params);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.oops"));
    }

    private void addComponents(PageParameters params) {

        StringValue oopsErrorTypeStr = params.get("ERROR_TYPE");
        OopsPageErrorType oopsErrorType = oopsErrorTypeStr != null ?
                OopsPageErrorType.valueOf(oopsErrorTypeStr.toString()) : OopsPageErrorType.GENERIC;

        Label errorTitleLabel;
        Label messageTextLabel;
        Label helpTextLabel;

        switch(oopsErrorType) {
            default:
                errorTitleLabel = new Label("errorTitle", new FIDLabelModel("title.oops_something_went_wrong"));
                messageTextLabel = new Label("messageText", new FIDLabelModel("message.problem_has_occured"));
                helpTextLabel = new Label("helpText", new FIDLabelModel("message.help_fix_issue_with_ticket",
                        new Object[]{getHelpUrl(), generateTicketNumber()}));
                helpTextLabel.setEscapeModelStrings(false);
                break;
        }

        add(errorTitleLabel);
        add(messageTextLabel);
        add(helpTextLabel);
    }

    private String generateTicketNumber() {
        Random rnd = new Random();
        int errorTicket = 10000 + rnd.nextInt(90000);
        logger.error("Error Ticket #: " + errorTicket);
        return String.valueOf(errorTicket);
    }

    private String getHelpUrl() {
        return configService.getString(ConfigEntry.HELP_SYSTEM_URL);
    }

}

