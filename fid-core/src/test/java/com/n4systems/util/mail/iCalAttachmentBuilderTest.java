package com.n4systems.util.mail;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class iCalAttachmentBuilderTest {

    private Date date = Date.from(LocalDate.of(2015, Month.JANUARY, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

    @Test
    public void testICalAttachmentBuilder() {
        ICalAttachmentBuilder builder = ICalAttachmentBuilder.anICalAttachment()
                .withSubject("Work Items Assigned")
                .withStartDate(date)
                .withAllDayEvent(true);

        byte[] bytes = builder.build();

        String result = new String(bytes);

        System.out.println(result);

        assertTrue(result.contains("SUMMARY;LANGUAGE=en-us:Work Items Assigned"));
        assertTrue(result.contains("DTSTART;VALUE=DATE:20150101"));
    }

}
