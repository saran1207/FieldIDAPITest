<h2>3. <@s.text name="label.select_serial_number_format"/></h2>
<div class="stepContent" id="step3" style="display:none">
	<@s.hidden id="rangeType" name="type" value="RANGE" />
	<div class="formRowHolder">
		<input type="radio" id="snRange" name="snFormats" value="RANGE" onchange="$('rangeType').value = this.value;" CHECKED/>
		<label class="snFormatLabel" for="snRange"><@s.text name="label.range"/></label>
		<p class="snFormatHelp"><@s.text name="label.range.text"/></p>
		<p class="snFormatHelp"><@s.text name="label.range.example"/></p>

		<table class="snParams">
			<tr>
				<th><@s.text name="label.prefix"/></th>
				<th><@s.text name="label.start"/></th>
				<th><@s.text name="label.suffix"/></th>
			</tr>
			<tr>
				<td><@s.textfield id="prefix" key="label.prefix" name="prefix"/></td>
				<td><@s.textfield id="start" name="start" value="1"/></td>
				<td><@s.textfield id="suffix" name="suffix" /></td>
			</tr>
			<tr>
				<td></td>
				<td><span style="display:none" id="rangeStartInvalid" class="errorMessage"><@s.text name="error.range_start_invalid"/></span></td>
				<td></td>
			</tr>
		</table>
	</div>
	<div class="formRowHolder">
		<input type="radio" id="snAuto" name="snFormats" value="AUTO" onchange="$('rangeType').value = this.value;"/>
		<label class="snFormatLabel" for="snAuto"><@s.text name="label.non_serialized_assets"/></label>
		<p class="snFormatHelp"><@s.text name="label.non_serialized_assets.text"/></p>
	</div>
	<div class="formRowHolder">
		<input type="radio" id="snBatch" name="snFormats" value="BATCH" onchange="$('rangeType').value = this.value;"/>
		<label class="snFormatLabel" for="snBatch"><@s.text name="label.batch_or_lot_number"/></label>
		<p class="snFormatHelp"><@s.text name="label.batch_or_lot_number.text"/></p>
		
		<div class="snParams">
			<div>
				<label for="ident"><@s.text name="label.identifier"/></label>
				<div><@s.textfield id="ident" name="ident"/></div>
			</div>
		</div>
	</div>
	<div class="formRowHolder">
		<input type="radio" id="snManual" name="snFormats" value="MANUAL" onchange="$('rangeType').value = this.value;"/>
		<label class="snFormatLabel" for="snManual"><@s.text name="label.manual"/></label>
		<p class="snFormatHelp"><@s.text name="label.manual.text"/></p>	
	</div>
	<div class="stepAction">
		<@s.submit key="label.continue" onclick="validateForm3(); return false;" theme="fieldidSimple"/>
		<@s.text name="label.or"/> <a href="#step2" onclick="backToStep2(); return false;"><@s.text name="label.back_to_step"/> 2</a>
	</div>
</div>
