<@s.bean id="moneyUtil" name="com.n4systems.tools.MoneyUtils"/>

<div id="event" >
	<div id="assetSummary" class="assetSummary">
		<h2>
			<span>${asset.type.name!""?html} <@s.text name="label.summary"/></span>
			<@s.url id="eventCertUrl" action="downloadEventCert" namespace="/file" reportType="INSPECTION_CERT" uniqueID="${uniqueID}" />
			<@s.url id="observationCertUrl" action="downloadEventCert" namespace="/file" reportType="OBSERVATION_CERT" uniqueID="${uniqueID}" />
							
			<#if event.anyCertPrintable>
				<#if event.eventCertPrintable && event.observationCertPrintable>
					<div id="cert_links" class="print" onmouseover="repositionCertLinks('cert_list', 'cert_links');" >
						<ul id="cert_list">
							<#if event.eventCertPrintable>
								<li><a href="${eventCertUrl}" target="_blank" >${event.type.group.reportTitle?html} (<@s.text name="label.pdfreport"/>)</a></li>
							</#if>
							<#if event.observationCertPrintable>
								<li><a href="${observationCertUrl}" target="_blank" ><@s.text name="label.printobservationcertificate"/></a></li>
							</#if>
						</ul>
						<img src="<@s.url value="/images/pdf_small.gif"/>" /> 
						<a href="javascript:void(0);" >
							<@s.text name="label.print"/>					
						</a>
					</div>					
				<#elseif event.eventCertPrintable>
					<div class="print">
						<img src="<@s.url value="/images/pdf_small.gif"/>" />
						<a href="${eventCertUrl}" target="_blank" ><@s.text name="label.print"/></a>
					</div>
				<#elseif event.observationCertPrintable>
					<div class="print">
						<img src="<@s.url value="/images/pdf_small.gif"/>" />
						<a href="${observationCertUrl}" target="_blank" ><@s.text name="label.print"/></a>	
					</div>
				</#if>
			</#if>
		</h2>
		
		<p>
			<label>${identifierLabel}</label>
			<span>
                <#if inside_iframe?exists>
                    <a href="#" onclick="parent.window.location='<@s.url namespace="/" action="asset" uniqueID="${asset.id}" />'"">${asset.identifier?html}</a>
                <#else>
                    <a href="<@s.url namespace="/" action="asset" uniqueID="${asset.id}" />">${asset.identifier?html}</a>
                </#if>
			</span>
		</p>
		<p>
			<label><@s.text name="label.rfidnumber"/></label>
			<span>
				${asset.rfidNumber!""?html}
			</span>
		</p>
		
		<p>
			<label><@s.text name="label.desc"/></label>
			<span>
				${asset.description?html}
			</span>
		</p>
	</div>
	
	<h2><@s.text name="label.owner"/></h2>
	
	<p>
		<label><@s.text name="label.organization"/></label>
		<span>${(event.owner.internalOrg.name)!?html}</span>
	</p>
	
	<p>
		<label><@s.text name="label.customer"/></label>
		<span>${(event.owner.customerOrg.name)!?html}</span>
	</p>
	
	<p>
		<label><@s.text name="label.division"/></label>
		<span>${(event.owner.divisionOrg.name)!?html}</span>
	</p>
	
	<p>
		<label><@s.text name="label.location"/></label>
		<span>${helper.getFullNameOfLocation(event.advancedLocation)!?html}</span>
	</p>
	
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
			<#if event.schedule?exists>
				${action.formatDate(event.schedule.nextDate, false)}
			<#else>
				<@s.text name="label.notscheduled"/>
			</#if>
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.eventbook"/></label>
		<span>${(event.book.name)!?html}</span>
	</p>

    <#if event.score?exists>
        <p>
            <label><@s.text name="label.score"/></label>
            <span>${event.score}</span>
        </p>
    </#if>
	
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
    <div class="resultContainer">
        <label><@s.text name="label.result"/></label>
		<div <#if event.status.displayName== "Pass" > class="passColor" <#elseif event.status.displayName == "Fail"> class="failColor" <#else> class="naColor" </#if>>
			<p class="inline"><@s.text name="${(event.status.label?html)!}"/></p> 
		</div>

    </div>

	
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
