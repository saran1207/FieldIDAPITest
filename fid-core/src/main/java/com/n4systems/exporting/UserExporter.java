package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.users.UserToViewConverter;
import com.n4systems.api.model.UserView;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.ListLoader;

public class UserExporter implements Exporter {
	private final ExportMapMarshaller<UserView> marshaller;
	private final ListLoader<User> userLoader;
	private final ModelToViewConverter<User, UserView> converter;

	public UserExporter(ListLoader<User> eventLoader, ExportMapMarshaller<UserView> marshaller, ModelToViewConverter<User, UserView> converter) {
		this.userLoader = eventLoader;
		this.marshaller = marshaller;
		this.converter = converter;
	}
	
	public UserExporter(ListLoader<User> userListLoader) {
		this(userListLoader, new ExportMapMarshaller<UserView>(UserView.class), new UserToViewConverter());
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		UserView view;
		
		for (User event : userLoader.load()) {
			try {
				view = converter.toView(event);
				mapWriter.write(marshaller.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export user [%s]", event), e);
			}
		}
	}

}
