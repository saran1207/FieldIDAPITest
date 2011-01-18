<#escape x as action.replaceCR(x?html) >
<head>
	
	<script language="Javascript" src="javascript/marryOrder.js"></script>
	<script language="javascript">
	
		ordersUrl = "<@s.url action="orders" namespace="/aHtml"  />";
		marryOrderUrl = "<@s.url action="marryOrder" namespace="/ajax"  />";
		marryOrderTitle = '<@s.text name="label.connectorder" />';
		
	</script>
	
	<@n4.includeStyle href="asset" type="page"/>
</head>


${action.setPageType('asset', 'show')!}


<div class="columnLeft">
	<div>	
		<#if asset.type.imageName?exists >
			<p>
				<img src="<@s.url action="downloadAssetTypeImage" namespace="/file" uniqueID="${asset.type.uniqueID}"  />" alt="<@s.text name="label.assetimage"/>" width="250"/>
			</p>
		</#if>
	</div>
	<div class="viewSection">
		<h2 class="subheading"><@s.text name="label.activity"/></h2> 
	</div>
	<div class="leftViewSection">
		<h3 class="subheading"><@s.text name="label.lastevent"/></h3>
		<#if eventCount gt 0 >
			<p>
				<#assign event=lastEvent/>
				<#assign urlLabel="label.view_this_event" />
				<@s.text name="label.lasteventdate_msg">
					<@s.param>${lastEvent.type.name!}</@s.param>
					<@s.param>${action.formatDateTime(lastEvent.date)}</@s.param>
					<@s.param><@s.text name="${(lastEvent.status.label?html)!}"/></@s.param>
					<@s.param><#include "../eventCrud/_viewEventLink.ftl"/></@s.param>
				</@s.text>
			</p>				
		<#else>	
			<p>
				<@s.text name="label.noevents"/>
			</p>	
		</#if>
	</div>

	<div class="leftViewSection">
		<h3 class="subheading"><@s.text name="label.nextevent"/></h3>
		<#if nextEvent?exists >
			<p>
				<@s.text name="label.nexteventdate_msg">
					<@s.param>${nextEvent.eventType.name!}</@s.param>
					<@s.param>${nextEvent.daysToDue!}</@s.param>
					<@s.param>${action.formatDate(nextEvent.nextDate, false)}</@s.param>
				</@s.text>
			</p>				
		<#else>	
			<p>
				<@s.text name="label.noevents"/>
			</p>	
		</#if>
	</div>
	
	<#if asset.subAssets?exists && !asset.subAssets.isEmpty() >
		<div id="assetComponents" class="leftViewSection">
			<h3 class="subheading"><@s.text name="label.subassets"/></h3>
			<#list asset.subAssets as subAsset >
				<p>
					<label>${subAsset.asset.type.name!}</label>
					<span>
						<a href="<@s.url action="asset"  uniqueID="${subAsset.asset.id}"/>">${subAsset.asset.serialNumber}</a>
					</span>  
				</p>
			</#list>
		</div>
	
	<#elseif parentAsset?exists>
		<div id="assetComponents" class="leftViewSection">
			<h3 class="subheading"><@s.text name="label.partof"/></h3>
			<p>
				<label>${parentAsset.type.name!}</label> 
				<span>
					<a href="<@s.url action="asset"  uniqueID="${parentAsset.id}"/>">${parentAsset.serialNumber}</a>
				</span>  
			</p>
		</div>		
	</#if>	
	
	<#if securityGuard.projectsEnabled && projects?exists && !projects.isEmpty() >
		<div id="projects" class="leftViewSection">
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
		<h2><@s.text name="label.assetsummary"/></h2>
		<p>
			<label><@s.text name="${sessionUser.serialNumberLabel}"/></label>
			<span class="fieldValue serialNumber">${asset.serialNumber}</span>
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
			<span class="fieldValue">${(asset.shopOrder.order.orderNumber)!}</span>
		</p>
		</#if>
		
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
	</div>
	
	<#include "_customerInformation.ftl"/>
	
	<#if !asset.orderedInfoOptionList.isEmpty() >
		<div class="viewSection smallViewSection" id="attributes" >
			<h2>${asset.type.name} <@s.text name="label.attributes"/></h2>
			<#list asset.orderedInfoOptionList as infoOption >
				<p>
					<label>${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </label>
					<span class="fieldValue" infoFieldName="${infoOption.infoField.name?j_string}">${infoOption.name}</span>
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
		<div class="viewSection smallViewSection">
			<h2><@s.text name="label.comments"/></h2>
			<p class="fieldValue">
				${asset.comments!}
			</p>
		</div>
	</#if>
	
</div>

<div class="columnRight">
	<div class="viewSection">
		<h2 class="subheading"><@s.text name="label.additionalinformation"/></h2> 
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
		<div class="rightViewSection">
			<h3 class="subheading"><@s.text name="label.instructions"/></h3>
			<p>
				${asset.type.instructions!}
			</p>
		</div>
	</#if>
				
	<#if !assetAttachments.isEmpty() || !asset.type.attachments.isEmpty() >
		<div class="rightViewSection">
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
		<div id="more" class="rightViewSection">
			<#if asset.type.cautions?exists && asset.type.cautions?length gt 0 >
				<p>
					<a href="${asset.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/></a>
				</p>
			</#if>
					
		</div>
	</#if>
</div>
</#escape>
