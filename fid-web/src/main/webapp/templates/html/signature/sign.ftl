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
<script type="text/javascript" src="/fieldid/javascript/prototype.js"></script>
<script type="text/javascript" src="/fieldid/javascript/common.js"></script>
<script type="text/javascript" src="/fieldid/javascript/canvas/cp_depends.js"></script>
<!--[if lt IE 9]>
<script type="text/javascript" src="/fieldid/javascript/canvas/flashcanvas.js"></script>
<![endif]-->
<script src="/fieldid/javascript/canvas/CanvasWidget.js" type="text/javascript"></script>
<script src="/fieldid/javascript/canvas/CanvasPainter.js" type="text/javascript"></script>
<script src="/fieldid/javascript/canvas/CPWidgets.js" type="text/javascript"></script>
<script src="/fieldid/javascript/canvas/CPAnimator.js" type="text/javascript"></script>
<script src="/fieldid/javascript/canvas/CPDrawing.js" type="text/javascript"></script>

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
	a {
		text-decoration: none;
		color: black;
	}
	canvas {
		border: 1px solid #AAAAAA;
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
    <button onclick="doClear()">Clear</button> <button onclick="doStore()">Store</button>
</div>

</body></html>
