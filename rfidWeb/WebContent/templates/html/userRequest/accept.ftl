
<#include "_userRequest.ftl"/>
<#include "_acceptForm.ftl"/>

<script type="text/javascript">
	function formCancel(){
		window.location = "<@s.url action="userRequestView" uniqueID="${uniqueID}" />";
	}
</script>