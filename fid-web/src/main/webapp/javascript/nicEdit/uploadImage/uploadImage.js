/* START CONFIG */
var nicUploadImageOptions = {
	buttons : {
		'uploadImage' : {name : 'uploadImage', type : 'nicUploadImageButton'}
	}/* NICEDIT_REMOVE_START */,
	iconFiles : {'uploadImage' : '../../images/nicEdit/uploadImage.png'}/* NICEDIT_REMOVE_END */
};
/* END CONFIG */

var nicUploadImageButton = nicEditorAdvancedButton.extend({

	addPane : function () {
		this.im = this.ne.selectedInstance.selElm().parentTag('IMG');

// make a FORM like this....
//		<form action="http://foo.fieldid.com/fieldid/imageUpload/foo?path=/foo/bar/&tenantId=123" enctype="multipart/form-data" method="post">
//			<input type="file" name="datafile" size="40">
//			<input type="submit" value="Send">
//		</form>

		var container = new bkElement('form')
			.setAttributes({id:'uploadImageForm',action:this.ne.options.uploadURI, enctype:'multipart/form-data', method:'post'})
			.setStyle({ padding: '10px' })
			.appendTo(this.pane.pane);

		var image = new bkElement('img')
			.setAttributes({'src':'../../images/loading-small.gif',id:'uploadAnimation'})
			.setStyle({padding:'5px',display:'none'})
			.appendTo(container);

		new bkElement('span')
			.setStyle({ fontSize: '14px', fontWeight : 'bold', paddingBottom: '5px' })
			.setContent('Insert an Image')
			.appendTo(container);

		this.fileInput = new bkElement('input')
			.setStyle({ fontSize: '14px', fontWeight : 'bold', paddingBottom: '5px' })
			.setAttributes({'type' : 'file',name:'data', size:'20'})
			.appendTo(container);

		this.fileInput.onchange = this.uploadImage.closure(this);

	},

	uploadImage : function() {
		var opts = {
			target: '#uploadImageForm',   // target element to update
			dataType: 'json',
			clearForm: true,
			success: this.onUploadImage.closure(this)
		};
		$('#uploadImageForm').ajaxForm(opts);
		$('#uploadAnimation').show();
		$('#uploadImageForm').trigger('submit');
	},

	onUploadImage : function(options,statusText,xhr) {
			this.removePane();
			var src = options.upload.links.original;
			if(!this.im) {
				this.ne.nicCommand('insertImage', src);
				this.im = this.findElm('IMG','src', src);
			}
			var w = parseInt(this.ne.selectedInstance.elm.getStyle('width'));
			if(this.im) {
				this.im.setAttributes({
					src : src,
					width : (w && options.upload.image.width) ? Math.min(w, options.upload.image.width) : ''
				});
			}
	}


});

nicEditors.registerPlugin(nicPlugin,nicUploadImageOptions);