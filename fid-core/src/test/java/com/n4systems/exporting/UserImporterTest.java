package com.n4systems.exporting;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.api.conversion.users.UserToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.model.UserViewBuilder;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.utils.email.WelcomeNotifier;

public class UserImporterTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_import_collaboration() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		OrgByNameLoader loader = createMock(OrgByNameLoader.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(
				new UserViewBuilder().withDefaultValues().withFirstName("joe").build()
				);
		
		Transaction transaction = new DummyTransaction();
		User user = UserBuilder.anEmployee().build();
		
		UserImporter importer = new UserImporter(reader, validator, saver, converter,  loader, notifier) { 
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
	public void test_import_send_email() throws Exception {
		MapReader reader = createMock(MapReader.class);
		UserSaver saver = createMock(UserSaver.class);
		OrgByNameLoader loader = createMock(OrgByNameLoader.class);
		WelcomeNotifier notifier = createMock(WelcomeNotifier.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		UserToModelConverter converter = createMock(UserToModelConverter.class);

		final List<UserView> userViews = Lists.newArrayList(
				new UserViewBuilder().withDefaultValues().withSendWelcomeEmail("Y").withFirstName("joe").build()
				);
		
		Transaction transaction = new DummyTransaction();
		User user = UserBuilder.anEmployee().build();
		
		UserImporter importer = new UserImporter(reader, validator, saver, converter,  loader, notifier) { 
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
		replay(loader);
		replay(notifier);
		replay(validator);
		replay(converter);
		
		importer.runImport(transaction);
		
		verify(validator);
		verify(converter);
	}
	
	
}
