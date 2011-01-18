package com.n4systems.fieldid.selenium.testcase.webservice;

import com.n4systems.fieldid.selenium.DBTestCase;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.webservice.client.InspectionServiceClient;
import com.n4systems.webservice.client.InspectionServicePortType;
import com.n4systems.webservice.server.bundles.ArrayOfProofTestBundle;
import com.n4systems.webservice.server.bundles.ArrayOfProofTestStatusBundle;
import com.n4systems.webservice.server.bundles.AuthBundle;
import com.n4systems.webservice.server.bundles.ProofTestBundle;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public class SubmitChantFileViaWebserviceTest extends DBTestCase {

    private static final String INSPECTION_SERVICE_PATH = "services/InspectionService";
    private static final String TEST_TENANT_NAME = TEST_TENANT_NAMES[0];

    @Override
    public void setupScenario(Scenario scenario) {
        EventType eventType = scenario.anEventType()
                .named("Chant Event")
                .withProofTestTypes(ProofTestType.CHANT)
                .build();

        BaseOrg custOrg = scenario.aCustomerOrg()
                .withName("test customer org")
                .withParent(scenario.defaultPrimaryOrg())
                .build();

        AssetType assetType = scenario.anAssetType().named("Foo Type").withEventTypes(eventType).build();

        scenario.anAsset()
            .ofType(assetType)
            .withSerialNumber("AA2DD")
            .withOwner(custOrg)
            .identifiedBy(scenario.defaultUser())
            .build();
    }

    @Test
    public void submitting_a_prooftest_via_service_should_successfully_record_event() throws Exception {
        InspectionServicePortType inspectionService = getInspectionService();

        ArrayOfProofTestBundle proofTestArray = createProofTestArray();
        ArrayOfProofTestStatusBundle statusBundle = inspectionService.uploadProofTest(createAuthBundle(), proofTestArray);

        assertEquals("SUCCESSFUL", statusBundle.getProofTestStatusBundle().get(0).getStatus().value());
    }

    private ArrayOfProofTestBundle createProofTestArray() throws Exception {
        ArrayOfProofTestBundle proofTestArray = new ArrayOfProofTestBundle();
        ProofTestBundle proofTest = new ProofTestBundle();
        proofTest.setFileData(getTestData());
        proofTest.setFileType("chant");
        proofTest.setCreateCustomer(false);
        proofTest.setCreateProduct(false);
        proofTest.setFileName("testfile.xml");
        proofTestArray.getProofTestBundle().add(proofTest);
        return proofTestArray;
    }

    private byte[] getTestData() throws Exception {
        InputStream resourceAsStream = SubmitChantFileViaWebserviceTest.class.getResourceAsStream("/ChantFileForTest.xml");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[512];
        int bytesRead;

        while ((bytesRead = resourceAsStream.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        return bos.toByteArray();
    }

    private InspectionServicePortType getInspectionService() throws Exception {
        InspectionServiceClient inspectionServiceClient = new InspectionServiceClient();

        URI theUri = new URI("http://"+getSeleniumConfig().getTestServerDomain()+getSeleniumConfig().getTestServerContextRoot()+INSPECTION_SERVICE_PATH);
        System.out.println("Going to URI: " + theUri);
        return inspectionServiceClient.getInspectionServiceHttpPort(theUri.toString());
    }

    private AuthBundle createAuthBundle() {
        AuthBundle authBundle = new AuthBundle();
        authBundle.setTenantName(TEST_TENANT_NAME);
        authBundle.setUserName("n4systems");
        authBundle.setPassword("Xk43g8!@");
        return authBundle;
    }

}
