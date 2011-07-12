function setTableSelected(tableId, checked) {
    $$("#"+tableId + " tbody tr").each(function(row) {
        if (checked)
            addSelectedClassToRow(row.id);
        else
            removeSelectedClassFromRow(row.id);
    });

    $$("#"+tableId + " tr input[type='checkbox']").each(function(checkbox) {
        checkbox.checked = checked;
    });
}

function addSelectedClassToRow(rowId) {
    $(rowId).addClassName('multiSelected');
}

function removeSelectedClassFromRow(rowId) {
    $(rowId).removeClassName('multiSelected');
}

function showRowSelectionStatus(checkbox, rowId, tableId) {
    if (checkbox.checked) {
        addSelectedClassToRow(rowId);
    } else {
        removeSelectedClassFromRow(rowId);
    }
    checkHeaderBoxIfAllItemsAreSelected(tableId);
}

function checkHeaderBoxIfAllItemsAreSelected(tableId) {
    var allChecked = true;
    $$("#"+tableId + " tbody tr input[type='checkbox']").each(function(checkbox) {
        allChecked = allChecked && checkbox.checked;
    });
    $$("#"+tableId + " thead input[type='checkbox']").each(function(headerCheckbox) {
        headerCheckbox.checked = allChecked;
    });
}
