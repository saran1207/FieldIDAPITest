function setTableSelected(tableId, checked) {
    $("#"+tableId + " tbody tr").each(function() {
        if (checked)
            addSelectedClassToRow(this.id);
        else
            removeSelectedClassFromRow(this.id);
    });

    $("#"+tableId + " tr input[type='checkbox']").attr('checked', checked);
}

function addSelectedClassToRow(rowId) {
    $('#'+rowId).addClass('multiSelected');
}

function removeSelectedClassFromRow(rowId) {
    $('#'+rowId).removeClass('multiSelected');
}

function showRowSelectionStatus(checkbox, rowId, tableId) {
    if (checkbox[0].checked) {
        addSelectedClassToRow(rowId);
    } else {
        removeSelectedClassFromRow(rowId);
    }
    checkHeaderBoxIfAllItemsAreSelected(tableId);
}

function checkHeaderBoxIfAllItemsAreSelected(tableId) {
    var allChecked = true;
    $("#"+tableId + " tbody tr input[type='checkbox']").each(function(checkbox) {
        allChecked = allChecked && checkbox.checked;
    });
    $("#"+tableId + " thead input[type='checkbox']").attr('checked', allChecked);
}
