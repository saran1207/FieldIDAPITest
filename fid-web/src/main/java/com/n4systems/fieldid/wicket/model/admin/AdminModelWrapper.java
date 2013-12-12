package com.n4systems.fieldid.wicket.model.admin;

import com.n4systems.fieldid.service.admin.AdminSecurityService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.concurrent.Callable;

public class AdminModelWrapper<T> implements IModel<T> {

	@SpringBean
	private AdminSecurityService adminSecurityService;

	private Long tenantId;
	private IModel<T> model;

	public AdminModelWrapper(Long tenantId, IModel<T> model) {
		Injector.get().inject(this);

		this.tenantId = tenantId;
		this.model = model;
	}

	@Override
	public T getObject() {
		return adminSecurityService.executeUnderTenant(tenantId, new Callable<T>() {
			@Override
			public T call() throws Exception {
				return model.getObject();
			}
		});
	}

	@Override
	public void setObject(T object) {
		model.setObject(object);
	}

	@Override
	public void detach() {
		model.detach();
	}
}
