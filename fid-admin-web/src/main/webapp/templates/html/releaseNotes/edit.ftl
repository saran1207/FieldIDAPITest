<h1>release notes</h1>
<@s.form action="releaseNotesUpdate" theme="simple">
		<div>
			<@s.label for="model.title" value="Title:"/>
			<@s.textfield name="model.title"  />
		</div>
		<div>
			<@s.label for="model.url" value="PDF URL:" required="true"/>
			<@s.textfield name="model.url" />
		</div>
	
	<h2>Bullets (blank bullets will be removed)</h2>
	<#list 0..5 as index>
		<div>
			<@s.label for="model.bullets[${index}].point" value="${index + 1}:"/>
			<@s.textfield name="model.bullets[${index}].point"/>
		</div>
	</#list >
	<div class="actions">
		<@s.submit value="Save"/> or <a href="<@s.url action="releaseNote"/>">cancel</a>
	</div>
</@s.form>