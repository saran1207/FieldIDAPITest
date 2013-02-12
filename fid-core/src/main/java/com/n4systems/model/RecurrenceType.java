 package com.n4systems.model;

 import com.google.common.base.Preconditions;
 import org.joda.time.DateTimeConstants;
 import org.joda.time.LocalDate;
 import org.joda.time.LocalDateTime;

 import java.util.EnumSet;

 public enum RecurrenceType {

     DAILY,
     WEEKLY_MONDAY,
     WEEKLY_TUESDAY,
     WEEKLY_WEDNESDAY,
     WEEKLY_THURSDAY,
     WEEKLY_FRIDAY,
     WEEKLY_SATURDAY,
     WEEKLY_SUNDAY,
     MONTHLY_1ST,
     MONTHLY_15TH,
     MONTHLY_LAST,
     WEEKDAYS,
     ANNUALLY;

     static private EnumSet<RecurrenceType> monthly = EnumSet.of(MONTHLY_1ST,MONTHLY_15TH,MONTHLY_LAST);
     static private EnumSet<RecurrenceType> weekly = EnumSet.of(WEEKLY_MONDAY,WEEKLY_TUESDAY,WEEKLY_WEDNESDAY,WEEKLY_THURSDAY,WEEKLY_FRIDAY,WEEKLY_SATURDAY,WEEKLY_SUNDAY);

     RecurrenceType() {
     }

     public LocalDate getNext(LocalDate day) {
         switch (this) {
             case DAILY :
                 return day.plusDays(1);
             case WEEKLY_MONDAY:
                 return nextDay(day, DateTimeConstants.MONDAY);
             case WEEKLY_TUESDAY:
                 return nextDay(day, DateTimeConstants.TUESDAY);
             case WEEKLY_WEDNESDAY:
                 return nextDay(day, DateTimeConstants.WEDNESDAY);
             case WEEKLY_THURSDAY:
                 return nextDay(day, DateTimeConstants.THURSDAY);
             case WEEKLY_FRIDAY:
                 return nextDay(day, DateTimeConstants.FRIDAY);
             case WEEKLY_SATURDAY:
                 return nextDay(day, DateTimeConstants.SATURDAY);
             case WEEKLY_SUNDAY:
                 return nextDay(day, DateTimeConstants.SUNDAY);
             case MONTHLY_1ST:
                 return nextMonth(day, 1);
             case MONTHLY_15TH:
                 return nextMonth(day, 15);
             case MONTHLY_LAST:
                 return nextMonth(day,-1);
             case WEEKDAYS:
                 return day.getDayOfWeek()==DateTimeConstants.FRIDAY ? day.plusDays(3) :
                         day.getDayOfWeek()==DateTimeConstants.SATURDAY ? day.plusDays(2) :
                         day.plusDays(1);
             case ANNUALLY:
                 return nextYear(day);
             // NOTE : ANNUALLY should be handled via getNext(day, triggerDate) method, never via this method!
             default:
                 throw new IllegalStateException("Recurrence " + this.name() + " not supported");
         }
     }

     private LocalDate nextYear(LocalDate day) {
         Preconditions.checkState(requiresDate(), "should only call this if a recurring schedules have specific dates");
         int year = day.getYear();
         return new LocalDate().withYear(year+1).withDayOfYear(1);
     }


     private LocalDate nextMonth(LocalDate date, int day) {
         Preconditions.checkState(!requiresDate(), "should only call this for ");
         if (day==-1) { //-1 means last day of month.   i.e. nextMonth(Jan4,2011) --> Jan31,2011
             day = date.dayOfMonth().getMaximumValue();
             if (date.getDayOfMonth()>=day) {
                 date = date.plusMonths(1);
             }
             return date.withDayOfMonth(date.dayOfMonth().getMaximumValue());
         } else {
             if (date.getDayOfMonth()>=day) {
                 date = date.plusMonths(1);
             }
             return date.withDayOfMonth(day);
         }
     }

     private LocalDate nextDay(LocalDate date, int day) {
         if (date.getDayOfWeek()>=day) {
             date = date.plusWeeks(1);
         }
         return date.withDayOfWeek(day);
     }

     public boolean requiresDate() {
         switch (this) {
             case DAILY:
             case WEEKLY_MONDAY:
             case WEEKLY_TUESDAY:
             case WEEKLY_WEDNESDAY:
             case WEEKLY_THURSDAY:
             case WEEKLY_FRIDAY:
             case WEEKLY_SATURDAY:
             case WEEKLY_SUNDAY:
             case MONTHLY_1ST:
             case MONTHLY_15TH:
             case WEEKDAYS:
             case MONTHLY_LAST:
                 return false;
             case ANNUALLY:
                 return true;
         }
         throw new IllegalStateException("recurrence type " + this + " not supported");
     }


     public boolean canHaveMultipleTimes() {
         switch (this) {
             case DAILY:
                 return true;
             case WEEKLY_MONDAY:
             case WEEKLY_TUESDAY:
             case WEEKLY_WEDNESDAY:
             case WEEKLY_THURSDAY:
             case WEEKLY_FRIDAY:
             case WEEKLY_SATURDAY:
             case WEEKLY_SUNDAY:
             case MONTHLY_1ST:
             case MONTHLY_15TH:
             case WEEKDAYS:
             case MONTHLY_LAST:
             case ANNUALLY:
                 return false;
         }
         throw new IllegalStateException("recurrence type " + this + " not supported");
     }

     public boolean isMonthly() {
         return monthly.contains(this);
     }

     public boolean isWeekly() {
         return weekly.contains(this);
     }

     public LocalDate previous() {
         LocalDate now = LocalDate.now();
         switch (this) {
             case ANNUALLY:
                 return now.minusYears(1);
             case DAILY:
             case WEEKLY_MONDAY:
             case WEEKLY_TUESDAY:
             case WEEKLY_WEDNESDAY:
             case WEEKLY_THURSDAY:
             case WEEKLY_FRIDAY:
             case WEEKLY_SATURDAY:
             case WEEKLY_SUNDAY:
             case WEEKDAYS:
                 return now.minusDays(1);
             case MONTHLY_1ST:
                 return now.getDayOfMonth()>1?now.withDayOfMonth(1) : now.minusDays(2);
             case MONTHLY_15TH:
                 return now.withDayOfMonth(1);
             case MONTHLY_LAST:
                 return now.withDayOfMonth(1);
         }
         throw new IllegalStateException("recurrence type " + this + " not supported");

     }

     public int getMonthlyDay(LocalDateTime d) {
         switch (this) {
             case MONTHLY_1ST:
                 return 1;
             case MONTHLY_15TH:
                 return 15;
             case MONTHLY_LAST:
                 return d.monthOfYear().getMaximumValue();
         }
         throw new IllegalArgumentException("MonthlyDay only applicable on monthly types = Monthly 1st/15th/last");
     }
 }
