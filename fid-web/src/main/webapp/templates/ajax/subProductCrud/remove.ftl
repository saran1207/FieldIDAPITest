Effect.Fade( "subProduct_" + ${subProductId}, { 
	afterFinish: function() { 
		$( "subProduct_" + ${subProductId} ).remove(); 
	} 
} );