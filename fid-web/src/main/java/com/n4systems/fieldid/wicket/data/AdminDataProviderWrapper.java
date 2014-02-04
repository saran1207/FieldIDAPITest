package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.admin.AdminSecurityService;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.concurrent.Callable;

public class AdminDataProviderWrapper<T> extends FieldIDDataProvider<T> {

	@SpringBean
	private AdminSecurityService adminSecurityService;

	private final Long tenantId;
	private final FieldIDDataProvider<T> dataProvider;

	public AdminDataProviderWrapper(Long tenantId, FieldIDDataProvider<T> dataProvider) {
		this.tenantId = tenantId;
		this.dataProvider = dataProvider;
	}

	@Override
	public Iterator<? extends T> iterator(final int first, final int count) {
		return adminSecurityService.executeUnderTenant(tenantId, new Callable<Iterator<? extends T>>() {
			@Override
			public Iterator<? extends T> call() throws Exception {
				return dataProvider.iterator(first, count);
			}
		});
	}

	@Override
	public int size() {
		return adminSecurityService.executeUnderTenant(tenantId, new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return dataProvider.size();
			}
		});
	}

	@Override
	public IModel<T> model(final T object) {
		return adminSecurityService.executeUnderTenant(tenantId, new Callable<IModel<T>>() {
			@Override
			public IModel<T> call() throws Exception {
				return dataProvider.model(object);
			}
		});
	}

	@Override
	public SortParam getSort() {
		return dataProvider.getSort();
	}

	@Override
	public void setSort(SortParam param) {
		dataProvider.setSort(param);
	}

	@Override
	public void setSort(String property, SortOrder order) {
		dataProvider.setSort(property, order);
	}

	@Override
	public void detach() {
		dataProvider.detach();
	}

}
