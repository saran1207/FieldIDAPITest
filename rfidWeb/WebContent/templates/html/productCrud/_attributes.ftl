<#assign fieldPrefix= 'product' />
<#assign useAutoAttributes=true />
<#if autoAttributeCriteria?exists >
	<#assign autoAttributeInputFields=autoAttributeCriteria.inputs />
</#if>

<#assign prefix= 'product' />
<#assign requires='true'>
<@s.if test="${fieldPrefix}InfoFields != null" >
	<#if useAutoAttributes?exists && useAutoAttributes == true >
		<#assign changeFunction="updateAttributes(this);" />
	
	<#else>
		<#assign changeFunction="" />
	</#if>
	
	<@s.iterator value="${fieldPrefix}InfoFields" id="infoField" status="stat" >
		<#if requires?exists && requires == 'true' && infoField.required  >
			<#assign required='true' />
			<#assign requiredClass='requiredField'/>
		<#else>
			<#assign required='false' />
			<#assign requiredClass=''/>
		</#if>
		
		<@s.if test="${prefix}InfoOptions[${stat.index}].name == null" >
			<#assign infoOptionIsNull=true>
		</@s.if>
		<@s.else>
			<#assign infoOptionIsNull=false>
		</@s.else>
		
		<#if !infoField.retired || infoOptionIsNull == false>
			<div class="infoSet">		
				<@s.hidden name="${prefix}InfoOptions[${stat.index}].infoFieldId"  value="${infoField.uniqueID}"/>
				
				<#if infoField.retired >
					<label class="label">${infoField.name} (<@s.text name="label.retired"/>)</label>
					<span id="${infoField.uniqueID}" class="attribute fieldHolder" >
						<@s.label id="${infoField.uniqueID}" cssClass="attribute ${requiredClass}" name="${prefix}InfoOptions[${stat.index}].name" />
						<@s.hidden name="${prefix}InfoOptions[${stat.index}].name" />
						<@s.hidden name="${prefix}InfoOptions[${stat.index}].uniqueIDString" /> 
					</span>
				<#else>
					<label class="label">${infoField.name?html} <#if  requires?exists && requires == 'true' && infoField.required ><#include "../common/_requiredMarker.ftl"/></#if> </label>	
					<#if infoField.fieldType == "selectbox" || infoField.fieldType == "combobox" >
						<@s.select cssClass="attribute ${requiredClass}"  list="%{ getComboBoxInfoOptions( ${fieldPrefix}InfoFields[${stat.index}], ${prefix}InfoOptions[${stat.index}] ) }" listKey="id" listValue="name" name="${prefix}InfoOptions[${stat.index}].uniqueIDString" id="${infoField.uniqueID}" theme="fieldid" >
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
						  	<@s.textfield id="${infoField.uniqueID}" cssClass="attribute  ${requiredClass}"  name="${prefix}InfoOptions[${stat.index}].name"  required="${required}"  theme="fieldid"/>
						<#else>
							<div class="fieldHolder">
						  		<span class="unitOfMeasure">
						  			<@s.textfield id="${infoField.uniqueID}" name="${prefix}InfoOptions[${stat.index}].name" theme="fieldidSimple" cssClass="dataEntry attribute unitOfMeasure  ${requiredClass}" readonly="true" required="${required}"/>
						  		</span>
						  		<span class="action">
							  		<a href="javascript: void(0);" id="unitOfMeasureSelector_${infoField.uniqueID}" class="editLink" onclick="$('unitSelectorDiv_${infoField.uniqueID}').toggle('normal'); if( $('unitSelectorDiv_${infoField.uniqueID}').visible() ) { loadUnitOfMeasure('${infoField.uniqueID}', ${ (infoField.unitOfMeasure.id)!"null" } ); }">
							  			<img style="border: none;" src="images/unit_of_measure.png" />
							  		</a>
								</span>
								<div id="unitSelectorDiv_${infoField.uniqueID}" class="unitMeasureDiv" style="display:none;" >
								   <table width="100%">
								     <tr><td align="center">
								   	  <img src="images/loading_animation_liferay.gif" />
								     </td></tr>
								  </table>
								</div>
							</div>
						</#if>
					</#if>
				</#if>
			</div>
		</#if>
	</@s.iterator>
</@s.if>