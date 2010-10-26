<head>
	<@n4.includeStyle href="safetyNetworkSmartSearch" type="feature"/>
	
	
</head>
<#if inVendorContext>
	<div id="registrationMessage">
		<#if hasRegisteredAsset>
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
				<@s.url id="registerUrl" action="assetAdd" linkedAsset="${contextAssetId}"/>
				<@s.submit key="label.copy_and_register_now" id="copyAsset" onclick="return redirect('${registerUrl}');" theme="simple"/>
			</div>
		</#if>
	</div>
</#if>
<div class="viewSection">
<#list linkedAssets as linkedAsset>
	<#assign tenant=linkedAsset.tenant />
	<#assign moreThanOne=(linkedAssets.size() > 1) />
	
	<h2>
		<#if moreThanOne>
			<a href="javascript:void(0);" id="criteria_open_${linkedAsset.id}" onclick="openSection('viewSection_${linkedAsset.id}', 'criteria_open_${linkedAsset.id}', 'criteria_close_${linkedAsset.id}') ;return false"><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
			<a href="javascript:void(0);" id="criteria_close_${linkedAsset.id}" onclick="closeSection('viewSection_${linkedAsset.id}', 'criteria_close_${linkedAsset.id}', 'criteria_open_${linkedAsset.id}');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif"/>" /></a>
		</#if>
		<#include "../common/_displayTenantLogo.ftl"/>&nbsp;${linkedAsset.owner.internalOrg.name}
	</h2>
	<div class="viewSection" id="viewSection_${linkedAsset.identifier}" <#if moreThanOne>style="overflow: hidden; display:none;"</#if> >
	
		<div class="columnLeft">
			<div class="viewSection smallViewSection">
				<h2><@s.text name="label.productsummary"/></h2>
				<p>
					
					<label><@s.text name="${sessionUser.serialNumberLabel}"/></label>
					<span class="fieldValue">${linkedAsset.serialNumber?html}</span>
				</p>
				<p>
					<label><@s.text name="label.rfidnumber"/></label>
					<span class="fieldValue">${linkedAsset.rfidNumber!?html}</span>
				</p>
				<p>
					<label><@s.text name="label.producttype"/></label>
					<span class="fieldValue">${linkedAsset.type.name?html}</span>
				</p>
				<p>
					<label><@s.text name="label.productstatus"/></label>
					<span class="fieldValue">${(linkedAsset.assetStatus.name)!?html}</span>
				</p>
				<p>
					<label><@s.text name="label.identified"/></label>
					<span class="fieldValue">${action.formatDate(linkedAsset.identified, false)}</span>
				</p>
				<#if linkedAsset.type.hasManufactureCertificate >
					<p>
						<label><@s.text name="label.manufacturecertificate"/></label>
						<span class="fieldValue">
							<img src="<@s.url value="/images/pdf_small.gif"/>"/>
								<a href="<@s.url action="downloadLinkedManufacturerCert" namespace="/file" uniqueID="${asset.uniqueID}" linkedAssetId="${linkedAsset.uniqueID}" />" target="_blank" >
									<@s.text name="label.downloadnow"/>
								</a>
						</span>
					</p>			
				</#if>	
			</div>
		
			<#if !linkedAsset.orderedInfoOptionList.isEmpty() >
				<div class="viewSection smallViewSection" >
					<h2>${linkedAsset.type.name?html} <@s.text name="label.attributes"/></h2>
					<#list linkedAsset.orderedInfoOptionList as infoOption >
						<p>
							<label>${infoOption.infoField.name?html} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </label>
							<span class="fieldValue">${infoOption.name?html}</span>
						</p>
					</#list>
				</div>
			</#if>
		</div>
		
		<div class="columnRight">
			<#if linkedAsset.type.warnings?exists && linkedAsset.type.warnings?length gt 0 >
				<div class="viewSection smallViewSection" >
					<h2><@s.text name="label.warnings"/></h2>
					<p class="fieldValue">
						${linkedAsset.type.warnings!?html}
					</p>
				</div>
			</#if>
		
			<#if linkedAsset.type.instructions?exists && linkedAsset.type.instructions?length gt 0 >
				<div class="viewSection smallViewSection" >
					<h2><@s.text name="label.instructions"/></h2>
					<p class="fieldValue">
						${linkedAsset.type.instructions!?html}
					</p>
				</div>
			</#if>
			
			<#if (linkedAsset.type.cautions?exists && linkedAsset.type.cautions?length gt 0) ||
				(linkedAsset.type.imageName?exists) || (!linkedAsset.type.attachments.isEmpty()) ||
				(!assetAttachments.isEmpty())>
				<div class="viewSection smallViewSection" >
					<h2><@s.text name="label.additionalinformation"/></h2> 
					<#if linkedAsset.type.cautions?exists && linkedAsset.type.cautions?length gt 0 >
						<p>
							<a href="${linkedAsset.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/></a>
						
						</p>
					</#if>
					
					<#assign attachments=action.getLinkedAssetAttachments(linkedAsset.ID) />
					<#if !attachments.isEmpty() >
						<#assign downloadAction="downloadAssetAttachedFileSafetyNetwork"/>
						<#assign attachmentID=linkedAsset.networkId/>
						<#include "/templates/html/common/_attachedFilesList.ftl"/>
					</#if>
					
					<#if !linkedAsset.type.attachments.isEmpty() >
						<#assign downloadAction="downloadAssetTypeAttachedFileSafetyNetwork"/>
						<#assign attachments=linkedAsset.type.attachments />
						<#assign attachmentID=linkedAsset.networkId/>
						<#include "/templates/html/common/_attachedFilesList.ftl"/>
					</#if>
					
					<#if linkedAsset.type.imageName?exists >
						<p>
							<img src="<@s.url action="downloadAssetTypeImageSafetyNetwork" namespace="/file" uniqueID="${linkedAsset.type.uniqueID}" networkId="${linkedAsset.networkId}"/>" alt="<@s.text name="label.productimage"/>" width="300"/>
						</p>
					</#if>
					
				</div>
			</#if>
		</div>
	</div>
</#list>
</div>
