<div class="editHeader">
	<h3>Notes</h3><#if superUser><p> | <a href="javascript:void(0);" onClick="editNote(${id});"><@s.text name="label.edit"/></a></p></#if>
</div>

<p>${primaryOrg.notes!}</p>