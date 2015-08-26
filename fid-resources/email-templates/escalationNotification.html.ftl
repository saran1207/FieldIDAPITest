<style type="text/css">

    #outlook a {padding:0;}
    body{width:100% !important; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; margin:0; padding:0;}

    .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {line-height: 100%;}
    #backgroundTable {margin:0; padding:0; width:100% !important; line-height: 100% !important;}
    img {outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;}
    a img {border:none;display:inline-block;}

    h1, h2, h3, h4, h5, h6 {color: black !important;}

    h1 a, h2 a, h3 a, h4 a, h5 a, h6 a {color: blue !important;}

    h1 a:active, h2 a:active,  h3 a:active, h4 a:active, h5 a:active, h6 a:active {
        color: red !important;
    }

    h1 a:visited, h2 a:visited,  h3 a:visited, h4 a:visited, h5 a:visited, h6 a:visited {
        color: purple !important;
    }

    table td {border-collapse: collapse;}

    table { border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt; }

    a {color: #000;}

    @media only screen and (max-device-width: 480px) {

        a[href^="tel"], a[href^="sms"] {
            text-decoration: none;
            color: black;
            pointer-events: none;
            cursor: default;
        }

        .mobile_link a[href^="tel"], .mobile_link a[href^="sms"] {
            text-decoration: default;
            color: orange !important;
            pointer-events: auto;
            cursor: default;
        }
    }


    @media only screen and (min-device-width: 768px) and (max-device-width: 1024px) {
        a[href^="tel"], a[href^="sms"] {
            text-decoration: none;
            color: blue;
            pointer-events: none;
            cursor: default;
        }

        .mobile_link a[href^="tel"], .mobile_link a[href^="sms"] {
            text-decoration: default;
            color: orange !important;
            pointer-events: auto;
            cursor: default;
        }
    }

    p {
        margin:0;
        color:#555;
        font-family:Helvetica, Arial, sans-serif;
        font-size:16px;
        line-height:160%;
    }
    a.link2{
        text-decoration:none;
        font-family:Helvetica, Arial, sans-serif;
        font-size:16px;
        color:#fff;
        border-radius:4px;
    }
    h1{
        font-family:Helvetica, Arial, sans-serif;
        font-size:26px;
        font-weight: 100;
        line-height:40px;
    }
    h2{
        color:#181818;
        font-family:Helvetica, Arial, sans-serif;
        font-size:22px;
        font-weight: normal;
    }

    .pad-the-top-big {
        padding-top: 25px;
    }

    .pad-the-top-and-bottom {
        padding-top: 20px;
        padding-bottom: 20px;
    }

    .size-13-font {
        font-size: 13px;
    }

    .grey-background {
        background: #f3f3f3;
    }

    .collapse-border {
        border-collapse: collapse;
    }

    .event-border {
        background:#FFF;
        border:1px solid #CCC;
        margin-top: 25px;
    }

    .logo-height {
        height: 50px;
    }

    .image-adjustment {
        vertical-align: top;
        padding-top: 35px;
        text-align: center;
        margin-bottom: -3px;
    }

    .priority-adjustment {
        font-size: 18px;
        font-family: Heveltica, Arial, sans-serif;
        color: #333;
    }

    .information-adjustment {
        padding-top: 20px;
        padding-right: 10px;
        font-family: Helvetica, Arial, sans-serif;
        font-size: 15px;
        line-height: 150%;
    }

    .information-paragraph {
        font-size: 13px;
        line-height: 25px;
    }

    .event-background {
        color: #fff;
    }

    .no-image-adjustment {
        padding-left: 30px;
        padding-top: 20px;
        padding-bottom: 20px;
    }

    .footer-bar {
        height: 1px;
        border: none;
        color: #333;
        background-color: #ddd;
    }

    .footer-font {
        font-family: Helvetica, Arial, sans-serif;
        line-height: 200%;
        color: #181818;
    }

    .footer-small {
        font-size: 11px;
    }

    .footer-large {
        font-size:13px;
    }

    .login-container {
        padding-top: 25px;
        padding-bottom: 115px;
    }

    .login-border {
        border-radius: 4px;
    }

    .perform-container {
        padding-top: 25px;
        padding-bottom: 10px;
    }

    .capitalize-text {
        text-transform: capitalize;
    }

    span.preheader {
        font-size: 1px;
        color: #ddd;
        visibility: hidden;
    }

</style>

<table cellpadding="0" width="100%" cellspacing="0" border="0" id="backgroundTable" class="grey-background">
    <tr>
        <td>
            <span class="preheader">${messageBody}</span>
            <div align="center">
                <img align="center" src="http://ce711d17a6fff7097cbb-77686725d730a71bab41d58d2c98ad28.r84.cf2.rackcdn.com/masterlock-fid-logo3.png" class="logo-height">
            </div>

            <table cellpadding="0" cellspacing="0" border="0" align="center" width="600">
                <tr>
                    <td width="100">&nbsp;</td>
                    <td width="600" align="center">
                        <p class="size-13-font">${messageBody}</p>
                    </td>
                    <td width="100">&nbsp;</td>
                </tr>
                <tr>
                    <td width="100%" colspan="3" align="center" class="pad-the-top-big">
					<#if assignedGroupName??>
                        <h1 class="capitalize-text">ASSIGNED TO ${assignedGroupName}</h1>
					<#else>
                        <h1>ASSIGNED TO ${assigneeName}</h1>
					</#if>
                    </td>
                </tr>
            </table>
            <table cellpadding="0" cellspacing="0" border="0" align="center" width="600" class="event-border collapse-border">
                <tr>
                    <td class="event-background">
                        <table cellpadding="0" class="collapse-border" cellspacing="0" border="0" align="center" width="600">
                            <tr>
                                <td width="600" valign="top" class="no-image-adjustment">
                                	<br>
									<#if isAction && actionPriority??>
										<div class="priority-adjustment capitalize-text">
											<strong>${actionPriority}</strong> PRIORITY
										</div>
									</#if>
									<p class="information-adjustment information-paragraph">
										<strong>Type:</strong> ${eventType} <br>
										<#if isThingEvent>
											<strong>Asset:</strong>&nbsp;
											<#if showLinks>
												<a target="_blank" href="${assetSummaryURL}">${assetName}</a>
											<#else>
											${assetName}
											</#if>
											<br>
											<strong>Owner:</strong> ${assetOwnerName}
											<br>
										</#if>
										<#if isPlaceEvent>
											<strong>Location:</strong>&nbsp;
											<#if showLinks>
												<a target="_blank" href="${placeSummaryURL}">${placeName}</a>
											<#else>
											${placeName}
											<br>
											</#if>
										</#if>
										<#if isAction>
											<#if triggeringPlaceName??>
												<strong>Location:</strong>&nbsp;
												<#if showLinks>
													<a target="_blank" href="${triggeringPlaceSummaryURL}">${triggeringPlaceName}</a>
												<#else>
													${triggeringPlaceName}
												</#if>
												<br>
											</#if>
											<#if triggeringAssetName??>
												<strong>Asset:</strong>&nbsp;
												<#if showLinks>
													<a target="_blank" href="${triggeringAssetSummaryURL}">${triggeringAssetName}</a>
												<#else>
													${triggeringAssetName}
												</#if>
											</#if>
										</#if>
										<strong>Due:</strong> ${dueDate}<br>
										<#if isAction>
											<strong>Issuing Event:</strong>&nbsp;
											<#if showLinks && triggeringEventURL??>
												<a target="_blank" href="${triggeringEventURL}">${triggeringEventName}</a>
											<#else>
												${triggeringEventName}
											</#if>
											<br>
										</#if>
									</p>
                            	</td>
                            </tr>
                        </table>
                    </td>
                </tr>
				<#if showLinks>
                    <tr>
                        <td class="event-background">
                            <table cellpadding="0" cellspacing="0" border="0" align="center" width="600">
                                <tr>
                                    <td width="100">&nbsp;</td>
                                    <td width="400" align="center" class="perform-container">
                                        <table cellpadding="0" cellspacing="0" border="0" align="center" width="200" height="50">
                                            <tr>
                                                <td bgcolor="#51A3FF" align="center" class="login-border" width="200" height="50">
                                                    <a target='_blank' href="${performEventUrlMap.get(event.id)}" class='link2'>Perform This Event Now</a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="100">&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
				</#if>
            </table>
            <table cellpadding="0" cellspacing="0" border="0" align="center" width="600">
                <tr>
                    <td width="100%" colspan="2" style="padding-top:35px;">
                        <hr class="footer-bar" />
                    </td>
                </tr>
                <tr>
                    <td width="60%" height="70" valign="middle" style="padding-bottom:20px;">
                        <span class="footer-font footer-large">Sent by Master Lock Field iD</span>
                        <br/>
                        <span class="footer-font footer-small">111 Queen Street East Suite 240<br>Toronto, Ontario, Canada, M5C 1S6</span>
                        <br/>
                        <span class="footer-font footer-large">
                            <a href="mailto:contact@fieldid.com">contact@fieldid.com</a>
                        </span>
                        <br/>
                        <span class="footer-font footer-small">
                            This e-mail address is not a valid return address and is not monitored - for questions please email <a href="mailto:support@fieldid.com">support@fieldid.com</a>
                        </span>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>