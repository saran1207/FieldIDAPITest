<@s.bean id="moneyUtil" name="com.n4systems.tools.MoneyUtils"/>

<div id="event" >
	<h2>${eventType.name?html} <@s.text name="label.information"/></h2>
		
	<p>
		<label><@s.text name="label.performed_by"/></label>
		<span>
			<#include "_userName.ftl"/>
		</span>
	</p>
	<p>
		<label><@s.text name="label.date_performed"/></label>
		<span>${action.formatDateTime(event.date)}</span>
	</p>

	<#if event.hasAssignToUpdate()>
	<p>
		<label><@s.text name="label.assigned_to"/></label>
		<span>${(event.assignedTo.assignedUser.userLabel)!action.getText('label.unassigned')}</span>
	</p>
	</#if>

	<p> 
		<label><@s.text name="label.scheduledon"/></label>
		<span>
			<#if event.nextDate?exists>
				${action.formatDateWithTime(event.nextDate, false)}
			<#else>
				<@s.text name="label.notscheduled"/>
			</#if>
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.eventbook"/></label>
		<span>${(event.book.name)!?html}</span>
	</p>
	
	<#list event.infoOptionMap.keySet() as key >
		<p>	
			<label>${key}:</label>
			<span> ${event.infoOptionMap[key]?html}</span>
		</p>
	</#list>
	
	<#assign formEvent=event/>
	<#assign identifier="eventForm"/>
	<#include "_event.ftl" />

    <h2><@s.text name="label.result"/></h2>

    <#if event.score?exists>
        <p class="scoreContainer">
            <label><@s.text name="label.score"/></label>
            <span>${event.score?string('0.####')}</span>
        </p>
    </#if>

    <div class="resultContainer">
        <label><@s.text name="label.result"/></label>
        <#if event.status?exists>
            <div
                <#if event.status.displayName== "Pass" > class="passColor"
                <#elseif event.status.displayName == "Fail"> class="failColor"
                <#elseif event.status.displayName == "N/A"> class="naColor"
                <#else> class="voidColor"
                </#if>>
                <p class="inline"><@s.text name="${(event.status.label?html)!}"/></p> 
            </div>
        <#else>
            <div><p>&nbsp;</p></div>
        </#if>

    </div>

    <p>
       <label><@s.text name="label.event_status"/></label>
       <span><#if event.eventStatus?exists>${event.eventStatus.displayName}</#if></span>
    </p>
    
	<#if event.proofTestInfo?exists >
		<h2><@s.text name="label.prooftest"/></h2>
		<p>
			<label><@s.text name="label.prooftesttype"/></label>
			<span><@s.text name="${(event.proofTestInfo.proofTestType.label)! action.getText( 'label.unknown') }"/></span>
		</p>
		<p>
			<label><@s.text name="label.peakload"/></label>
			<span><@s.text name="${(event.proofTestInfo.peakLoad)!?html}"/></span>
		</p>
		<p>
			<label><@s.text name="label.duration"/></label>
			<span><@s.text name="${(event.proofTestInfo.duration)!?html}"/></span>
		</p>
		<p>
			<label><@s.text name="label.peakloadduration"/></label>
			<span><@s.text name="${(event.proofTestInfo.peakLoadDuration)!?html}"/></span>
		</p>
		
		<#if event.proofTestInfo.proofTestType?exists && event.proofTestInfo.proofTestType.uploadable >
			<p>
				<label id="chartImage">
					<img alt="<@s.text name="label.nochart"/>" src="<@s.url action="downloadProofTestChart" namespace="/file" includeParams="none" uniqueID="${uniqueID}"/>" width="450"/>
				</label>
			</p>
		</#if>
		
	</#if>
	
	
	
	
	<h2><@s.text name="label.posteventinformation"/></h2>
	
	<p>
		<label><@s.text name="label.comments"/></label>
		<#escape x as action.replaceCR(x?html) >
			<span>${event.comments!}</span>
		</#escape>
	</p>
	
	<p>
		<label><@s.text name="label.modifiedby"/></label>
		<span>
			<#if event.modifiedBy?exists >
				<#assign user=event.modifiedBy >
			<#else>
				<#assign user=event >
			</#if>
			<#include "_userName.ftl"/> 
			<@s.text name="label.on"/> ${action.formatDateTime(event.modified)}
		</span>
	</p>
	<p>
		<label><@s.text name="label.assetstatus"/></label>
		<span>
			${(event.assetStatus.name)!}
		</span>
	</p>
	
	<#if !linkedEvent >
		<#assign downloadAction="downloadAttachedFile"/>
		<#assign attachments=event.attachments/>
		
		<#include "/templates/html/common/_attachedFilesShow.ftl"/>
	</#if>
	
	
	<#if subEvents?exists >
		<#list subEvents as subEvent >
			<h2>
				<a href="javascript:void(0);" id="expand_${subEvent.id}" onclick=" openSection( 'subEvent_${subEvent.id}', 'expand_${subEvent.id}', 'collapse_${subEvent.id}' ); return false;"><img src="<@s.url value="/images/expandLarge.gif" />" ></a>
				<a href="javascript:void(0);" id="collapse_${subEvent.id}" onclick="closeSection( 'subEvent_${subEvent.id}', 'collapse_${subEvent.id}', 'expand_${subEvent.id}' ); return false;" style="display:none"><img src="<@s.url value="/images/collapseLarge.gif" />" ></a>
				${subEvent.type.name?html} - ${(subEvent.name?html)!}
			</h2>
			<div id="subEvent_${subEvent.id}" style="display:none">
				<#if !subEvent.infoOptionMap.keySet().isEmpty() >
					<h2><@s.text name="label.assetsummary"/></h2>
					<#list subEvent.infoOptionMap.keySet() as key >
						<p>	
							<label>${key?html}:</label>
							<span> ${subEvent.infoOptionMap[key]?html}</span>
						</p>
					</#list>
				</#if>
			
				<#assign formEvent=subEvent>
				<#assign identifier=subEvent.id/>
				
				<#include "_event.ftl" />
				<h2><@s.text name="label.posteventinformation"/></h2>
		
				<p>
					<label><@s.text name="label.comments"/></label>
					<#escape x as action.replaceCR(x?html) >
						<span>${subEvent.comments!?html}</span>
					</#escape>
				</p>
				<#assign downloadAction="downloadSubAttachedFile"/>
				<#assign attachments=subEvent.attachments/>
				<#assign attachmentID=subEvent.id>
				<#include "/templates/html/common/_attachedFilesShow.ftl"/>
			</div>
			
		</#list>
	</#if>
	
	<#if (event.gpsLocation?exists) && tenant.settings.gpsCapture >
		<div>
			<h2><@s.text name="label.gpslocation"/></h2> 
		</div>		
		<div style="height:300px;" id="mapCanvas" class="googleMap eventMap"></div>				
		<script type="text/javascript">
			var map = googleMapFactory.create('mapCanvas');
			map.makeMarker = googleMapFactory.makeMarkerForStatus;		
			Event.observe(window, 'load', function() { 
				map.addLocation(${action.latitude}, ${action.longitude}, '', "${event.status}", "../../");
				map.show();				
			});		
		</script>						
	</#if>	

</div>
