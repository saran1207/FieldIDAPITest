<head>
	<style type="text/css">
		#signInForm {
			/*margin: 10px 0px;*/
		}
        #secondaryContent ul {
            padding-top: 10px;
            padding-left: 15px;
            font-size:1.3em;
        }
        #secondaryContent li {
            list-style: circle;
            padding-bottom: 10px;
            color: #888888;
        }
        #secondaryContent h2 {
            font-size:1.3em;
        }
	</style>
</head>
<div id="mainContent">
	<div class="titleBlock">
		<h1><@s.text name="title.enter_your_new_password"/></h1>
		<p class="titleSummary"><@s.text name="instructions.enter_new_password"/></p>
	</div>

	<@s.form action="finalizeReset" theme="fieldid" cssClass="minForm" >
		<#include "/templates/html/common/_formErrors.ftl" />
		<@s.hidden name="k" value="${k}"/>
        <@s.hidden name="u" value="${u}"/>
		<div id="normal_container" class="togglable">
			<label class="label"><@s.text name="label.enter_new_password"/></label>
			<@s.password name="newPassword"/>

			<label class="label"><@s.text name="label.re_enter_new_password"/></label>
			<@s.password name="confirmPassword"/>
		</div>

		<div class="actions togglable" id="normalActions_container">
			<@s.submit key="hbutton.save_new_password" id="signInButton"/> <@s.text name="label.or"/> <a href="<@s.url action="login"></@>"><@s.text name="label.cancel"/></a>
		</div>

	</@s.form>

</div>

<div id="secondaryContent">

	<h2><@s.text name="label.tips_for_your_new_password"/></h2>

    <ul>
        <li>
            <@s.text name="instructions.password_length"/>
        </li>
        <li>
            <@s.text name="instructions.password_characters"/>
        </li>
        <li>
            <@s.text name="instructions.password_case"/>
        </li>
    </ul>

</div>

