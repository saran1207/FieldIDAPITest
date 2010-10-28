<#assign localInspection = action.isLocalInspection(rowIdx)/>
<#assign assetId = action.getAssetId(rowIdx)>

<#if criteria.includeNetworkResults>
	<td>
		<#if localInspection>
			<@s.text name="label.no"/>
		<#else>
			<@s.text name="label.yes"/>
		</#if>
	</td>
</#if>
<td id="actionsContainer_${entityId}">
	<#include "../eventCrud/_eventViewLightBoxOptions.ftl"/>
	<@s.url id="printReport" action="printReport.action" namespace="/aHtml" searchId="${searchId}"/>
	
	<span class="floatingDropdown floatingDropdownRelative">
		<a href="javascript:void(0);" class="dropDownLink" id="moreActions" onmouseover="positionDropDown(this, ${entityId});">
			<span><@s.text name="label.actions"/></span>
			<img src="images/dropdown_arrow.png"/>
		</a>
		<ul id="moreActions_list_${entityId}">
			<li>
				<a href='<@s.url action="event" namespace="/aHtml/iframe" uniqueID="${entityId}"/>'  ${inspectionLightViewOptions} ><@s.text name="link.view" /></a>
			</li>
			<#if sessionUser.hasAccess("editinspection") && localInspection >
			<li>
				<a href='<@s.url action="selectEventEdit" namespace="/" uniqueID="${entityId}"/>'><@s.text name="label.edit" /></a>
			</li>
			</#if>
			<li>
				<a href='${printReport}' class='lightview summaryReport' rel='ajax' title=' :: :: scrolling: false, autosize: true' ><@s.text name="label.print_report" /></a>
			</li>
			<#if sessionUser.hasAccess('createinspection') && localInspection >
				<li id="floatingDropdownStartEventLink">
					<a href='<@s.url action="quickEvent" assetId="${assetId}" />' >
						<@s.text name="label.startevent"/>
					</a>
				</li>
			</#if>
			<#if localInspection>
				<li>
					<a href="<@s.url action="asset" uniqueID="${assetId}" />" >
						<@s.text name="label.view_asset"/>
					</a>
				</li>
			</#if>
			<#if sessionUser.hasAccess('tag') && localInspection>
				<li>
					<a href='<@s.url action="assetEdit" uniqueID="${assetId}" />' >
						<@s.text name="label.edit_asset"/>
					</a>
				</li>
			</#if>
		</ul>
	</span>
</td>