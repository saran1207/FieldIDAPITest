package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "prioritycode")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PriorityCode extends ArchivableEntityWithTenant implements Listable<Long>, Saveable {
    private static final long serialVersionUID = 1L;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="auto_schedule")
    private PriorityCodeAutoScheduleType autoSchedule;

    @Column(name="auto_schedule_custom_days")
    private Integer autoScheduleCustomDays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public PriorityCodeAutoScheduleType getAutoSchedule() {
        return autoSchedule;
    }

    public void setAutoSchedule(PriorityCodeAutoScheduleType autoSchedule) {
        this.autoSchedule = autoSchedule;
    }

    public Integer getAutoScheduleCustomDays() {
        return autoScheduleCustomDays;
    }

    public void setAutoScheduleCustomDays(Integer autoScheduleCustomDays) {
        this.autoScheduleCustomDays = autoScheduleCustomDays;
    }

}
