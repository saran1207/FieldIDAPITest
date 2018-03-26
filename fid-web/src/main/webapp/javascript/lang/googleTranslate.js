
if (showGoogleTranslateOption()) {
    function googleTranslateElementInit() {
        new google.translate.TranslateElement({
            pageLanguage: 'en',
            includedLanguages: 'en,es,fr,de',
            layout: google.translate.TranslateElement.InlineLayout.HORIZONTAL
        }, 'google_translate_element');


    }
}
else {
    var ele1 = document.getElementById("google_translate_element");
    if (ele1 != null)
        ele1.style.display = "none";
}

function showGoogleTranslateOption() {
    var userLang = navigator.language || navigator.userLanguage || navigator.languages;
    if (!Array.isArray(userLang) && userLang.substr(0, 2) != "en")
        return true;
    else
        return false;
}
