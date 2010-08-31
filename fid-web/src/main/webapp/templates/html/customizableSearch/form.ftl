<#if !mappingGroups.isEmpty() >
<@s.url id="searchAction" action="${currentAction}" escapeAmp="false" includeContext="false" currentPage="${currentPage!}" searchId="${searchId!}" />
<@s.form method="post" id="selectTableColumns" action="${searchAction}" theme="simple" cssClass="simple">
	<#include "_selectColumns.ftl"/>
</@s.form>
<script type="text/javascript">
	$('selectTableColumns').observe('submit', 
		function(event) {
			var columns = $$("#selectTableColumns input[type='checkbox']");
			for (var i = 0; i < columns.size(); i++) {
				if (columns[i].checked) {
					return;
				}
			} 
			
			event.stop();
			updateMessages( new Array(), [ "<@s.text name="error.nocolumnsselected"/>" ] ); 
			window.scrollTo(0,0);
		} );
</script>
</#if>