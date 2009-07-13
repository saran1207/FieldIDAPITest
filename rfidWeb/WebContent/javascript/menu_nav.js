<!--
//  function ShowMenuByID(menuID) {
//    imgID = menuID + "row";
//    document.getElementById(menuID).style.visibility="visible";

//    hqx=0;
//  }

//  function ShowMenu(it) {
 //   imgID = it.id + "row";
  //  it.style.visibility = "visible";
  //  hqx=0;
//  }

//  function HideMenuByID(menuID) {
//    imgID = menuID + "row";
//    document.getElementById(menuID).style.visibility="hidden";
//    hqx=1;
//  }

//  function HideMenu(it) {
//    imgID = it.id + "row";
//    it.style.visibility = "hidden";
//    hqx=1;
//  }

  function ShowMenu(it, no) {
    imgID = it.id + "row";

    if (document.body.offsetWidth-800 > 0) {
    	it.style.left=(document.body.offsetWidth-800)/2 + 110*no;
    } else {
    	it.style.left=110;
   	}

    it.style.visibility = "visible";
  }

  function HideMenu(it) {
    imgID = it.id + "row";

    it.style.visibility = "hidden";
  }

  function ShowMenuByID(menuID, no) {
    imgID = menuID + "row";

    if (document.body.offsetWidth-800 > 0) {
    	document.getElementById(menuID).style.left=(document.body.offsetWidth-800)/2 + 110*no;
    } else {
    	document.getElementById(menuID).style.left=110;
   	}

    document.getElementById(menuID).style.visibility="visible";
  }

  function HideMenuByID(menuID) {
    imgID = menuID + "row";
    document.getElementById(menuID).style.visibility="hidden";
  }


//-->
