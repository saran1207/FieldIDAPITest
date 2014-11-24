package com.n4systems.model.procedure;

import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

@Entity
@Table(name="loto_settings")
public class LotoSettings extends EntityWithTenant {

    @Column(name="application_process")
    private String applicationProcess;

    @Column(name="removal_process")
    private String removalProcess;

    @Column(name="testing_and_verification")
    private String testingAndVerification;

    @Column(name="annotation_type")
    @Enumerated(EnumType.STRING)
    private AnnotationType annotationType;

    public LotoSettings() {}

    public LotoSettings(Tenant tenant) {
        this(null, null, null, AnnotationType.ARROW_STYLE);
        setTenant(tenant);
    }

    public LotoSettings(String applicationProcess, String removalProcess, String testingAndVerification, AnnotationType annotationType) {
        this.applicationProcess = applicationProcess;
        this.removalProcess = removalProcess;
        this.testingAndVerification = testingAndVerification;
        this.annotationType = annotationType;
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

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }
}
