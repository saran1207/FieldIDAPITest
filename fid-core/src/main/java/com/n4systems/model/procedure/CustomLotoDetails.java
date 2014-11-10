package com.n4systems.model.procedure;

import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="loto_custom_details")
public class CustomLotoDetails extends EntityWithTenant {

    @Column(name="application_process")
    private String applicationProcess;

    @Column(name="removal_process")
    private String removalProcess;

    @Column(name="testing_and_verification")
    private String testingAndVerification;


    public CustomLotoDetails() {}

    public CustomLotoDetails(Tenant tenant) {
        this(null, null, null);
        setTenant(tenant);
    }

    public CustomLotoDetails(String applicationProcess, String removalProcess, String testingAndVerification) {
        this.applicationProcess = applicationProcess;
        this.removalProcess = removalProcess;
        this.testingAndVerification = testingAndVerification;
    }

    public String getApplicationProcess() {
        return applicationProcess;
    }

    public void setApplicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
    }

    public String getRemovalProcess() {
        return removalProcess;
    }

    public void setRemovalProcess(String removalProcess) {
        this.removalProcess = removalProcess;
    }

    public String getTestingAndVerification() {
        return testingAndVerification;
    }

    public void setTestingAndVerification(String testingAndVerification) {
        this.testingAndVerification = testingAndVerification;
    }
}
