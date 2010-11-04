<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
    <@n4.includeStyle href="safetyNetworkAsset" type="page"/>
</head>

<#include '../vendor/_vendorinfo.ftl'/>

<div id="mainContent">

    <#include '_topLayout.ftl'/>

    <ul class="options" id="innerList">
        <li>
            <a href="<@s.url action="showNetworkAsset" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.traceability"/>
            </a>
        </li>
        <li>
            <a href="<@s.url action="networkAssetEvents" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.events"/>
            </a>
        </li>
        <li class="selected">
            <@s.text name="nav.schedules"/>
        </li>
    </ul>

    <div style="clear: both;"></div>

    <#if eventSchedules?exists && !eventSchedules.isEmpty()>
        <table class="list" id="scheduleList">
            <tr>
                <th class="rowName"><@s.text name="label.eventtype"/></th>
                <th><@s.text name="label.nextscheduleddate"/></th>
            </tr>
            <tbody id="schedules">
                <#list eventSchedules as eventSchedule>
                    <tr>
                        <td class="name">${eventSchedule.eventType.name}</td>
                        
                        <td>
                            <span >${action.formatDate(eventSchedule.nextDate, false)}</span>
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
