 package com.n4systems.model;

 import org.joda.time.DateTimeConstants;
 import org.joda.time.LocalDate;
 import org.joda.time.Period;
 import org.joda.time.PeriodType;

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
     ANNUALLY;

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
             case ANNUALLY:
                 return nextYear(day);
             default:
                 throw new IllegalStateException("Recurrence " + this.name() + " not supported");
         }
     }

     private LocalDate nextMonth(LocalDate date, int day) {
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

     private LocalDate nextHalfYear(LocalDate date) {
         return date.plus(new Period(6, PeriodType.months()));
     }

     private LocalDate nextYear(LocalDate date) {
         return date.plusYears(1);
     }

     private LocalDate nextDay(LocalDate date, int day) {
         if (date.getDayOfWeek()>=day) {
             date = date.plusWeeks(1);
         }
         return date.withDayOfWeek(day);
     }
 }
