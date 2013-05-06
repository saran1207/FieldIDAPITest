package com.n4systems.fieldid.service.procedure;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.builders.ImageAnnotationBuilder;
import com.n4systems.model.builders.IsolationPointBuilder;
import com.n4systems.model.builders.ProcedureDefinitionBuilder;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.services.SecurityContext;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProcedureDefinitionServiceTest extends FieldIdServiceTest {

    @TestTarget private ProcedureDefinitionService procedureDefinitionService;

    @TestMock private PersistenceService persistenceService;
    @TestMock private SecurityContext securityContext;
    @TestMock private S3Service s3Service;



    @Test
    public void test_cloneProcedureDefinition_empty() {

        ProcedureDefinition source = ProcedureDefinitionBuilder.aProcedureDefinition().build();
        ProcedureDefinition result = procedureDefinitionService.cloneProcedureDefinition(source);

        assertNull(result.getId());
        assertEquals(source.getTenant().getId(), result.getTenant().getId());
        assertEquals(source.getProcedureCode(), result.getProcedureCode());
        assertEquals(source.getElectronicIdentifier(), result.getElectronicIdentifier());
        assertEquals(source.getWarnings(), result.getWarnings());
        assertEquals(source.getDevelopedBy(), result.getDevelopedBy());
        assertEquals(source.getEquipmentDescription(), result.getEquipmentDescription());
        assertEquals(source.getEquipmentLocation(), result.getEquipmentLocation());
        assertEquals(source.getEquipmentNumber(), result.getEquipmentNumber());
        assertEquals(PublishedState.DRAFT, result.getPublishedState());

        verifyTestMocks();
    }

    @Test
    public void test_cloneProcedureDefinition_withIsolationPoints() {
        List<IsolationPoint> isolationPoints = Lists.newArrayList(
                IsolationPointBuilder.anIsolationPoint().withIdentifer("A").build(),
                IsolationPointBuilder.anIsolationPoint().withIdentifer("B").build(),
                IsolationPointBuilder.anIsolationPoint().withIdentifer("C").build()
        );
        ProcedureDefinition source = ProcedureDefinitionBuilder.aProcedureDefinition().withIsolationPoints(isolationPoints).build();
        ProcedureDefinition result = procedureDefinitionService.cloneProcedureDefinition(source);

        List<IsolationPoint> pts = result.getIsolationPoints();
        assertEquals(3, pts.size());
        for (int i = 0; i<3; i++) {
            assertIsolationPoint(isolationPoints, pts, i);
        }
    }

    @Test
    public void test_cloneProcedureDefinition_withAnnotations() {
        ImageAnnotation annotation = ImageAnnotationBuilder.anImageAnnotation().withText("hello").withX(.1).withY(.2).build();
        List<IsolationPoint> isolationPoints = Lists.newArrayList(
                IsolationPointBuilder.anIsolationPoint().withIdentifer("A").withAnnotation(annotation).build()
        );

        s3Service.copyProcedureDefImageToTemp(isA(ProcedureDefinitionImage.class), isA(ProcedureDefinitionImage.class) );
        ProcedureDefinition source = ProcedureDefinitionBuilder.aProcedureDefinition().withIsolationPoints(isolationPoints).build();
        ProcedureDefinition result = procedureDefinitionService.cloneProcedureDefinition(source);

        List<IsolationPoint> pts = result.getIsolationPoints();
        assertEquals(1, pts.size());
        ImageAnnotation actual = pts.get(0).getAnnotation();
        assertEquals(.1,actual.getX(),.000001);
        assertEquals(.2, actual.getY(), .000001);
        assertEquals("hello", actual.getText());
        assertEquals(actual.getImage().getFileName(),annotation.getImage().getFileName());
    }

    private void assertIsolationPoint(List<IsolationPoint> expecteds, List<IsolationPoint> actuals, int index) {
        IsolationPoint expected = expecteds.get(index);
        IsolationPoint actual = actuals.get(index);

        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getSourceText(), actual.getSourceText());
        assertEquals(expected.getCheck(), actual.getCheck());
        assertEquals(expected.getDeviceDefinition().getFreeformDescription(), actual.getDeviceDefinition().getFreeformDescription());
        assertEquals(expected.getLockDefinition().getFreeformDescription(), actual.getLockDefinition().getFreeformDescription());
        assertEquals(expected.getElectronicIdentifier(), actual.getElectronicIdentifier());
        assertEquals(expected.getSourceType(), actual.getSourceType());
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.getMethod(), actual.getMethod());
    }
}
