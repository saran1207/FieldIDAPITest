var currentSignatureCriteriaId = 0;
var currentSignatureCriteriaCount = 0;
var signatureClearUrl;
var submitSignatureUrl;

function initializeSignatureWindowOpenedHook() {
    document.observe('lightview:opened', storeCriteriaId);
}

function getCurrentSignatureCriteriaId() {
    return currentSignatureCriteriaId;
}

function storeCriteriaId(e) {
    var attrs = e.target.attributes;
    for (var i =0; i < attrs.length; i++) {
        if (attrs[i].name == "criteriaid") {
            currentSignatureCriteriaId = attrs[i].value;
        }
        if (attrs[i].name == "currentcriteriaindex") {
            currentSignatureCriteriaCount = attrs[i].value;
        }
    }
}

function storeSignature(data) {
    var signatureParams = {
        pngData: data,
        criteriaId: currentSignatureCriteriaId,
        currentCriteriaIndex: currentSignatureCriteriaCount
    };

    getResponse(submitSignatureUrl, "post", signatureParams);
}

function clearSignature(criteriaId, currentCriteriaIndex) {
    currentSignatureCriteriaId = criteriaId;
    getResponse(signatureClearUrl, "post", { criteriaId: criteriaId, currentCriteriaIndex: currentCriteriaIndex });
}

function performThumbnailRefresh(newThumbnailSection) {
    try {
        Lightview.hide();
    } catch(err) {}
    $('signatureCriteria'+currentSignatureCriteriaId).replace(newThumbnailSection);
}

Event.observe(window, 'load', initializeSignatureWindowOpenedHook);