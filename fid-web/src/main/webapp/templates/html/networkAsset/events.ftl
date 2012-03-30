<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
    <@n4.includeStyle href="safetyNetworkAsset" type="page"/>

    <script type="text/javascript">

        jQuery(document).ready(function(){
            jQuery('.viewNetworkEventLink').colorbox({iframe: true, scrolling: true, width:'650px', height: '600px'});
        });

    </script>
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
        <li class="selected">
            <@s.text name="nav.events"/>
        </li>
        <li>
            <a href="<@s.url action="networkEventScheduleList" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.schedules"/>
            </a>
        </li>
    </ul>

    <div style="clear: both;"></div>

    <#if !events.isEmpty() >
        <table class="list">
            <tr>
                <th><@s.text name="label.date_performed"/></th>
                <th><@s.text name="label.eventtype"/></th>
                <th><@s.text name="label.result"/></th>
                <th><@s.text name="label.performed_by"/></th>
                <th><@s.text name="label.details"/></th>
            </tr>
            <#list events as event >
                <tr>
                    <td>${action.formatDateTime(event.date)}</td>
                    <td>${event.type.name}</td>
                    <td><@s.text name="${(event.status.label?html)!}"/></td>
                    <td>
                        <#assign tenant=event.tenant/>
                        <#include "../common/_displayTenantLogo.ftl"/>
                    </td>
                    <td>
                        <#include "_networkEventLink.ftl"/>
                    </td>
                </tr>
            </#list>
        </table>
    <#else>
        <div class="emptyList">
            <h2><@s.text name="label.noresults"/></h2>
            <p>
                <@s.text name="message.emptyeventlist" />
            </p>
        </div>
    </#if>



</div>
