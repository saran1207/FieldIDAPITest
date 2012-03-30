<#assign assetId="${action.getAssetIdForEventScheduleId(entityId)}" />
<#assign eventTypeId="${action.getEventTypeIdForEventScheduleId(entityId)}" />
<#assign eventId="${action.getEventIdForEventScheduleId(entityId)!0}" />

<td id="actionsContainer_${entityId}" class="actionsContainer">
	<ul class="floatingDropdown">
		<li>
			<a href="javascript:void(0);" class="dropDownLink" id="moreActions">
				<span><@s.text name="label.actions"/></span>
				<img src="images/dropdown_arrow.png"/>
	
			</a>
			<ul id="moreActions_list_${entityId}" class="sub_menu">
				<#if sessionUser.hasAccess("createevent") >
					<li id="floatingDropdownStartEventLink">
						<a href='<@s.url action="selectEventAdd" namespace="/" assetId="${assetId}" type="${eventTypeId}" scheduleId="${entityId}" />'>
							<@s.text name="label.startevent"/>
						</a>
					</li>
				</#if>
				<li>
					<a href='<@s.url action="eventScheduleList" assetId="${assetId}" />'>
						<@s.text name="label.view_schedules"/>
					</a>
				</li>
				<#if !sessionUser.isReadOnlyUser()>
					<li>
						<a href='<@s.url action="eventScheduleList" assetId="${assetId}" />'>
							<@s.text name="label.edit_schedule"/>
						</a>
					</li>
					<#if eventId == "0">
						<li>
							<a href='<@s.url action="eventScheduleDelete" uniqueID="${entityId}" assetId="${assetId}" searchId="${searchId}" currentPage="${currentPage}" />'>
								<@s.text name="label.delete_schedule"/>
							</a>
						</li>
					</#if>
				</#if>
				<#if eventId != "0">
					<li>
						<#include "../eventCrud/_eventViewLightBoxOptions.ftl"/>
						<a href='<@s.url action="event" namespace="/aHtml/iframe" uniqueID="${eventId}" assetId="${assetId}"/>'  class="eventLightbox">
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
		</li>
	</ul>
</td>
