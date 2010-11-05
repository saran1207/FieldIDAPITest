<head>
	<@n4.includeStyle type="page" href="refer"/>
</head>
${action.setPageType('my_account', 'refer')!}

<div id="referralsTop">
	<div id="referralInstructions">
		<p><@s.text name="label.referral_text"/></p>
		
		<div id="referralStep1" class="referralStep">
			<h3 ><@s.text name="label.referral_step1.title"/></h3>
			<div class="referralSubStep">
				<h4 ><@s.text name="label.referral_step1_1.title"/></h4>
				<p>
					<@s.url id="inviteUrl" action="invite"/>
					<@s.text name="label.referral_step1_1.desc">
						<@s.param>${inviteUrl}</@s.param>
					</@s.text>
				</p>
			</div>
		
			<div class="referralSubStep">
				<h4 ><@s.text name="label.referral_step1_2.title"/></h4>
				<p><@s.text name="label.referral_step1_2.desc"/></p>
			</div>
		</div>
		
		<div id="referralStep2"  class="referralStep">
			<h3 ><@s.text name="label.referral_step2.title"/></h3>
			<p><@s.text name="label.referral_step2.desc"/></p>
			
		</div>
	</div>
	
	<div id="referralLinkBox">
		<h3><@s.text name="label.your_referral_link"/></h3>
		<p>${referralUrl}</p>
		<span><@s.text name="label.your_referral_link.desc"/></span>
	</div>
</div>
<div id="referralsBottom">
	<#if !referrals.empty>
		<table class="list">
			<tr>
				<th><@s.text name="label.company" /></th>
				<th><@s.text name="label.signupdate" /></th>
			</tr>
			<#list referrals as referral>
				<tr>
					<td>${action.getCompanyName(referral.referredTenant.id)}</td>
					<td>${action.formatDate(referral.signupDate, true)}</td>
				</tr>	
			</#list>
		</table>
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noreferrals" /></h2>
		</div>
	</#if>
</div>