<div class=" formRowHolder">
    <div class="identifierRow titleRow">
        <div class="shortColHead">
            <label><@s.text name="label.number.short"/></label>
        </div>
        <div>
            <label><@s.text name="label.id_number"/></label>
        </div>
        <div>
            <label><@s.text name="label.rfid_or_barcode"/></label>
        </div>
        <div>
            <label><@s.text name="label.referencenumber"/></label>
        </div>
    </div>
    <@s.iterator value="identifiers" id="identifier" status="ident_stat" >
        <@s.hidden id="id_${ident_stat.index}" name="identifiers[${ident_stat.index}].assetId"/>
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
