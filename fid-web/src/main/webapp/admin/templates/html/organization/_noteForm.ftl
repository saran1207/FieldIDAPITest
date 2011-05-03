<h3>Notes</h3>
<@s.form id="noteForm" action="updateOrg" namespace="/adminAjax" theme="fieldidSimple" >
	<@s.hidden name="id"/>
	<@s.textarea id="orgNotes" name="primaryOrg.notes"cols="90" rows="10"/>
	
	<div class="noteFormActions">	
		<input id="updateNoteButton" type="button" onClick="updateNote();" value="<@s.text name='label.save'/>"/>
		<@s.text name="label.or"/> 
		<a href="javascript:void(0);" onClick="cancelNote(${id});"><@s.text name="label.cancel"/></a>
		<img id="noteFormLoading" class="loading" src="<@s.url value="../images/loading-small.gif"/>" />
	</div>
</@s.form>