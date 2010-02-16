package com.n4systems.reporting;

import static com.n4systems.model.builders.InspectionBuilder.anInspection;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.Assert;
import org.junit.Test;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.FluentArrayList;
public class InspectionCertificateReportGeneratorTest {

	
	
	@Test
	public void should_send_a_printable_inspection_to_the_cert_generateor() throws Exception {
		Inspection printableInspection = createPrintableInspection();
		
		InspectionCertificateGenerator certGenerator = createMock(InspectionCertificateGenerator.class);
		expect(certGenerator.generate(InspectionReportType.INSPECTION_CERT, printableInspection, null)).andReturn(null);
		replay(certGenerator);
		
		InspectionCertificateReportGenerator sut = new InspectionCertificateReportGenerator(certGenerator);
		
		sut.setType(InspectionReportType.INSPECTION_CERT);
		Transaction transaction = null;
		sut.generate(new FluentArrayList<Inspection>(printableInspection), new NullOutputStream(), "packageName", transaction);
		
		verify(certGenerator);
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
