package com.n4systems.fieldid.service.pentaho;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.pentaho.PentahoTest;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rrana on 2016-03-09.
 */
public class PentahoService extends FieldIdPersistenceService {

    private static final Logger logger=Logger.getLogger(PentahoService.class);

    public List<String> getPerformedByList() {
        QueryBuilder<String> query = new QueryBuilder<String>(PentahoTest.class, new OpenSecurityFilter());
        query.setSimpleSelect("performedby");
        List<String> list = persistenceService.findAll(query);

        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getLocation() {
        QueryBuilder<String> query = new QueryBuilder<String>(PentahoTest.class, new OpenSecurityFilter());
        query.setSimpleSelect("location");
        List<String> list = persistenceService.findAll(query);

        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getEventStatus() {
        QueryBuilder<String> query = new QueryBuilder<String>(PentahoTest.class, new OpenSecurityFilter());
        query.setSimpleSelect("eventstatus");
        List<String> list = persistenceService.findAll(query);

        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getEventResult() {
        QueryBuilder<String> query = new QueryBuilder<String>(PentahoTest.class, new OpenSecurityFilter());
        query.setSimpleSelect("event_result");
        List<String> list = persistenceService.findAll(query);

        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getEventType() {
        QueryBuilder<String> query = new QueryBuilder<String>(PentahoTest.class, new OpenSecurityFilter());
        query.setSimpleSelect("eventtype");
        List<String> list = persistenceService.findAll(query);

        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getAssetType() {
        QueryBuilder<String> query = new QueryBuilder<String>(PentahoTest.class, new OpenSecurityFilter());
        query.setSimpleSelect("assettype");
        List<String> list = persistenceService.findAll(query);

        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getAssetStatus() {
        QueryBuilder<String> query = new QueryBuilder<String>(PentahoTest.class, new OpenSecurityFilter());
        query.setSimpleSelect("assetstatus");
        List<String> list = persistenceService.findAll(query);

        return list.stream().distinct().collect(Collectors.toList());
    }

}
