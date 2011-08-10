<title>Oops!</title>

<div id="errorContainer" class="errorContainer">
	<h2><@s.text name="title.oops_something_went_wrong"/></h2>
	<div id="errorList" class="errorList">
		<span class="errorMessage"><@s.text name="message.problem_has_occured"/></span>
	</div>
	<p>
		<@s.text name="message.help_fix_issue">
			<@s.param>${helpUrl}</@s.param>
		</@s.text>
	</p>
</div>
