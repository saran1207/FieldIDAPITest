package com.fieldid.data;

import com.fieldid.data.service.*;
import com.n4systems.fieldid.config.FieldIdCoreConfig;
import com.n4systems.fieldid.config.FieldIdDownloadConfig;
import com.n4systems.fieldid.config.FieldIdEntityRemovalConfig;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.Objects;

@SpringBootApplication
@Import({FieldIdCoreConfig.class, FieldIdDownloadConfig.class, FieldIdEntityRemovalConfig.class})
public class Application implements CommandLineRunner {
	private static Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) {
		System.setProperty("persistence.unit", "fieldid-cli");
		SpringApplication.run(Application.class, args);
	}

	@Autowired private PersistenceService persistenceService;
	@Autowired private EventTypeMigrator eventTypeMigrator;
	@Autowired private EventTypeGroupMigrator eventTypeGroupMigrator;
	@Autowired private ObservationCountGroupMigrator observationCountGroupMigrator;
	@Autowired private ButtonGroupMigrator buttonGroupMigrator;
	@Autowired private ScoreGroupMigrator scoreGroupMigrator;
	@Autowired private PrintOutMigrator printOutMigrator;

	private void help() {
		System.out.println("Help: java -jar this.jar <eventtype|eventtypegroup|observationcountgroup|buttongroup|scoregroup|printout> <from tenant> <to tenant> <name> [new name]");
		System.exit(1);
	}

	@Override
	public void run(String...args) throws Exception {
		if (args.length < 4) help();

		MigrationAction action = null;
		switch (args[0].toLowerCase()) {
			case "eventtype":
				action = MigrationAction.COPY_EVENT_TYPE;
				break;
			case "eventtypegroup":
				action = MigrationAction.COPY_EVENT_TYPE_GROUP;
				break;
			case "observationcountgroup":
				action = MigrationAction.COPY_OBSERVATION_COUNT_GROUP;
				break;
			case "buttongroup":
				action = MigrationAction.COPY_BUTTON_GROUP;
				break;
			case "scoregroup":
				action = MigrationAction.COPY_SCORE_GROUP;
				break;
			case "printout":
				action = MigrationAction.COPY_PRINT_OUT;
				break;
			default:
				logger.error("Unknown option: " + args[0]);
				help();
				break;
		}

		Tenant fromTenant = findByName(Tenant.class, args[1]);
		Tenant newTenant =  args[2].equalsIgnoreCase(args[1]) ? null : findByName(Tenant.class, args[2]);
		String name = args[3];
		String newName = (args.length >= 5) ? args[4] : null;

		runAction(action, fromTenant, newTenant, name, newName);
		logger.info("Done!");
		System.exit(0);
	}

	private <T> T findByName(Class<T> clazz, String name) {
		return findByName(clazz, name, null);
	}

	private <T> T findByName(Class<T> clazz, String name, Tenant tenant) {
		QueryBuilder<T> query = new QueryBuilder<>(clazz, (tenant != null) ? new TenantOnlySecurityFilter(tenant) : new OpenSecurityFilter());
		query.addWhere(WhereClauseFactory.create("name", name));
		T t = persistenceService.find(query);
		Objects.requireNonNull(t, "Unable to find " + clazz.getSimpleName() + " for name: " + name + ((tenant != null) ? " under Tenant: " + tenant.getName() : ""));
		return t;
	}

	private void runAction(MigrationAction action, Tenant tenant, Tenant newTenant, String name, String newName) {
		Long newTenantId = (newTenant != null) ? newTenant.getId() : null;
		Class<? extends BaseEntity> targetType;
		DataMigrator<?> migrator;
		switch (action) {
			case COPY_EVENT_TYPE:
				migrator = eventTypeMigrator;
				targetType = EventType.class;
				break;
			case COPY_EVENT_TYPE_GROUP:
				migrator = eventTypeGroupMigrator;
				targetType = EventTypeGroup.class;
				break;
			case COPY_OBSERVATION_COUNT_GROUP:
				migrator = observationCountGroupMigrator;
				targetType = ObservationCountGroup.class;
				break;
			case COPY_BUTTON_GROUP:
				migrator = buttonGroupMigrator;
				targetType = ButtonGroup.class;
				break;
			case COPY_SCORE_GROUP:
				migrator = scoreGroupMigrator;
				targetType = ScoreGroup.class;
				break;
			case COPY_PRINT_OUT:
				migrator = printOutMigrator;
				targetType = PrintOut.class;
				break;
			default:
				throw new IllegalArgumentException(action.name());
		}

		migrator.copy(findByName(targetType, name, tenant).getId(), newTenantId, newName);
	}
}
