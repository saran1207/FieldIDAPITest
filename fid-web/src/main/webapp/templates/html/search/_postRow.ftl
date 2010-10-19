<td>
	<span class="floatingDropdown floatingDropdownRelative"><a href="javascript:void(0);" id="moreActions" onmouseover="positionDropdown(this, ${entityId} );"><@s.text name="label.more_actions"/></a>
		<ul id="moreActions_list_${entityId}">
			<li>
				<a href="product.action?uniqueID=${entityId}" >
					<@s.text name="label.view_asset"/>
				</a>
			</li>
			<li>
				<a href='<@s.url action="inspectionGroups" uniqueID="${entityId}" />' >
					<@s.text name="label.view_events"/>
				</a>
			</li>
			<li>
				<a href='<@s.url action="" uniqueID="${entityId}" />' >
					<@s.text name="label.startevent"/>
				</a>
			</li>
				<li>
				<a href='<@s.url action="" uniqueID="${entityId}" />' >
					<@s.text name="label.edit_asset"/>
				</a>
			</li>
			<li>
				<a href='<@s.url action="" uniqueID="${entityId}" />' >
					<@s.text name="label.merge"/>
				</a>
			</li>
		</ul>
	</span>
</td>

