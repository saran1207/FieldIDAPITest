<#assign html>
	<@s.form action="productCreate" id="subProductCreateForm_${productTypeId}" namespace="/ajax" theme="fieldid" cssClass="crudForm" cssStyle="display:none">
		<@s.hidden name="ownerId"/>
		<@s.hidden name="identified"/>
		<@s.hidden name="productTypeId" id="productType"/>
		<#include "/templates/html/common/_formErrors.ftl"/>
		<p id="serialNumberRow_${productTypeId}">
			<@s.label required="true" value="${action.getText( Session.sessionUser.serialNumberLabel ) }"/>
			<span class="serialNumber">
				<@s.textfield name="serialNumber" id="subProductSerialNumber_${productTypeId}" onchange="checkSerialNumber('subProductSerialNumber_${productTypeId}', -${productTypeId});" theme="fieldidSimple" />
			</span>
			<span class="action">
				<a href="generateSerialNumber" target="_blank" onclick="generateSerialNumber('subProductSerialNumber_${productTypeId}', -${productTypeId}); return false;" ><@s.text name="label.generate"/></a>
			</span>
			<span class="serialNumberStatus"></span>
		</p>
		
		<p>
			<label><@s.text name="label.rfidnumber"/></label>
			<@s.textfield name="rfidNumber" id="subProductRfidNumber_${productTypeId}" />
		</p>
		
		<#assign fieldPrefix= 'product' />
		<#assign useAutoAttributes=true />
		<#if autoAttributeCriteria?exists >
			<#assign autoAttributeInputFields=autoAttributeCriteria.inputs />
		</#if>
		
		<#assign prefix= 'product' />
		<#assign requires='true'>
		<@s.if test="${fieldPrefix}InfoFields != null" >
			<#if useAutoAttributes?exists && useAutoAttributes == true >
				<#assign changeFunction="updateAttributes(this.form);" />
			
			<#else>
				<#assign changeFunction="" />
			</#if>
			
			<@s.iterator value="${fieldPrefix}InfoFields" id="infoField" status="stat" >
				
				<#if requires?exists && requires == 'true' && infoField.required  >
					<#assign required='true' />
				<#else>
					<#assign required='false' />
				</#if>
				
				<p>		
					<@s.hidden name="${prefix}InfoOptions[${stat.index}].infoFieldId"  value="${infoField.uniqueID}"/>
					<#if infoField.retired >
						<label>${infoField.name} (${action.getText( 'label.retired' ) })</label>
						<@s.if test="${prefix}InfoOptions[${stat.index}].name != null" >
							<@s.label id="${infoField.uniqueID}" cssClass="attribute" label="" name="${prefix}InfoOptions[${stat.index}].name" />
							<@s.hidden name="${prefix}InfoOptions[${stat.index}].name" />
							<@s.hidden name="${prefix}InfoOptions[${stat.index}].uniqueIDString" /> 
						</@s.if>
					<#else>
						<@s.label value="${infoField.name}" required="${required}" />
						<#if infoField.fieldType == "selectbox" || infoField.fieldType == "combobox" >
							<@s.select cssClass="attribute"  list="%{ getComboBoxInfoOptions( ${fieldPrefix}InfoFields[${stat.index}], ${prefix}InfoOptions[${stat.index}] ) }" listKey="id" listValue="name" name="${prefix}InfoOptions[${stat.index}].uniqueIDString" id="${infoField.uniqueID}"  >
								<#if autoAttributeInputFields?exists && autoAttributeInputFields.contains( infoField ) >
								 	<@s.param name="onchange">${changeFunction}</@s.param>
								</#if>  
								<#if !(noblanks?exists && noblanks == 'true') >
									<@s.param name="headerKey">0</@s.param>
									<@s.param name="headerValue"></@s.param>
								</#if>
								
							</@s.select>
							
						
							<#if infoField.fieldType == "combobox" && ( !noComboBox?exists || noComboBox == false ) >
									
						        <script type="text/javascript">
						         	new toCombo('${prefix}InfoOptions[${stat.index}].uniqueIDString');
						        </script>
					       	</#if>
					      
						</#if>
						<#if infoField.fieldType == "textfield" >
						
						  	<#if !infoField.usingUnitOfMeasure >
							  	<@s.textfield id="${infoField.uniqueID}" cssClass="attribute"  name="${prefix}InfoOptions[${stat.index}].name"  required="${required}"  />
							<#else>
						  		<span class="unitOfMeasure">
						  			<@s.textfield id="${infoField.uniqueID}" name="${prefix}InfoOptions[${stat.index}].name" theme="fieldidSimple" cssClass="dataEntry attribute unitOfMeasure" readonly="true" required="${required}"/>
						  		</span>
						  		<span class="action">
							  		<a href="javascript: void(0);" id="unitOfMeasureSelector_${infoField.uniqueID}" class="editLink" onclick="$('unitSelectorDiv_${infoField.uniqueID}').toggle('normal'); if( $('unitSelectorDiv_${infoField.uniqueID}').visible() ) { loadUnitOfMeasure('${infoField.uniqueID}', ${ (infoField.unitOfMeasure.id)!"null" } ); }">
							  			<img style="border: none;" src="images/security_alarm_panel_2.png" />
							  		</a>
								</span>
								<span id="unitSelectorDiv_${infoField.uniqueID}" class="unitMeasureDiv" style="display:none;" >
								   <table width="100%">
								     <tr><td align="center">
								   	  <img src="images/loading_animation_liferay.gif" />
								     </td></tr>
								  </table>
								</span>
								
							</#if>
						</#if>
					</#if>
				</p>
				
			</@s.iterator>
			</@s.if>
		
		<div class="formAction">
			<@s.submit key="label.cancel" onclick="Effect.BlindUp( 'subProductCreateForm_${productTypeId}', { afterFinish: function() { $('subProductCreateForm_${productTypeId}').remove(); } } ); return false;" />
			<@s.submit key="label.save" onclick="checkDuplicateRfids('subProductRfidNumber_${productTypeId}', this); return false;" />
		</div>
	</@s.form>
</#assign>

<#escape x as x?js_string>
	var formCode = '${html}';
	
	
	
	
	if( $( 'subProductCreateForm_${productTypeId}' ) ) {
	
		$( 'subProductCreateForm_${productTypeId}' ).remove();
		$( 'subProductType_${productTypeId}' ).insert( formCode );
		$( 'subProductCreateForm_${productTypeId}' ).show();
	} else {
		$( 'subProductType_${productTypeId}' ).insert( formCode );
	}
		
	if( ! $( 'subProductCreateForm_${productTypeId}' ).visible() ) {
		Effect.BlindDown( 'subProductCreateForm_${productTypeId}' );
	}
	$( 'subProductCreateForm_${productTypeId}' ).observe( 'submit', submitCreateForm );
	
</#escape>