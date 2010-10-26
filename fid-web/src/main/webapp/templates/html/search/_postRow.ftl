
<td id="actionsContainer_${entityId}">
	<span class="floatingDropdown floatingDropdownRelative">
		<a href="javascript:void(0);" class="dropDownLink" id="moreActions" onmouseover="positionDropDown(this, ${entityId});">
			<span><@s.text name="label.actions"/></span>
			<img src="images/dropdown_arrow.png"/>
		</a>
			
		<ul id="moreActions_list_${entityId}">
			<li>
				<a href="asset.action?uniqueID=${entityId}" >
					<@s.text name="label.view_asset"/>
				</a>
			</li>
			<li>
				<a href='<@s.url action="assetInspections" uniqueID="${entityId}" />' >
					<@s.text name="label.view_events"/>
				</a>
			</li>
			<li>
				<a href='<@s.url action="inspectionScheduleList" assetId="${entityId}" />' >
					<@s.text name="label.view_schedules"/>
				</a>
			</li>
			<#if sessionUser.hasAccess('createinspection') >
				<li id="floatingDropdownStartEventLink">
					<a href='<@s.url action="quickInspect" assetId="${entityId}" />' >
						<@s.text name="label.startevent"/>
					</a>
				</li>
			</#if>
			<#if sessionUser.hasAccess('tag') >
				<li>
					<a href='<@s.url action="assetEdit" uniqueID="${entityId}" />' >
						<@s.text name="label.edit_asset"/>
					</a>
				</li>
				<li>
					<a href='<@s.url action="assetMergeAdd" uniqueID="${entityId}" />' >
						<@s.text name="label.merge"/>
					</a>
				</li>
			</#if>
		</ul>
	</span>
</td>

