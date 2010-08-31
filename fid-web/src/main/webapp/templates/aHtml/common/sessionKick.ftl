<div class="lightBoxView">
	<h2 class="modalHeader"><@s.text name="label.session_kicked" /></h2>
	<p class="instructions">
		<@s.text name="label.why_you_have_been_signed_out"/>
	</p>
	<p class="lightBoxAction">
		<button target="_top" onclick="promptForLogin(); return false" id="sessionKickedSignIn"><@s.text name="label.sign_back_in"/></button>
		<@s.text name="label.or"/>
		<a href="#" onclick="closeLightBox(); return false"><@s.text name="label.close"/></a>
	</p>
</div>

