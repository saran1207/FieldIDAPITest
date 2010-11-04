
<head>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projectEvents.css"/>"/>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projectAssets.css"/>"/>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projectNotes.css"/>"/>
	<script type="text/javascript" src="<@s.url value="/javascript/fileUpload.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/projects.js"/>"></script>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projects.css"/>"/>
	<script type="text/javascript">
		attachAssetToProjectUrl = "<@s.url action="jobAssetCreate" namespace="/ajax"/>";
		removeAssetUrl = "<@s.url action="jobAssetDelete" namespace="/ajax"/>";
		removeNoteUrl = "<@s.url action="jobNoteDelete" namespace="/ajax"/>";
		removeNoteWarning = '<@s.text name="warning.deleteprojectnote"/>';
		uploadUrl = '<@s.url action="uploadForm" namespace="/aHtml/fileUploads" />';
		removeText = '<@s.text name="label.remove"/>';
	</script>
	
	<style>
		#results {
			overflow:hidden;
			padding:5px;
			border-bottom:1px solid #D0DAFD;
		}
		
		#results .error span {
			line-height:30px;
			padding:0 10px;
		}
	</style>
</head>
${action.setPageType('job', 'show')!}

<div class="crudForm largeForm bigForm pageSection layout">
	<h2><@s.text name="label.projectdetails"/> 
		<#if sessionUser.hasAccess("managejobs")> 
			<a href="<@s.url action="jobEdit" uniqueID="${project.id}"/>"><@s.text name="label.littleedit"/></a>
		</#if>	
	</h2>
	<@s.hidden id="uniqueID" name="uniqueID"/>
	<div class="sectionContent">
		<div class="twoColumn" >
			<div class="infoSet">
				<label for="projectID"><@s.text name="label.projectid"/></label>
				<span>${(project.projectID?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="name"><@s.text name="label.title"/></label>
				<span>${(project.name?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="organization"><@s.text name="label.organization"/> </label>
				<span>${(project.owner.internalOrg.name?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="customer"><@s.text name="label.customer"/> </label>
				<span>${(project.owner.customerOrg.name?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="division"><@s.text name="label.division"/> </label>
				<span>${(project.owner.divisionOrg.name?html)!}</span>
			</div>
						
			<div class="infoSet">
				<label for="status"><@s.text name="label.status"/></label>
				<span>${project.status?html!}</span>
			</div>

			<div class="infoSet">
				<label for="open"><@s.text name="label.open"/></label>
				<span><#if project.open><@s.text name="value.yes" /><#else><@s.text name="value.no" /></#if></span>
			</div>
			<div class="infoSet">
				<label for="description"><@s.text name="label.description"/></label>
				<span>${action.replaceCR((project.description?html)!"")}</span>
			</div>
			
		</div>
		
		<div class="twoColumn lastColumn" >
			<div class="infoSet">
				<label for="started"><@s.text name="label.datecreated"/></label>
				<span>${action.formatDateTime(project.created)}</span>
			</div>
			<div class="infoSet">
				<label for="started"><@s.text name="label.datestarted"/></label>
				<span>${action.formatDateTime(project.started)}</span>
			</div>
			
			<div class="infoSet">
				<label for="estimated"><@s.text name="label.estimatedcompletion"/> </label>
				<span>${action.formatDateTime(project.estimatedCompletion)}</span>
			</div>
			
			<div class="infoSet">
				<label for="completion"><@s.text name="label.actualcompletion"/> </label>
				<span>${action.formatDateTime(project.actualCompletion)}</span>
			</div>
						
			<div class="infoSet">
				<label for="duration"><@s.text name="label.duration"/></label>
				<span>${project.duration?html !}</span>
			</div>

			<div class="infoSet">
				<label for="poNumber"><@s.text name="label.ponumber"/></label>
				<span>${(project.poNumber?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="workPerformed"><@s.text name="label.workperformed"/></label>
				<span>${action.replaceCR((project.workPerformed?html)!"")}</span>
			</div>
			
		</div>
	</div>
</div>
<#if !project.eventJob>
	<div id="assets" class="pageSection" style="" >
		<h2><@s.text name="label.assetsonproject"/>
			<#if sessionUser.hasAccess( "managejobs" )> 
				<a id="addNewAsset" href="<@s.url action="jobAssetAdd" uniqueID="${project.id}"/>"><@s.text name="label.addasset"/></a>
				<a id="closeNewAsset" href="close" style="display:none"><@s.text name="label.closesearch"/></a>
			</#if> 
		</h2>
		<div class="sectionContent">
			<div id="assetLookup" style="display:none">
				<#assign namespace="/ajax"/>
				<#assign assetSearchAction="jobFindAsset"/>
				<#assign assetFormId="projectAssetSearch"/>
				<#include "../eventGroup/_searchForm.ftl"/>
				<div id="results" class="hidden">
				</div>
			</div>
			<div id="noLinkedAssets" <#if !assets.isEmpty()> style="display:none" </#if> >
				<div class="emptyAssets">
					<label><@s.text name="label.emptyprojectassetslist"/> <#if sessionUser.hasAccess("managejobs")><@s.text name="label.emptyprojectassetslistinstruction"/></#if></label>
				</div>
			</div>
			<div id="linkedAssets" <#if assets.isEmpty()> style="display:none" </#if> >
				<#list assets as asset >
					<#include "_attachedAsset.ftl"/>
				</#list>
			</div>
			<div id="linkedAssetsMore" class="viewMore" <#if assets.isEmpty()> style="display:none" </#if> >
				<a href="<@s.url action="jobAssets" projectId="${project.id}"/>"><@s.text name="label.allassets"/></a>
			</div>
		</div>
	</div>
<#else>
	<div id="events" class="pageSection" style="" >
		<h2><@s.text name="label.eventsonproject"/>	<#if sessionUser.hasAccess("createevent")><a href="<@s.url action="startAssignSchedulesToJob" jobId="${uniqueID}"/>"><@s.text name="label.addanevent"/></a></#if></h2>
		<div class="sectionContent" id="emptyEventList" <#if !schedules.isEmpty()> style="display:none" </#if>>
			<div id="noLinkedEvents"  >
				<div class="emptyEvents">
				</div>
                <label><@s.text name="label.emptyprojecteventlist"/> <#if sessionUser.hasAccess("createevent")><@s.text name="label.emptyprojecteventlistinstruction"/></#if></label>
            </div>
		</div>
		
		<div class="sectionContent" id="events" <#if schedules.isEmpty()> style="display:none" </#if>>
			<div id="eventCounts">
				<a id="incompleteEventCount" href="<@s.url action="jobEvents" projectId="${project.id}" searchStatuses="INCOMPLETE"/>">${action.getText("label.incompleteevents", "", countOfIncompleteSchedules.toString())}</a>
				<a id="completeEventCount" href="<@s.url action="jobEvents" projectId="${project.id}" searchStatuses="COMPLETE"/>">${action.getText("label.completeevents", "", countOfCompleteSchedules.toString())}</a>
			</div>
			
			<div id="linkedEvents" >
				<#list schedules as event >
					<#include "_attachedEvents.ftl"/>
				</#list>
			</div>
			<div id="linkedEventsMore" class="viewMore"  >
				<a href="<@s.url action="jobEvents" projectId="${project.id}"/>"><@s.text name="label.view_all_events"/></a>
			</div>
		</div>
	</div>
</#if>



<div id="notesandattachments" class="pageSection" style="clear:both">
	<h2>
		<@s.text name="label.notes"/>
		<#if sessionUser.hasAccess( "managejobs" )> 
			<a id="addNewNote" href="<@s.url action="jobNoteAdd" projectId="${project.id}"/>"><@s.text name="label.addnote"/></a>
			<a id="closeNewNote" href="close" style="display:none"><@s.text name="label.closeform"/></a>
		</#if>
	</h2>
	<div class="sectionContent">
		<#if sessionUser.hasAccess( "managejobs" )>
			<@s.form id="addNote" action="jobNoteCreate" namespace="/ajax"  method="post" theme="fieldid" cssClass="crudForm largeForm " cssStyle="display:none"> 
				<#include "../projectNotes/_form.ftl"/>
				<div class="formAction" >
					<label><@s.submit key="label.save" /></label>
				</div>
			</@s.form>
		</#if>
		<div id="noNotes" <#if !notes.isEmpty()> style="display:none" </#if> >
			<div class="emptyNotes">
				<label><@s.text name="label.emptynoteslist"/><#if sessionUser.hasAccess( "managejobs" )><@s.text name="label.emptynoteslistinstruction"/></#if></label>
			</div>
		</div>
		<div id="notes" <#if notes.isEmpty()> style="display:none" </#if> >
			<#list notes as note>
				<#include "_attachedNote.ftl"/>
			</#list>
		</div>
		<div id="notesMore" class="viewMore" <#if notes.isEmpty() > style="display:none" </#if> >
			<a href="<@s.url action="jobNotes" projectId="${project.id}"/>"><@s.text name="label.allnotes"/></a>
		</div>
	</div>
</div>



<div id="resources" class="pageSection" style="">
	<h2>
		<@s.text name="label.resourcesassigned"/>
		<#if sessionUser.hasAccess("managejobs")> 
			<a id="addNewResource" href="javascript:void(0);"><@s.text name="label.assign_resource"/></a>
			<a id="closeNewResource" href="close" style="display:none"><@s.text name="label.closeform"/></a>
		</#if>
	
	</h2>
	<div class="sectionContent">
		<#if sessionUser.hasAccess("managejobs")>
			<@s.form namespace="/ajax" action="jobResourceCreate" id="addResource" theme="fieldid" cssClass="crudForm" cssStyle="display:none">
				<@s.hidden name="jobId" value="${project.id}"/> 
				<div class="infoSet">
					<label for="" class=""><@s.text name="label.employee"/></label>
					<@s.select name="uniqueID" id="employee" list="jobResources.employees" listKey="id" listValue="name"/>
				</div>
				<div class="formAction">
					<@s.submit key="label.assign"/>
				</div>
			</@s.form>
		</#if>
		<div id="noResources" <#if !jobResources.resources.isEmpty()> style="display:none" </#if> >
			<div class="emptyResources">
				<label><@s.text name="label.emptyresourceslist"/><#if sessionUser.hasAccess( "managejobs" )><@s.text name="label.emptyresourceslistinstruction"/></#if></label>
			</div>
		</div>
		
		<div id="jobResources" <#if jobResources.resources.isEmpty()> style="display:none" </#if>>	
			<#list jobResources.resources as user >
				<#include "_attachedUser.ftl"/>
			</#list>
		</div>
	</div>
</div>

<script type="text/javascript">
	

	<#if sessionUser.hasAccess( "managejobs" )>
		<#if !project.eventJob> 
			$( 'projectAssetSearch' ).observe( 'submit', 
				function( event ) {
					event.stop(); 
					var element = Event.element( event ); 
					element.request( getStandardCallbacks() );
				} );
			
			$( 'closeNewAsset' ).observe( 'click', 
				function( event ) { 
					event.stop(); 
					closeAssetSearch();
				} );
				
			$( 'addNewAsset' ).observe( 'click', 
				function( event ) { 
					event.stop(); 
					openAssetSearch();
				} );
			
			var removeLinks = $$('.removeAssetLink');
			if( removeLinks != null ) {
				for( var i = 0; i < removeLinks.length; i++ ) {
					removeLinks[i].observe('click', removeAsset);
				}
			}
			
		</#if>
		$('addResource').observe('submit',
				function(event) {
					event.stop();
					var element = Event.element(event); 
					element.request(getStandardCallbacks());
				});
				
		$('addNote').observe( 'submit', function( event ) { event.stop(); var form = Event.element( event ); form.request( getStandardCallbacks() ); } );
		$( 'addNewNote' ).observe( 'click', 
			function( event ) { 
				event.stop(); 
				openNoteForm();
			} );
		
		$( 'closeNewNote' ).observe( 'click', 
			function( event ) { 
				event.stop(); 
				closeNoteForm();
			} );

		var removeLinks = $$('.removeNoteLink');
		if(removeLinks != null) {
			for( var i = 0; i < removeLinks.length; i++ ) {
				removeLinks[i].observe('click', removeNote);
				removeLinks[i].onclick = null;
			}
		}	
		
		
		document.observe("dom:loaded", 
			function(event) {
				addUploadFile(${limits.diskSpaceMaxed?string('true','false')}, '<span id="attachment" class="limitWarning"><@s.text name="warning.disk_space_maxed"/></span>');
			}
		);
		
		
		$('addNewResource').observe('click', 
			function( event ) { 
				event.stop(); 
				openResourceForm();
			});
		
		$('closeNewResource').observe('click', 
			function(event) { 
				event.stop(); 
				closeResourceForm();
			});
		
		
					
	</#if>
</script>
