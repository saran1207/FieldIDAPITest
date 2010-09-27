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
        <li>
            <a href="<@s.url action="networkProductInspections" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.inspections"/>
            </a>
        </li>
        <li class="selected">
            <@s.text name="nav.schedules"/>
        </li>
    </ul>

    <div style="clear: both;"></div>

    <#if inspectionSchedules?exists && !inspectionSchedules.isEmpty()>
        <table class="list" id="scheduleList">
            <tr>
                <th class="rowName"><@s.text name="label.inspectiontype"/></th>
                <th><@s.text name="label.nextscheduleddate"/></th>
            </tr>
            <tbody id="schedules">
                <#list inspectionSchedules as inspectionSchedule>
                    <tr>
                        <td class="name">${inspectionSchedule.inspectionType.name}</td>
                        
                        <td>
                            <span >${action.formatDate(inspectionSchedule.nextDate, false)}</span>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    <#else>
        <div class="emptyList">
            <h2><@s.text name="label.noresults"/></h2>
            <p>
                <@s.text name="message.emptyschedulelist" />
            </p>
        </div>
    </#if>


</div>