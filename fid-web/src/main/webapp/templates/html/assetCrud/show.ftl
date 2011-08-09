<#escape x as action.replaceCR(x?html) >
<head>

	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<@n4.includeScript src="googleMaps.js"/>
	
	<script language="Javascript" src="javascript/marryOrder.js"></script>
	<script type="text/javascript">
	
		ordersUrl = "<@s.url action="orders" namespace="/aHtml"  />";
		marryOrderUrl = "<@s.url action="marryOrder" namespace="/ajax"  />";
		marryOrderTitle = '<@s.text name="label.connectorder" />';
	</script>
	
	<@n4.includeStyle href="asset" type="page"/>
</head>
		
	

${action.setPageType('asset', 'show')!}


<div class="columnLeft">
	<div>	
		<#if asset.imageName?exists >
			<p>
				<img src="<@s.url action="downloadAssetImage" namespace="/file" uniqueID="${asset.uniqueID}"  />" alt="<@s.text name="label.assetimage"/>" width="264"/>
			</p>
		<#elseif asset.type.imageName?exists >
			<p>
				<img src="<@s.url action="downloadAssetTypeImage" namespace="/file" uniqueID="${asset.type.uniqueID}"  />" alt="<@s.text name="label.assetimage"/>" width="264"/>
			</p>
		</#if>
	</div>
	<div class="viewSection">
		<h2><@s.text name="label.activity_feed"/></h2> 
	</div>
	<div class="leftViewSection">
		<h3 class="subheading"><@s.text name="label.lasteventperformed"/></h3>
		<#if eventCount gt 0 >
			<#assign event=lastEvent/>
			<#assign urlLabel="label.view_this_event" />
			
			<div id="lastEvent">
				<div <#if lastEvent.status.displayName== "Pass" > class="passColor inline" <#elseif lastEvent.status.displayName == "Fail"> class="failColor inline" <#else> class="naColor inline" </#if>>
					<p class="inline"><@s.text name="${(lastEvent.status.label?html)!}"/></p> 
				</div>
	
				<@s.text name="label.lasteventdate_msg">
					<@s.param>${lastEvent.type.name!}</@s.param>
					<@s.param>${action.formatDateTime(lastEvent.date)}</@s.param>
				</@s.text>
			</div>
			<p>
				<#include "../eventCrud/_viewEventLink.ftl"/>
				<#if sessionUser.hasAccess("createevent")>
					|
					<a href="<@s.url action="quickEvent" assetId="${uniqueID}" />"><@s.text name="label.newevent"/></a> 
				</#if>
			</p>		
		<#else >	
			<p><@s.text name="label.nolastevents"/></p>
			<#if sessionUser.hasAccess("createevent")>
				<p><a href="<@s.url action="quickEvent" assetId="${uniqueID}" />"><@s.text name="label.newevent"/></a></p>
			</#if>	
		</#if>
	</div>

	<div class="leftViewSection topBorder">
		<h3 class="subheading"><@s.text name="label.nextscheduledevent"/></h3>
		<#if nextEvent?exists >
			<div id="nextEvent">
				<#if nextEvent.pastDue>
					<div class="failColor inline">
						<p class="inline"><@s.text name="label.overdue"/></p> 
					</div>
					<@s.text name="label.nexteventdate_pastdue">
						<@s.param>${nextEvent.eventType.name!}</@s.param>
						<@s.param>${action.formatDate(nextEvent.nextDate, false)}</@s.param>
					</@s.text>
				<#elseif nextEvent.daysToDue == 0>
					<div class="passColor inline">
						<p class="inline"><@s.text name="label.today"/></p> 
					</div>
					<@s.text name="label.nexteventdate_due_today">
						<@s.param>${nextEvent.eventType.name!}</@s.param>
						<@s.param>${action.formatDate(nextEvent.nextDate, false)}</@s.param>
					</@s.text>					
				<#else>
					<div class="passColor inline">
						<p class="inline"><@s.text name="label.x_days_away"><@s.param>${nextEvent.daysToDue!}</@s.param></@s.text></p> 
					</div>
					<@s.text name="label.nexteventdate_msg">
						<@s.param>${nextEvent.eventType.name!}</@s.param>
						<@s.param>${action.formatDate(nextEvent.nextDate, false)}</@s.param>
					</@s.text>
				</#if>
			</div>				
		<#else>	
			<p>
				<@s.text name="label.nonextevents"/>
			</p>	
		</#if>
	</div>
	
	<#if asset.subAssets?exists && !asset.subAssets.isEmpty() >
		<div id="assetComponents" class="leftViewSection topBorder">
			<h3 class="subheading"><@s.text name="label.subassets"/></h3>
			<#list asset.subAssets as subAsset >
				<p>
					<label>${subAsset.asset.type.name!}</label>
					<span>
						<a href="<@s.url action="asset"  uniqueID="${subAsset.asset.id}"/>">${subAsset.asset.identifier}</a>
					</span>  
				</p>
			</#list>
		</div>
	
	<#elseif parentAsset?exists>
		<div id="assetComponents" class="leftViewSection topBorder">
			<h3 class="subheading"><@s.text name="label.partof"/></h3>
			<p>
				<label>${parentAsset.type.name!}</label> 
				<span>
					<a href="<@s.url action="asset"  uniqueID="${parentAsset.id}"/>">${parentAsset.identifier}</a>
				</span>  
			</p>
		</div>		
	</#if>	
	
	<#if securityGuard.projectsEnabled && projects?exists && !projects.isEmpty() >
		<div id="projects" class="leftViewSection topBorder">
			<h3 class="subheading"><@s.text name="label.projects"/></h3>
			<#list projects as project >
				<p>
					<a href="<@s.url action="job"  uniqueID="${project.id}"/>">${project.name!}</a>
				</p>
			</#list>
		</div>
	</#if>
