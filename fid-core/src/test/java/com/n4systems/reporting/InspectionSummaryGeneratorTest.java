package com.n4systems.reporting;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static com.n4systems.model.builders.OrgBuilder.*;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JasperPrint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;

public class InspectionSummaryGeneratorTest {
	
	private DateTimeDefiner dateDefiner = new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault());
	private ReportDefiner reportDefiner;
	private PersistenceManager persistenceManager;
	private InspectionManager inspectionManager;
	private User user;
	
	@Before
	public void setUp() {
		reportDefiner = createMock(ReportDefiner.class);
		persistenceManager = createMock(PersistenceManager.class);
		inspectionManager = createMock(InspectionManager.class);
		user = UserBuilder.aUser().createObject();
	}
	
	@Test
	public void generate_inspection_summary() throws Exception {
		final Inspection printableInspection = createPrintableInspection();
		
		setReportDefinerExpectations(printableInspection);
		
		InspectionSummaryGenerator generator = new InspectionSummaryGenerator(dateDefiner, persistenceManager, inspectionManager) {
			@Override
			protected List<Long> getSearchIds(ReportDefiner reportDefiner,
					User user) {
				return Arrays.asList(printableInspection.getId());
			}

			@Override
			protected PrimaryOrg getTenant(User user, Long tenantId) {
				return (PrimaryOrg) aPrimaryOrg().build();
			}
		};
		
		JasperPrint jp = generator.generate(reportDefiner, user);
		assertNotNull(jp);
		verify(reportDefiner);
		verify(inspectionManager);
	}

	private void setReportDefinerExpectations(Inspection inspection) {
		expect(reportDefiner.getSerialNumber()).andReturn("");
		expect(reportDefiner.getRfidNumber()).andReturn("");
		expect(reportDefiner.getOrderNumber()).andReturn("");
		expect(reportDefiner.getPurchaseOrder()).andReturn("");
		expect(reportDefiner.getToDate()).andReturn(new Date());
		expect(reportDefiner.getFromDate()).andReturn(new Date());
		expect(reportDefiner.getProductType()).andReturn(null);
		expect(reportDefiner.getInspectionBook()).andReturn(null);
		expect(reportDefiner.getInspectionTypeGroup()).andReturn(null);
		expect(reportDefiner.getPerformedBy()).andReturn(null);
		expect(reportDefiner.getOwner()).andReturn(null);
		replay(reportDefiner);
		
		expect(inspectionManager.findAllFields(eq(inspection.getId()), isA(SecurityFilter.class))).andReturn(inspection);
		replay(inspectionManager);
	}

	private Inspection createPrintableInspection() {
		InspectionTypeGroup inspectionTypeGroup = createPrintableInspectionTypeGroup();
				
		InspectionType printableInspectionType = InspectionTypeBuilder.anInspectionType().withGroup(inspectionTypeGroup).build();
		Inspection inspection = anInspection().ofType(printableInspectionType).build();
		return inspection;
	}
	
	private InspectionTypeGroup createPrintableInspectionTypeGroup() {
		InspectionTypeGroup inspectionTypeGroup = new InspectionTypeGroup();
		inspectionTypeGroup.setPrintOut(new PrintOut());
		inspectionTypeGroup.getPrintOut().setPdfTemplate("somefile");
		inspectionTypeGroup.getPrintOut().setType(PrintOutType.CERT);
		
		Assert.assertTrue(inspectionTypeGroup.getPrintOutForReportType(InspectionReportType.INSPECTION_CERT) != null);
		
		return inspectionTypeGroup;
	}
}
