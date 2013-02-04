package com.n4systems.services.dashboard;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithUser;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        query.addSimpleWhere("selected", true);

        return persistenceService.find(query);
    }

    public List<DashboardLayout> findDashboardLayouts(boolean excludeSelected) {
        QueryBuilder<DashboardLayout> query = createUserSecurityBuilder(DashboardLayout.class);
        query.addSimpleWhere("user.id", securityContext.getUserSecurityFilter().getUserId());

        if(excludeSelected) {
            query.addSimpleWhere("selected", false);
        }

        return persistenceService.findAll(query);
    }

    public DashboardLayout getDashboardLayout(Long id) {
        return persistenceService.find(DashboardLayout.class, id);
    }

    public void delete(DashboardLayout layout) {
        persistenceService.remove(layout);
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
        layout.setName(getCurrentUser().getDisplayName());
        layout.setSelected(true);

        return layout;
    }

    public WidgetDefinition createWidgetDefinition(WidgetType type) {
        WidgetDefinition def = new WidgetDefinition(type);
        if (def.getConfig() instanceof ConfigurationWithUser) {
            ConfigurationWithUser config = (ConfigurationWithUser) def.getConfig();
            config.setUser(getCurrentUser());
        }
        return def;
    }
}
