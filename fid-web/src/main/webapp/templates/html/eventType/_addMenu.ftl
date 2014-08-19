<div style="position: absolute;right: 12px;top: 51px;">
    <#if securityGuard.isInspectionsEnabled() || securityGuard.isLotoEnabled()>
        <div class="dropdown-btn">
            <a class="btn btn-secondary btn-sml" href="#">Add Event Type<img src="/fieldid/images/menu-down.png"></a>
            <ul class="pull-right">
                <#if securityGuard.isInspectionsEnabled()>
                    <li><a href="/fieldid/eventTypeAdd.action?newEventType=Asset"><@s.text name="label.asset_event"/></a></li>
                    <li><a href="/fieldid/eventTypeAdd.action?newEventType=Place"><@s.text name="label.place_event"/></a></li>
                    <li><a href="/fieldid/eventTypeAdd.action?newEventType=Action"><@s.text name="label.action"/></a></li>
                </#if>
                <#if securityGuard.isLotoEnabled()>
                    <li><a href="/fieldid/eventTypeAdd.action?newEventType=ProcedureAudit"><@s.text name="label.procedure_audit"/></a></li>
                </#if>
            </ul>
        </div>
    </#if>
</div>
