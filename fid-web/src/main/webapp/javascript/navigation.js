  function SetMethod(method) {
    document.forms[1].method.value=method;
  }

  function SetMethod_page(method, pageNo) {
    document.forms[1].method.value=method;
    document.forms[1].pageNo.value=pageNo;
  }

  function GetMethod(){
    return document.forms[1].method.value;
  }

  function SeleLang(lang) {
     location.href = "mixed.do?method=setLocale&language="+ lang;
  }
  
  function Mixed(method) {
    if (method=="populateData") {
      var go = false;
      go = (confirm("Please confirm you are going to run data populator."));
      if (!go) return;
    }

     location.href = "mixed.do?method=" + method;
  }

  function Navi(action) {
     if (action == "home") {
	     location.href = "mixed.do?method=home";
     } else {
	     location.href = action + ".do";
     }
  }
