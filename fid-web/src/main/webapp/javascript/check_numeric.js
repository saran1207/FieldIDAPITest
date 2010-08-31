function CheckMe(it) {
  while (it.value.charAt(it.value.length - 1) == ' ')
  it.value = it.value.substring(0, it.value.length - 1);

  while (it.value.charAt(0) == ' ')
  it.value = it.value.substring(1, it.value.length);

  if (isNaN(it.value) || eval(it.value) <= 0) {
    it.value = "";
    return;
  }
}


function CheckMe2(it) {
  var s = it.value;
  if (!isPosInteger(s)) {
    it.value = "";
  }
}

function isPosInteger (s) {
  if (isEmpty(s)) {
    return true;
  } else {
    var startPos = 0;
    if (s.charAt(0) == "+") startPos = 1;
    return isInteger(s.substring(startPos, s.length));
  }
}

function isSignedInteger (s) {
  if (isEmpty(s)) {
    return true;
  } else {
    var startPos = 0;
    if ((s.charAt(0) == "-") || (s.charAt(0) == "+")) startPos = 1;
    return isInteger(s.substring(startPos, s.length));
  }
}

function isInteger (s) {
  var i;
  for (i = 0; i < s.length; i++) {
    var c = s.charAt(i);
    if (!isDigit(c)) return false;
  }
  return true;
}


function isEmpty(s) {
  return ((s == null) || (s.length == 0))
}

function isDigit (c) {
  return ((c >= "0") && (c <= "9"))
}



