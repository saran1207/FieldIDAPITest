<head>
	<@n4.includeStyle type="page" href="refer"/>
</head>
${action.setPageType('my_account', 'refer')!}

<h2 id="referralTitle" class="sectionTitle"><@s.text name="label.refer_fieldid"/></h2>

<div id="referralsTop">
	<div id="referralInstructions">
		<span id="referralMainText"><@s.text name="label.referral_text"/></span>
		
		<img src="images/mail-send.png" />
		<div class="referralStep">
			<h4><@s.text name="label.referral_step1.title"/></h4>
			<div class="referralSubStep">	
				<span class="subStepTitle"><@s.text name="label.referral_step1_1.title"/></span>
				<span>
					<@s.url id="inviteUrl" action="invite"/>
					<@s.text name="label.referral_step1_1.desc">
						<@s.param>${inviteUrl}</@s.param>
					</@s.text>
				</span>
			</div>
		
			<div class="referralSubStep">
				<span class="subStepTitle"><@s.text name="label.referral_step1_2.title"/></span>
				<span><@s.text name="label.referral_step1_2.desc"/></span>
			</div>
		</div>
		
		<img src="images/mail-receive.png" />
		<div class="referralStep">
			<h4><@s.text name="label.referral_step2.title"/></h4>
			<div class="referralSubStep">
				<span><@s.text name="label.referral_step2.desc"/></span>
			</div>
		</div>
	</div>
	
	<div id="referralLinkBox">
		<h2><@s.text name="label.your_referral_link"/></h2>
		<p onClick="this.select();">${referralUrl}</p>
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