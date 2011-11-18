package com.n4systems.reporting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;

public abstract class ReportMapProducer {

	protected final DateTimeDefinition dateTimeDefinition;
	private Map<String, Object> reportMap = new HashMap<String, Object>();

	public ReportMapProducer(DateTimeDefinition dateTimeDefinition) {
		super();
		this.dateTimeDefinition = dateTimeDefinition;
	}
	
	public Map<String, Object> produceMap() {
		addParameters();
		return reportMap;
	}
	
	protected abstract void addParameters();
	
	
	
	protected String normalizeString(String str) {
		return str.toLowerCase().replaceAll("\\s", "");
	}


	/**
	 * Wrapper method for {@link DateHelper#date2String(String, Date)}
	 * 
	 * @see DateHelper#date2String(String, Date)
	 * @param format
	 *            A SimpleDateFormat String date format
	 * @param date
	 *            Date object
	 * @return The formatted date
	 */
	protected String formatDate(Date date, boolean showTime) {
		if (date instanceof PlainDate) {
			return new FieldidDateFormatter(date, dateTimeDefinition, false, showTime).format();
		}
		return new FieldidDateFormatter(date, dateTimeDefinition, true, showTime).format();
		
	}

	protected void add(String key, Object value) {
		reportMap.put(key, value);
	}
}
