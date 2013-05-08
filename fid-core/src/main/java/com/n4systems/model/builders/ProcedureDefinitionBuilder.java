package com.n4systems.model.builders;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.model.Asset;
import com.n4systems.model.Tenant;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.user.User;

import java.util.Date;
import java.util.List;
import java.util.Set;


public class ProcedureDefinitionBuilder extends EntityWithTenantBuilder<ProcedureDefinition> {

    private Asset asset;
    private String procedureCode;
    private String electronicIdentifier;
    private Long revisionNumber;
    private String warnings;
    private boolean completeIsolationPointInOrder;
    private User developedBy;
    private String equipmentNumber;
    private String equipmentLocation;
    private String building;
    private String equipmentDescription;
    private PublishedState publishedState = PublishedState.DRAFT;
    private List<IsolationPoint> isolationPoints = Lists.newArrayList();
    private Set<ProcedureDefinitionImage> images = Sets.newHashSet();
    private Date originDate;
    private Date retireDate;
    private User approvedBy;
    private boolean authorizationNotificationSent = false;


    public static ProcedureDefinitionBuilder aProcedureDefinition() {
        return new ProcedureDefinitionBuilder();
    }

    public ProcedureDefinitionBuilder() {
        this(TenantBuilder.n4(),
                AssetBuilder.anAsset().withId(1L).build(),
                "procedureCode"
                );
    }

    private ProcedureDefinitionBuilder(Tenant tenant, Asset asset, String procedureCode) {
        this.tenant = tenant;
        this.asset = asset;
        this.procedureCode = procedureCode;
    }

    public ProcedureDefinitionBuilder withAsset(Asset asset) {
        this.asset = asset;
        return this;
    }

    public ProcedureDefinitionBuilder withIsolationPoints(List<IsolationPoint> isolationPoints) {
        this.isolationPoints = isolationPoints;
        images = Sets.newHashSet();
        for (IsolationPoint isolationPoint:isolationPoints) {
            if (isolationPoint.getAnnotation()!=null) {
                images.add((ProcedureDefinitionImage) isolationPoint.getAnnotation().getImage());
            }
        }
        return this;
    }

    public ProcedureDefinitionBuilder withRevisionNumber(Long revisionNumber) {
        this.revisionNumber = revisionNumber;
        return this;
    }

    @Override
    public ProcedureDefinition createObject() {
        ProcedureDefinition procedureDefinition = assignAbstractFields(new ProcedureDefinition());
        procedureDefinition.setAsset(asset);
        procedureDefinition.setProcedureCode(procedureCode);
        procedureDefinition.setIsolationPoints(isolationPoints);
        procedureDefinition.setImages(Lists.newArrayList(images.iterator()));
        procedureDefinition.setRevisionNumber(revisionNumber);
        return procedureDefinition;
    }

}

