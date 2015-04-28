package com.n4systems.model.procedure;

import com.n4systems.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This entity is used to describe Procedures that have an Assignee, but have not yet had notifications sent out to
 * inform the assignees of their work.
 *
 * Created by Jordan Heath on 15-04-27.
 */
@Entity
@Table(name = "procedure_notifications")
public class ProcedureNotification extends BaseEntity {

    @OneToOne
    @JoinColumn(name="procedure_id")
    private Procedure procedure;

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }
}
