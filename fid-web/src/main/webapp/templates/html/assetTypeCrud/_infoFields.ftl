<head>
	<script language="javascript" src="javascript/assetTypeCrud.js"> </script>
</head>

<div id="infoFieldEditing">
	<@s.fielderror>
		<@s.param>infoFields</@s.param>				
	</@s.fielderror>
	<@s.fielderror>
		<@s.param>editInfoOptions</@s.param>				
	</@s.fielderror>
	<div class="infoFieldHeader">
		<div class="infoFieldCol formLabel"><@s.text name="label.name" /></div>
		<div class="infoFieldCol formLabel selectBoxCol"><@s.text name="label.type" /></div>
		<div class="infoFieldCol formLabel selectBoxCol"><@s.text name="label.default" /></div>
		<div class="infoFieldCol formLabel checkboxCol"><@s.text name="label.required" /></div>
		
	</div>
	<div id="infoFields" class="section">
		<@s.iterator value="infoFields" id="infoField" status="stat" >
			<#if infoField?exists >
			<div class="handle <#if infoField.retired >retired</#if>" id="field_${stat.index}"  <#if infoFields[stat.index].deleted == true > style="display:none" </#if> >
				<@s.hidden name="infoFields[${stat.index}].uniqueID" />
				<@s.hidden name="infoFields[${stat.index}].weight" />
				<@s.hidden name="infoFields[${stat.index}].deleted" />
				<@s.hidden name="infoFields[${stat.index}].retired" />
				<div class="infoFieldCol"><@s.textfield   name="infoFields[${stat.index}].name" size="30" disabled="${infoField.retired ? string}" cssClass="name"/> </div>
				<div class="infoFieldCol selectBoxCol"><@s.select list="infoFieldTypes" listKey="name" listValue="label" name="infoFields[${stat.index}].fieldType" onchange="changeFieldType(this)" disabled="${infoField.retired ? string}" cssClass="fieldType" /></div>
				<div class="infoFieldCol selectBoxCol"><@s.select list="unitsOfMeasure" listKey="id" listValue="name"  name="infoFields[${stat.index}].defaultUnitOfMeasure" disabled="${infoField.retired ? string}" /></div> 
				<div class="infoFieldCol checkboxCol"><@s.checkbox name="infoFields[${stat.index}].required" disabled="${infoField.retired ? string}"/></div>
				
				
				<div class="infoFieldCol linkCol">
					<#if ! undeletableInfoFields.contains( infoFields[stat.index].uniqueID ) ><a href="_infoFields.ftl#" onclick="destroyField(this); return false;" ><@s.text name="hbutton.delete" /></a>
					<#else>
						<a href="_infoFields.ftl#" onclick="retire( this ); return false;"><#if infoField.retired ><@s.text name="label.unretire" /><#else><@s.text name="label.retire" /></#if></a>
					</#if>
					
					<a href="_infoFields.ftl#" onclick="openCloseOptions( 'infoOptionContainer_${stat.index}'); return false;" class="editInfoOptions" ><@s.text name="label.editinfooptions" /></a>
					
				</div>
				
				<div style="display:none" class="infoOptionContainer" id="infoOptionContainer_${stat.index}" >
					<@s.text name="label.infooptions" />
					<div id="infoOptions_${stat.index}" class="infoOptions" >
					<#if editInfoOptions?exists > 
					<@s.iterator value="editInfoOptions" id="infoOption" status="infoOptionStat" >
						
						<#if stat.index == infoOption.infoFieldIndex >
							<div class="infoOptionHandle" id="infoOption_${infoOptionStat.index}" <#if infoOption.deleted == true > style="display:none" </#if>>
								<@s.hidden name="editInfoOptions[${infoOptionStat.index}].uniqueID" />
								<@s.hidden name="editInfoOptions[${infoOptionStat.index}].weight"  />
								<@s.textfield name="editInfoOptions[${infoOptionStat.index}].name"  size="30"/>
								<@s.hidden name="editInfoOptions[${infoOptionStat.index}].infoFieldIndex" />
								<@s.hidden name="editInfoOptions[${infoOptionStat.index}].deleted" />
								<a href="_infoFields.ftl#" onclick="destroyOption(this); return false;" ><@s.text name="hbutton.delete" /></a>
							</div>
						</#if>
					</@s.iterator>
					</#if>
					</div>
					<input type="button" name="add" class="addOption" onclick="createOption('infoOptions_${stat.index}', 'field_${stat.index}'); " value="<@s.text name="hbutton.add" />"/> <input type="button" name="undo"  onclick="undoOptionDeletes(this)" value="<@s.text name="label.undodeletes" />"/>
					
				</div>
				
			</div>
			</#if>
		</@s.iterator>
		
	</div>
	<div>
		<input type="button" name="add" id="addInfoField" onclick="createField(); " value="<@s.text name="hbutton.add" />"/> <input type="button" name="undo"  onclick="undoDeletes()" value="<@s.text name="label.undodeletes" />"/>
	</div>
