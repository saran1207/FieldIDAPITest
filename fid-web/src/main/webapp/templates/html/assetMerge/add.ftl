${action.setPageType('asset', 'edit')!}
<head>
	<@n4.includeStyle href="steps" />
    <style>
        .emptyList h2 {
            color: #333;
        }
    </style>
	<script type="text/javascript" src="<@s.url value="/javascript/steps.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/pagination.js"/>"></script>
	<script type="text/javascript">
		function findAssets() {
			$$('.assetLink').each( function(element) { 
					element.observe( 'click', selectWinner );
				});
		}
		
		function selectWinner(event) {
			event.stop();
			var element = Event.element(event);
			
			$('winningAssetId').value = element.getAttribute('assetId');
			var winningAssetDescription = findFirstParentTag('TR', element);
			
			//copy row to confirm screen.
			$('winningAsset').update(winningAssetDescription.innerHTML);
			$$('#winningAsset .selectAction').each( function(element) {element.update('<@s.text name="label.winner"/>');});
			
			$$('#results tr').each(function(element) {
					element.removeClassName("selected");
				});
			
			winningAssetDescription.addClassName("selected");
			backToStep(3);
			 
		}
		
		function findFirstParentTag(tagName, element) {
			var tags = element.ancestors();
			for (var i = 0; i < tags.length; i++) {
				if (tags[i].tagName == tagName.toUpperCase()) {
					return tags[i];
				}
			}
			return null;
		}
		
		function submitCreateForm(event) {
			event.stop();
			var form = Event.element(event);
			$('step3').hide();
			$('step4Loading').show();
			form.request(getStandardCallbacks());
		}
		
		function toStep2() {
			backToStep(2);
		}
		
		function toStep3() {
		
		}
	</script>
</head>
<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<div id="steps">
	<div class="step">
		<h2>1. <@s.text name="label.duplicate_asset"/></h2>
		<div class="stepContent" id="step1">
			<p class="instructions">
				<@s.text name="instruction.select_duplicate_asset"/>
			</p>
			<table class="list">
				<tr>
					<th>${identifierLabel}</th>
					<th><@s.text name="label.rfidnumber"/></th>
					<th><@s.text name="label.customername"/></th>
					<th><@s.text name="label.assettype"/></th>
					<th><@s.text name="label.identifieddate"/></th>
				</tr>
				<tr>
					<td>${losingAsset.identifier?html}</td>
					<td>${(losingAsset.rfidNumber?html)!}</td>
					<td>${(losingAsset.owner.name?html)!}</td>
					<td>${losingAsset.type.name?html}</td>
					<td>${action.formatDate(losingAsset.identified, false)}</td>
				</tr>
			</table>
			<div class="stepAction">
				<@s.submit theme="fieldidSimple" key="label.confirm_as_losing_asset" onclick="toStep2(); return false;"/>
			</div>
		</div>
	</div>
	
	<div class="step stepClosed" >
		<h2>2. <@s.text name="label.select_asset_you_want_to_keep"/></h2>
		
		<div class="stepContent" id="step2" style="display:none">
			<p class="instructions">
				<@s.text name="instruction.select_asset_you_want_to_keep"/>
			</p>
			<div id="assetLookup">
				<#assign namespace="/ajax"/>
				<#assign assetSearchAction="mergeFindAsset"/>
				<#assign assetFormId="mergeSmartSearch"/>
				<#assign overRideAssetType=losingAsset.type.id/>
				<#assign useOverRides=true/>
				<#assign usePaginatedResults=true/>
				<#assign useAjaxPaginatedResults=true>
				<#include "../eventGroup/_searchForm.ftl"/>
				<div id="results" class="hidden">
				</div>
			</div>
			<div class="stepAction">
				<a href="javascript:void(0);" onclick="backToStep(1)"><@s.text name="label.back_to_step"/> 1</a>
			</div>
		</div>
		<script type="text/javascript">
			$( 'mergeSmartSearch' ).observe( 'submit', 
				function( event ) {
					event.stop();
					if ($$('#mergeSmartSearch input').first().value.strip().empty()){
						alert("You cannot search for a blank identifier or rfid number");
					}else{
						var element = Event.element( event ); 
						element.request( getStandardCallbacks() );
					}
				} );
		</script>	
	</div>


	<div class="step stepClosed">
		<h2>3. <@s.text name="label.confirm_merger"/></h2>
		
		<div class="stepContent" id="step3" style="display:none">
			<p class="instructions">
				<@s.text name="instruction.confirm_merger"/>
			</p>
			<table id="mergeSummary" class="list">
				<tr>
					<th></th>
					<th>${identifierLabel}</th>
					<th><@s.text name="label.rfidnumber"/></th>
					<th><@s.text name="label.customername"/></th>
					<th><@s.text name="label.assettype"/></th>
					<th><@s.text name="label.identifieddate"/></th>
					<th><@s.text name="label.referencenumber"/></th>
				</tr>
				<tr>
					<td><@s.text name="label.loser"/></td>
					<td>${losingAsset.identifier?html}</td>
					<td>${(losingAsset.rfidNumber?html)!}</td>
					<td>${(losingAsset.owner.name?html)!}</td>
					<td>${losingAsset.type.name?html}</td>
					<td>${action.formatDate(losingAsset.identified, false)}</td>
					<td>&nbsp;</td>   <!-- without this placeholder, top border won't show up on cell -->
				</tr>
				<tr id="winningAsset">
				</tr>
			</table>
			

			<@s.form action="assetMergeCreate" id="assetMergeCreate" theme="fieldidSimple">
				<@s.hidden name="uniqueID"/>
				<@s.hidden id="winningAssetId" name="winningAssetId"/>
				<div class="stepAction">
					<@s.submit key="label.merge" id="merge"/>
					<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="$('winningAssetId').value=''; backToStep(2);"><@s.text name="label.back_to_step"/> 2</a>
				</div>
			</@s.form>				
		</div>
		<script type="text/javascript">
			$('assetMergeCreate').observe('submit', function(event){ $('cancel').disable(); $('merge').disable(); });
		</script>
	</div>
	
	<@s.url id="cancelUrl" action="assetEdit" uniqueID="${uniqueID}"/>
	<div id="cancelButton" class="stepAction" >
		<@s.submit key="label.cancel_merge" id="cancel" onclick="return redirect('${cancelUrl}');"/>
	</div>
</div>


<div class="helpfulHints">
	<h3><@s.text name="label.what_will_be_copied"/></h3>
	<p><@s.text name="label.what_will_be_copied.full"/></p>

	<h3><@s.text name="label.what_will_be_deleted"/></h3>
	<p><@s.text name="label.what_will_be_deleted.full"/></p>

	<h3><@s.text name="label.can_it_be_undone"/></h3>
	<p><@s.text name="label.can_it_be_undone.full"/></p>

	<h3><@s.text name="label.when_will_the_asset_be_merged"/></h3>
	<p><@s.text name="label.when_will_the_asset_be_merged.full"/></p>
</div>