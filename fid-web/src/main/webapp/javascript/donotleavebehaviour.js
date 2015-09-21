/**
 * This JS will create some "dirty data" functionality for the LOTO Procedure builder
 *
 * Created by Jordan Heath on 2015-07-02.
 */

var windowDirty = false;
var windowDirtyInitialized = false;

function initWindowDirty() {
    if(windowDirtyInitialized) return;
    windowDirtyInitialized = true;

    //We set this to true, but some special buttons will set this value to false when clicked.
    windowDirty = true;

    window.addEventListener("beforeunload", function(event) {
        if(windowDirty) {
            event.returnValue = "You may have unsaved changes!";
        }
    });
}

function undirty() {
    windowDirty = false;
}
