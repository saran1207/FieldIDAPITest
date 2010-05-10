	<head>
		<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/inspection.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/inspectionBook.js"/>" ></script>
		<script type="text/javascript">
			changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="ajax"   />';
			updateInspectionBooksUrl = '<@s.url action="inspectionBooks" namespace="ajax"   />';
			productTypeId = ${product.type.id}
			var proofTestTypes = ${json.toJson( proofTestTypesUpload )}
		</script>
		<script type="text/javascript">
			function imageFileUploaded( fileName, directory ){
				$("singleFileUpload").remove();
				$("proofTestUploadFile").show();
				$("newFile").value = "true";
				$("proofTestDirectory").value = directory;
			}
			
			function uploadAnotherFile() { 
				var iframe = '<iframe id="singleFileUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />"  scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="20" ></iframe>';

				$( "proofTestUploadFile" ).hide();
				$( "proofTestUpload" ).insert( { top: iframe } );
				$("newFile").value = "true";
				$("proofTestDirectory").value = "";
			}
			
		</script>
		
		<@n4.includeStyle type="page" href="inspection" />
		
		
		<#include "/templates/html/common/_calendar.ftl"/>
	</head>
	<title>${(inspectionType.name)!} <@s.text name="label.on"/> ${product.serialNumber}</title>
	
	<#include "/templates/html/common/_formErrors.ftl" />
	
	<#include "_inspectionSummaryForm.ftl"/>

	<#include "_attributes.ftl"/>

	<#if inspection.id?exists && action.isParentProduct() >
		<p>
			<label><@s.text name="label.result"/></label>
			<span><@s.select name="result" list="results" listKey="name()" listValue="%{getText( label )}" /></span>
		</p>
	</#if>
	
	<#assign formInspection=inspection>
	<#assign identifier="inspectionForm">
	<#include "_inspection.ftl" />
	
	<#if action.isParentProduct() && !inspectionType.supportedProofTests.isEmpty() >
		<#include "_proofTest.ftl"/>
	</#if>
	
	<#include "_postInspectionForm.ftl" />
		
	
	<#include "../common/_attachedFilesForm.ftl" />
