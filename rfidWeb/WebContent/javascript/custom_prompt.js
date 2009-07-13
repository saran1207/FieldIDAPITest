		var response = null 

			function prompt2(promptpicture, prompttitle, message, sendto) { 

				promptbox = document.createElement('div'); 

				promptbox.setAttribute ('id' , 'prompt') 

					document.getElementsByTagName('body')[0].appendChild(promptbox) 

					promptbox = eval("document.getElementById('prompt').style") 

					promptbox.position = 'absolute' 

					promptbox.top = 250 

					promptbox.left = 400 

					promptbox.width = 300 

					promptbox.border = 'outset 1 #bbbbbb' 

					document.getElementById('prompt').innerHTML = "<table cellspacing='0' cellpadding='0' border='0' width='100%'><tr valign='middle'><td class='titlebar'>" + prompttitle + "</td></tr></table>" 

					document.getElementById('prompt').innerHTML = document.getElementById('prompt').innerHTML + "<table cellspacing='0' cellpadding='0' border='0' width='100%' class='promptbox'><tr><td>" + message + "</td></tr><tr><td><input type='text' id='promptbox' onblur='this.focus()' class='promptbox'></td></tr><tr><td align='right'><br><input type='button' class='prompt' value='OK' onMouseOver='this.style.border=\"1 outset #dddddd\"' onMouseOut='this.style.border=\"1 solid transparent\"' onClick='" + sendto + "(document.getElementById(\"promptbox\").value); document.getElementsByTagName(\"body\")[0].removeChild(document.getElementById(\"prompt\"))'> <input type='button' class='prompt' value='Cancel' onMouseOver='this.style.border=\"1 outset transparent\"' onMouseOut='this.style.border=\"1 solid transparent\"' onClick='" + sendto + "(\"\"); document.getElementsByTagName(\"body\")[0].removeChild(document.getElementById(\"prompt\"))'></td></tr></table>" 

					document.getElementById("promptbox").focus() 

				} 

		function renameRFID(value) { 

			if(value.length<=0)
			{
				return false;
			} else {
			    document.forms[1].action = "productinfo.do?method=renameRFID&newRFID=" + value;
			    document.forms[1].submit();
			}
		} 
