package com.n4systems.fieldid.service.asset;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.date.DateService;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.ejb.entity.IdentifierCounter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;

public class AssetIdentifierService extends FieldIdPersistenceService {

    @Autowired
    private OrgService orgService;

    @Autowired
    private DateService dateService;

    public void updateIdentifierCounter(IdentifierCounter identifierCounter) {
        persistenceService.update(identifierCounter);
    }

    public Collection<IdentifierCounter> getIdentifierCounters() {
        QueryBuilder<IdentifierCounter> query = new QueryBuilder<IdentifierCounter>(IdentifierCounter.class, new OpenSecurityFilter());
        return persistenceService.findAll(query);
    }

    public IdentifierCounter getIdentifierCounter(Long tenantId) {
        QueryBuilder<IdentifierCounter> query = new QueryBuilder<IdentifierCounter>(IdentifierCounter.class, new TenantOnlySecurityFilter(tenantId));
        return persistenceService.find(query);
    }

    /**
     * Gets the next counter for a tenants serial number, formatted as a string
     * based on their decimal format specified.  This is synchronized to ensure multiple
     * users won't get the same counter value.
     */
    public synchronized String getNextCounterValue(Long tenantId) {
        IdentifierCounter identifierCounter = getIdentifierCounter(tenantId);

        identifierCounter = checkForCounterReset(identifierCounter);

        DecimalFormat decimalFormat = new DecimalFormat(identifierCounter.getDecimalFormat());

        Long counterValue = identifierCounter.getCounter();
        String counterValueString = decimalFormat.format(counterValue);

        // increment to the next one
        identifierCounter.setCounter(counterValue+1);
        persistenceService.update(identifierCounter);

        return counterValueString;
    }

    private IdentifierCounter checkForCounterReset(IdentifierCounter identifierCounter) {
        Integer lastResetYear = identifierCounter.getLastReset().toInstant().atZone(DateTimeZone.UTC.toTimeZone().toZoneId()).toLocalDate().getYear();

        if(lastResetYear < LocalDate.now(getUserTimeZone().toZoneId()).getYear() && identifierCounter.isResetAnnually()) {
            identifierCounter.setLastReset(DateHelper.getFirstDayOfThisYear());
            identifierCounter.setCounter(1L);
            updateIdentifierCounter(identifierCounter);
        }
        return identifierCounter;
    }

    public String generateIdentifier(AssetType assetType) {
        PrimaryOrg primaryOrg = orgService.getPrimaryOrgForTenant(getCurrentTenant().getId());
        return generateIdentifier(primaryOrg, assetType);
    }

    public String generateIdentifier(PrimaryOrg primaryOrg, AssetType assetType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        DecimalFormat padTwoSpaces = new DecimalFormat("00");
        DecimalFormat padThreeSpaces = new DecimalFormat("000");

        String identifier = primaryOrg.getIdentifierFormat();

        if (assetType != null && assetType.isIdentifierOverridden()) {
            identifier = assetType.getIdentifierFormat();
        }

        if (StringUtils.isBlank(identifier)) {
            return "";
        }

        // go through and replace the appropriate symbols with their meaning
        identifier = identifier.replaceAll("%m", padTwoSpaces.format(calendar.get(Calendar.MONTH)+1));
        identifier = identifier.replaceAll("%d", padTwoSpaces.format(calendar.get(Calendar.DAY_OF_MONTH)));
        identifier = identifier.replaceAll("%Y", new Integer(calendar.get(Calendar.YEAR)).toString());
        identifier = identifier.replaceAll("%y", new Integer(calendar.get(Calendar.YEAR)).toString().substring(2));
        identifier = identifier.replaceAll("%H", padTwoSpaces.format(calendar.get(Calendar.HOUR_OF_DAY)));
        identifier = identifier.replaceAll("%M", padTwoSpaces.format(calendar.get(Calendar.MINUTE)));
        identifier = identifier.replaceAll("%S", padTwoSpaces.format(calendar.get(Calendar.SECOND)));
        identifier = identifier.replaceAll("%L", padThreeSpaces.format(calendar.get(Calendar.MILLISECOND)));
        identifier = identifier.replaceAll("%j", deriveJergensDateCode(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));

        // see if we need to do the counter
        if (identifier.indexOf("%g") >= 0) {
            identifier = identifier.replaceAll("%g", getNextCounterValue(primaryOrg.getTenant().getId()));
        }

        return identifier;
    }

    /**
     * Returns a Jergens style date code given the month and 4 digit year
     * @param month
     * @param year
     * @return
     */
    private String deriveJergensDateCode(int month, int year) {
        // Map month (January = A, Feb = B, etc.)
        char monthCode = 'A';
        for (int i=1; i<=month; i++) {
            monthCode++;
        }

        // Map year (1993 = A, 1994 = B, 1995 = C.. etc)
        char yearCode = 'A';
        for (int i=1993; i<=year; i++) {
            yearCode++;
        }

        String dateCode = String.valueOf(monthCode)+String.valueOf(yearCode);

        return dateCode;
    }
}
