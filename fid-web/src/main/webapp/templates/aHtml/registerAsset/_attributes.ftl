<#-- /templates/html/assetCrud also contains a version of this template -->
<#if !useAutoAttributes?exists>
	<#assign useAutoAttributes=true />
</#if>
<#if autoAttributeCriteria?exists >
	<#assign autoAttributeInputFields=autoAttributeCriteria.inputs />
</#if>
<#if !requires?exists>
	<#assign requires=true>
</#if>
<#if assetInfoFields?exists >
	<#if useAutoAttributes?exists && useAutoAttributes == true >
		<#assign changeFunction="updateAttributes(this);" />
	
	<#else>
		<#assign changeFunction="" />
	</#if>
	
	<@s.iterator value="assetInfoFields" id="infoField" status="stat" >
		<#if requires && infoField.required  >
			<#assign requiredClass='requiredField'/>
		<#else>
			<#assign requiredClass=''/>
		</#if>
		
		<@s.if test="assetInfoOptions[${stat.index}].name == null" >
			<#assign infoOptionIsNull=true>
		</@s.if>
		<@s.else>
			<#assign infoOptionIsNull=false>
		</@s.else>
		
		<#if !infoField.retired || infoOptionIsNull == false>
			<div class="infoSet <#if infoField.fieldType == 'datefield'>increasedWidthFieldHolder</#if>" infoFieldName="${infoField.name?j_string}">		
				<@s.hidden name="assetInfoOptions[${stat.index}].infoFieldId"  value="${infoField.uniqueID}"/>
				
				<#if infoField.retired >
					<label class="label">${infoField.name?html} (<@s.text name="label.retired"/>)</label>
					<span id="${infoField.uniqueID}" class="attribute fieldHolder" >
						<@s.label id="${infoField.uniqueID}" cssClass="attribute ${requiredClass}" name="assetInfoOptions[${stat.index}].name" />
						<@s.hidden name="assetInfoOptions[${stat.index}].name" />
						<@s.hidden name="assetInfoOptions[${stat.index}].uniqueIDString" /> 
					</span>
				<#else>
					<label class="label">${infoField.name?html} <#if  requires == true && infoField.required ><#include "/templates/html/common/_requiredMarker.ftl"/></#if> </label>	
					<#if infoField.fieldType == "selectbox" || infoField.fieldType == "combobox" >
						<@s.select cssClass="attribute ${requiredClass}"  list="%{ getComboBoxInfoOptions( assetInfoFields[${stat.index}], assetInfoOptions[${stat.index}] ) }" listKey="id" listValue="name" name="assetInfoOptions[${stat.index}].uniqueIDString" id="${infoField.uniqueID}" theme="fieldid" >
							<#if autoAttributeInputFields?exists && autoAttributeInputFields.contains( infoField ) >
							 	<@s.param name="onchange">${changeFunction}</@s.param>
							</#if>  
							<#if !(noblanks?exists && noblanks) >
								<@s.param name="headerKey">0</@s.param>
								<@s.param name="headerValue"></@s.param>
							</#if>
							
						</@s.select>
						
					
						<#if infoField.fieldType == "combobox" && ( !noComboBox?exists || noComboBox == false ) >
								
					        <script type="text/javascript">
					         	new toCombo('assetInfoOptions[${stat.index}].uniqueIDString');
					        </script>
				       	</#if>
				      
					</#if>
					<#if infoField.fieldType == "textfield" >
					  	<#if !infoField.usingUnitOfMeasure >
						  	<@s.textfield id="${infoField.uniqueID}" cssClass="attribute  ${requiredClass}"  name="assetInfoOptions[${stat.index}].name"   theme="fieldid"/>
						<#else>
							<div class="fieldHolder">
						  		<span class="unitOfMeasure">
						  			<@s.textfield id="${infoField.uniqueID}" name="assetInfoOptions[${stat.index}].name" theme="fieldidSimple" cssClass="dataEntry attribute unitOfMeasure  ${requiredClass}" readonly="true" />
						  		</span>
						  		<span class="action">
							  		<a href="javascript: void(0);" id="unitOfMeasureSelector_${infoField.uniqueID}" class="editLink" onclick="$('unitSelectorDiv_${infoField.uniqueID}').toggle('normal'); if( $('unitSelectorDiv_${infoField.uniqueID}').visible() ) { loadUnitOfMeasure('${infoField.uniqueID}', ${ (infoField.unitOfMeasure.id)!"null" } ); }">
							  			<img style="border: none;" src="../../images/unit_of_measure.png" />
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
					
					<#if infoField.fieldType == "datefield" >
						<#if infoField.includeTime>
							<@s.textfield theme="fieldid" id="${infoField.uniqueID}" name="assetInfoOptions[${stat.index}].name" cssClass="datetimepicker"/>
						<#else>
							<@s.textfield theme="fieldid" id="${infoField.uniqueID}" name="assetInfoOptions[${stat.index}].name" cssClass="datepicker"/>
						</#if>
						<script type="text/javascript">
							initDatePicker();
						</script>
					</#if>
					
				</#if>
			</div>
		</#if>
	</@s.iterator>
</#if>
