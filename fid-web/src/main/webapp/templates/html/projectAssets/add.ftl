<head>

	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projectAssets.css"/>"/>
	<script type="text/javascript" src="<@s.url value="/javascript/projects.js"/>"></script>
	<script type="text/javascript">
		function findAssets() {
			var assetLinks = $$( '.assetLink' );
			if( assetLinks != null ) {
				for( var i = 0; i < assetLinks.length; i++ ) {
					assetLinks[i].observe( 'click', attachAssetListener );
				}
			}
		}
		
		var attachAssetToProjectUrl = "";
		
		function attachAssetListener( event ) {
			event.stop();
			redirect( attachAssetToProjectUrl + "?projectId=" + ${project.id} + "&uniqueID=" + Event.element( event ).getAttribute( 'assetId' ) );
		}
		
		function oneResultAsset( assetId, results ) {
			$('results').replace( results );
			findAssets();
			$('results').highlight();
		}
	
		attachAssetToProjectUrl = "<@s.url action="jobAssetCreate" />";
	</script>
	
	<style>
		#results {
			overflow:hidden;
			padding:5px;
			border-bottom:1px solid #D0DAFD;
		}
		
		#results .error span {
			line-height:30px;
			padding:0 10px;
		}
	</style>
</head>
${action.setPageType('job','assets')!}
<#include "_secondaryNav.ftl"/>
<div id="assetLookup" >
	<@s.hidden name="projectId"/>
	<#assign namespace="/ajax"/>
	<#assign usePaginatedResults=true/>
	<#assign useAjaxPaginatedResults=true>
	<#assign assetSearchAction="jobFindAsset"/>
	<#assign assetFormId="projectAssetSearch"/>
	<#include "../eventGroup/_searchForm.ftl"/>
	<div id="results" class="hidden">
		
	</div>
</div>


<script type="text/javascript">
	$( 'projectAssetSearch' ).observe( 'submit', 
		function( event ) {
			event.stop(); 
			var element = Event.element( event ); 
			element.request( getStandardCallbacks() );
		} );
</script>