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

    @Column(name = "printout_name")
    private String printoutName;

    @Column(name = "printout_type")
    @Enumerated(EnumType.STRING)
    private LotoPrintoutType printoutType;

    @Column(name = "selected")
    private boolean selected;

    @Column(name = "s3_path")
    private String s3Path;

    public LotoPrintout() {}

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

    public String getS3Path() {
        return s3Path;
    }

    public void setS3Path(String s3Path) {
        this.s3Path = s3Path;
    }

    @Override
    public String getDisplayName() {
        return printoutName;
    }
}
