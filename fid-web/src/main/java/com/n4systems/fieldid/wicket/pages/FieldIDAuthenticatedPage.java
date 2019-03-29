package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.UIConstants;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.user.User;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.StringUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Subclasses of this page can be full endpoints, but they won't have the outside
// layout for navigation. This is useful for lightbox rendered items.
public class FieldIDAuthenticatedPage extends FieldIDWicketPage implements UIConstants {

    @SpringBean private PersistenceService persistenceService;

    @SpringBean protected ConfigService configService;

    public FieldIDAuthenticatedPage(PageParameters params) {
        super(params);
        verifyLoggedIn();
        verifyEulaAcceptance();
    }

    public FieldIDAuthenticatedPage() {
        verifyLoggedIn();
        verifyEulaAcceptance();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        verifyNonConcurrentSession();
    }

    private void verifyLoggedIn() {
        SessionUser sessionUser = getSessionUser();

        if (sessionUser == null) {
            new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession()).storeUrl();
            throw new RedirectToUrlException("/login.action");
        }
    }

    private void verifyEulaAcceptance() {
        SessionUser sessionUser = getSessionUser();

        if (sessionUser.isAdmin() && !FieldIDSession.get().getEulaAcceptance().isLatestEulaAccepted()) {
            throw new RedirectToUrlException("/eulaAcceptanceAdd.action");
        }
    }

    private void verifyNonConcurrentSession() {
        // We detect concurrent sessions in the request cycle, but boot them out here.
        // Reason is that there's a bug in wicket that prevents you from setting a response page in the RequestCycleListener
        // We need to do it in the request cycle listener because ALL requests (including ajax requests that don't fire page constructors)
        // need to refresh the session timeout counter.
		if (FieldIDSession.get().isConcurrentSessionDetectedInRequestCycle()) {
			throw new RestartResponseException(SessionBootedPage.class);
		}
    }

    public User getCurrentUser() {
        return persistenceService.find(User.class, getSessionUser().getUniqueID());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }

    public String getUserIQJs() {

        Map<String,String> tokens = new HashMap<>();

        tokens.put("userIQSiteId",configService.getConfig() == null || configService.getConfig().getWeb() == null || configService.getConfig().getWeb().getUserIQSiteId()==null?"":configService.getConfig().getWeb().getUserIQSiteId());
        tokens.put("userId",getSessionUser()==null||getSessionUser().getUserID()==null?"":getSessionUser().getUserID());
        tokens.put("userName",getSessionUser()==null||getSessionUser().getName()==null?"":getSessionUser().getName());
        tokens.put("salesforceId",getSessionUser()==null||getSessionUser().getTenant()==null||getSessionUser().getTenant().getSalesforceId()==null?"":getSessionUser().getTenant().getSalesforceId());
        tokens.put("tenantName",getSessionUser()==null||getSessionUser().getOwner()==null||getSessionUser().getOwner().getName()==null?"":getSessionUser().getOwner().getName());
        tokens.put("userEmail",getSessionUser()==null||getSessionUser().getEmailAddress()==null?"":getSessionUser().getEmailAddress());
        Date createdDate = new Date();
        if (getCurrentUser() != null) createdDate = getCurrentUser().getCreated();
        if (createdDate == null) {
            createdDate = java.sql.Date.valueOf(LocalDate.of(2000, Month.JANUARY,1));
        }
        tokens.put("userCreatedDate", new SimpleDateFormat("yyyy-MM-dd").format(createdDate));

        String patternString = "%(" + StringUtils.concat(tokens.keySet(), "|") + ")%";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(BASE_USEIQ_SCRIPT);

        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
