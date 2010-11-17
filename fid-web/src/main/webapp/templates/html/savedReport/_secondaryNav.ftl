<ul class="" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="savedReports"/>">&#171; <@s.text name="label.view_all_saved_reports"/></a></li>
	</#if>
</ul>
