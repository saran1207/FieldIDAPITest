<script type="text/javascript">
	function changeSelection( checkbox ) {
		var row = $( 'asset_' + checkbox.id );
		
		if( checkbox.checked ) {
			row.addClassName( "selectedAsset" );
		} else {
			row.removeClassName( "selectedAsset" );
		}
	}
	
	function formSubmit() {
		var  doConfirm = false;
		for( var i = 0; i < selectedAssets.size(); i++ ){
			if( !( $( selectedAssets[i] ).checked ) ) {
				doConfirm = true;
				break;
			}
		}
		
		if( doConfirm ) {
			return confirm( '${action.getText('warning.changingassettypeselection')}' ); 
		}
		return true;
	}
	
	var selectedAssets = new Array();
	
		
</script>
${action.setPageType('event_type', 'select_asset_types')!}

<#if ! assetTypes.isEmpty() >
	<@s.form action="eventTypeAssetTypesSave" id="eventTypeAssetTypesSave" theme="simple">
		<@s.hidden name="eventTypeId" />
		<table class="list" >
			<tr>
				<th class="checkboxRow"><@s.text name="label.selected"/></th>
				<th ><@s.text name="label.assettype"/></th>
			</tr>
			
			<#list assetTypes as assetType>
				<tr  <#if eventTypeAssetTypes[assetType_index] > class="selectedAsset"</#if> id="asset_selectType_${assetType.id}">
					<td><@s.checkbox name="eventTypeAssetTypes[${assetType_index}]" onclick="changeSelection(this)" id="selectType_${assetType.id}" /></td>
					<td class="name">${assetType.name}
						<#if assetType.screenBean?exists > - ${(assetType.screenBean.name)!}</#if>
					</td>
					
					<#if eventTypeAssetTypes[assetType_index] >
						<script type="text/javascript">
							selectedAssets.push( 'selectType_${assetType.id}' );
						</script>
					</#if>
				</tr>
			</#list>
		</table>
		<div class="formAction">
			<@s.url id="cancelUrl" action="eventType" includeParams="none" uniqueID="${eventTypeId}"/>
			<@s.submit key="hbutton.save" onclick="return formSubmit();" />
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
<#else>
	<div class="emptyList" >
		<p>
			<@s.text name="label.emptyassettypes" />
		</p>
	</div>
	
</#if>

