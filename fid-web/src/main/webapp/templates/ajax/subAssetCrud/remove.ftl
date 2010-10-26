Effect.Fade( "subAsset_" + ${subAssetId}, {
	afterFinish: function() { 
		$( "subAsset_" + ${subAssetId} ).remove(); 
	} 
} );