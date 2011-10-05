<div class="note" id="note_${note.id}">
	<div class="noteInformation">
		<p class="noteText">${ action.replaceCR( ( note.comments?html) ! "" )}</p>
		<#if note.fileName?exists > 
			<p class="noteText"><a href="<@s.url action="downloadJobNoteFile" namespace="/file" projectId="${project.id}" attachmentID="${note.id}"/>">${ action.replaceCR( note.fileName?html) }</a></p>
		</#if>
		<p class="noteCreation"><@s.text name="label.created"/> ${action.formatDateTime(note.created)} <@s.text name="label.by"/> ${ note.modifiedBy.userLabel?html }</p>
	</div>
	<#if sessionUser.hasAccess("managejobs") >
		<div class="noteRemoval">
			<a href="<@s.url action="jobNoteDelete" uniqueID="${note.id}" projectId="${project.id}"/>" class="removeNoteLink" noteId="${note.id}">
				<img alt="x" src="<@s.url value="/images/x.gif"/>" noteId="${note.id}"/>
			</a>
		</div>
	</#if>
</div>