<h1>release notes <a href="<@s.url action="releaseNotesEdit"/>">Edit</a></h1>

<div>
	<label>Title:</label>
	<span>${model.title?html}</span>
</div>

<div>
	<label>PDF url:</label>
	<span>${model.url!"no url given"}</span>
</div>

<h2>Bullets</h2>
<#if !model.bullets.empty>
	<ul>
		<#list model.bullets as bullet>
			<li>${bullet?html}</li>
		</#list>
	</ul>
<#else>
	No bullet points given.
</#if>
