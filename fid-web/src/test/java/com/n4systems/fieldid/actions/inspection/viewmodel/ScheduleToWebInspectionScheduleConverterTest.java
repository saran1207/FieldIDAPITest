package com.n4systems.fieldid.actions.inspection.viewmodel;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.builders.AssetTypeScheduleBuilder;
import org.junit.Test;

import com.n4systems.fieldid.actions.helpers.UserDateConverter;
import com.n4systems.fieldid.actions.inspection.WebInspectionSchedule;

public class ScheduleToWebInspectionScheduleConverterTest {

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
	public void should_return_null_when_given_a_null_inspection_schedule() throws Exception {
		ScheduleToWebInspectionScheduleConverter sut = new ScheduleToWebInspectionScheduleConverter(new NullUserDateConverter());
		assertThat(sut.convert(null, new Date()), nullValue(WebInspectionSchedule.class));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_have_the_right_inspection_type_name_and_id() throws Exception {
		AssetTypeSchedule schedule = AssetTypeScheduleBuilder.anAssetTypeSchedule().build();

		ScheduleToWebInspectionScheduleConverter sut = new ScheduleToWebInspectionScheduleConverter(new NullUserDateConverter());

		WebInspectionSchedule convertedWebSchedule = sut.convert(schedule, new Date());

		assertThat(convertedWebSchedule, allOf(hasProperty("type", equalTo(schedule.getEventType().getId())), hasProperty("typeName", equalTo(schedule.getEventType().getName()))));
	}


	@Test
	public void should_convert_the_date_from_schedule_the_string_representation() throws Exception {
		AssetTypeSchedule schedule = AssetTypeScheduleBuilder.anAssetTypeSchedule().build();

		ScheduleToWebInspectionScheduleConverter sut = new ScheduleToWebInspectionScheduleConverter(new NullUserDateConverter() {
			@Override
			public String convertDate(Date date) {
				return "converted date";
			}
		});

		WebInspectionSchedule convertedWebSchedule = sut.convert(schedule, new Date());

		assertThat(convertedWebSchedule, hasProperty("date", equalTo("converted date")));
	}
}
