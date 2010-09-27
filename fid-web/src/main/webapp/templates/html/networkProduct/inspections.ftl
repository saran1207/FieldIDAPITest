<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
    <@n4.includeStyle href="safetyNetworkProduct" type="page"/>
</head>

<#include '../vendor/_vendorinfo.ftl'/>

<div id="mainContent">

    <h1>Asset - ${product.serialNumber}</h1>

    <p></p><br/>

    <div class="registerThisNow">
        <p>
            <a href="#">Register this asset now</a><br/>
            By registering this asset you will be able to enter your own asset information and perform events.
        </p>
    </div>

    <p></p><br/>

    <ul class="options" id="innerList">
        <li>
            <a href="<@s.url action="showNetworkProduct" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.traceability"/>
            </a>
        </li>
        <li class="selected">
            <@s.text name="nav.inspections"/>
        </li>
        <li>
            <a href="<@s.url action="networkInspectionScheduleList" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.schedules"/>
            </a>
        </li>
    </ul>

    <div style="clear: both;"></div>

    <#if !inspections.isEmpty() >
        <table class="list">
            <tr>
                <th><@s.text name="label.date_performed"/></th>
                <th><@s.text name="label.inspectiontype"/></th>
                <th><@s.text name="label.result"/></th>
                <th><@s.text name="label.performed_by"/></th>
                <th><@s.text name="label.details"/></th>
            </tr>
            <#list inspections as inspection >
                <tr>
                    <td>${action.formatDateTime(inspection.date)}</td>
                    <td>${inspection.type.name}</td>
                    <td><@s.text name="${(inspection.status.label?html)!}"/></td>
                    <td>
                        <#assign tenant=inspection.tenant/>
                        <#include "../common/_displayTenantLogo.ftl"/>
                    </td>
                    <td>
                        <#include "_networkInspectionLink.ftl"/>
                    </td>
                </tr>
            </#list>
        </table>
    <#else>
        <div class="emptyList">
            <h2><@s.text name="label.noresults"/></h2>
            <p>
                <@s.text name="message.emptyinspectionlist" />
            </p>
        </div>
    </#if>



</div>