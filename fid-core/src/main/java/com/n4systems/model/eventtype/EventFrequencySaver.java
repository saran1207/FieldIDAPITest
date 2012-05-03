package com.n4systems.model.eventtype;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.persistence.savers.Saver;

import javax.persistence.EntityManager;

public class EventFrequencySaver extends Saver<AssetTypeSchedule> {

    @Override
    public void save(EntityManager em, AssetTypeSchedule schedule) {
        updateAssetTypeModificationDates(em , schedule);
        super.save(em, schedule);
    }

    @Override
    public AssetTypeSchedule update(EntityManager em, AssetTypeSchedule schedule) {
        updateAssetTypeModificationDates(em , schedule);
        return super.update(em, schedule);
    }

    @Override
    public void remove(EntityManager em, AssetTypeSchedule schedule) {
        updateAssetTypeModificationDates(em , schedule);
        super.remove(em, schedule);
    }

    private void updateAssetTypeModificationDates(EntityManager em, AssetTypeSchedule schedule) {
        AssetType assetType = schedule.getAssetType();
        assetType.touch();
        em.merge(assetType);
    }

}