</div>
<div style="display:none" >
	<div class="handle" id="standby" style="display:none">
		<@s.hidden name="infouniqueID" />
		<@s.hidden name="infoweight" />
		<@s.hidden name="infodeleted" />
		<div class="infoFieldCol"><@s.textfield name="infoname" size="30" cssClass="name"/></div>
		<div class="infoFieldCol selectBoxCol"><@s.select list="infoFieldTypes" listKey="name" listValue="label" cssClass="fieldType" name="infofieldType" onchange="changeFieldType(this)" /></div>
		<div class="infoFieldCol selectBoxCol"><@s.select list="unitsOfMeasure" listKey="id" listValue="name"  name="infodefaultUnitOfMeasure" cssStyle="display:none"/></div>
		<div class="infoFieldCol checkboxCol"><@s.checkbox name="inforequired"/></div>
		
		
		<div class="infoFieldCol linkCol">
			<a href="_infoFields.ftl#" onclick="destroyField(this); return false;" ><@s.text name="hbutton.delete" /></a>
			<a href="_infoFields.ftl#" onclick="openCloseOptions( 'infoOptionContainer_' + findFieldIndex( this.parentNode.parentNode.id ) ); return false;" class="editInfoOptions"><@s.text name="label.editinfooptions" /></a>
		</div>
		
		<div style="clear:both; display:none" class="infoOptionContainer" id="infoOptionContainer_" >
			<@s.text name="label.infooptions" />
			<div id="infoOptions_" class="infoOptions"> 
			
			</div>
			<input type="button" name="add" class="addOption" onclick="createOption( 'infoOptions_' + findFieldIndex( this.parentNode.parentNode.id ), 'field_' + findFieldIndex( this.parentNode.parentNode.id ) ); " value="<@s.text name="hbutton.add" />"/> <input type="button" name="undo"  onclick="undoOptionDeletes(this)" value="<@s.text name="label.undodeletes" />"/>
					
		</div>
	</div>
	
	<div class="infoOptionHandle" id="optionStandby" style="display:none">
		
		
		<@s.hidden name="infoOp.weight" value="" />
		<@s.textfield name="infoOp.name" value="" size="30"/>
		<@s.hidden name="infoOp.infoFieldIndex" value=""/>
		<@s.hidden name="infoOp.deleted" value="false" />
		<a href="_infoFields.ftl#" onclick="destroyOption(this); return false;" ><@s.text name="hbutton.delete" /></a>
	</div>
</div>

<script type="text/javascript">
	
	arrayIndexOptions = ${editInfoOptions.size() };
	arrayIndex = ${infoFields.size()} ;
	Element.extend(document).observe('dom:loaded', prepInterface );
	createLineItemSortables();
	setupRemoved();
	
	correctSorting();
	unretireLabel = "<@s.text name="label.unretire" />"
	retireLabel = "<@s.text name="label.retire" />"
	
</script>
