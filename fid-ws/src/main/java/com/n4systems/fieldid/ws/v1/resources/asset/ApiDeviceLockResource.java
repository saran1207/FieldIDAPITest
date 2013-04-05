package com.n4systems.fieldid.ws.v1.resources.asset;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.Asset;
import com.n4systems.util.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("deviceLock")
public class ApiDeviceLockResource extends SetupDataResource<ApiDeviceLock,Asset> {

    @Autowired private ApiAssetResource apiAssetResource;

    public ApiDeviceLockResource() {
        super(Asset.class, false);
    }

    @Override
    protected ApiDeviceLock convertEntityToApiModel(Asset asset) {
        ApiDeviceLock apiDeviceLock = new ApiDeviceLock();
        apiDeviceLock.setActive(asset.isActive());
        apiDeviceLock.setModified(asset.getModified());
        apiDeviceLock.setSid(asset.getMobileGUID());
        apiDeviceLock.setIdentifier(asset.getIdentifier());
        apiDeviceLock.setRfidNumber(asset.getRfidNumber());
        apiDeviceLock.setAttributeValues(apiAssetResource.findAllAttributeValues(asset));
        apiDeviceLock.setDevice(asset.getType().getGroup().isLotoDevice());
        apiDeviceLock.setLock(asset.getType().getGroup().isLotoLock());
        apiDeviceLock.setTypeId(asset.getType().getId());
        return apiDeviceLock;
    }

    @Override
    protected void addTermsToBuilder(QueryBuilder<Asset> builder) {
        WhereParameterGroup deviceOrLockTerm = new WhereParameterGroup("deviceOrLockTerm");
        deviceOrLockTerm.setChainOperator(WhereClause.ChainOp.AND);
        deviceOrLockTerm.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "type.group.lotoDevice", true, WhereClause.ChainOp.OR));
        deviceOrLockTerm.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "type.group.lotoLock", true, WhereClause.ChainOp.OR));
        builder.addWhere(deviceOrLockTerm);
    }

}
