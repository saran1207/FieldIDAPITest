<script type="text/javascript">
	function changeSelection( checkbox ) {
		var row = $( 'event_' + checkbox.id );
		
		if( checkbox.checked ) {
			row.addClassName( "selectedEvent" );
		} else {
			row.removeClassName( "selectedEvent" );
		}
	}
	
	function formSubmit() {
		var  doConfirm = false;
		for( var i = 0; i < selectedEvents.size(); i++ ){
			if( !( $( selectedEvents[i] ).checked ) ) {
				doConfirm = true;
				break;
			}
		}
		
		if( doConfirm ) {
			return confirm( '${action.getText('warning.changinginspectiontypeselection')}' ); 
		}
		return true;
	}
	
	var selectedEvents = new Array();
	
		
</script>

${action.setPageType('product_type', 'select_inspection_types')!}
<#if ! inspectionTypes.isEmpty() >
	<@s.form action="productTypeEventTypesSave" theme="simple">
		<@s.hidden name="productTypeId" />
		<table class="list" >
			<tr>
				<th class="checkboxRow"><@s.text name="label.selected"/></th>
				<th ><@s.text name="label.inspectiontype"/></th>
				
				
			</tr>
			
			<#list inspectionTypes as inspectionType>
				<tr  <#if productTypeInspectionTypes[inspectionType_index] > class="selectedEvent"</#if> id="event_selectType_${inspectionType.id}">
					<td><@s.checkbox name="productTypeInspectionTypes[${inspectionType_index}]" onclick="changeSelection(this)" id="selectType_${inspectionType.id}" /></td>
					<td class="name">${inspectionType.name}
						<#if inspectionType.screenBean?exists > - ${(inspectionType.screenBean.name)!}</#if>
					</td>
					
					<#if productTypeInspectionTypes[inspectionType_index] >
						<script type="text/javascript">
							selectedEvents.push( 'selectType_${inspectionType.id}' );
						</script>
					</#if>
				</tr>
			</#list>
		</table>
		<div class="formAction">
			<button  onclick=" window.location = '<@s.url action="productType" includeParams="none" uniqueID="${productTypeId}" />'; return false;" ><@s.text name="hbutton.cancel" /></button>
			<@s.submit key="hbutton.save" onclick="return formSubmit();" />
		</div>
	</@s.form>
<#else>
	<div class="emptyList" >
		<p>
			<@s.text name="label.emptyinspectiontypes" />
		</p>
	</div>
	
</#if>

