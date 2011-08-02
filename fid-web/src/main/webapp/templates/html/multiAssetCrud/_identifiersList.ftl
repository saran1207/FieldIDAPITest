<div class=" formRowHolder">
    <div class="identifierRow titleRow">
        <div class="shortColHead">
            <label><@s.text name="label.number.short"/></label>
        </div>
        <div>
            <label>${identifierLabel}</label>
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
            <div id="identifierInvalid_${ident_stat.index}" style="display: none;" class="errorMessage invalidIdentifier">
                <@s.text name="error.identifier_format"/>
            </div>
        </div>
        <div class="identifierRow">
            <div class="shortCol">
                <label>${ident_stat.index+1}.</label>
            </div>
            <div>
                <@s.textfield id="identifier_${ident_stat.index}" cssClass="identifierInput identifier" name="identifiers[${ident_stat.index}].identifier" onkeyup="identifierIsValid(${ident_stat.index});" />
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
