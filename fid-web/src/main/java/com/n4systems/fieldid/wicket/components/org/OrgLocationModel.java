package com.n4systems.fieldid.wicket.components.org;


import com.google.common.base.Preconditions;
import com.n4systems.model.Asset;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

public class OrgLocationModel extends LoadableDetachableModel<EntityWithTenant> {

    private IModel<BaseOrg> orgModel;
    private IModel<PredefinedLocation> locationModel;
    private EntityWithTenant obj;
    private boolean isLocationModel;

    public OrgLocationModel(IModel<BaseOrg> orgModel) {
        Preconditions.checkNotNull(orgModel,"you must supply valid BaseOrg model");
        this.orgModel = orgModel;
        this.locationModel = null;
        setInitialValue();
    }

    public OrgLocationModel(IModel<BaseOrg> orgModel, IModel<PredefinedLocation> locationModel) {
        Preconditions.checkNotNull(orgModel,"you must supply valid BaseOrg model");
        this.orgModel = orgModel;
        this.locationModel = locationModel;
        this.isLocationModel = true;
        setInitialValue();
    }

    private void setInitialValue() {
        if (locationModel !=null && locationModel.getObject()!=null) {
            setObject(locationModel.getObject());
        } else if (orgModel!=null && orgModel.getObject()!=null) {
            setObject(orgModel.getObject());
        } else {
            setObject(null);
        }
    }

    @Override
    public void detach() {
        orgModel.detach();
        if (locationModel!=null) {
            locationModel.detach();
        }
        super.detach();
    }

    private void setOrg(BaseOrg org) {
        orgModel.setObject(org);
    }

    private void setLocation(PredefinedLocation location) {
        if (locationModel !=null) {
            locationModel.setObject(location);
        }
    }

    @Override
    protected EntityWithTenant load() {
        return obj;
    }

    @Override
    public void setObject(EntityWithTenant object) {
        this.obj = object;
        PredefinedLocation location = null;
        BaseOrg org = null;
        if (obj instanceof BaseOrg) {
            org = (BaseOrg) obj;
        } else if (obj instanceof PredefinedLocation) {
            location = (PredefinedLocation) obj;
            org = location.getBaseOrg();
        }
        setOrg(org);
        setLocation(location);
        if (isLocationModel) {
            super.setObject(location);
        } else
            super.setObject(org);
    }

    @Override
    public EntityWithTenant getObject() {
        return super.getObject();
    }

    @Override
    protected void onDetach() {
        if (locationModel!=null) {
            locationModel.detach();
        }
        orgModel.detach();
        super.onDetach();
    }

    public void setLocationModel(IModel<PredefinedLocation> locationModel) {
        this.locationModel = locationModel;
    }

    public BaseOrg getOrg() {
        return orgModel.getObject();
    }

    public PredefinedLocation getPredefinedLocation() {
        return locationModel!=null ? locationModel.getObject() : null;
    }
}
