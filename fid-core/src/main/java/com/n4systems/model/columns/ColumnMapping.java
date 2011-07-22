package com.n4systems.model.columns;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.parents.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "column_mappings")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ColumnMapping extends AbstractEntity implements Comparable<ColumnMapping>{
	
    @Column(name="label")
    private String label;

    @Column(name="path_expression")
    private String pathExpression;

    @Column(name="sort_expression")
    private String sortExpression;

    @Column(name="sortable")
    private boolean sortable;

    @Column(name="join_expression")
    private String joinExpression;

    @Column(name="output_handler")
    private String outputHandler;

    @Column(name="default_order")
    private Integer defaultOrder;

    @Column(name="name")
    private String name;

    @Column(name="required_extended_feature")
    private ExtendedFeature requiredExtendedFeature;

    @Column(name="excluded_by_extended_feature")
    private ExtendedFeature excludedByExtendedFeature;

    @ManyToOne
    @JoinColumn(name="group_id")
    private ColumnMappingGroup group;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPathExpression() {
        return pathExpression;
    }

    public void setPathExpression(String pathExpression) {
        this.pathExpression = pathExpression;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public String getOutputHandler() {
        return outputHandler;
    }

    public void setOutputHandler(String outputHandler) {
        this.outputHandler = outputHandler;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getSortExpression() {
        return sortExpression;
    }

    public void setSortExpression(String sortExpression) {
        this.sortExpression = sortExpression;
    }

    public ColumnMappingGroup getGroup() {
        return group;
    }

    public void setGroup(ColumnMappingGroup group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExtendedFeature getRequiredExtendedFeature() {
        return requiredExtendedFeature;
    }

    public void setRequiredExtendedFeature(ExtendedFeature requiredExtendedFeature) {
        this.requiredExtendedFeature = requiredExtendedFeature;
    }

    public String getJoinExpression() {
        return joinExpression;
    }

    public void setJoinExpression(String joinExpression) {
        this.joinExpression = joinExpression;
    }
    
    public int compareTo(ColumnMapping other) {
		return (defaultOrder < other.defaultOrder ? -1 : (defaultOrder == other.defaultOrder ? id.compareTo(other.id) : 1));
    }
    
	public ExtendedFeature getExcludedByExtendedFeature() {
		return excludedByExtendedFeature;
	}

	public void setExcludedByExtendedFeature(
			ExtendedFeature excludedByExtendedFeature) {
		this.excludedByExtendedFeature = excludedByExtendedFeature;
	}
}
