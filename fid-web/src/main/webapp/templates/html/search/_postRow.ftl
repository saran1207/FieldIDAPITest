
<td>
		<span class="inspectionTypes"><a href="javascript:void(0);" id="" onmouseover="positionDropdown(this);"><@s.text name="label.more_actions"/></a>
			<ul id="moreActions_list">
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

