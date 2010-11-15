${action.setPageType('auto_attribute', 'edit')!}
<style>	
	.formAction div{
		padding: 0;
	}
</style>
<@s.form action="autoAttributeCriteriaEdit!save" cssClass="inputForm" theme="css_xhtml" >
	<@s.hidden name="uniqueID" id="uniqueId"/>
	
	<h2> <@s.text name="label.assettype" /> : <@s.text name="${assetType.name?html!}" /> </h2>
	
	<div class="dropBoxHolder" >
		
		<div  class="dropBox" >
			<div class="dropBoxPadding" id="availablelist">
			<h3 ><@s.text name="label.availablefields" /></h3>
			<@s.iterator value="assetType.infoFields" id="infoField" status="stat" >
				<#if infoField?exists && available.contains( infoField.uniqueID ) >
					<div class="dynamicOption dragOption <#if infoField.hasStaticInfoOption() > staticOption </#if>" id="field_${infoField.uniqueID}">
						<@s.hidden name="available[]" value="%{${infoField.uniqueID}}" />
						<span class="">${infoField.name?html}</span>
						<span>[
							<#switch infoField.fieldType >
								<#case 'textfield'>
								T
								<#break >
								<#case 'combobox'>
								C
								<#break >
								<#case 'selectbox'>
								S
								<#break >
							</#switch>
							]
						</span>
							
					</div>
					<script type="text/javascript">
						new Draggable("field_${infoField.uniqueID}", {revert:true})
					</script>
				</#if>
			</@s.iterator>
			</div>
		</div>
		
	
		<div  class="dropBox">
			<div class="dropBoxPadding" id="inputlist">
			<h3 ><@s.text name="label.inputfields" /></h3>
			<@s.iterator value="assetType.infoFields" id="infoField" status="stat" >
				<#if infoField?exists && inputs.contains( infoField.uniqueID ) >
					<div class="dynamicOption dragOption <#if infoField.hasStaticInfoOption() > staticOption </#if>" id="field_${infoField.uniqueID}" >
						<@s.hidden name="inputs[]" value="%{${infoField.uniqueID}}" />
						<span class="">${infoField.name}</span>
						<span>[
							<#switch infoField.fieldType >
								<#case 'textfield'>
								T
								<#break >
								<#case 'combobox'>
								C
								<#break >
								<#case 'selectbox'>
								S
								<#break >
							</#switch>
							]
						</span>
							
					</div>
					<script type="text/javascript">
						new Draggable("field_${infoField.uniqueID}", {revert:true})
					</script>
				</#if>
			</@s.iterator>
			
			</div>
			<@s.fielderror>
				<@s.param>inputs</@s.param>				
			</@s.fielderror>
		</div>
		
		<div class="dropBox" >
			<div class="dropBoxPadding"  id="outputlist">
			<h3 ><@s.text name="label.outputfields" /></h3>
			<@s.iterator value="assetType.infoFields" id="infoField" status="stat" >
				<#if infoField?exists && outputs.contains( infoField.uniqueID ) >
					<div class="dynamicOption dragOption <#if infoField.hasStaticInfoOption() > staticOption </#if>" id="field_${infoField.uniqueID}" >
						<@s.hidden name="outputs[]" value="%{${infoField.uniqueID}}" />
						<span class="">${infoField.name}</span>
						<span>[
							<#switch infoField.fieldType >
								<#case 'textfield'>
								T
								<#break >
								<#case 'combobox'>
								C
								<#break >
								<#case 'selectbox'>
								S
								<#break >
							</#switch>
							]
						</span>
							
					</div>
					<script type="text/javascript">
						new Draggable("field_${infoField.uniqueID}", {revert:true})
					</script>
				</#if>
			</@s.iterator>
			
			</div>
			<@s.fielderror>
				<@s.param>outputs</@s.param>				
			</@s.fielderror>
			
		</div>
		<div class="clearBoth" ></div>
	</div>
	<div class="help">
		<h4 >Instructions </h4>
		<p>
			To create an Auto Attribute Criteria, simply drag the fields from the available list into either the input or output list.<br/>
			You can place only combo box and select box fields in to the inputs list.  All field types can be placed in the output list
			Refer to the legend below to identify what type each field is. 
		</p>
		<table class="legend">
			<tr><th>Symbol</th><th>Field Type</th> </tr>
			<tr><td>[ T ]</td><td>text field</td> </tr>
			<tr><td>[ S ]</td><td>select box</td> </tr>
			<tr><td>[ C ]</td><td>combo box</td> </tr>
		</table>
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="autoAttributeCriteriaList"/>
		
		<@s.submit key="hbutton.save" onclick="return formSubmit();"/>
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect( '${cancelUrl}' );" />	<@s.text name="label.cancel"/></a>
		<#if autoAttributeCriteria.id?exists >
			<@s.url id="removeUrl" action="autoAttributeCriteriaRemove" uniqueID="${uniqueID}"/>
			<@s.text name="label.or"/>
			<a href="#" onclick="if( confirm('Are you sure?') ) { return redirect( '${removeUrl}' ); }" ><@s.text name="label.delete"/></a>
		</#if>
		
	</div>
	<#if autoAttributeCriteria.id?exists  >
		<div class="actions">
			<a href="<@s.url value="autoAttributeDefinitionList.action" criteriaId="${autoAttributeCriteria.id}"/>"><@s.text name="label.viewautoattributedefinitions" /></a>
		</div>
	</#if>
</@s.form >
<script type="text/javascript" >
	
	Droppables.add( "outputlist", {accept:'dynamicOption', onDrop: function(element){$('outputlist').appendChild( element);} } );
	Droppables.add( "inputlist", {accept:'staticOption', onDrop: function(element){$('inputlist').appendChild( element);} } );
	Droppables.add( "availablelist", {accept:['dynamicOption','staticOption'], onDrop: function(element){$('availablelist').appendChild( element);} } );
	
	function formSubmit() {
		if( "${autoAttributeCriteria.id!}" != "" ) {
			if( !confirm( 'You have changed the fields in the criteria. Saving these changes will cause all existing auto attribute definitions to be deleted. Are you sure you want to do this?') ) {
				return false;	
			}
		
		}
		renameElements( 'inputlist', 'inputs' );
		renameElements( 'outputlist', 'outputs' );
		
		return true;
		
	}
	
	function renameElements( listId, inputName ) {
		var inputs = $( listId ).getElementsByTagName( 'input' );
		for( var i = 0; i < inputs.length; i++ ) {
			inputs[i].name = inputName + '[' + i + ']';
		}
	}
</script>

