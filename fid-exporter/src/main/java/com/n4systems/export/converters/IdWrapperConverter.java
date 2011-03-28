package com.n4systems.export.converters;

import javax.persistence.EntityManager;

import com.n4systems.export.IdWrapper;
import com.n4systems.export.PersistenceTask;
import com.n4systems.export.ReadonlyTransactionExecutor;
import com.n4systems.model.Asset;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IdWrapperConverter extends ExportConverter {
	private final long tenantId;
	private final int masterAssetCount;
	private int currentAsset = 0;

	public IdWrapperConverter(long tenantId, int masterAssetCount) {
		this.tenantId = tenantId;
		this.masterAssetCount = masterAssetCount;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(IdWrapper.class);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, final MarshallingContext context) {
		final IdWrapper assetId = (IdWrapper) source;
		
		currentAsset++;
		System.out.println(String.format("Converting %d/%d", currentAsset, masterAssetCount));
		
		try {
			ReadonlyTransactionExecutor.execute(new PersistenceTask() {
				@Override
				public void runTask(EntityManager em) throws Exception {
					Asset asset = em.find(Asset.class, assetId.getId());
					if (asset.getTenant().getId() != tenantId) {
						throw new SecurityException("TenantId [" + tenantId + "] did not match Asset [" + asset + "]");
					}
					
					context.convertAnother(asset, new MasterAssetConverter(em));
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
