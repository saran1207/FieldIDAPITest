<title><@s.text name="title.eula"/></title>

<head>
	<script type="text/javascript">
		var acceptEulaEnabledText = '<@s.text name="label.accept"/>';
		Element.extend(document).observe('dom:loaded', function() {
				$('eulaLegalText').observe('mouseover', checkIfAtBottomOfEula);	
				$('eulaLegalText').observe('mousemove', checkIfAtBottomOfEula);	
				$('eulaLegalText').observe('keydown', checkIfAtBottomOfEula);
				$('eulaLegalText').observe('keyup', checkIfAtBottomOfEula);
				$('eulaLegalText').observe('scroll', checkIfAtBottomOfEula);	
			});
			
		function checkIfAtBottomOfEula() {
			if ($('eulaLegalText').scrollTop >= $('eulaLegalText').scrollHeight - $('eulaLegalText').getHeight()) {
				toggleEula();
			}
		} 	
			
		function toggleEula() {
			$('acceptEula').enable();
			$('acceptEula').value = acceptEulaEnabledText;
		}
	</script>
	<style type="text/css">
		.pageSection {
			width:888px;
			}
		#eulaWarning, #eulaQuestions {
			float:none;
			width:auto;
		}
		#eulaLegalText {
			height: 200px;
			width: 800px;
			overflow: auto;
			padding:5px;
			border: 1px solid #888;
		}
		#eulaLegalText, #eulaQuestions, #eulaWarning {
			margin-left: 15px;
			}
		h1 { padding: 15px;}
		#eulaForm {
			background-color: #fff;
		}
	</style>
</head>


<@s.form action="eulaAcceptanceCreate" id="eulaForm" theme="fieldid" cssClass="crudForm pageSection">
	<h1><@s.text name="title.eula"/></h1>
	<@s.hidden name="eulaId"/>
	<div class="sectionContent">
		<div class="infoSet">
			<label for="published"><@s.text name="label.published"/></label>
			<span id="published">${action.formatDateTime(currentEULA.effectiveDate)}</span>
		</div>
		
		<div class="infoSet">
			<label for="version"><@s.text name="label.version"/></label>
			<span id="version">${currentEULA.version?html}</span>
		</div>
	
		<div class="infoSet"> 
			<span id="eulaWarning">
				<@s.text name="warning.read_eula"/>
			</span>
			<span id="eulaQuestions">
				<@s.text name="label.questions_on_eula"/> <a href="<@s.url action="currentEula" namespace="/file"/>" target="_blank"><@s.text name="label.print_eula"/></a>
			</span>
		</div>
		<div class="infoSet">
			<div id="eulaLegalText">
				<pre>${currentEULA.legalText}</pre>
			</div>
		</div>
		<div class="formAction">
			<@s.submit key="label.scroll_to_bottom" id="acceptEula" disabled="true"/>
		</div>
	</div>
	
	
</@s.form> 