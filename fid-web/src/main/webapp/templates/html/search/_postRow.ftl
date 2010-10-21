<td>
	<span class="floatingDropdown floatingDropdownRelative">
		<a href="javascript:void(0);" class="dropDownLink" id="moreActions" onmouseover="positionDropdown(this, ${entityId}, 12, -76);">
			<span><@s.text name="label.actions"/></span>
			<img src="images/dropdown_arrow.png"/>
		</a>
			
		<ul id="moreActions_list_${entityId}">
			<li>
				<a href="product.action?uniqueID=${entityId}" >
					<@s.text name="label.view_asset"/>
				</a>
			</li>
			<li>
				<a href='<@s.url action="productInspections" uniqueID="${entityId}" />' >
					<@s.text name="label.view_events"/>
				</a>
			</li>
			<li>
				<a href='<@s.url action="inspectionScheduleList" productId="${entityId}" />' >
					<@s.text name="label.view_schedules"/>
				</a>
			</li>
			<#if sessionUser.hasAccess('createinspection') >
				<li id="floatingDropdownStartEventLink">
					<a href='<@s.url action="quickInspect" productId="${entityId}" />' >
						<@s.text name="label.startevent"/>
					</a>
				</li>
			</#if>
			<#if sessionUser.hasAccess('tag') >
				<li>
					<a href='<@s.url action="productEdit" uniqueID="${entityId}" />' >
						<@s.text name="label.edit_asset"/>
					</a>
				</li>
				<li>
					<a href='<@s.url action="productMergeAdd" uniqueID="${entityId}" />' >
						<@s.text name="label.merge"/>
					</a>
				</li>
			</#if>
		</ul>
	</span>
</td>

