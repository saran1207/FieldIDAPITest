package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.util.ConfigEntry;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.Random;

public class OopsPage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(OopsPage.class);

    public static String PARAM_ERROR_TYPE_KEY = "ERROR_TYPE";

    public OopsPage() {
        this(null);
    }

    public OopsPage(PageParameters params) {
        super(params);
        addComponents(params);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.oops"));
    }

    private void addComponents(PageParameters params) {

        OopsPageErrorType oopsErrorType;
        if (params != null) {
            StringValue oopsErrorTypeStr = params.get(PARAM_ERROR_TYPE_KEY);
            oopsErrorType = oopsErrorTypeStr != null ?
                            OopsPageErrorType.valueOf(oopsErrorTypeStr.toString()) : OopsPageErrorType.GENERIC;
        }
        else
            oopsErrorType = OopsPageErrorType.GENERIC;

        Label errorTitleLabel;
        Label messageTextLabel;
        Label helpTextLabel;
        WebMarkupContainer homeButton = new WebMarkupContainer("homeButton");
        homeButton.setVisible(false);

        switch(oopsErrorType) {
            case NEEDS_PERMISSION:
                errorTitleLabel = new Label("errorTitle", new FIDLabelModel("title.oops_needs_permission"));
                messageTextLabel = new Label("messageText", new FIDLabelModel("label.missing_permission_for_action"));
                helpTextLabel = new Label("helpText", new FIDLabelModel("label.contact_admin_for_info"));
                homeButton.setVisible(true);
                break;

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
        add(homeButton);
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

