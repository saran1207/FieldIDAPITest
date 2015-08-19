package com.n4systems.model;

import javax.persistence.*;

/**
 * Created by jheath on 2015-08-18.
 */
@Entity
@Table(name = "escalation_rule_asset_linker")
public class EscalationRuleAssetLinker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rule_id")
    private AssignmentEscalationRule rule;

    @Column(name = "rule_has_run")
    private boolean ruleHasRun;
}
