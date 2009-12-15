<div id="contentHeader">
	<#include "_notificationArea.ftl"/>
    <div id="contentTitle">
	    <h1>
	    	<#include "_title.ftl"/>
	    </h1>
	    
	    
		<div class="backToLink">
			<#if navOptions.onAdmin>
				<a href="<@s.url action="administration"/>">&#171; <@s.text name="label.back_to_administration"/></a>
			</#if>
			<#if navOptions.returnToReport && reportActive>
				<a href="<@s.url action="returnToReport"/>">&#171; <@s.text name="label.return_to_report"/></a>
			</#if>
			<#if navOptions.onSafetyNetwork>
				<a href="<@s.url action="safetyNetwork"/>">&#171; <@s.text name="label.back_to_safety_network"/></a>
			</#if>
		</div>
	    
	    <div id="smartSearch">
			<@s.form method="post" action="productInformation" id="smartSearch" theme="fieldid">
				<label for="search"><@s.text name="label.find"/>:</label>
				<@s.textfield name="search" id="searchText" value="${action.getText('label.smart_search_search_types')}" cssClass="description"/>
				<@s.submit name="load" key="label.load" id="smartSearchButton"/>
				
				
			</@s.form>
			<#if !sessionUser.customerUser && !vendorContextList.empty>
				<@s.form id="vendorContextForm" action="home" theme="simple">
					<span><@s.text name="label.vendor_context"/>: </span>
					<span class="switchContainer">
						<span id="vendorContextNameLink">
							<a href="" onclick="$('vendorContextSwitch').show(); $('vendorContextNameLink').hide(); return false;" >${currentVendorContextName}</a>
						</span>
						<span id="vendorContextSwitch" style="display: none;">
						
							<@s.select id="vendorContext" name="vendorContext" list="vendorContextList" listKey="id" listValue="name" headerKey="" headerValue="${sessionUserOwner.name}" onchange="$('vendorContextForm').submit();"/>
						
						</span>
					</span>
				</@s.form>
			</#if>
		</div>

	</div>
	
	
	<#include "_options.ftl"/>
</div>

<script type="text/javascript">
	$('searchText').observe('focus', clearDescription);
	$('searchText').observe('blur', replaceDescription);
	$('smartSearch').observe('submit', submitSmartSearch);
</script>