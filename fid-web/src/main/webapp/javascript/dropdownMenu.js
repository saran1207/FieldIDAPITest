/**
 * Used with dropdown menu.
 */
function handleDropdownMenuCollapse(event) {
    if (!event.target.matches('.dropdown-menu-button-image, .dropdown-menu-button')) {
        jQuery('.dropdown-menu-content').removeClass('dropdown-menu-show');
    }
}
window.addEventListener('click', handleDropdownMenuCollapse);

function toggleDropdownMenu(menuButton) {

    /* The link was clicked - use this to get the parent and then the
     element that contains the popup menu items */
    var buttonChildNodes = menuButton.parentElement.childNodes;
    for (var i = 0; i < buttonChildNodes.length; i++) {
        var nodeClassName = buttonChildNodes[i].className;
        if (typeof nodeClassName != 'undefined') {
            if (nodeClassName.indexOf('dropdown-menu-content') > -1) {
                buttonChildNodes[i].classList.toggle('dropdown-menu-show');

                /* Collapse any other already open menus */
                var allDropDowns = document.getElementsByClassName('dropdown-menu-content');
                for (var dropDownEle in allDropDowns) {
                    if (allDropDowns[dropDownEle] !== buttonChildNodes[i]) {
                        var eleClassList = allDropDowns[dropDownEle].classList;
                        if ((typeof eleClassList != 'undefined') && eleClassList.contains("dropdown-menu-show"))
                            eleClassList.remove('dropdown-menu-show');
                    }
                }
                break;
            }

        }
    }
}
