<#escape x as action.replaceCR(x?html) >
<head>
	
	<script language="Javascript" src="javascript/marryOrder.js"></script>
	<script language="javascript">
	
		ordersUrl = "<@s.url action="orders" namespace="/aHtml"  />";
		marryOrderUrl = "<@s.url action="marryOrder" namespace="/ajax"  />";
		marryOrderTitle = '<@s.text name="label.connectorder" />';
		
		function editCustomerInformation() {
			product
		}
	</script>
</head>


${action.setPageType('product', 'show')!}
<div class="columnLeft">
	<div class="viewSection smallViewSection" >
		<h2><@s.text name="label.productsummary"/></h2>
		<p>
			<label><@s.text name="${sessionUser.serialNumberLabel}"/></label>
			<span class="fieldValue serialNumber">${asset.serialNumber}</span>
		</p>
		<p>
			<label><@s.text name="label.rfidnumber"/></label>
			<span class="fieldValue">${asset.rfidNumber!}</span>
		</p>
		<p>
			<label><@s.text name="label.publishedoversafetynetwork"/></label>
			<span class="fieldValue"><@s.text name="${publishedStateLabel}"/></span>
		</p>
		<p>
			<label><@s.text name="label.producttype"/></label>
			<span class="fieldValue">${asset.type.name}</span>
		</p>
		<p>
			<label><@s.text name="label.productstatus"/></label>
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
		<#list asset.assetSerialExtensionValues as extension >
			<p>
				<label><@s.text name="${extension.assetSerialExtension.extensionLabel}"/></label>
				<span class="fieldValue">${(extension.extensionValue)!}</span>
			</p>
		</#list>
		
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
					<label><@s.text name="label.productcode"/></label>
					<span class="fieldValue">${(asset.shopOrder.productCode)!}</span>
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
	
	<div class="viewSection smallViewSection" >
		<h2><@s.text name="label.lastinspection"/> 
			<a href="<@s.url action="inspectionGroups" uniqueID="${uniqueID}" />" id="manageInspections"><@s.text name="label.manageinspections"/></a>
		</h2>
		<#if inspectionCount gt 0 >
			<p>
				<label><@s.text name="label.lastinspectiondate"/></label>
				<span class="fieldValue">${action.formatDateTime(lastInspection.date)}</span>
			</p>
			<p>
				<label><@s.text name="label.inspectiontype"/></label>
				<span class="fieldValue">${ lastInspection.type.name! }</span>
			</p>
			<p>
				<label><@s.text name="label.result"/></label>
				<span class="fieldValue"><@s.text name="${(lastInspection.status.label?html)!}"/></span>
			</p>
			<p>
				<label><@s.text name="label.details"/></label>
				<span class="fieldValue">
					<#assign inspection=lastInspection/>
					<#include "../inspectionCrud/_viewInspectionLink.ftl"/>
				</span>
			</p>				
		<#else>	
			<p class="fieldValue">
				<@s.text name="label.noinspections"/>
			</p>	
		</#if>
	</div>
</div>

<div class="columnRight">
	<#if asset.type.warnings?exists && asset.type.warnings?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.warnings"/></h2>
			<p class="fieldValue">
				${asset.type.warnings!}
			</p>
		</div>
	</#if>
	
	<#if asset.type.instructions?exists && asset.type.instructions?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.instructions"/></h2>
			<p class="fieldValue">
				${asset.type.instructions!}
			</p>
		</div>
	</#if>
	
	<#if securityGuard.projectsEnabled && projects?exists && !projects.isEmpty() >
		<div id="projects" class="viewSection smallViewSection" >
			<h2><@s.text name="label.projects"/></h2>
			<#list projects as project >
				<p class="fieldValue">
					<a href="<@s.url action="job"  uniqueID="${project.id}"/>">${project.name!}</a>
				</p>
			</#list>
		</div>
	</#if>
	
	<#if asset.subAssets?exists && !asset.subAssets.isEmpty() >
		<div id="assetComponents" class="viewSection smallViewSection" >
			<h2><@s.text name="label.subproducts"/></h2>
			<#list asset.subAssets as subAsset >
				<p>
					<label><a href="<@s.url action="product"  uniqueID="${subAsset.asset.id}"/>">${subAsset.asset.type.name!}</a></label>
					<span>
						${subAsset.label!}
					</span>  
				</p>
			
			</#list>
		</div>
	
	<#elseif parentAsset?exists>
		<div id="assetComponents" class="viewSection smallViewSection" >
			<h2><@s.text name="label.partof"/></h2>
			<p>
				<label>${parentAsset.type.name!}</label> 
				<span>
					<a href="<@s.url action="product"  uniqueID="${parentAsset.id}"/>">${parentAsset.serialNumber}</a>
				</span>  
			</p>
		</div>
	</#if>
			
	
	<#if asset.comments?exists && asset.comments?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.comments"/></h2>
			<p class="fieldValue">
				${asset.comments!}
			</p>
		</div>
	</#if>
	
	<#if linkedAssets?exists && !linkedAssets.empty >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.fieldidsafetynetwork"/></h2>
			<p >
				<label class="heading"><@s.text name="label.company"/></label>
				<span class="heading">
					<@s.text name="label.serialnumber"/>
				</span>
			</p>
			
			<#list linkedAssets as linkedAsset >
				<p >
					<label><#assign tenant=linkedAsset.tenant/>
							<#include "../common/_displayTenantLogo.ftl"/>
					</label>
					<span class="fieldValue">
						${linkedAsset.serialNumber}
						&nbsp;
						<#if linkedAsset.type.hasManufactureCertificate >
							<img src="<@s.url value="/images/pdf_small.gif"/>"/>
							<a href="<@s.url action="downloadLinkedManufacturerCert" namespace="/file" uniqueID="${asset.uniqueID}" linkedAssetId="${linkedAsset.id}" />" target="_blank" >
								<@s.text name="label.downloadnow"/>
							</a>
						</#if>	
					</span>
				</p>	
			</#list>
		</div>
	</#if>
	
	<#if (asset.type.cautions?exists && asset.type.cautions?length gt 0) ||
			(asset.type.imageName?exists) || (!asset.type.attachments.isEmpty()) ||
			(!assetAttachments.isEmpty())>
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.additionalinformation"/></h2> 
			<#if asset.type.cautions?exists && asset.type.cautions?length gt 0 >
				<p>
					<a href="${asset.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/></a>
				
				</p>
			</#if>
			
			<#if !assetAttachments.isEmpty() >
				<#assign downloadAction="downloadProductAttachedFile"/>
				<#assign attachments=assetAttachments />
				<#assign attachmentID=asset.uniqueID/>
				<#include "/templates/html/common/_attachedFilesList.ftl"/>
			</#if>
			
			<#if !asset.type.attachments.isEmpty() >
				<#assign downloadAction="downloadProductTypeAttachedFile"/>
				<#assign attachments=asset.type.attachments />
				<#assign attachmentID=asset.type.uniqueID/>
				<#include "/templates/html/common/_attachedFilesList.ftl"/>
			</#if>
			
			<#if asset.type.imageName?exists >
				<p>
					<img src="<@s.url action="downloadProductTypeImage" namespace="/file" uniqueID="${asset.type.uniqueID}"  />" alt="<@s.text name="label.productimage"/>" width="300"/>
				</p>
			</#if>
			
		</div>
	</#if>
</div>
</#escape>