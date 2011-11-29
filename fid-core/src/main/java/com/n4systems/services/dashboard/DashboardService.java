package com.n4systems.services.dashboard;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;

public class DashboardService extends FieldIdPersistenceService {

    @Transactional(readOnly = true)
    public DashboardLayout findLayout() {
        DashboardLayout layout = findCurrentLayout();

        if (layout == null) {
            layout = createDefaultLayout();
        }

        return layout;
    }

    @Transactional
    public void saveLayout(DashboardLayout layout) {
        if (layout.getId() != null) {
            persistenceService.update(layout);
        } else {
            layout.setUser(findCurrentUser());
            persistenceService.save(layout);
        }
    }

    private DashboardLayout findCurrentLayout() {
        QueryBuilder<DashboardLayout> query = createUserSecurityBuilder(DashboardLayout.class);
        query.addSimpleWhere("user.id", securityContext.getUserSecurityFilter().getUserId());

        return persistenceService.find(query);
    }

    private User findCurrentUser() {
        QueryBuilder<User> query = createUserSecurityBuilder(User.class);
        query.addSimpleWhere("id", securityContext.getUserSecurityFilter().getUserId());

        return persistenceService.find(query);
    }

    private DashboardLayout createDefaultLayout() {
        DashboardLayout layout = new DashboardLayout();
        DashboardColumn firstColumn = new DashboardColumn();
        DashboardColumn secondColumn = new DashboardColumn();
        layout.getColumns().add(firstColumn);
        layout.getColumns().add(secondColumn);
        return layout;
    }

}
