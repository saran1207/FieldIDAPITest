package com.n4systems.fieldid.wicket.components.org;


import com.google.common.base.Preconditions;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class OrgLocationModel extends LoadableDetachableModel<EntityWithTenant> {

    private IModel<BaseOrg> orgModel;
    private IModel<PredefinedLocation> locationModel;
    private EntityWithTenant obj;

    public OrgLocationModel(IModel<BaseOrg> orgModel, IModel<PredefinedLocation> locationModel) {
        Preconditions.checkNotNull(orgModel,"you must supply valid BaseOrg model");
        this.orgModel = orgModel;
        this.setLocationModel(locationModel);
        setInitialValue();
    }

    private void setInitialValue() {
        if (getLocationModel() !=null && getLocationModel().getObject()!=null) {
            setObject(getLocationModel().getObject());
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
            getLocationModel().detach();
        }
        super.detach();
    }

    private void setOrg(BaseOrg org) {
        orgModel.setObject(org);
    }

    private void setLocation(PredefinedLocation location) {
        if (getLocationModel() !=null) {
            getLocationModel().setObject(location);
        }
    }

    @Override
    protected EntityWithTenant load() {
        return obj;
    }

    @Override
    public void setObject(EntityWithTenant object) {
        this.obj = object;
        if (obj instanceof BaseOrg) {
            setOrg((BaseOrg) obj);
            setLocation(null);
        } else if (obj instanceof PredefinedLocation) {
            setLocation((PredefinedLocation) obj);
            setOrg(null);
        }
        super.setObject(object);
    }

    @Override
    public EntityWithTenant getObject() {
        return super.getObject();
    }

    @Override
    protected void onDetach() {
        getLocationModel().detach();
        orgModel.detach();
        super.onDetach();
    }

    public IModel<PredefinedLocation> getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(IModel<PredefinedLocation> locationModel) {
        this.locationModel = locationModel;
    }
}
