package com.n4systems.fieldid.wicket.model.reporting;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class DynamicAssetColumnsModel extends FieldIDSpringModel<List<ColumnMappingView>> {

    @SpringBean
    private PersistenceManager persistenceManager;

    @Override
    protected List<ColumnMappingView> load() {
        return null;
    }

}
