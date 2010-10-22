<#assign assetId="${action.getProductIdForInspectionScheduleId(entityId)}" />
<#assign inspectionTypeId="${action.getInspectionTypeIdForInspectionScheduleId(entityId)}" />
<#assign inspectionId="${action.getInspectionIdForInspectionScheduleId(entityId)!0}" />

<td>
	<span class="floatingDropdown floatingDropdownRelative">
		<a href="javascript:void(0);" class="dropDownLink" id="moreActions" onmouseover="positionDropdown(this, ${entityId}, 12, -81);">
			<span><@s.text name="label.actions"/></span>
			<img src="images/dropdown_arrow.png"/>

		</a>
		<ul id="moreActions_list_${entityId}">
			<#if sessionUser.hasAccess("createinspection") >
				<li id="floatingDropdownStartEventLink">
					<a href='<@s.url action="selectInspectionAdd" namespace="/" assetId="${assetId}" type="${inspectionTypeId}" scheduleId="${entityId}" />'>
						<@s.text name="label.startevent"/>
					</a>
				</li>
			</#if>
			<li>
				<a href='<@s.url action="inspectionScheduleList" assetId="${assetId}" />'>
					<@s.text name="label.view_schedules"/>
				</a>
			</li>
			<#if !sessionUser.isCustomerUser()>
				<li>
					<a href='<@s.url action="inspectionScheduleList" assetId="${assetId}" />'>
						<@s.text name="label.edit_schedule"/>
					</a>
				</li>
				<#if inspectionId == "0">
					<li>
						<a href='<@s.url action="inspectionScheduleDelete" uniqueID="${entityId}" assetId="${assetId}" searchId="${searchId}" currentPage="${currentPage}" />'>
							<@s.text name="label.delete_schedule"/>
						</a>
					</li>
				</#if>
			</#if>
			<#if inspectionId != "0">
				<li>
					<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
					<a href='<@s.url action="inspection" namespace="/aHtml/iframe" uniqueID="${inspectionId}" assetId="${assetId}"/>'  ${inspectionLightViewOptions} >
						<@s.text name="label.view_inspection"/>
					</a>
				</li>
			</#if>
			<li>
				<a href="product.action?uniqueID=${assetId}" >
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
