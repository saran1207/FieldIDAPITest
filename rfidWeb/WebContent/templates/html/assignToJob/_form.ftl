<@s.hidden name="searchId" />
<@s.hidden name="currentPage" />
	
<div class="infoSet">
	<label for="job"><@s.text name="label.job"/></label>
	<@s.select name="job" list="jobs" listKey="id" listValue="name" headerKey="" headerValue="${action.getText('label.removejob')}"/>
</div>

