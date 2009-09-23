${action.setPageType('user_registration', 'show')!}

<#include "_userRequest.ftl" />

<div class="formAction">
	<a href="<@s.url action="userRequestAccept" uniqueID="${uniqueID}"/>" id="acceptRequest"><@s.text name="label.accept"/></a>
	<a href="<@s.url action="userRequestDeny" uniqueID="${uniqueID}"/>" ><@s.text name="label.deny"/></a>
</div>
	
<script type="text/javascript" >
	$('acceptRequest').observe('click',
			function(event){
				event.stop();
				<#if action.customersExist() >
					Lightview.show( { href:'#acceptForm', options:{ width:740, height: 500, menubar: false } } );	
				<#else>
					alert('<@s.text name="warning.nocustomerscreated"/>');
				</#if>
			}
		);
</script>
	
