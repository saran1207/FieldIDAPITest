<script type="text/javascript">
	function selectAll(){
		$$('input[type="checkbox"]').each(function(checkBox){
			checkBox.checked=true;
			changeSelection(checkBox);
		});
	}
	
	function selectNone(){
		$$('input[type="checkbox"]').each(function(checkBox){
			checkBox.checked=false;
			changeSelection(checkBox);
		});
	}

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

<style>
.list th{
	min-width: 65px;
}

.list th.checkboxRow a{
	color: #356CA2;
}
</style>
${action.setPageType('event_type', 'select_asset_types')!}

<#if ! assetTypes.isEmpty() >
	<@s.form action="eventTypeAssetTypesSave" id="eventTypeAssetTypesSave" theme="simple">
		<@s.hidden name="eventTypeId" />
		<table class="list" >
			<tr>
				<th class="checkboxRow"><a href="#" onclick="selectAll();"><@s.text name="label.all"/></a> | <a href="#" onclick="selectNone();"><@s.text name="label.none"/></a></th>
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