</div>

<div class="columnCenter">
	<div class="viewSection smallViewSection" >
		<h2>${asset.type.name} <@s.text name="label.summary"/></h2>
		<p>
			<label>${identifierLabel}</label>
			<span class="fieldValue identifier">${asset.identifier}</span>
		</p>
		<p>
			<label><@s.text name="label.rfidnumber"/></label>
			<span class="fieldValue">${asset.rfidNumber!}</span>
		</p>
		<p>
			<label><@s.text name="label.visibility"/></label>
			<span class="fieldValue"><@s.text name="${publishedStateLabel}"/></span>
		</p>
		<p>
			<label><@s.text name="label.assettype"/></label>
			<span class="fieldValue">${asset.type.name}</span>
		</p>
		<p>
			<label><@s.text name="label.assetstatus"/></label>
			<span class="fieldValue">${(asset.assetStatus.name)!}</span>
		</p>
		<p>
			<label><@s.text name="label.identified"/></label>
			<span class="fieldValue">${action.formatDate(asset.identified, false)}</span>
		</p>
		<p>
			<label><@s.text name="label.identifiedby"/></label>
			<span class="fieldValue">${(asset.identifiedBy.displayName)!}</span>
		</p>
		<p>
			<label><@s.text name="label.modifiedby"/></label>
			<span class="fieldValue">${(asset.modifiedBy.displayName)!}</span>
		</p>
		<#if !securityGuard.integrationEnabled >
		<p>
			<label><@s.text name="label.ordernumber"/></label>
			<span class="fieldValue">${(asset.nonIntergrationOrderNumber)!}</span>
		</p>
		</#if>
		
		<#if securityGuard.manufacturerCertificateEnabled>
			<#if asset.type.hasManufactureCertificate >
				<p>
					<label><@s.text name="label.manufacturecertificate"/></label>
					<span class="fieldValue">
						<img src="<@s.url value="/images/pdf_small.gif"/>"/>
						<a href="<@s.url action="downloadManufacturerCert" namespace="/file" uniqueID="${asset.uniqueID}" />" target="_blank" >
							<@s.text name="label.downloadnow"/>
						</a>
					</span>
				</p>			
			</#if>	
		</#if>
	</div>
	
	<#include "_customerInformation.ftl"/>
	
	<#if !asset.orderedInfoOptionList.isEmpty() >
		<div class="viewSection smallViewSection" id="attributes" >
			<h2>${asset.type.name} <@s.text name="label.attributes"/></h2>
			<#list asset.orderedInfoOptionList as infoOption >
				<p>
					<label>${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </label>
					<span class="fieldValue" infoFieldName="${infoOption.infoField.name?j_string}">
						<#if infoOption.infoField.fieldType = 'datefield'>
							${action.convertTimestamp(infoOption.name, infoOption.infoField.includeTime)}
						<#else>
							${infoOption.name}
						</#if>
					</span>
				</p>
			</#list>
		</div>
	</#if>
	
	<#if securityGuard.integrationEnabled >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.orderdetails"/></h2>
			<p>
				<label><@s.text name="label.onumber"/></label>
				<span class="fieldValue" id="marriedOrderDiv">
					<#if !asset.shopOrder?exists >
						<a href="<@s.url action="orders" namespace="/aHtml" asset="${uniqueID}"  />" class="lightview" title="<@s.text name="label.connectorder" /> :: :: scrolling:true, autosize: true, ajax: { onComplete: ajaxForm } "  rel='ajax'><@s.text name="label.connectorder" /></a>
					<#else>
						${(asset.shopOrder.order.orderNumber)!}
					</#if>
				</span>
			</p>
			
			<#if asset.shopOrder?exists >
				<p>
					<label><@s.text name="label.assetcode"/></label>
					<span class="fieldValue">${(asset.shopOrder.assetCode)!}</span>
				</p>
				<p>
					<label><@s.text name="label.quantity"/></label>
					<span class="fieldValue">${(asset.shopOrder.quantity)!}</span>
				</p>
				<#if asset.shopOrder.order.orderDate?exists >
				<p>
					<label><@s.text name="label.orderdate"/></label>
					<span class="fieldValue">${(asset.shopOrder.order.orderDate)!}</span>
				</p>
				</#if>
				<#if asset.shopOrder.description?exists >
				<p>
					<label><@s.text name="label.description"/></label>
					<span class="fieldValue">${(asset.shopOrder.description)!}</span>
				</p>
				</#if>
			</#if>	
		</div>
	</#if>	
	
	<#if asset.comments?exists && asset.comments?length gt 0 >
		<div class="viewSection smallViewSection assetComments">
			<h2><@s.text name="label.comments"/></h2>
			<p class="fieldValue">
				${asset.comments!}
			</p>
		</div>
	</#if>
	
</div>

<div class="columnRight">
	<div class="viewSection">
		<h2><@s.text name="label.additionalinformation"/></h2> 
	</div>
	<#if asset.type.warnings?exists && asset.type.warnings?length gt 0 >
		<div class="rightViewSection">
			<h3 class="subheading"><@s.text name="label.warnings"/></h3>
			<p>
				${asset.type.warnings!}
			</p>
		</div>
	</#if>
	
	<#if asset.type.instructions?exists && asset.type.instructions?length gt 0 >
		<div class="rightViewSection topBorder">
			<h3 class="subheading"><@s.text name="label.more_information"/></h3>
			<p>
				${asset.type.instructions!}
			</p>
		</div>
	</#if>
				
	<#if !assetAttachments.isEmpty() || !asset.type.attachments.isEmpty() >
		<div class="rightViewSection topBorder">
			<h3 class="subheading"><@s.text name="label.attachments"/></h3>
			<#if !assetAttachments.isEmpty() >
					<#assign downloadAction="downloadAssetAttachedFile"/>
					<#assign attachments=assetAttachments />
					<#assign attachmentID=asset.uniqueID/>
					<#include "_attachedFilesList.ftl"/>
			</#if>
			
			<#if !asset.type.attachments.isEmpty() >
					<#assign downloadAction="downloadAssetTypeAttachedFile"/>
					<#assign attachments=asset.type.attachments />
					<#assign attachmentID=asset.type.uniqueID/>
					<#include "_attachedFilesList.ftl"/>
			</#if>			
		</div>
		
		<div style="clear:both"></div>
	</#if>	
	
	<#if (asset.type.cautions?exists && asset.type.cautions?length gt 0) ||
			(asset.type.imageName?exists) || (!asset.type.attachments.isEmpty()) ||
			(!assetAttachments.isEmpty())>
		<#if asset.type.cautions?exists && asset.type.cautions?length gt 0 >
			<div id="more" class="rightViewSection topBorder">
					<p>
						<a href="${asset.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/></a>
					</p>
			</div>
		</#if>
	</#if>

	<#if (asset.gpsLocation?exists) >
		<script type="text/javascript">
			Event.observe(window, 'load', function() { 
				googleMap.initializeWithMarker('mapCanvas',${action.latitude}, ${action.longitude})
			});		
		</script>						
		<#include "/templates/html/common/googleMap.ftl">
	</#if>
			
</div>
</#escape>
