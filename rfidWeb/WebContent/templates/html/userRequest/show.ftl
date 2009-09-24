${action.setPageType('user_registration', 'show')!}
<#include "/templates/html/common/_orgPicker.ftl"/>

<div id="request" class="fullForm">
	<#include "_userRequest.ftl" />
	
	<div class="actions">
		<@s.submit theme="fieldid" id="acceptRequest" key="label.accept"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="userRequestDeny" uniqueID="${uniqueID}"/>" ><@s.text name="label.deny"/></a>
	</div>
</div>	

<#include "acceptForm.ftl"/>

<head>
<@n4.includeScript>
	document.observe("dom:loaded", function(event) {
		$('acceptRequest').observe('click',
				function(event){
					event.stop();
					<#if action.customersExist() >
						$('request').hide();
						$('acceptForm').show();	
					<#else>
						alert('<@s.text name="warning.nocustomerscreated"/>');
					</#if>
				}
			);
	
		$('cancelAccept').observe('click', 
			function(event) {
				event.stop();
				$('request').hide();
				$('acceptForm').show();	
			});
		$('acceptForm').hide();
	});
	
	
</@n4.includeScript>
</head>
