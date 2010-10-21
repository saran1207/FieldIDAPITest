<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
    <@n4.includeStyle href="safetyNetworkProduct" type="page"/>
</head>

<#include '../vendor/_vendorinfo.ftl'/>

<div id="mainContent">

    <#include '_topLayout.ftl'/>

    <ul class="options" id="innerList">
        <li class="selected">
            <@s.text name="nav.traceability"/>
        </li>
        <li>
            <a href="<@s.url action="networkProductInspections" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.inspections"/>
            </a>
        </li>
        <li>
            <a href="<@s.url action="networkInspectionScheduleList" namespace="/" uniqueID="${uniqueID}"/>">
                <@s.text name="nav.schedules"/>
            </a>
        </li>
    </ul>

    <div style="clear: both;"></div>

    <div class="leftProduct">

    <div class="productInformationSection">
        <@s.text name="label.productsummary"/>
    </div>

    <table class="productInformation">
        <tr>
            <td>
                <@s.text name="${sessionUser.serialNumberLabel}"/>
            </td>
            <td class="value">
                ${asset.serialNumber}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.rfidnumber"/>
            </td>
            <td class="value">
                ${asset.rfidNumber!}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.producttype"/>
            </td>
            <td class="value">
                ${asset.type.name}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.productstatus"/>
            </td>
            <td class="value">
                ${(asset.assetStatus.name)!}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.identified"/>
            </td>
            <td class="value">
                ${action.formatDate(asset.identified, false)}
            </td>
        </tr>
        <#if asset.type.hasManufactureCertificate >
        <tr>
            <td>
                <@s.text name="label.manufacturecertificate"/>
            </td>
            <td class="value">
                <img src="<@s.url value="/images/pdf_small.gif"/>"/>
                <a href="<@s.url action="downloadSafetyNetworkManufacturerCert" namespace="/file" uniqueID="${asset.uniqueID}" />" target="_blank" >
                    <@s.text name="label.downloadnow"/>
                </a>
            </td>
        </tr>
        </#if>
    </table>

	<#if !asset.orderedInfoOptionList.isEmpty() >

        <div class="productInformationSection">
            ${asset.type.name} <@s.text name="label.attributes"/>
        </div>

        <table class="productInformation">
            <#list asset.orderedInfoOptionList as infoOption >
				<tr>
					<td>${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </td>
					<td class="value">${infoOption.name}</td>
				</tr>
			</#list>
        </table>
    </#if>
    </div>
    <#if asset.type.imageName?exists >
        <div class="rightProduct">
            <img src="<@s.url action="downloadProductTypeImageSafetyNetwork" namespace="/file" uniqueID="${asset.type.uniqueID}" networkId="${asset.id}" />" width="300"/>
        </div>
    </#if>

    <div style="clear: both;"></div>

    <#if asset.type.instructions?exists && asset.type.instructions?length gt 0 >
        <div class="productInformationSection">
            <@s.text name="label.warnings"/>
        </div>
        <div class="productInfoText">
            ${asset.type.warnings!}
        </div>
    </#if>

    <#if asset.type.instructions?exists && asset.type.instructions?length gt 0 >
        <div class="productInformationSection">
            <@s.text name="label.instructions"/>
        </div>
        <div class="productInfoText">
            ${asset.type.instructions!}
        </div>
    </#if>

	<#if (asset.type.cautions?exists && asset.type.cautions?length gt 0)
			|| (!asset.type.attachments.isEmpty()) || (!productAttachments.isEmpty()) >

        <div class="productInformationSection">
            <@s.text name="label.additionalinformation"/>

            <div class="moreFromTheWeb">
                <#if asset.type.cautions?exists && asset.type.cautions?length gt 0 >
                    <p>
                        <a href="${asset.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/> &#187;</a>
                    </p>
                </#if>
            </div>
        </div>


        <#if !productAttachments.isEmpty() >
            <#assign downloadAction="downloadProductAttachedFileSafetyNetwork"/>
            <#assign attachments=productAttachments />
            <#assign attachmentID=asset.uniqueID/>
            <#include "_attachedFilesDisplay.ftl"/>
        </#if>
    
        <#if !asset.type.attachments.isEmpty() >
            <#assign downloadAction="downloadProductTypeAttachedFileSafetyNetwork"/>
            <#assign attachments=asset.type.attachments />
            <#assign attachmentID=asset.type.uniqueID/>
            <#include "_attachedFilesDisplay.ftl"/>
        </#if>

    </#if>


</div>