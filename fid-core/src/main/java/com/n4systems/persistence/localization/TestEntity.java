package com.n4systems.persistence.localization;

import com.n4systems.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="test")
public class TestEntity extends BaseEntity {

    @AttributeOverrides( {
            @AttributeOverride(name=LocalizedText.TEXT_COLUMN, column = @Column(name="name") ),
            @AttributeOverride(name=LocalizedText.LOCALIZATION_ID_COLUMN, column = @Column(name="lid"))
    } )
    @Embedded
    private LocalizedText name;

    public void setName(LocalizedText name) {
        this.name = name;
    }

    public LocalizedText getName() {
        return name;
    }
}





