package com.n4systems.reporting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.reporting.mapbuilders.ReportField;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;

public class InspectionReportMapProducer extends AbsractInspectionReportMapProducer {
	private static final String UNASSIGNED_USER_NAME = "Unassigned";

	private final Inspection inspection;

	public InspectionReportMapProducer(Inspection inspection, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.inspection = inspection;
	}

	@Override
	protected void inspectionParameter() {
		Inspection inspection = (Inspection) this.getInspection();
		add("productLabel", null);
		add("location", inspection.getAdvancedLocation().getFreeformLocation());
		add("predefinedLocations", collapseIntoList(inspection.getAdvancedLocation()));
		add("inspectionBook", (inspection.getBook() != null) ? inspection.getBook().getName() : null);
		add("inspectionResult", inspection.getStatus().getDisplayName());
		add("proofTestInfo", addProofTestInfoParams(inspection));

		add(ReportField.ASSIGNED_USER.getParamKey(), assignedUserName());

		fillInDate(inspection);
	}

	private List<String> collapseIntoList(Location advancedLocation) {
		List<String> locationList = new ArrayList<String>();
		
		if (advancedLocation.hasPredefinedLocation()) {
			
			PredefinedLocation node = advancedLocation.getPredefinedLocation();
			locationList.add(node.getName());
			while (node.hasParent()) {

				node = node.getParent();
				locationList.add(node.getName());
			}
			
			Collections.reverse(locationList);
			return locationList;
		} else {
			locationList.add(advancedLocation.getFreeformLocation());
			return locationList;
		}

	}

	private String assignedUserName() {
		if (!inspection.hasAssignToUpdate()) {
			return null;
		} else if (inspection.getAssignedTo().isUnassigned()) {
			return UNASSIGNED_USER_NAME;
		} else {
			return inspection.getAssignedTo().getAssignedUser().getUserLabel();
		}

	}

	private void fillInDate(Inspection inspection) {
		String performedDateAsString = formatDate(inspection.getDate(), true);
		Date performedDateTimeZoneShifted = DateHelper.convertToUserTimeZone(inspection.getDate(), dateTimeDefinition.getTimeZone());

		add("inspectionDate", performedDateAsString);
		add("datePerformed", performedDateAsString);

		add("datePerformed_date", performedDateTimeZoneShifted);
		add("inspectionDate_date", performedDateTimeZoneShifted);
	}

	@Override
	protected File imagePath(FileAttachment imageAttachment) {
		return PathHandler.getInspectionAttachmentFile((Inspection) getInspection(), imageAttachment);
	}

	@Override
	protected AbstractInspection getInspection() {
		return inspection;
	}
}
