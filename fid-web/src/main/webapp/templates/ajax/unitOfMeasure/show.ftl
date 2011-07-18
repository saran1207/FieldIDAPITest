<#assign html>
	<@s.form theme="fieldidSimple" action="selectUnitOfMeasure" namespace="/ajax" id="unitOfMeasureForm_${infoFieldId}" cssClass="inputForm" cssStyle="width:auto">
		<@s.hidden name="infoFieldId"/>
		<table>
			<tr class="fieldData">
				<td><@s.text name="label.unitofmeasure"/></td>
				<td><@s.select name="unitOfMeasureId" id="unitOfMeasureId_${infoFieldId}" onchange="loadUnitOfMeasure('${infoFieldId}');" list="unitOfMeasures" listKey="id" listValue="name" /></td>
			</tr>
			<#list inputOrder as currentUnitOfMeasure >
				<tr class="fieldData">
					<td>${currentUnitOfMeasure.name}</td>
					<td><@s.textfield name="unitInputs[${currentUnitOfMeasure_index}]" id="${currentUnitOfMeasure.id}_${infoFieldId}" /></td>
				</tr>
			</#list>
			<tr>
				<td>&nbsp;</td>
				<td>
					<@s.submit key="hbutton.submit"/>
					<@s.text name="label.or"/>
					<a href="javascript:void(0);" onClick="$('unitSelectorDiv_${infoFieldId}').hide();">
						<@s.text name="label.cancel" />
					</a>
				</td>
			</tr>
		</table>
	</@s.form>
</#assign>

<#escape j as j?js_string>
	var unitOfMeasure_${infoFieldId}Inputs = new Array();
	<#list inputOrder as currentUnitOfMeasure >
		unitOfMeasure_${infoFieldId}Inputs += '${currentUnitOfMeasure.id}_${infoFieldId}';
	</#list>
	
	if( $( 'unitOfMeasureForm_${infoFieldId}' ) != null ) {
		$( 'unitOfMeasureForm_${infoFieldId}' ).stopObserving( 'submit', submitUnitOfMeasure );
	} 
	
	$('unitSelectorDiv_${infoFieldId}').replace( new Element( 'div', { 'id': 'unitSelectorDiv_${infoFieldId}', 'class': 'unitMeasureDiv'} ).insert( '${html}' ) );

	$( 'unitOfMeasureForm_${infoFieldId}' ).observe( 'submit', submitUnitOfMeasure ); 

</#escape>