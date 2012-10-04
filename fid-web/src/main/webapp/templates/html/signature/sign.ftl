<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><title>Canvas Painter</title>
<!--
	Copyright (c) 2005, 2006 Rafael Robayna

	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

	Additional Contributions by: Morris Johns
-->

</head><body>
<@n4.includeScript src="prototype.js"/>
<@n4.includeScript src="common.js"/>
<@n4.includeScript src="canvas/cp_depends.js"/>

<!--[if IE]>
<@n4.includeScript src="canvas/flashcanvas.js"/>
<![endif]-->
<@n4.includeScript src="canvas/CanvasWidget.js"/>
<@n4.includeScript src="canvas/CanvasPainter.js"/>
<@n4.includeScript src="canvas/CPWidgets.js"/>
<@n4.includeScript src="canvas/CPAnimator.js"/>
<@n4.includeScript src="canvas/CPDrawing.js"/>
<@n4.includeScript src=""/>

<@n4.includeStyle href="fieldid"/>

<style type="text/css">
	body {
		font-family: arial, helvetica;
		font-size: 11px;
		margin: 0px;
		padding: 0px;
	}
	h1 {
		font-size: 14pt;
		font-style: italic;
		margin-bottom: 8px;
	}
	canvas {
		border: 1px solid #AAAAAA;
	}
    a.clearLink {
        font-size: 12px;
        cursor: pointer;
    }
	#canvas {
		position: absolute;
		left: 10px;
		top: 10px;
        background: url("/fieldid/images/signature-line.png") no-repeat;
	}
	#canvasInterface {
		position: absolute;
		left: 10px;
		top: 10px;
	}
	#chooserWidgets canvas {
		margin-bottom: 10px;
	}
    .bottomButtons {
        display:block;
        position:absolute;
        left: 10px;
        top: 263px;
    }

</style>
<script type="text/javascript">
	var canvasPainter;

	function doOnLoad() {
		if(CanvasHelper.canvasExists("canvas")) {
			canvasPainter = new CanvasPainter("canvas", "canvasInterface", {x: 10, y: 10});

            canvasPainter.setLineWidth(5);
            setCPDrawAction(1);
		}
	}

    Event.observe(window, 'load', doOnLoad);

	function printError(error) {
	}

	function setCPDrawAction(action) {
		canvasPainter.setDrawAction(action);
	}

    function doClear() {
        setCPDrawAction(5);
    }

    function doStore() {
        var data = $('canvas').toDataURL("image/png");
        parent.storeSignature(data);
    }
</script>
&nbsp;

<canvas id="canvas" width="750" height="250" onselectstart="return false;"></canvas>
<canvas id="canvasInterface" width="750" height="250" onselectstart="return false;"></canvas>

<div class="bottomButtons">
    <button onclick="doStore()"><@s.text name="label.store"/></button> <@s.text name="label.or"/> <a class="clearLink"  onclick="doClear()"><@s.text name="label.clear"/></a>
</div>

</body></html>
