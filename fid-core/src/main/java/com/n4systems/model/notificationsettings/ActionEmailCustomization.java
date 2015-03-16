package com.n4systems.model.notificationsettings;

import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This table is used to store customized Subject and Sub Heading values for the HTML Action Assigned notification
 * emails.
 *
 * Created by Jordan Heath on 15-03-12.
 */
@Entity
@Table(name = "action_email_customization")
public class ActionEmailCustomization extends EntityWithTenant {

    //TODO You need to provide default values for these...
    //Try to do this the same way it was done for Procedure Definitions... that way if one is null, the other two
    //don't get wiped.  Screw worrying about it for now... your head is not clear.
    @Column(name="email_subject", nullable=true)
    private String emailSubject;

    @Column(name="sub_heading", nullable=true)
    private String subHeading;

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }
}
