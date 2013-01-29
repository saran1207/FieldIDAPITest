var textFieldHints = new Object();

// Called from Wicket to initialize a text field hint.
function prepareHintListeners(id, hintText) {
    var hintedTextField = $('#'+id);
    hintedTextField.val(hintText)
        .addClass("description")
        .bind('focus', clearHint)
        .bind('blur', replaceHint);
}

function clearHint(event) {
    var element = event.target;
    if ($(element).hasClass("description")) {
        $(element).removeClass("description");
        textFieldHints[element.id] = $(element).val();
        $(element).val("");
    }
}

function replaceHint(event) {
    var element = event.target;
    if (jQuery.trim(element.value).length == 0) {
        $(element).addClass("description");
        $(element).val(textFieldHints[element.id]);
    }
}

