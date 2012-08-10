package com.n4systems.reporting;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ReportMapProducer {
    private static Logger logger = Logger.getLogger(AbsractEventReportMapProducer.class);

    protected final S3Service s3Service;
	protected final DateTimeDefinition dateTimeDefinition;
	private Map<String, Object> reportMap = new HashMap<String, Object>();

	public ReportMapProducer(DateTimeDefinition dateTimeDefinition, S3Service s3Service) {
		super();
		this.dateTimeDefinition = dateTimeDefinition;
        this.s3Service = s3Service;
	}
	
	public Map<String, Object> produceMap() {
		addParameters();
		return reportMap;
	}
	
	protected abstract void addParameters();
	
	
	
	protected String normalizeString(String str) {
		return str.toLowerCase().replaceAll("\\s", "");
	}

	protected String formatDate(Date date, boolean showTime) {
		if (date instanceof PlainDate) {
			return new FieldIdDateFormatter(date, dateTimeDefinition, false, showTime).format();
		}
		return new FieldIdDateFormatter(date, dateTimeDefinition, true, showTime).format();
		
	}

	protected void add(String key, Object value) {
		reportMap.put(key, value);
	}

    protected byte[] getCustomerLogo(BaseOrg owner) {
        if (owner == null || owner.isInternal()) return null;
        try {
            byte[] logo = s3Service.downloadCustomerLogo(owner.getCustomerOrg().getId());
            return logo;
        } catch (IOException e) {
            logger.warn("Unable to download customer logo for report", e);
            return null;
        }
    }

}
