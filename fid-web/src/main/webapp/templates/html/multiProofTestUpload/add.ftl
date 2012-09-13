
${action.setPageType('event', 'multi_proof_test')!}
<head>
    <title><@s.text name="title.multi_proof_test_upload"/></title>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<@n4.includeScript src="eventBook" />
	<script type="text/javascript" >
		updateEventBooksUrl = '<@s.url action="eventBooks" namespace="ajax"  includeParams="none" />';
		function multiProofSubmit( form )
		{
			var Flash;
			if(document.embeds && document.embeds.length>=1 && navigator.userAgent.indexOf("Safari") == -1) {
				Flash = $("EmbedFlashFilesUpload");
			} else {
				Flash = $("FlashFilesUpload");
			}
			
			
			var FormValues = $( 'proofTestForm' ).serialize();
			
			Flash.SetVariable("SubmitFlash", FormValues); 
			return false;
		}
		
		function replaceScreenWithMessageIfNotIE() {
			if (!Prototype.Browser.IE) {
				$('proofTestForm').hide();
				$('ieOnlyMessage').show();
			}
		}
		
		onDocumentLoad(replaceScreenWithMessageIfNotIE);
	</script>
</head>


<@s.form action="multiProofTestCreate" id="proofTestForm" cssClass="crudForm" theme="simple">
<#assign flashVars></#assign>

	<OBJECT id="FlashFilesUpload" codeBase="https://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0"
			width="450" height="350" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" VIEWASTEXT>
		
		<PARAM NAME="FlashVars" VALUE="showLink=false&redirectUploadUrl=multiProofTestCreate.action&uploadButtonVisible=no&backgroundColor=FFFFFF&uploadUrl=/fieldid/multiProofTestUpload.action">
		<PARAM NAME="BGColor" VALUE="#F8F6E6">
		<PARAM NAME="Movie" VALUE="flash/ElementITMultiPowUpload1.7.swf">
		<PARAM NAME="Src" VALUE="flash/ElementITMultiPowUpload1.7.swf">
		<PARAM NAME="WMode" VALUE="Window">
		<PARAM NAME="Play" VALUE="-1">
		<PARAM NAME="Loop" VALUE="-1">
		<PARAM NAME="Quality" VALUE="High">
		<PARAM NAME="SAlign" VALUE="">
		<PARAM NAME="Menu" VALUE="-1">
		<PARAM NAME="Base" VALUE="">
		<PARAM NAME="AllowScriptAccess" VALUE="always">
		<PARAM NAME="Scale" VALUE="ShowAll">
		<PARAM NAME="DeviceFont" VALUE="0">
		<PARAM NAME="EmbedMovie" VALUE="0">
		<PARAM NAME="SWRemote" VALUE="">
		<PARAM NAME="MovieData" VALUE="">
		<PARAM NAME="SeamlessTabbing" VALUE="1">
		<PARAM NAME="Profile" VALUE="0">
		<PARAM NAME="ProfileAddress" VALUE="">
		<PARAM NAME="ProfilePort" VALUE="0">
	
		
		<embed bgcolor="#F8F6E6" id="EmbedFlashFilesUpload" src="flash/ElementITMultiPowUpload1.7.swf" quality="high" pluginspage="https://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"	type="application/x-shockwave-flash" width="450" height="350" flashvars="showLink=false&uploadButtonVisible=no&redirectUploadUrl=multiProofTestCreate.action&backgroundColor=FFFFFF&uploadUrl=/fieldid/multiProofTestUpload.action">
		</embed>
	</OBJECT>
	<p>
		<label><@s.text name="label.prooftesttype"/></label> 
		<span>
			<@s.select name="proofTestType" list="proofTestTypes" listKey="name()" listValue="%{ getText( label ) }" />
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.customer"/></label>
		<span><@n4.orgPicker name="owner" required="true" orgType="customer"/></span>
	</p>
	
	<p>
		<label><@s.text name="label.eventbook"/></label>
		<span>
			<@s.select id="eventBooks" name="event.book.iD" list="eventBooks" listKey="id" listValue="name" headerKey="" headerValue=""/>
		</span>
	</p>
	
	<div class="formAction" >
		<button onclick="multiProofSubmit(this.form); return false;"><@s.text name="hbutton.upload"/></button>
	</div>  
  
</@s.form>

<div id="ieOnlyMessage" class="hide">
	<p class="error">
		<@s.text name="label.ie_only_for_multi_proof_upload"/>
	</>
</div>
