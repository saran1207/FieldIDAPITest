package com.n4systems.reporting;

import java.util.Date;

import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;
import com.n4systems.util.ReportMap;

public abstract class ReportMapProducer {

	protected final DateTimeDefinition dateTimeDefinition;
	private ReportMap<Object> reportMap = new ReportMap<Object>();

	public ReportMapProducer(DateTimeDefinition dateTimeDefinition) {
		super();
		this.dateTimeDefinition = dateTimeDefinition;
	}
	
	public ReportMap<Object> produceMap() {
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
