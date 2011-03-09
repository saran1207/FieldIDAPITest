<head>
	<@n4.includeStyle type="page" href="event" />
	<script type="text/javascript" src="<@s.url value="/javascript/event.js"/>" ></script>
</head>

${action.setPageType('event', 'list')!}
<#include "_eventSearchForm.ftl" />


<div class="formAction container" >
	<#if Session.sessionUser.hasAccess("createevent") >
		<a id="startEvent" href="#" onclick="return redirect('<@s.url action="quickEvent" assetId="${uniqueID}" includeParams="none" />');"><@s.text name="label.newevent"/></a>		
	</#if>
	
</div>

<#if eventGroups?exists >
	<#if !eventGroups.isEmpty() >
		<table class="list" id="resultsTable" >
			<tr>
				<th><@s.text name="label.daterange"/></th>
				<th class="addEventColumn"></th>
			</tr>
			<#list eventGroups?sort_by("firstDate")?reverse as eventGroup >
				<tr>
					<td>
						<#if eventGroup_index == 0>
							<#assign openStyle>style="display:none"</#assign>
							<#assign closeStyle> </#assign>
						<#else>
							<#assign openStyle> </#assign>
							<#assign closeStyle>style="display:none"</#assign>
						</#if>
						<a href="javascript:void(0);" id="expand_${eventGroup.id}" onclick=" openSection( 'events_${eventGroup.id}', 'expand_${eventGroup.id}', 'collapse_${eventGroup.id}' ); return false;" ${openStyle}><img src="<@s.url value="/images/expandLarge.gif"/>" ></a>
						<a href="javascript:void(0);" id="collapse_${eventGroup.id}" onclick="closeSection( 'events_${eventGroup.id}', 'collapse_${eventGroup.id}', 'expand_${eventGroup.id}' ); return false;" ${closeStyle}><img src="<@s.url value="/images/collapseLarge.gif"/>" ></a>
						${action.formatDate(eventGroup.firstDate, true)!} - ${action.formatDate(eventGroup.firstDate, true)!}
						
						<div id="events_${eventGroup.id}" ${closeStyle} class="eventList">
						<#list eventGroup.availableEvents?sort_by( 'date' )?reverse as event >
							<div class="events" >
								<span class="eventType">
									<a id="showEvent_${event.id}" href="<@s.url action="event" uniqueID="${event.id}" />">
										${ ( event.type.name )!}
									</a>
								</span>
								<span class="datePerformed">
									${action.formatDateTime(event.date)}
								</span>
								<span class="performedBy">
									<@s.text name="label.performed_by"/>: ${ (event.performedBy.userLabel)! }
								</span>
								<span class="editEvent">
									<#if sessionUser.hasAccess("editevent") >
										<a id="editEvent_${event.id}" href="<@s.url action="selectEventEdit" uniqueID="${event.id}" />">
											<@s.text name="label.edit"/>
										</a>
									</#if>
								</span>
							</div>
						</#list>
						</div>	
						
					</td>
					<td>
						<#if Session.sessionUser.hasAccess("createevent") && !inVendorContext>
							<div class="eventTypes"><a href="javascript:void(0);" id="${eventGroup.id}" onmouseover="positionDropdown(this);"><@s.text name="label.addevent"/></a><br/>
								<ul id="${eventGroup.id}_list">
									<#include "_eventSelect.ftl" />
								</ul>
							</div>
						</#if>
					</td>	
				</tr>
				
			</#list>
		</table>
	
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults"/></h2>
			<p>
				<@s.text name="message.emptyeventlist" />
			</p>
		</div>
	</#if>
</#if>

	
	
	
