package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.timeline.TimePointInfoProvider;
import com.n4systems.fieldid.wicket.components.timeline.TimelinePanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPointResult;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class ProcedureResultsPage extends FieldIDFrontEndPage {

    @SpringBean PersistenceService persistenceService;

    private IModel<Procedure> procedureModel;

    public ProcedureResultsPage(PageParameters params) {
        Long procedureId = params.get("id").toLong();
        procedureModel = new EntityModel<Procedure>(Procedure.class, procedureId);


        PropertyModel<List<IsolationPointResult>> lockResults = ProxyModel.of(procedureModel, on(Procedure.class).getLockResults());
        PropertyModel<List<IsolationPointResult>> unlockResults = ProxyModel.of(procedureModel, on(Procedure.class).getUnlockResults());

        add(new TimelinePanel("lockResults", lockResults, new IsolationPointResultTimePointProvider(true)));
    }

    class IsolationPointResultTimePointProvider implements TimePointInfoProvider<IsolationPointResult> {

        private final boolean lock;
        IsolationPointResultTimePointProvider(boolean lock) { this.lock=lock; }

        @Override
        public Date getDate(IsolationPointResult item) {
            return item.getCheckCheckTime();
        }

        @Override
        public String getTitle(IsolationPointResult item) {
            return item.getIsolationPoint().getIdentifier() + " " + (lock ? getString("label.locked_out") : getString("label.unlocked"));
        }

        @Override
        public String getText(IsolationPointResult item) {
            return item.getIsolationPoint().getMethod();
        }

        @Override
        public String getUrl(IsolationPointResult item) {
            return "http://www.fieldid.com/assets/images/video/fieldid-overview-base-large.png";
        }
    }

}
