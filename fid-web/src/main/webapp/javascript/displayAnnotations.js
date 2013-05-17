var defaults={};

function createLabel(annotation) {
    var value = annotation.text;
    var direction =  annotation.x < .5 ? 'arrow-left' : 'arrow-right';
    var type = annotation.cssStyle;
    var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(direction).addClass(type);
    var icon = $('<span/>').addClass('icon').appendTo(span);
    var viewPanel = $('<span>'+value+'</span>').addClass("noteText").appendTo(span);
    viewPanel.css('width',(value.length + 1) * 6 + 'px');
    return span;
}

var lockingImagesAnnotated = false;
var unlockingImagesAnnotated = false;

function annotateLockingImages() {
    if (!lockingImagesAnnotated) {
        lockingImagesAnnotated = true;
        $('.lockingTimeline div.media-image').each(
            function(index, element) {
                var currentAnnotation = isolationAnnotations[index];
                $(element).addAnnotations(
                    function(annotation) {
                        return createLabel(annotation);
                    }, [ currentAnnotation ], { xPosition:"left" } );
            });
    }
}

function annotateUnlockingImages() {
    if (!unlockingImagesAnnotated) {
        unlockingImagesAnnotated = true;
        $('.unlockingTimeline div.media-image').each(
            function(index, element) {
                var currentAnnotation = isolationAnnotations[isolationAnnotations.length - 1 - index];
                $(element).addAnnotations(
                    function(annotation) {
                        return createLabel(annotation);
                    }, [ currentAnnotation ], { xPosition:"left" });
            });
    }
}


var lockTimeoutCheck = null;
var unlockTimeoutCheck = null;

var numIsolationPoints = 0;

function waitForLockingTimelineToLoadThenAnnotate() {
    lockTimeoutCheck = window.setInterval(annotateLockAfterLoad, 100);
}

function waitForUnlockingTimelineToLoadThenAnnotate() {
    unlockTimeoutCheck = window.setInterval(annotateUnlockAfterLoad, 100);
}

function annotateLockAfterLoad() {
    if ($('.lockingTimeline div.media-image img').size() == numIsolationPoints) {
        $($('.lockingTimeline div.media-image img')[numIsolationPoints-1]).load(function() { annotateLockingImages() });
        window.clearInterval(lockTimeoutCheck);
    }
}

function annotateUnlockAfterLoad() {
    if ($('.unlockingTimeline div.media-image img').size() == numIsolationPoints) {
        $($('.unlockingTimeline div.media-image img')[numIsolationPoints-1]).load(function() { annotateUnlockingImages() } );
        window.clearInterval(unlockTimeoutCheck);
    }
}