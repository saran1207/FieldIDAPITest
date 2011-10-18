package com.n4systems.fieldid.wicket.components.dashboard.subcomponents;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class LinkToJob extends Panel {

    public LinkToJob(String id, IModel<Long> jobId) {
        super(id);
        add(new NonWicketLink("jobLink", "job.action?uniqueID="+jobId.getObject()));
        
    }

}
