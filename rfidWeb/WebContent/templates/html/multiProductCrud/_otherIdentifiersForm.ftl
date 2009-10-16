<@s.form id="step4form" cssClass="inputForm" theme="fieldidSimple">
	<div class=" formRowHolder">
		<div class="identifierRow titleRow">
			<div class="shortColHead">
				<label><@s.text name="label.number.short"/></label>
			</div>
			<div>
				<label><@s.text name="label.serialnumber"/></label>
			</div>
			<div>
				<label><@s.text name="label.rfid_or_barcode"/></label>
			</div>
			<div>
				<label><@s.text name="label.referencenumber"/></label>
			</div>
		</div>
		<@s.iterator value="identifiers" id="identifier" status="ident_stat" >
			<div class="identifierErrorRow">
				<div id="serialNumberInvalid_${ident_stat.index}" style="display: none;" class="errorMessage invalidSerial">
					<@s.text name="error.serial_number_format"/>
				</div>
			</div>
			<div class="identifierRow">
				<div class="shortCol">
					<label>${ident_stat.index+1}.</label>
				</div>
				<div>
					<@s.textfield id="serial_${ident_stat.index}" cssClass="identifierInput serialNumber" name="identifiers[${ident_stat.index}].serialNumber" onkeyup="serialIsValid(${ident_stat.index});" />
				</div>
				<div>
					<@s.textfield cssClass="identifierInput" name="identifiers[${ident_stat.index}].rfidNumber" size="50" />
				</div>
				<div>
					<@s.textfield cssClass="identifierInput" name="identifiers[${ident_stat.index}].referenceNumber" size="50" />
				</div>
			</div>
		</@s.iterator>
	</div>
</@s.form>
<div class="stepAction" id="step4Actions">
	<@s.form action="productMultiAddCreate" namespace="/" id="masterForm" theme="fieldid">
		<@s.submit key="label.save_and_create" onclick="mergeAndSubmit('step1form', 'step4form', 'masterForm'); return false;"/>
		<@s.text name="label.or"/> <a href="#step3" onclick="backToStep3(); return false;"><@s.text name="label.back_to_step"/> 3</a>
	</@s.form>
</div>