 package com.n4systems.model;

 import com.google.common.base.Preconditions;
 import com.google.common.collect.Lists;
import org.joda.time.*;

 import java.util.Date;
 import java.util.List;

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

     public LocalDate getNext(LocalDate day, Date date) {
         if (date==null || !requiresDate()) {
             return getNext(day);
         } else {
             return getNext(day, new MonthDay(date));
         }
     }

     public LocalDate getNext(LocalDate day, MonthDay triggerDate) {
         Preconditions.checkState(requiresDate() && triggerDate!=null, "use getNext(day) if NO date required for recurrence");
         // strip away all the stuff we don't need in our date, we are only concerned with month day.
         //  e.g.   "July 1"...strip away July 1,2012 08:43.89s
         int year = day.getYear();
         List<LocalDate> triggerDates = Lists.newArrayList();
         LocalDate d = new LocalDate().withYear(year).withMonthOfYear(triggerDate.getMonthOfYear()).withDayOfMonth(triggerDate.getDayOfMonth());

         while (true) {
            List<LocalDate> triggerDatesInYear = getTriggerDatesInYear(year, triggerDate);
            for (LocalDate triggerDay:triggerDatesInYear) {
                 if (triggerDay.isAfter(day)) {
                     return triggerDay;
                 }
             }
             year++;
         }
     }

     public LocalDate getNext(LocalDate day) {
         Preconditions.checkState(requiresDate()==false, "use getNext(day, MonthDay) if date required for recurrence");
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
                 return null;
             default:
                 throw new IllegalStateException("Recurrence " + this.name() + " not supported");
         }
     }

     private List<LocalDate> getTriggerDatesInYear(int thisYear, MonthDay triggerDate) {
         List<LocalDate> triggerDates = Lists.newArrayList();
         switch (this) {
             case ANNUALLY:
                 return Lists.newArrayList(new LocalDate().withYear(thisYear).withMonthOfYear(triggerDate.getMonthOfYear()).withDayOfMonth(triggerDate.getDayOfMonth()));
             // SEMI-ANNUALLY:
             //   return [ triggerDay, triggerDay+6months]
             default:
                 throw new IllegalStateException("can't get trigger dates in year for type " + this);
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
             case MONTHLY_LAST:
                 return false;
             case ANNUALLY:
                 return true;
         }
         throw new IllegalStateException("recurrence type " + this + " not supported");
     }
 }
