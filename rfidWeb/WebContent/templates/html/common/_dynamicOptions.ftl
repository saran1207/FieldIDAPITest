<head>
	<script language="javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"> </script>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
	<script type="text/javascript">
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
	</script>
</head>
<@s.if test="${fieldPrefix}InfoFields != null" >
<#if useAutoAttributes?exists && useAutoAttributes == true >
	<#assign changeFunction="updateAttributes(this);" />

<#else>
	<#assign changeFunction="" />
</#if>

<@s.iterator value="${fieldPrefix}InfoFields" id="infoField" status="stat" >
	<#if requires?exists && requires == 'true' && infoField.required  >
		<#assign required='true' />
		<#assign requiredClass=' requiredField' />
	<#else>
		<#assign required='false' />
		<#assign requiredClass=''/>
	</#if>
	
	<div class="formRowHolder">		
		<@s.hidden name="${prefix}InfoOptions[${stat.index}].infoFieldId"  value="${infoField.uniqueID}"/>
		<#if infoField.retired >
			<@s.if test="${prefix}InfoOptions[${stat.index}].name != null" >
				<@s.label id="${infoField.uniqueID}" cssClass="attribute" label="${infoField.name} (${action.getText( 'label.retired' ) })" name="${prefix}InfoOptions[${stat.index}].name" labelposition="left" />
				<@s.hidden name="${prefix}InfoOptions[${stat.index}].name" />
				<@s.hidden name="${prefix}InfoOptions[${stat.index}].uniqueIDString" /> 
			</@s.if>
		<#else>
			<#if infoField.fieldType == "selectbox" || infoField.fieldType == "combobox" >
				
			
				<@s.select cssClass="attribute${requiredClass}" label="${infoField.name}" list="%{ getComboBoxInfoOptions( ${fieldPrefix}InfoFields[${stat.index}], ${prefix}InfoOptions[${stat.index}] ) }" listKey="id" listValue="name" name="${prefix}InfoOptions[${stat.index}].uniqueIDString" labelposition="left"  id="${infoField.uniqueID}"  required="${required}" >
					<#if autoAttributeInputFields?exists && autoAttributeInputFields.contains( infoField ) >
					 	<@s.param name="onchange">${changeFunction}</@s.param>
					</#if>
					<#if !(noblanks?exists && noblanks == 'true') >					
						<@s.param name="headerKey">0</@s.param>
						<@s.param name="headerValue"></@s.param>
					</#if>
										
				</@s.select>
		        <#if ( infoField.fieldType == "combobox" && ( !noComboBox?exists || noComboBox == false ) ) >
			        <script type="text/javascript">
			         	new toCombo('${prefix}InfoOptions[${stat.index}].uniqueIDString');
			        </script>
		       	</#if>
			</#if>
			<#if infoField.fieldType == "textfield" >
			
			  	<#if !infoField.usingUnitOfMeasure >
				  	<@s.textfield id="${infoField.uniqueID}" cssClass="attribute${requiredClass}" label="${infoField.name}" name="${prefix}InfoOptions[${stat.index}].name" labelposition="left" required="${required}"  />
				<#else>
					<div class="dates">
				  		<@s.textfield id="${infoField.uniqueID}" label="${infoField.name}" name="${prefix}InfoOptions[${stat.index}].name" labelposition="left" cssClass="dataEntry attribute${requiredClass}" readonly="true" required="${required}"/>
				  		<div style="padding:10px 0px;">
					  		<a href="javascript: void(0);" id="unitOfMeasureSelector_${infoField.uniqueID}" class="editLink" onclick="$('unitSelectorDiv_${infoField.uniqueID}').toggle(); if( $('unitSelectorDiv_${infoField.uniqueID}').visible() ) {  loadUnitOfMeasure('${infoField.uniqueID}'); }">
					  			<img style="border: none;" src="images/unit_of_measure.png" />
					  		</a>
					  		
					  		<div id="unitSelectorDiv_${infoField.uniqueID}" class="unitMeasureDiv" style="display:none;" >
							   <table width="100%">
							     <tr><td align="center">
							   	  <img src="images/loading_animation_liferay.gif" />
							     </td></tr>
							  </table>
							</div>
						</div>
			  		</div>
					
				</#if>
			</#if>
		</#if>
		
	</div>
	
</@s.iterator>
</@s.if>