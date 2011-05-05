${action.setPageName('tasks')!}

<@s.actionerror />
<@s.actionmessage />

<h2>
	Scheduler Control: 
	
	<@s.url id="reloadSchedulerUrl" namespace="/admin" value="reloadScheduler.action" />
	<button type="button" onclick="window.location = '<@s.text name="%{reloadSchedulerUrl}" />';">Reload</button>
	
	<@s.url id="enableDisableSchedulerUrl" namespace="/admin" value="enableDisableScheduler.action">
	    <@s.param name="enable" value="%{!schedulerEnabled}" />
	</@s.url>
	<@s.if test="%{schedulerEnabled}">
		<button type="button" onclick="window.location = '<@s.text name="%{enableDisableSchedulerUrl}" />';">Stop</button>
	</@s.if>
	<@s.else>
		<button type="button" onclick="window.location = '<@s.text name="%{enableDisableSchedulerUrl}" />';">Start</button>
	</@s.else>
</h2>

<h2>Scheduled Tasks</h2>
<table class="list">
<tr><th>ID</th><th>Cron Pattern</th><th>Status</th><th>Last Run Date</th><th>Last Run Time (ms)</th><tr></tr></tr>
<@s.iterator value="scheduledTasks">
	<tr>
		<td><@s.property value="configId" /></td>
		<td><@s.property value="cronExpression" /></td>
		<td><@s.property value="status" /></td>
		<td><@s.date name="lastExecutionDate" format="dd/MM/yyyy hh:mm:ss"/></td>
		<td><@s.property value="lastTimeElapsed" /></td>
		<td></td>
	</tr>
</@s.iterator>
</table>

<br />
<h2>Tasks Configurations</h2>
<table class="list">
<tr>
	<th>ID</th><th>Created</th><th>Modified</th><th>Cron Pattern</th><th>Group</th><th>Task Class</th><th>Enabled</th><th>Entity ID</th><th></th>
</tr>
<@s.iterator id="conf" value="taskConfigs">
	<@s.url id="enableDisableTaskUrl" namespace="/admin" value="enableDisableTask.action">
	    <@s.param name="configId" value="id" />
	    <@s.param name="enable" value="%{!isEnabled()}" />
	</@s.url>

	<tr>
		<td><@s.property value="id"/></td>
		<td><@s.date name="created" format="dd/MM/yyyy hh:mm:ss"/></td>
		<td><@s.date name="modified" format="dd/MM/yyyy hh:mm:ss"/></td>
		<td><@s.property value="cronExpression" /></td>
		<td><@s.property value="taskGroup" /></td>
		<td><@s.property value="className" /></td>
		<td><@s.property value="isEnabled()" /></td>
		<td><@s.property value="taskEntityId" /></td>
		<td>
			<@s.a href="%{enableDisableTaskUrl}">
				<@s.if test="isEnabled()">Disable</@s.if>
				<@s.else>Enable</@s.else>
			</@s.a>
		</td>
	</tr>
</@s.iterator>
</table>