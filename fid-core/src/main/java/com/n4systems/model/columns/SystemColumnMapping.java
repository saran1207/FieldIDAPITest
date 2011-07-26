package com.n4systems.model.columns;

import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="system_column_mappings")
@PrimaryKeyJoinColumn(name="column_id")
public class SystemColumnMapping extends ColumnMapping implements UnsecuredEntity {

}
