Lightview.show(	{
		href: '<@s.url action="showKick" namespace="/aHtml"/>',
		rel: 'ajax',
		title: '<@s.text name="label.session_kicked"/>',
		options: {
			topclose: false,
			width: 500,
			height: 400,
			ajax: {	method: 'get' }
		}	
	});