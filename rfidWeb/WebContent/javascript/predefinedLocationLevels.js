var lastIndex = 0;
var deleteLevelUrl = "";
onDocumentLoad(function() {
	$$('#removeLast').each(function(e){ 
		e.observe('click', function(event) {
			event.stop();
			postForm(deleteLevelUrl, {'levelName.index' : lastIndex});
		});
	});
	
	
	$$('#levels .cancel').each(function(e){
		e.observe('click', function(event) {
			event.stop();
			var element = Event.element(event);
			var nameCell = element.up('td');
			
			nameCell.down('.name').show();
			nameCell.down('.levelForm').hide().reset();
		}); 
	});
	$$('#levels .edit').each(function(e){
		e.observe('click', function(event) {
			event.stop();
			var element = Event.element(event);
			var row = element.up('tr');
			
			row.down('.name').hide();
			row.down('.levelForm').show();
			row.down('.levelForm').findFirstElement().focus().select();
		}); 
	});
});