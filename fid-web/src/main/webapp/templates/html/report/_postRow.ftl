<#assign localEvent = action.isLocalEvent(rowIdx)/>
<#assign assetId = action.getAssetId(rowIdx)>

<#if criteria.includeNetworkResults>
	<td>
		<#if localEvent>
			<@s.text name="label.no"/>
		<#else>
			<@s.text name="label.yes"/>
		</#if>
	</td>
</#if>
<td id="actionsContainer_${entityId}" class="actionsContainer">
	<#include "../eventCrud/_eventViewLightBoxOptions.ftl"/>
	<@s.url id="printReport" action="printReport.action" namespace="/aHtml" searchId="${searchId}"/>
	
	<span class="floatingDropdown floatingDropdownRelative">
		<a href="javascript:void(0);" class="dropDownLink" id="moreActions" onmouseover="positionDropDown(this, ${entityId});">
			<span><@s.text name="label.actions"/></span>
			<img src="images/dropdown_arrow.png"/>
		</a>
		<ul id="moreActions_list_${entityId}">
			<li>
				<a href='<@s.url action="event" namespace="/aHtml/iframe" uniqueID="${entityId}"/>'  ${eventLightViewOptions} ><@s.text name="link.view" /></a>
			</li>
			<#if sessionUser.hasAccess("editevent") && localEvent >
			<li>
				<a href='<@s.url action="selectEventEdit" namespace="/" uniqueID="${entityId}"/>'><@s.text name="label.edit" /></a>
			</li>
			</#if>
			<#if action.isPrintable(rowIdx)>
				<li>
					<@s.url id="eventCertUrl" action="downloadEventCert" namespace="/file" reportType="INSPECTION_CERT" uniqueID="${entityId}" />
					<a href="${eventCertUrl}" target="_blank" /><@s.text name="label.print_report" /></a>
				</li>
			</#if>
			<#if sessionUser.hasAccess('createevent') && localEvent >
				<li id="floatingDropdownStartEventLink">
					<a href='<@s.url action="quickEvent" assetId="${assetId}" />' >
						<@s.text name="label.startevent"/>
					</a>
				</li>
			</#if>
			<#if localEvent>
				<li>
					<a href="<@s.url action="asset" uniqueID="${assetId}" />" >
						<@s.text name="label.view_asset"/>
					</a>
				</li>
			</#if>
			<#if sessionUser.hasAccess('tag') && localEvent>
				<li>
					<a href='<@s.url action="assetEdit" uniqueID="${assetId}" />' >
						<@s.text name="label.edit_asset"/>
					</a>
				</li>
			</#if>
		</ul>
	</span>
</td>