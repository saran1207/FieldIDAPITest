<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/event.css"/>" />
	
	<#include "/templates/html/common/_tooltip.ftl"/>
	<style>
		.textContainer {
			background-image: url(images/events-blank-slate.png);
		}
		.list .reducedWidth {
			min-width: 10px;
		}
	</style> 
	
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<@n4.includeScript src="googleMaps.js"/>
	<@n4.includeScript src="event.js"/>
	
</head>

<#assign events = eventsWithLocation/>
 
<div class="headerActions">
	<#if sessionUser.hasAccess("createevent")>
		<a id="startEvent" href="#" onclick="return redirect('<@s.url action="quickEvent" assetId="${uniqueID}" includeParams="none" />');"><@s.text name="label.start_event"/></a>
	</#if>

	<#assign page = pagedEvents/>
	
	<div class="buttonBar">
		<#if !events.isEmpty()>		
			<a id="mapButton" class="${action.showMap.toString()}" href="#" onclick="return redirect('<@s.url action="assetEvents" uniqueID="${uniqueID}" mode="map"/>');" ><@s.text name="label.view_events_by_map"/></a>
		</#if>					
		<a id="listButton" class="${action.showList.toString()}" href="#" onclick="return redirect('<@s.url action="assetEvents" uniqueID="${uniqueID}" mode="list"/>' );" ><@s.text name="label.view_events_by_list"/></a>
		<a id="groupByDateButton" class="${action.showGroups.toString()}" href="#" onclick="return redirect('<@s.url action="assetEvents" uniqueID="${uniqueID}" mode="date"/>');" ><@s.text name="label.view_events_by_date_group"/></a>
	</div>
</div>

<script type="text/javascript">
	jQuery('.buttonBar .true').css({ opacity: 1.0 });
</script> 

<div class="assetEvents"> 
<#if showList && !pagedEvents.list.isEmpty() >
	
	<#assign page = pagedEvents/>
	
	<div id="tooltip" class="tooltip">
		<@s.text name="label.gps_location_recorded"/>
	</div>
	
	<#include '../common/_pagination.ftl' />
	<table id="eventsList" class="list">
		<tr>
			<@s.text id="dateLabel" name="label.date_performed"/>
			<@s.text id="typeLabel" name="label.eventtype"/>
			<@s.text id="performedByLabel" name="label.performed_by"/>
			<@s.text id="resultLabel" name="label.result"/>
			<@s.text id="assetStatusLabel" name="label.assetstatus"/>
		
			<#assign columns = [ "date", "type.name", "performedByFullName", "status", "assetStatus.name" ] >		
			<#assign labels = [ "${dateLabel}", "${typeLabel}", "${performedByLabel}", "${resultLabel}", "${assetStatusLabel}"]>
			<#assign x=0>
			
			<#list columns as column>
				<#if !sortColumn?exists && column == "date">
					<#assign selected = true>
				<#elseif sortColumn?exists && column == sortColumn>
					<#assign selected = true>
				<#else>
					<#assign selected = false>		
				</#if>				
				<#include "_eventColumnHeader.ftl">
				<#assign x=x+1>
			</#list>
			<th>&nbsp;</th>
			<#if tenant.settings.gpsCapture>
				<th class="reducedWidth">&nbsp;</th> 
			</#if>
		</tr>
		<#list page.list as event >
			<tr>
				<td>${action.formatDateTime(event.date)}</td>
				<td>${event.type.name}</td>
				<td>
					<#assign user=event.performedBy />
					<#include "../eventCrud/_userName.ftl"/>
				</td>
				
				
				<td <#if event.status.displayName== "Pass" >class="passColor"<#elseif event.status.displayName == "Fail">class="failColor"<#else>class="naColor"</#if>><p class="coloredBackground"><@s.text name="${(event.status.label?html)!}"/></p></td>
				<td><@s.text name="${(event.assetStatus.name?html)!}"/>&nbsp;</td>
				<td>
					<#if useContext>
						<#assign additionsToQueryString="&useContext=true"/>
					</#if>
					<#include "../eventCrud/_viewEventLink.ftl"/> 
					<#if sessionUser.hasAccess("editevent") && asset.owner.tenant = event.owner.tenant >
						|
						<a href="#" onclick="return redirect('<@s.url action="selectEventEdit" uniqueID="${event.id}" includeParams="none" />');" ><@s.text name="label.edit"/></a>
					</#if>
				</td>
				<#if (tenant.settings.gpsCapture && event.gpsLocation?exists) >
					<td class="reducedWidth">
						<img src="<@s.url value="/images/gps-icon-small.png"/>"/>
					</td>
				<#else>
					<td class="reducedWidth">
						&nbsp;
					</td>
				</#if>
			</tr>
		</#list>
	</table>
	<#include '../common/_pagination.ftl' />	
	
	<script type="text/javascript">
		document.observe("dom:loaded", function() {
			jQuery("#eventsList img").tooltip({
				tip: '#tooltip',
				position: 'center right',				
				offset: [0, 15],				
				delay: 0
			});		
		});
	</script>
<#elseif showList>
	<div class="initialMessage" >
		<div class="textContainer">
			<h1><@s.text name="label.emptyeventlist"/></h1>
			<p>
				<@s.text name="label.emptyeventlist_message">
					<@s.param>${asset.type.name}</@s.param>
				</@s.text>
			</p>
		</div>
	</div>
</#if>

	
<#if showMap && !events.isEmpty()>  <!-- TODO DD: what to show if no event locations exist? ask matt --> 
		
	<script type="text/javascript">
		Event.observe(window, 'load', function() { 
			googleMap.initialize('mapCanvas');			
		});				
	</script>
	<div style="height:500px">
		<div id="mapCanvas" class="googleMap"/>
	</div>
	
	<#list events as event>
		<#assign content>${action.getEventDescription(event)}</#assign>		
		<#assign urlLabel>View Event Details</#assign>
		<#assign url>
			<#include "../eventCrud/_viewEventLink.ftl"/><br>
		</#assign>
		<script type="text/javascript">
			googleMap.addMarker(${event.gpsLocation}, "${url?j_string}"+"${content?j_string}");
		</script>
	</#list>
</#if>


<#if showGroups && eventGroups?exists >
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
																		
									<#list eventTypes as eventType>
										<li>
											<#if subEvent?exists && subEvent  > 
												<@s.url id="eventUrl" action="subEventAdd" assetId="${subAsset.asset.id}" type="${eventType.id}" parentAssetId="${assetId}" token="${token}" namespace="/"/>
											<#else>
												<@s.url id="eventUrl" action="selectEventAdd" assetId="${uniqueID}" type="${eventType.id}" eventGroupId="${(eventGroup.id)!}"/>
											</#if>
											<a href="${eventUrl}" >${eventType.name}</a>
										</li>
									</#list>
									<#if eventTypes.isEmpty() >
										<li>
											<@s.text name="label.noeventtypes"/>
										</li>
									</#if>																										
									
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


</div>
