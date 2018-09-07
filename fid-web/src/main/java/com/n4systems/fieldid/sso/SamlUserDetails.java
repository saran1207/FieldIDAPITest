package com.n4systems.fieldid.sso;

import java.io.Serializable;

/**
 * Created by agrabovskis on 2018-08-08.
 */
public class SamlUserDetails implements Serializable {

    private String tenantName;
    private String userId;
    private String emailAddress;
    private String spEntityId;
    private String idpEntityId;
    private boolean authenticated;
    private Long userIdInFieldId;

    public SamlUserDetails(String tenantName, String userId, String emailAddress, String spEntityId, String idpEntityId, boolean authenticated) {
        this.tenantName = tenantName;
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.spEntityId = spEntityId;
        this.idpEntityId = idpEntityId;
        this.authenticated = authenticated;
        userIdInFieldId = null;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSpEntityId() {
        return spEntityId;
    }

    public void setSpEntityId(String spEntityId) {
        this.spEntityId = spEntityId;
    }

    public String getIdpEntityId() {
        return idpEntityId;
    }

    public void setIdpEntityId(String idpEntityId) {
        this.idpEntityId = idpEntityId;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Long getUserIdInFieldId() {
        return userIdInFieldId;
    }

    public void setUserIdInFieldId(Long userIdInFieldId) {
        this.userIdInFieldId = userIdInFieldId;
    }
}
