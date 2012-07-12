package com.n4systems.model;

public enum RecurrenceTime {

    MIDNIGHT(0,0),
    ONE_AM(1,0),
    TWO_AM(2,0),
    THREE_AM(3,0),
    FOUR_AM(4,0),
    FIVE_AM(5,0),
    SIX_AM(6,0),
    SEVEN_AM(7,0),
    EIGHT_AM(8,0),
    NINE_AM(9,0),
    TEN_AM(10,0),
    ELEVEN_AM(11,0),
    NOON(12,0),
    ONE_PM(13,0),
    TWO_PM(14,0),
    THREE_PM(15,0),
    FOUR_PM(16,0),
    FIVE_PM(17,0),
    SIX_PM(18,0),
    SEVEN_PM(19,0),
    EIGHT_PM(20,0),
    NINE_PM(21,0),
    TEN_PM(22,0),
    ELEVEN_PM(23,0);

    private int hour;
    private int minutes;

    RecurrenceTime(int hour, int minutes) {
        this.hour = hour;  // in military time.
        this.minutes = minutes;
    }

    public int getHourAmPm() {
        return hour>12 ? hour-12 : hour;
    }

    public boolean isAm() {
        return hour<12;
    }
    public boolean isPm() {
        return hour>=12;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getTotalMinutes() {
        return hour*60+minutes;
    }


}
