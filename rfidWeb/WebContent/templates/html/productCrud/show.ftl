<#escape x as action.replaceCR(x?html) >
<head>
	
	<script language="Javascript" src="javascript/marryOrder.js"></script>
	<script language="javascript">
	
		ordersUrl = "<@s.url action="orders" namespace="/ajax"  />";
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
			<span class="fieldValue">${product.serialNumber}</span>
		</p>
		<p>
			<label><@s.text name="label.rfidnumber"/></label>
			<span class="fieldValue">${product.rfidNumber!}</span>
		</p>
		<p>
			<label><@s.text name="label.publishedoversafetynetwork"/></label>
			<span class="fieldValue"><@s.text name="${publishedStateLabel}"/></span>
		</p>
		<p>
			<label><@s.text name="label.producttype"/></label>
			<span class="fieldValue">${product.type.name}</span>
		</p>
		<p>
			<label><@s.text name="label.productstatus"/></label>
			<span class="fieldValue">${(product.productStatus.name)!}</span>
		</p>
		<p>
			<label><@s.text name="label.identified"/></label>
			<span class="fieldValue">${action.formatDate(product.identified, false)}</span>
		</p>
		<p>
			<label><@s.text name="label.identifiedby"/></label>
			<span class="fieldValue">${(product.identifiedBy.displayName)!}</span>
		</p>
		<p>
			<label><@s.text name="label.modifiedby"/></label>
			<span class="fieldValue">${(product.modifiedBy.displayName)!}</span>
		</p>
		<#if !securityGuard.integrationEnabled >
		<p>
			<label><@s.text name="label.ordernumber"/></label>
			<span class="fieldValue">${(product.shopOrder.order.orderNumber)!}</span>
		</p>
		</#if>
		<#list product.productSerialExtensionValues as extension >
			<p>
				<label><@s.text name="${extension.productSerialExtension.extensionLabel}"/></label>
				<span class="fieldValue">${(extension.extensionValue)!}</span>
			</p>
		</#list>
		
		<#if product.type.hasManufactureCertificate >
			<p>
				<label><@s.text name="label.manufacturecertificate"/></label>
				<span class="fieldValue">
					<img src="<@s.url value="/images/pdf_small.gif"/>"/>
					<a href="<@s.url action="downloadManufacturerCert" namespace="/file" uniqueID="${product.uniqueID}" />" target="_blank" >
						<@s.text name="label.downloadnow"/>
					</a>
				</span>
			</p>			
		</#if>	
	</div>
	
	<#include "_customerInformation.ftl"/>
	
	<#if !product.orderedInfoOptionList.isEmpty() >
		<div class="viewSection smallViewSection" >
			<h2>${product.type.name} <@s.text name="label.attributes"/></h2>
			<#list product.orderedInfoOptionList as infoOption >
				<p>
					<label>${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </label>
					<span class="fieldValue">${infoOption.name}</span>
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
					<#if !product.shopOrder?exists >
						<a href="<@s.url action="orders" namespace="/ajax" product="${uniqueID}"  />" class="lightview" title="<@s.text name="label.connectorder" /> :: :: scrolling:true, autosize: true, ajax: { onComplete: ajaxForm } "  rel='ajax'><@s.text name="label.connectorder" /></a>
					<#else>
						${(product.shopOrder.order.orderNumber)!}
					</#if>
						
				</span>
			</p>
			
			<#if product.shopOrder?exists >
				<p>
					<label><@s.text name="label.productcode"/></label>
					<span class="fieldValue">${(product.shopOrder.productCode)!}</span>
				</p>
				<p>
					<label><@s.text name="label.quantity"/></label>
					<span class="fieldValue">${(product.shopOrder.quantity)!}</span>
				</p>
				<#if product.shopOrder.order.orderDate?exists >
				<p>
					<label><@s.text name="label.orderdate"/></label>
					<span class="fieldValue">${(product.shopOrder.order.orderDate)!}</span>
				</p>
				</#if>
				<#if product.shopOrder.description?exists >
				<p>
					<label><@s.text name="label.description"/></label>
					<span class="fieldValue">${(product.shopOrder.description)!}</span>
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
				<span class="fieldValue">${ lastInspection.status! }</span>
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
	<#if product.type.warnings?exists && product.type.warnings?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.warnings"/></h2>
			<p class="fieldValue">
				${product.type.warnings!}
			</p>
		</div>
	</#if>
	
	<#if product.type.instructions?exists && product.type.instructions?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.instructions"/></h2>
			<p class="fieldValue">
				${product.type.instructions!}
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
	
	<#if product.subProducts?exists && !product.subProducts.isEmpty() >
		<div id="productComponents" class="viewSection smallViewSection" >
			<h2><@s.text name="label.subproducts"/></h2>
			<#list product.subProducts as subProduct >
				<p>
					<label><a href="<@s.url action="product"  uniqueID="${subProduct.product.id}"/>">${subProduct.product.type.name!}</a></label> 
					<span>
						${subProduct.label!}
					</span>  
				</p>
			
			</#list>
		</div>
	
	<#elseif parentProduct?exists>
		<div id="productComponents" class="viewSection smallViewSection" >
			<h2><@s.text name="label.partof"/></h2>
			<p>
				<label>${parentProduct.type.name!}</label> 
				<span>
					<a href="<@s.url action="product"  uniqueID="${parentProduct.id}"/>">${parentProduct.serialNumber}</a>
				</span>  
			</p>
		</div>
	</#if>
			
	
	<#if product.comments?exists && product.comments?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.comments"/></h2>
			<p class="fieldValue">
				${product.comments!}
			</p>
		</div>
	</#if>
	
	<#if linkedProducts?exists && !linkedProducts.empty >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.fieldidsafetynetwork"/></h2>
			<p >
				<label class="heading"><@s.text name="label.company"/></label>
				<span class="heading">
					<@s.text name="label.serialnumber"/>
				</span>
			</p>
			
			<#list linkedProducts as linkedProduct >
				<p >
					<label><#assign tenant=linkedProduct.tenant/>
							<#include "../common/_displayTenantLogo.ftl"/>
					</label>
					<span class="fieldValue">
						${linkedProduct.serialNumber}
						&nbsp;
						<#if linkedProduct.type.hasManufactureCertificate >
							<img src="<@s.url value="/images/pdf_small.gif"/>"/>
							<a href="<@s.url action="downloadLinkedManufacturerCert" namespace="/file" uniqueID="${product.uniqueID}" linkedProductId="${linkedProduct.id}" />" target="_blank" >
								<@s.text name="label.downloadnow"/>
							</a>
						</#if>	
					</span>
				</p>	
			</#list>
		</div>
	</#if>
	
	<#if (product.type.cautions?exists && product.type.cautions?length gt 0) || 
			(product.type.imageName?exists) || (!product.type.attachments.isEmpty()) ||
			(!productAttachments.isEmpty())>
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.additionalinformation"/></h2> 
			<#if product.type.cautions?exists && product.type.cautions?length gt 0 >
				<p>
					<a href="${product.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/></a>
				
				</p>
			</#if>
			
			<#if !productAttachments.isEmpty() >
				<#assign downloadAction="downloadProductAttachedFile"/>
				<#assign attachments=productAttachments />
				<#assign attachmentID=product.uniqueID/>
				<#include "/templates/html/common/_attachedFilesList.ftl"/>
			</#if>
			
			<#if !product.type.attachments.isEmpty() >
				<#assign downloadAction="downloadProductTypeAttachedFile"/>
				<#assign attachments=product.type.attachments />
				<#assign attachmentID=product.type.uniqueID/>
				<#include "/templates/html/common/_attachedFilesList.ftl"/>
			</#if>
			
			<#if product.type.imageName?exists >	
				<p>
					<img src="<@s.url action="downloadProductTypeImage" namespace="/file" uniqueID="${product.type.uniqueID}"  />" alt="<@s.text name="label.productimage"/>" width="300"/>
				</p>
			</#if>
			
		</div>
	</#if>
</div>
</#escape>