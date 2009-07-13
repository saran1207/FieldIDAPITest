<#include "../common/_formErrors.ftl"/>
<h2><@s.text name="label.savedreportdetails"/></h2>
<@s.hidden name="searchId"/>
<div class="contentSection">
	<div class="infoSet">
		<label for="name"><@s.text name="label.name"/> <@s.text name="indicator.required"/></label>
		<@s.textfield name="name"/>
	</div>
</div>
