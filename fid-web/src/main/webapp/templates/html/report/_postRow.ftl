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
<td>
	<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
	<@s.url id="printReport" action="printReport.action" namespace="/aHtml" searchId="${searchId}"/>
	
	<span class="floatingDropdown floatingDropdownRelative">
		<a href="javascript:void(0);" class="dropDownLink" id="moreActions" onmouseover="positionDropdown(this, ${entityId}, 12, -50 );">
			<span><@s.text name="label.actions"/></span>
			<img src="images/dropdown_arrow.png"/>
		</a>
		<ul id="moreActions_list_${entityId}">
			<li>
				<a href='<@s.url action="inspection" namespace="/aHtml/iframe" uniqueID="${entityId}"/>'  ${inspectionLightViewOptions} ><@s.text name="link.view" /></a>
			</li>
			<li>
				<#if sessionUser.hasAccess("editinspection") && localInspection>
					<a href='<@s.url action="selectInspectionEdit" namespace="/" uniqueID="${entityId}"/>'><@s.text name="label.edit" /></a>
				</#if>
			</li>
			<li>
				<a href='${printReport}' class='lightview summaryReport' rel='ajax' title=' :: :: scrolling: false, autosize: true' ><@s.text name="label.print_report" /></a>
			</li>
			<#if sessionUser.hasAccess('createinspection') >
				<li id="floatingDropdownStartEventLink">
					<a href='<@s.url action="quickInspect" productId="${assetId}" />' >
						<@s.text name="label.startevent"/>
					</a>
				</li>
			</#if>
			<li>
				<a href="<@s.url action="product" uniqueID="${assetId}" />" >
					<@s.text name="label.view_asset"/>
				</a>
			</li>
			<#if sessionUser.hasAccess('tag') >
				<li>
					<a href='<@s.url action="productEdit" uniqueID="${assetId}" />' >
						<@s.text name="label.edit_asset"/>
					</a>
				</li>
			</#if>
		</ul>
	</span>
</td>