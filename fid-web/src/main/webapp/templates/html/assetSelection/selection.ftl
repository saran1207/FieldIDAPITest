<title><@s.text name="title.mass_event"/></title>
<#assign repeatedAssetHtml>
	<div class="emptyList" ><p><@s.text name="message.asset_already_added" /></p></div>
</#assign>
<#assign assetAddedHtml>
	<div class="emptyList" ><p><@s.text name="message.asset_added" /></p></div>
</#assign>
<#assign assetRemovedHtml>
	<div class="emptyList" ><p><@s.text name="message.asset_remove" /></p></div>
</#assign>


<#assign assetSearchAction="assets"/>
<#assign namespace="/ajax"/>
<#assign usePaginatedResults=true/>
<#assign useAjaxPaginatedResults=true/>
<#include "../eventGroup/_searchForm.ftl"/>
<div id="resultBlock">
	<div id="searchResults">
		<div id="introduction">	
			<div id="writtenInstructions">
				<p class="instructions">
					<@s.text name="instructions.how_to_select_multiple_assets"/>
				</p>
			</div>
			<div id="videoInstruction">
				<a class="lightview" id="showVideo" title="<@s.text name="label.instructional_video"/> ::  :: width: 900, height: 570"rel="iframe" href='/videos/instructions/multi-event-video/index.html'><img src="<@s.url value="/images/multi-event-video-small.jpg"/>" alt="<@s.text name="label.multi_event_video"/>"/></a>
			</div>
		</div>
	</div>
	<div class="hide" id="searchLimit">
		<@s.text name="label.limit_reached"/>
	</div>
</div>
<@s.form action="selectEventType" namespace="/multiEvent" theme="fieldid" cssClass="hide">
	<div class="formActions prominent" id="performEvent">
		<@s.submit key="label.perform_event" id="perform_event" disabled="true"/>
	</div>
	<h2><@s.text name="label.number_assets_selected"/> <span id="listSize">0</span></h2>
	<div id="emptySelection" class="emptyList">
		<p><@s.text name="label.select_some_assets_to_do_multi_event"/></p>
	</div>
	<table id="selectedAssets" class="list hide">
		<tr class="header">
			<th>${identifierLabel}</th>
			<th><@s.text name="label.rfidnumber"/></th>
			<th><@s.text name="label.owner"/></th>
			<th><@s.text name="label.assettype"/></th>
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
	<@n4.includeStyle type="page" href="multi_event"/>
	<script type="text/javascript" src="<@s.url value="/javascript/pagination.js"/>"></script>
	<@n4.includeScript>
		function selectAsset(event) {
			event.stop();
			var element = Event.element(event);
			assetFound('serial', element.readAttribute('assetId'), element.asset);
		}
	
		var selectionIndex = 0;
		
		function assetFound(serialNumber, id, asset) {
			if ($("asset_"+id) != null) {
				updateResults('${repeatedAssetHtml?js_string}');
				return;
			}
			
			$('selectEventType').show();
		
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
				$('assetSearchForm').disable();
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
			$('assetSearchForm').observe('submit', function(event) {
				event.stop();
				$('assetSearchForm').request(getStandardCallbacks());
			});
			
			$('assetSearchForm_search').focus();
		});
		
	</@n4.includeScript>
</head>