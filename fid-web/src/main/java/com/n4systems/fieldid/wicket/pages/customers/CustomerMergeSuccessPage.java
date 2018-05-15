package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.MessageFormat;

/**
 * Created by agrabovskis on 2018-02-07.
 */
public class CustomerMergeSuccessPage extends FieldIDFrontEndPage {

    public static final String LOSING_CUSTOMER_NAME_KEY = "losingCustomerName";
    public static final String WINNING_CUSTOMER_NAME_KEY = "winningCustomerName";
    public static final String WINNING_CUSTOMER_ID_KEY = "winningCustomerId";

    private String losingCustomerName;
    private String winningCustomerName;
    private String winningCustomerId;

    public CustomerMergeSuccessPage(PageParameters params) {
        super(params);
        losingCustomerName = params.get(LOSING_CUSTOMER_NAME_KEY).toString();
        winningCustomerName = params.get(WINNING_CUSTOMER_NAME_KEY).toString();
        winningCustomerId = params.get(WINNING_CUSTOMER_ID_KEY).toString();
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/steps.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        String title = MessageFormat.format("{0} {1} {2} {3}",
                getString("title.merge_customers"), losingCustomerName,
                getString("label.into"), winningCustomerName);

        return new Label(labelId, title);
    }

    private void addComponents() {
        add(new Link("showCustomer") {
            @Override
            public void onClick() {
                PageParameters params = new PageParameters();
                params.add(CustomerActionsPage.INITIAL_TAB_SELECTION_KEY, CustomerActionsPage.SHOW_CUSTOMER_VIEW_PAGE);
                params.add(CustomerActionsPage.INITIAL_CUSTOMER_ID, winningCustomerId);
                getRequestCycle().setResponsePage(CustomerActionsPage.class, params);
            }
        }.add(new Label("winningCustomerName", winningCustomerName)));
    }
}
