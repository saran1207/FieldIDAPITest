${action.setPageType('inspection', 'multi_event')}

<#assign repeatedAssetHtml>
	<div class="emptyList" >
		<p>
			<@s.text name="message.asset_already_added" />
		</p>
	</div>
</#assign>
<#assign assetAddedHtml>
	<div class="emptyList" >
		<p>
			<@s.text name="message.asset_added" />
		</p>
	</div>
</#assign>
<#assign assetRemovedHtml>
	<div class="emptyList" >
		<p>
			<@s.text name="message.asset_remove" />
		</p>
	</div>
</#assign>


<#assign productSearchAction="products"/>
<#assign namespace="/ajax"/>
<#include "../inspectionGroup/_searchForm.ftl"/>
<div id="resultBlock">
	<div id="searchResults">
		<div id="introduction">
			<br/><br/>----- here is the intro ------<br/><br/>
		</div>
	</div>
	<div class="hide" id="searchLimit">
		<@s.text name="label.limit_reached"/>
	</div>
</div>
<hr/>
<@s.form action="selectEventType" namespace="/multiInspect" theme="fieldid">
	<div class="formActions">
		<@s.submit key="label.perform_event" id="perform_event" disabled="true"/>
	</div>
	<div><@s.text name="label.number_assets_selected"/> <span id="listSize">0</span></div>
	<div id="emptySelection"><@s.text name="label.select_some_assets_to_do_multi_event"/></div>
	<table id="selectedAssets" class="list hide">
		<tr class="header">
			<th><@s.text name="${Session.sessionUser.serialNumberLabel}"/></th>
			<th><@s.text name="label.rfidnumber"/></th>
			<th><@s.text name="label.owner"/></th>
			<th><@s.text name="label.producttype"/></th>
			<th><@s.text name="label.identified"/></th>
			<th><@s.text name="label.reference_number"/></th>
			<th>&nbsp;</th>
		</tr>
	</table>
	
</@s.form>

<#assign row>
<tr id="asset_%%ID%%"> 
	<td>
		%%SERIAL_NUMBER%%
		<@s.hidden name="assetIds[%%INDEX%%]" value="%%ID%%"/>
	</td>
	<td>%%RFID%%</td>
	<td>%%OWNER%%</td>
	<td>%%TYPE%%</td>
	<td>%%IDENTIFIED%%</td>
	<td>%%REFERENCE_NUMBER%%</td>
	<td><a href="#" id="remove_asset_%%ID%%" assetId="%%ID%%"><@s.text name="label.remove"/></a></td>
</tr>
</#assign>

<head>
	<style>
		#resultBlock {
			min-height: 80px; 
		}
	</style>
	<@n4.includeScript>
		function selectAsset(event) {
			event.stop();
			var element = Event.element(event);
			assetFound('serial', element.readAttribute('productId'), element.asset);
		}
	
		var selectionIndex = 0;
		
		function assetFound(serialNumber, id, asset) {
			if ($("asset_"+id) != null) {
				updateResults('${repeatedAssetHtml?js_string}');
				return;
			}
		
			var html = '${row?js_string}';
			
			
			var valueSubsitutedHtml = html.replace(/%%INDEX%%/g, selectionIndex)
							.replace(/%%ID%%/g, id)
							.replace(/%%SERIAL_NUMBER%%/g, asset.serialNumber)
							.replace(/%%RFID%%/g, asset.rfidNumber)
							.replace(/%%OWNER%%/g, asset.owner)
							.replace(/%%TYPE%%/g, asset.type)
							.replace(/%%IDENTIFIED%%/g, asset.identifiedDate)
							.replace(/%%REFERENCE_NUMBER%%/, asset.customerReferenceNumber);
							 
			$$('#selectedAssets .header').first().insert({after: valueSubsitutedHtml});
			
			$("asset_" + id).highlight({ afterFinish:function() { $("asset_" + id).setStyle({backgroundColor:''}); } });	
			
			$('remove_asset_'+id).observe('click', function(event) {
				event.stop();
				var element = Event.element(event);
				
				$('asset_' + element.readAttribute('assetId')).remove();
				
				updateListCount();
				updateResults('${assetRemovedHtml?js_string}');
			});
			
			
			selectionIndex++;
			
			closeResults();
			updateListCount();
			updateResults('${assetAddedHtml?js_string}');
			
			if (assetListCount() >= ${listLimitSize}) {
				$('productSearchForm').disable();
				$('searchLimit').show();
			}
		}
		
		function closeResults() {
			$('searchResults').update('');
		}
		
		function assetListCount() {
			return $$("#selectedAssets [id^='asset_']").size();
		}
		
		function updateListCount() {
			var listSize = assetListCount()
			if (listSize <= 0) {
				$('perform_event').disable();
				$('selectedAssets').hide();
				$('emptySelection').show();
			} else {
				$('perform_event').enable();
				$('selectedAssets').show();
				$('emptySelection').hide();
			}
			
			$('listSize').update(assetListCount());
		}
		
		function updateResults(html) {
			$('searchResults').update(html);
			$('searchResults').highlight();
		}
		
		onDocumentLoad(function() {
			$('productSearchForm').observe('submit', function(event) {
				event.stop();
				$('productSearchForm').request(getStandardCallbacks());
			});
		});
		
	</@n4.includeScript>
</head>