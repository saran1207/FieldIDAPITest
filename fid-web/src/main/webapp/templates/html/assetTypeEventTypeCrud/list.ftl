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
			return confirm( '${action.getText('warning.changingeventtypeselection')}' ); 
		}
		return true;
	}
	
	var selectedEvents = new Array();
	
		
</script>

${action.setPageType('asset_type', 'select_inspection_types')!}
<#if ! eventTypes.isEmpty() >
	<@s.form action="assetTypeEventTypesSave" id="assetTypeEventTypesSave" theme="simple">
		<@s.hidden name="assetTypeId" />
		<table class="list" >
			<tr>
				<th class="checkboxRow"><@s.text name="label.selected"/></th>
				<th ><@s.text name="label.eventtype"/></th>
				
				
			</tr>
			
			<#list eventTypes as eventType>
				<tr  <#if assetTypeEventTypes[eventType_index] > class="selectedEvent"</#if> id="event_selectType_${eventType.id}">
					<td><@s.checkbox name="assetTypeEventTypes[${eventType_index}]" onclick="changeSelection(this)" id="selectType_${eventType.id}" /></td>
					<td class="name">${eventType.name}
						<#if eventType.screenBean?exists > - ${(eventType.screenBean.name)!}</#if>
					</td>
					
					<#if assetTypeEventTypes[eventType_index] >
						<script type="text/javascript">
							selectedEvents.push( 'selectType_${eventType.id}' );
						</script>
					</#if>
				</tr>
			</#list>
		</table>
		<div class="formAction">
			<button  onclick=" window.location = '<@s.url action="assetType" includeParams="none" uniqueID="${assetTypeId}" />'; return false;" ><@s.text name="hbutton.cancel" /></button>
			<@s.submit key="hbutton.save" onclick="return formSubmit();" />
		</div>
	</@s.form>
<#else>
	<div class="emptyList" >
		<p>
			<@s.text name="label.emptyeventtypes" />
		</p>
	</div>
	
</#if>

