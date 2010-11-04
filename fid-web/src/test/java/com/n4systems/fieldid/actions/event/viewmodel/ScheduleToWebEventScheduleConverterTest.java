package com.n4systems.fieldid.actions.event.viewmodel;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.builders.AssetTypeScheduleBuilder;
import org.junit.Test;

import com.n4systems.fieldid.actions.helpers.UserDateConverter;

public class ScheduleToWebEventScheduleConverterTest {

	private class NullUserDateConverter implements UserDateConverter {

		public String convertDate(Date date) {
			return "";
		}

		public Date convertDate(String date) {
			return new Date();
		}

		public String convertDateTime(Date date) {
			return "";
		}

		public Date convertDateTime(String date) {
			return new Date();
		}

		public Date convertToEndOfDay(String date) {
			return new Date();
		}

		public boolean isValidDate(String date, boolean usingTime) {
			return true;
		}

	}

	@Test
	public void should_return_null_when_given_a_null_event_schedule() throws Exception {
		ScheduleToWebEventScheduleConverter sut = new ScheduleToWebEventScheduleConverter(new NullUserDateConverter());
		assertThat(sut.convert(null, new Date()), nullValue(WebEventSchedule.class));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_have_the_right_event_type_name_and_id() throws Exception {
		AssetTypeSchedule schedule = AssetTypeScheduleBuilder.anAssetTypeSchedule().build();

		ScheduleToWebEventScheduleConverter sut = new ScheduleToWebEventScheduleConverter(new NullUserDateConverter());

		WebEventSchedule convertedWebSchedule = sut.convert(schedule, new Date());

		assertThat(convertedWebSchedule, allOf(hasProperty("type", equalTo(schedule.getEventType().getId())), hasProperty("typeName", equalTo(schedule.getEventType().getName()))));
	}


	@Test
	public void should_convert_the_date_from_schedule_the_string_representation() throws Exception {
		AssetTypeSchedule schedule = AssetTypeScheduleBuilder.anAssetTypeSchedule().build();

		ScheduleToWebEventScheduleConverter sut = new ScheduleToWebEventScheduleConverter(new NullUserDateConverter() {
			@Override
			public String convertDate(Date date) {
				return "converted date";
			}
		});

		WebEventSchedule convertedWebSchedule = sut.convert(schedule, new Date());

		assertThat(convertedWebSchedule, hasProperty("date", equalTo("converted date")));
	}
}
