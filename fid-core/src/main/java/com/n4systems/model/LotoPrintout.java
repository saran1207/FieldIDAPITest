package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

/**
 * This is the JPA Entity for the loto_printouts table.
 *
 * Created by jheath on 2014-10-22.
 */
@Entity
@Table(name = "loto_printouts")
public class LotoPrintout extends EntityWithTenant implements Listable<Long> {

    public LotoPrintout() {
        super();
    }

    @Column(name = "printout_name")
    private String printoutName;

    @Column(name = "printout_type")
    @Enumerated(EnumType.STRING)
    private LotoPrintoutType printoutType;

    @Column(name = "selected")
    private boolean selected;

    //Test constructor
    public LotoPrintout(String printoutName) {
        this.printoutName = printoutName;
    }

    public String getPrintoutName() {
        return printoutName;
    }

    public void setPrintoutName(String printoutName) {
        this.printoutName = printoutName;
    }

    public LotoPrintoutType getPrintoutType() {
        return printoutType;
    }

    public void setPrintoutType(LotoPrintoutType printoutType) {
        this.printoutType = printoutType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPrintoutTypeLabel() {
        if(printoutType == null) {
            return null;
        } else {
            return printoutType.getLabel();
        }
    }

    public void setPrintoutTypeLabel(String label) {
        if (label.equals(printoutType.SHORT.getLabel())) {
            printoutType = printoutType.SHORT;
        } else if (label.equals(printoutType.LONG.getLabel())) {
            printoutType = printoutType.LONG;
        }
    }

    @Override
    public String getDisplayName() {
        return printoutName;
    }
}
