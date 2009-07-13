package com.n4systems.compliance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;

/**
 * All calculations are done in utc time 
 * @author aaitken
 *
 */
public class ComplianceRisk {
	
	private InspectionSchedule schedule;
	private Date lastInspectionDate;

	public ComplianceRisk(InspectionSchedule schedule, Date lastInspectionDate) {
		super();
		this.schedule = schedule;
		this.lastInspectionDate = new PlainDate(lastInspectionDate);
		
	}

	public InspectionSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(InspectionSchedule schedule) {
		this.schedule = schedule;
	}
	
	public float getCurrentRisk() {
		return sample( DateHelper.getToday() );
	}
	
	public Date getLastInspectionDate() {
		return lastInspectionDate;
	}
	
	public void setLastInspectionDate(Date lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}
	
	public Date getStartDate() {
		return getLastInspectionDate();
	}
	
	public Date getDayBefore( Date date ) {
		return DateHelper.addDaysToDate( date,	-1L );
	}
	
	public Date getDayAfter( Date date ) {
		return DateHelper.addDaysToDate( date,	1L ); 
	}
	
	
	public Date getComplianceDate() {
		return  DateHelper.getDateWithOutTime( schedule.getNextDate() );
	}
	
	/**
	 * equation y = x / ( xc - x0 )
	 *  
	 * @param dateToSample
	 * @return
	 */
	public float sample( Date dateToSample ) {
		float y;
		float x = (float)DateHelper.getDaysDelta(dateToSample, getStartDate());
		float xc = (float)DateHelper.getDaysDelta(getComplianceDate(), getStartDate());
		
		if( xc != 0 ) {
			y =  x / xc;
		} else {
			y = 1F;
		}
		
		if( y > 1 ) {
			y = 1.0F;
		}
		
		return Math.abs( y * 100 );
		
	}
	
	public List<Date> getDateSet( ) {
		return getDateSet( 0 );
	}
	
	public List<Date> getDateSet( int additionalDays ) {
		List<Date> dates = new ArrayList<Date>();
		
		Date lastDate = ( getComplianceDate().after( DateHelper.getToday() ) ) ? getComplianceDate() : DateHelper.getToday(); 
		 
		lastDate = DateHelper.addDaysToDate( lastDate, Long.valueOf( additionalDays ) );
		Long days = DateHelper.getDaysDelta( getStartDate(), lastDate );
		for( int i = 0; i < days.intValue(); i++ ) {
			dates.add( DateHelper.addDaysToDate( getStartDate(), Long.valueOf( i ) ) );
		}
		
		return dates;
	}
	
	public Date getToday( ) {
		return DateHelper.getToday();
	}
	
}