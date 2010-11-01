<#assign assetId="${action.getAssetIdForInspectionScheduleId(entityId)}" />
<#assign inspectionTypeId="${action.getInspectionTypeIdForInspectionScheduleId(entityId)}" />
<#assign inspectionId="${action.getInspectionIdForInspectionScheduleId(entityId)!0}" />

<td id="actionsContainer_${entityId}">
	<span class="floatingDropdown floatingDropdownRelative">
		<a href="javascript:void(0);" class="dropDownLink" id="moreActions" onmouseover="positionDropDown(this, ${entityId});">
			<span><@s.text name="label.actions"/></span>
			<img src="images/dropdown_arrow.png"/>

		</a>
		<ul id="moreActions_list_${entityId}">
			<#if sessionUser.hasAccess("createinspection") >
				<li id="floatingDropdownStartEventLink">
					<a href='<@s.url action="selectEventAdd" namespace="/" assetId="${assetId}" type="${inspectionTypeId}" scheduleId="${entityId}" />'>
						<@s.text name="label.startevent"/>
					</a>
				</li>
			</#if>
			<li>
				<a href='<@s.url action="eventScheduleList" assetId="${assetId}" />'>
					<@s.text name="label.view_schedules"/>
				</a>
			</li>
			<#if !sessionUser.isCustomerUser()>
				<li>
					<a href='<@s.url action="eventScheduleList" assetId="${assetId}" />'>
						<@s.text name="label.edit_schedule"/>
					</a>
				</li>
				<#if inspectionId == "0">
					<li>
						<a href='<@s.url action="eventScheduleDelete" uniqueID="${entityId}" assetId="${assetId}" searchId="${searchId}" currentPage="${currentPage}" />'>
							<@s.text name="label.delete_schedule"/>
						</a>
					</li>
				</#if>
			</#if>
			<#if inspectionId != "0">
				<li>
					<#include "../eventCrud/_eventViewLightBoxOptions.ftl"/>
					<a href='<@s.url action="event" namespace="/aHtml/iframe" uniqueID="${inspectionId}" assetId="${assetId}"/>'  ${inspectionLightViewOptions} >
						<@s.text name="label.view_event"/>
					</a>
				</li>
			</#if>
			<li>
				<a href="asset.action?uniqueID=${assetId}" >
					<@s.text name="label.view_asset"/>
				</a>
			</li>
			<#if sessionUser.hasAccess('tag') >
				<li>
					<a href='<@s.url action="assetEdit" uniqueID="${assetId}" />' >
						<@s.text name="label.edit_asset"/>
					</a>
				</li>
			</#if>	
		</ul>
	</span>
</td>
