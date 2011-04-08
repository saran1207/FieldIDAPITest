<#assign lightBoxProperties=":: :: width: 470, height: 300, menubar: true, topclose: false, autosize: false, scrolling: true" />

<script type="text/javascript">
	recImageUrl = '<@s.url value="/images/rec.png"/>';
	defImageUrl = '<@s.url value="/images/def.png"/>';
	recSelectImageUrl = '<@s.url value="/images/rec-plus.png"/>';
	defSelectImageUrl = '<@s.url value="/images/def-plus.png"/>';
</script>

<script src="/fieldid/javascript/lightview.js" type="text/javascript"></script>
<script type="text/javascript">
    var currentSignatureCriteriaId = 0;
    var currentSignatureCriteriaCount = 0;
    var signatureClearUrl = '<@s.url action="clearSignature.action" namespace="/ajax"/>';
    var submitSignatureUrl = '<@s.url action="storeSignature.action" namespace="/ajax"/>';

    function initializeSignatureWindowOpenedHook() {
        document.observe('lightview:opened', storeCriteriaId);
    }

    function getCurrentSignatureCriteriaId() {
        return currentSignatureCriteriaId;
    }

    function storeCriteriaId(e) {
        var attrs = e.target.attributes;
        for (var i =0; i < attrs.length; i++) {
            if (attrs[i].name == "criteriaid") {
                currentSignatureCriteriaId = attrs[i].value;
            }
            if (attrs[i].name == "criteriacount") {
                currentSignatureCriteriaCount = attrs[i].value;
            }
        }
    }

    function storeSignature(data) {
        var signatureParams = {
            pngData: data,
            criteriaId: currentSignatureCriteriaId,
            criteriaCount: currentSignatureCriteriaCount,
        };

        getResponse(submitSignatureUrl, "post", signatureParams);
    }

    function clearSignature(criteriaId, criteriaCount) {
        currentSignatureCriteriaId = criteriaId;
        getResponse(signatureClearUrl, "post", { criteriaId: criteriaId, criteriaCount: criteriaCount });
    }

    function performThumbnailRefresh(newThumbnailSection) {
        Lightview.hide();
        $('signatureCriteria'+currentSignatureCriteriaId).replace(newThumbnailSection);
    }

    Event.observe(window, 'load', initializeSignatureWindowOpenedHook);
</script>


<#list section.criteria as criteria >
	<#if !criteria.retired >				

		<div class="infoSet" >

			<label class="label eventFormLabel">${criteria.displayName}</label>

            <#if criteriaResults?exists && criteriaResults[criteriaCount]?exists>
                <#assign criteriaResult = criteriaResults[criteriaCount]>
            </#if>

            <#if (criteriaResult.id)?exists >
                <@s.hidden id="criteriaResultId_${criteria.id}" name="criteriaResults[${criteriaCount}].id" value="${criteriaResult.id}" />
            </#if>

            <@s.hidden id="criteriaResultType_${criteria.id}" name="criteriaResults[${criteriaCount}].type" value="${criteria.criteriaType.name()}" />
            <@s.hidden id="criteriaResultCriteria_${criteria.id}" name="criteriaResults[${criteriaCount}].criteriaId" value="${criteria.id}" />

            <#if criteria.oneClickCriteria>
                <#include '_oneClickCriteriaResultEdit.ftl'>
            <#elseif criteria.textFieldCriteria>
                <#include '_textFieldCriteriaResultEdit.ftl'>
            <#elseif criteria.selectCriteria>
                <#include '_selectCriteriaResultEdit.ftl'>
            <#elseif criteria.comboBoxCriteria>
                <#include '_comboBoxCriteriaResultEdit.ftl'>
            <#elseif criteria.unitOfMeasureCriteria>
                <#include '_unitOfMeasureCriteriaResultEdit.ftl'>
            <#elseif criteria.signatureCriteria>
                <#assign criteriaId = criteria.id />
                <#--<#assign temporarySignatureFile = false />-->
                <#include '_signatureCriteriaResultEdit.ftl'>
            </#if>

			<span class="recDefButtons">
				<#assign recCount=(action.countRecommendations(criteriaCount))!0 />
				<#assign defCount=(action.countDeficiencies(criteriaCount))!0 />

				<a id='recButton_${criteria.id}' href='#rec_${criteria.id}' title='<@s.text name="label.recommendations"/>${lightBoxProperties}' class='lightview' >
					<img id='recImage_${criteria.id}' src="<@s.url value="/images/rec.png"/>" />
				</a>

				<a id='defButton_${criteria.id}' href='#def_${criteria.id}' title='<@s.text name="label.deficiencies"/>${lightBoxProperties}' class='lightview' >
					<img id='defImage_${criteria.id}' src="<@s.url value="/images/def.png"/>" />
				</a>
			</span>
			<#include "../observationsCrud/_recSelect.ftl"/>
			<#include "../observationsCrud/_defSelect.ftl"/>

        </div>

		<script type="text/javascript">
			<#-- setup the inital rec/def counts and set the initial images -->
			recCounts[${criteria.id}] = ${recCount};
			defCounts[${criteria.id}] = ${defCount};
			updateRecommendationImage(${criteria.id});
			updateDeficiencyImage(${criteria.id});
		</script>

		<#assign criteriaCount=criteriaCount+1 />
	</#if>
</#list>
