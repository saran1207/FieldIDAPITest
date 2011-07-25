package com.n4systems.exporting;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.api.conversion.users.UserToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.model.UserViewBuilder;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.tenant.UserLimits;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.UserType;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.utils.email.WelcomeNotifier;

public class UserImporterTest {
	private static final String TIME_ZONE_ID = "timeZoneId";

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_collaboration() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(new UserViewBuilder().withDefaultValues().withFirstName("joe").build());

		Transaction transaction = new DummyTransaction();
		User user = UserBuilder.anEmployee().build();

		UserImporter importer = new UserImporter(reader, validator, null, saver, converter, notifier, TIME_ZONE_ID) {
			@Override
			List<UserView> getViews() {
				return userViews;
			};
		};

		expect(converter.toModel(userViews.get(0), transaction)).andReturn(user);
		expect(saver.saveOrUpdate(user)).andReturn(user);
		reader.close();

		replay(reader);
		replay(saver);
		replay(notifier);
		replay(validator);
		replay(converter);

		importer.runImport(transaction);

		verify(validator);
		verify(converter);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_send_email() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(new UserViewBuilder().withDefaultValues().withSendWelcomeEmail("Y")
				.withFirstName("joe").build());

		Transaction transaction = new DummyTransaction();
		User user = UserBuilder.anEmployee().build();

		UserImporter importer = new UserImporter(reader, validator, null, saver, converter, notifier, TIME_ZONE_ID) {
			@Override
			List<UserView> getViews() {
				return userViews;
			};
		};

		expect(converter.toModel(userViews.get(0), transaction)).andReturn(user);
		expect(saver.saveOrUpdate(user)).andReturn(user);
		notifier.sendWelcomeNotificationTo(user);
		reader.close();

		replay(reader);
		replay(saver);
		replay(notifier);
		replay(validator);
		replay(converter);

		importer.runImport(transaction);

		verify(validator);
		verify(converter);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_invalid_account() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		OrgByNameLoader loader = createMock(OrgByNameLoader.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		// should let bad account pass thru...responsiblity of
		// accountTypeValidator to catch this.
		final List<UserView> userViews = Lists.newArrayList(new UserViewBuilder().withDefaultValues().withAccountType("badAccountType")
				.build());

		Transaction transaction = new DummyTransaction();
		User user = UserBuilder.anEmployee().build();

		UserImporter importer = createImporter(reader, saver, notifier, validator, converter, userViews, 50, 50, 50);

		expect(converter.toModel(userViews.get(0), transaction)).andReturn(user);
		expect(saver.saveOrUpdate(user)).andReturn(user);
		notifier.sendWelcomeNotificationTo(user);
		reader.close();

		replay(reader);
		replay(saver);
		replay(loader);
		replay(notifier);
		replay(validator);
		replay(converter);

		importer.runImport(transaction);

		verify(validator);
		verify(converter);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_limited() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(new UserViewBuilder().withDefaultValues().withUserId("a").build(),
				new UserViewBuilder().withDefaultValues().withUserId("b").withAccountType(UserType.READONLY).build(), new UserViewBuilder()
						.withDefaultValues().withUserId("c").withAccountType(UserType.LITE).build());

		User user = UserBuilder.anEmployee().build();

		UserImporter importer = createImporter(reader, saver, notifier, validator, converter, userViews, 0, 0, 0);

		expect(validator.validate(userViews.get(0), 2)).andReturn(new ArrayList<ValidationResult>());
		expect(validator.validate(userViews.get(1), 3)).andReturn(new ArrayList<ValidationResult>());
		expect(validator.validate(userViews.get(2), 4)).andReturn(new ArrayList<ValidationResult>());
		notifier.sendWelcomeNotificationTo(user);
		reader.close();

		replay(reader);
		replay(saver);
		replay(notifier);
		replay(validator);
		replay(converter);

		List<ValidationResult> results = importer.readAndValidate();

		assertEquals(3, results.size());

		assertEquals("You can not import more than 0 employee (full) users. (Attempted to import 1)", results.get(2).getMessage());
		assertEquals("You can not import more than 0 read only users. (Attempted to import 1)", results.get(1).getMessage());
		assertEquals("You can not import more than 0 lite users. (Attempted to import 1)", results.get(0).getMessage());

		verify(validator);
		verify(converter);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_unlimited() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(new UserViewBuilder().withDefaultValues().withUserId("a").build(),
				new UserViewBuilder().withDefaultValues().withUserId("b").withAccountType(UserType.READONLY).build(), new UserViewBuilder()
						.withDefaultValues().withUserId("c").withAccountType(UserType.LITE).build());

		User user = UserBuilder.anEmployee().build();

		UserImporter importer = createImporter(reader, saver, notifier, validator, converter, userViews, -1, -1, -1);

		expect(validator.validate(userViews.get(0), 2)).andReturn(new ArrayList<ValidationResult>());
		expect(validator.validate(userViews.get(1), 3)).andReturn(new ArrayList<ValidationResult>());
		expect(validator.validate(userViews.get(2), 4)).andReturn(new ArrayList<ValidationResult>());
		notifier.sendWelcomeNotificationTo(user);
		reader.close();

		replay(reader);
		replay(saver);
		replay(notifier);
		replay(validator);
		replay(converter);

		List<ValidationResult> results = importer.readAndValidate();

		assertEquals(0, results.size());

		verify(validator);
		verify(converter);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_duplicate() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(new UserViewBuilder().withDefaultValues().withUserId("SAMEUSERID").build(),
				new UserViewBuilder().withDefaultValues().withUserId("SAMEUSERID").build());

		User user = UserBuilder.anEmployee().build();

		UserImporter importer = createImporter(reader, saver, notifier, validator, converter, userViews, -1, -1, -1);

		expect(validator.validate(userViews.get(0), 2)).andReturn(new ArrayList<ValidationResult>());
		expect(validator.validate(userViews.get(1), 3)).andReturn(new ArrayList<ValidationResult>());
		notifier.sendWelcomeNotificationTo(user);
		reader.close();

		replay(reader);
		replay(saver);
		replay(notifier);
		replay(validator);
		replay(converter);

		List<ValidationResult> results = importer.readAndValidate();

		assertEquals(1, results.size());
		assertEquals("Username SAMEUSERID is duplicated", results.get(0).getMessage());

		verify(validator);
		verify(converter);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_null_userid() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(new UserViewBuilder().withDefaultValues().withUserId(null).build());

		User user = UserBuilder.anEmployee().build();

		UserImporter importer = createImporter(reader, saver, notifier, validator, converter, userViews, -1, -1, -1);

		expect(validator.validate(userViews.get(0), 2)).andReturn(new ArrayList<ValidationResult>());
		notifier.sendWelcomeNotificationTo(user);
		reader.close();

		replay(reader);
		replay(saver);
		replay(notifier);
		replay(validator);
		replay(converter);

		List<ValidationResult> results = importer.readAndValidate();

		verify(validator);
		verify(converter);
	}

	private UserImporter createImporter(MapReader reader, UserSaver saver, WelcomeNotifier notifier,
			Validator<ExternalModelView> validator, UserToModelConverter converter, final List<UserView> userViews,
			final int employeesLimit, final int liteUsersLimit, final int readOnlyUsersLimit) {

		UserLimits settings = new UserLimits(employeesLimit, liteUsersLimit, readOnlyUsersLimit);
		UserImporter importer = new UserImporter(reader, validator, settings, saver, converter, notifier, TIME_ZONE_ID) {
			@Override
			List<UserView> getViews() {
				return userViews;
			};
		};
		return importer;
	}

}
