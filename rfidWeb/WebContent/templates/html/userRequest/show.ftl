${action.setPageType('user_registration', 'show')!}

<#include "_userRequest.ftl" />

<div  style="position:absolute; left:-10000px">
	<#include "_acceptForm.ftl"/>
	<script type="text/javascript">
		function formCancel(){
			Lightview.hide();
		}
	</script>
</div>

<div class="formAction">
	<a href="acceptRequest" id="acceptRequest"><@s.text name="label.accept"/></a>
	<a href="<@s.url action="userRequestDeny" uniqueID="${uniqueID}"/>" ><@s.text name="label.deny"/></a>
</div>
	
<script type="text/javascript" >
	$('acceptRequest').observe('click',
			function(event){
				event.stop();
				<#if customers.empty >
					alert('<@s.text name="warning.nocustomerscreated"/>');	
				<#else>
					Lightview.show( { href:'#acceptForm', options:{ width:740, height: 500, menubar: false } } );
				</#if>
			}
		);
</script>
	
