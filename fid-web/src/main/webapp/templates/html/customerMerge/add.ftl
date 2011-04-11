${action.setPageType('customer','edit')!}
<head>
	<@n4.includeStyle href="steps" />
	<@n4.includeScript src="steps.js" />
	<@n4.includeScript src="mergeCustomer.js" />
	
	<script type="text/javascript">
	function doSubmit(event) { 
		if (event.keyCode == 13) { 
			$('customerSearchForm').request(getStandardCallbacks());
			return false; 
		}
	}	
	</script>
</head>

<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<div id="steps">
	
	<div class="step">
		<h2>
			1. 
			<@s.text name="label.duplicate_customer">
				<@s.param>${losingCustomer.name}</@s.param>
			</@s.text>
		</h2>
		<div class="stepContent" id="step1">
			<p class="instructions">
				<@s.text name="instruction.select_duplicate_customer">
					<@s.param>${losingCustomer.name}</@s.param>
				</@s.text>
			</p>
			<div class="stepAction">
				<@s.submit theme="fieldidSimple" key="label.confirm_as_losing_customer" onclick="backToStep(2); return false;"/>
			</div>
		</div>
	</div>
	
	<div class="step stepClosed" >
		<h2>2. <@s.text name="label.select_customer_you_want_to_keep"/></h2>
		
		<div class="stepContent" id="step2" style="display:none">
			<p class="instructions">
				<@s.text name="instruction.select_customer_you_want_to_keep"/>
			</p>
			
			<@s.form id="customerSearchForm" namespace="/ajax" action="mergeFindCustomer" cssClass="simpleInputForm" theme="simple">
				<@s.hidden name="uniqueID" />
				<label class="label"><@s.text name="label.name"/></label>
				<span><@s.textfield id="search" name="search" onkeypress="return doSubmit(event);" /></span>
				<input type="button" value='<@s.text name="hbutton.search"/>' id="search" onclick="$( 'customerSearchForm' ).request(getStandardCallbacks()); return false;" />
			</@s.form>
			
			<div id="customerLookup">
				<div id="results">
				</div>
			</div>
			<div class="stepAction">
				<a href="javascript:void(0);" onclick="backToStep(1)"><@s.text name="label.back_to_step"/> 1</a>
			</div>
		</div>
	</div>
	
	<div class="step stepClosed">
		<h2>3. <@s.text name="label.confirm_merger"/></h2>
		
		<div class="stepContent" id="step3" style="display:none">
			<p class="instructions">
				<@s.text name="instruction.confirm_customer_merger"/>
			</p>
			<table id="mergeSummary" class="list">
				<tr>
					<th></th>
					<th><@s.text name="label.name" /></th>
					<th><@s.text name="label.id" /></th>
					<th><@s.text name="label.created" /></th>
				</tr>
				<tr>
					<td><@s.text name="label.loser"/></td>
					<td>${losingCustomer.name}</td>
					<td>${losingCustomer.code!}</td>
					<td>${action.formatDateTime(losingCustomer.created)}</td>
				</tr>
				<tr id="winningCustomer">
				</tr>
			</table>
			
			<@s.form action="customerMergeCreate" id="customerMergeCreate" theme="fieldidSimple">
				<@s.hidden name="uniqueID"/>
				<@s.hidden id="winningCustomerId" name="winningCustomerId"/>
				<div class="stepAction">
					<@s.submit key="label.merge" id="merge"/>
					<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="clearWinner();"><@s.text name="label.back_to_step"/> 2</a>
				</div>
			</@s.form>				
		</div>
		<script type="text/javascript">
			$('customerMergeCreate').observe('submit', function(event){ $('cancel').disable(); $('merge').disable(); });
		</script>
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