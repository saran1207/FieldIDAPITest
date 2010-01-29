${action.setPageType('product', 'traceability')!}
<head>
	<@n4.includeStyle href="safetyNetworkSmartSearch" type="feature"/>
	
	
</head>
<#if inVendorContext>
	<div id="registrationMessage">
		<#if hasRegisteredProduct>
			<div id="alreadyRegisteredMessage">
				<h2 class="clean"><@s.text name="label.you_have_already_registered_this_asset"/></h2>
				<span class="weak"><@s.text name="instruction.you_can_locate_this_product_in_default_context_by_searching"/></span>
			</div>
		<#elseif sessionUser.hasAccess('tag') >
			<div id="registerThisAssetMessage">
				<h2 class="clean"><@s.text name="label.copy_and_register_this_asset_into_your_account"/></h2>
				<span class="weak"><@s.text name="instruction.registering_this_asset_will_allow_you_to_do"/></span>
			</div>
			<div id="registerThisAction">
				<@s.url id="registerUrl" action="productAdd" linkedProduct="${contextProductId}"/>
				<@s.submit key="label.copy_and_register_now" id="copyAsset" onclick="return redirect('${registerUrl}');" theme="simple"/>
			</div>
		</#if>
	</div>
</#if>
<div class="viewSection">
<#list linkedProducts as linkedProduct>
	<#assign tenant=linkedProduct.tenant />
	<#assign moreThanOne=(linkedProducts.size() > 1) />
	
	<h2>
		<#if moreThanOne>
			<a href="javascript:void(0);" id="criteria_open_${linkedProduct.id}" onclick="openSection('viewSection_${linkedProduct.id}', 'criteria_open_${linkedProduct.id}', 'criteria_close_${linkedProduct.id}') ;return false"><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
			<a href="javascript:void(0);" id="criteria_close_${linkedProduct.id}" onclick="closeSection('viewSection_${linkedProduct.id}', 'criteria_close_${linkedProduct.id}', 'criteria_open_${linkedProduct.id}');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif"/>" /></a>				 	
		</#if>
		<#include "../common/_displayTenantLogo.ftl"/>&nbsp;${linkedProduct.owner.internalOrg.name}
	</h2>
	<div class="viewSection" id="viewSection_${linkedProduct.id}" <#if moreThanOne>style="overflow: hidden; display:none;"</#if> >
	
		<div class="columnLeft">
			<div class="viewSection smallViewSection">
				<h2><@s.text name="label.productsummary"/></h2>
				<p>
					
					<label><@s.text name="${sessionUser.serialNumberLabel}"/></label>
					<span class="fieldValue">${linkedProduct.serialNumber}</span>
				</p>
				<p>
					<label><@s.text name="label.rfidnumber"/></label>
					<span class="fieldValue">${linkedProduct.rfidNumber!}</span>
				</p>
				<p>
					<label><@s.text name="label.producttype"/></label>
					<span class="fieldValue">${linkedProduct.type.name}</span>
				</p>
				<p>
					<label><@s.text name="label.productstatus"/></label>
					<span class="fieldValue">${(linkedProduct.productStatus.name)!}</span>
				</p>
				<p>
					<label><@s.text name="label.identified"/></label>
					<span class="fieldValue">${action.formatDate(linkedProduct.identified, false)}</span>
				</p>
				<#if linkedProduct.type.hasManufactureCertificate >
					<p>
						<label><@s.text name="label.manufacturecertificate"/></label>
						<span class="fieldValue">
							<img src="<@s.url value="/images/pdf_small.gif"/>"/>
								<a href="<@s.url action="downloadLinkedManufacturerCert" namespace="/file" uniqueID="${product.uniqueID}" linkedProductId="${linkedProduct.uniqueID}" />" target="_blank" >
									<@s.text name="label.downloadnow"/>
								</a>
						</span>
					</p>			
				</#if>	
			</div>
		
			<div class="viewSection smallViewSection" id="customerInformation" >
				<#if securityGuard.jobSitesEnabled >
					<h2><@s.text name="label.siteinformation"/><#if sessionUser.anEndUser> <a href="<@s.url action="customerInformationEdit" uniqueID="${product.id}"/>"><@s.text name="label.littleedit"/></a></#if></h2>
					<p>
						<label><@s.text name="label.assignedto"/></label>
						<span class="fieldValue">${(product.assignedUser.userLabel)!}</span>
					</p>
				<#else>	
					<h2><@s.text name="label.customerinformation"/><#if sessionUser.anEndUser> <a href="<@s.url action="customerInformationEdit" uniqueID="${product.id}"/>"><@s.text name="label.littleedit"/></a></#if></h2>
				</#if>
				<p>
					<label><@s.text name="label.location"/></label>
					<span class="fieldValue">${(product.location)!}</span>
				</p>
				<p>
					<label><@s.text name="label.referencenumber"/></label>
					<span class="fieldValue">${product.customerRefNumber!}</span>
				</p>
				<p>
					<label><@s.text name="label.purchaseorder"/></label>
					<span class="fieldValue">${product.purchaseOrder!}</span>
				</p>
			</div>
		
			<#if !linkedProduct.orderedInfoOptionList.isEmpty() >
				<div class="viewSection smallViewSection" >
					<h2>${linkedProduct.type.name} <@s.text name="label.attributes"/></h2>
					<#list linkedProduct.orderedInfoOptionList as infoOption >
						<p>
							<label>${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </label>
							<span class="fieldValue">${infoOption.name}</span>
						</p>
					</#list>
				</div>
			</#if>
		</div>
		
		<div class="columnRight">
			<#if linkedProduct.type.warnings?exists && linkedProduct.type.warnings?length gt 0 >
				<div class="viewSection smallViewSection" >
					<h2><@s.text name="label.warnings"/></h2>
					<p class="fieldValue">
						${linkedProduct.type.warnings!}
					</p>
				</div>
			</#if>
		
			<#if linkedProduct.type.instructions?exists && linkedProduct.type.instructions?length gt 0 >
				<div class="viewSection smallViewSection" >
					<h2><@s.text name="label.instructions"/></h2>
					<p class="fieldValue">
						${linkedProduct.type.instructions!}
					</p>
				</div>
			</#if>
			
			<#if (linkedProduct.type.cautions?exists && linkedProduct.type.cautions?length gt 0) || 
				(linkedProduct.type.imageName?exists) || (!linkedProduct.type.attachments.isEmpty()) ||
				(!productAttachments.isEmpty())>
				<div class="viewSection smallViewSection" >
					<h2><@s.text name="label.additionalinformation"/></h2> 
					<#if linkedProduct.type.cautions?exists && linkedProduct.type.cautions?length gt 0 >
						<p>
							<a href="${linkedProduct.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/></a>
						
						</p>
					</#if>
					
					<#assign attachments=action.getLinkedProductAttachments(linkedProduct.ID) />
					<#if !attachments.isEmpty() >
						<#assign downloadAction="downloadProductAttachedFileSafetyNetwork"/>
						<#assign attachmentID=linkedProduct.networkId/>
						<#include "/templates/html/common/_attachedFilesList.ftl"/>
					</#if>
					
					<#if !linkedProduct.type.attachments.isEmpty() >
						<#assign downloadAction="downloadProductTypeAttachedFileSafetyNetwork"/>
						<#assign attachments=linkedProduct.type.attachments />
						<#assign attachmentID=linkedProduct.networkId/>
						<#include "/templates/html/common/_attachedFilesList.ftl"/>
					</#if>
					
					<#if linkedProduct.type.imageName?exists >	
						<p>
							<img src="<@s.url action="downloadProductTypeImageSafetyNetwork" namespace="/file" uniqueID="${linkedProduct.type.uniqueID}" networkId="${linkedProduct.networkId}"/>" alt="<@s.text name="label.productimage"/>" width="300"/>
						</p>
					</#if>
					
				</div>
			</#if>
		</div>
	</div>
</#list>
</div>
