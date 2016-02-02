package com.n4systems.fieldid.ws.v1.resources.event.criteria;

import java.net.URL;

/**
 * Created by rrana on 2016-02-02.
 *
 * This DTO is used to send information to the mobile app.
 *
 * There is a seperate upload service for mobile to use when syncing images back up to web.
 * ApiCriteriaImageResource.java
 */
public class ApiCriteriaImageDownload {
    private String sid;
    private String criteriaResultSid;
    private String comments;
    private URL image;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCriteriaResultSid() {
        return criteriaResultSid;
    }

    public void setCriteriaResultSid(String criteriaResultSid) {
        this.criteriaResultSid = criteriaResultSid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }
}
