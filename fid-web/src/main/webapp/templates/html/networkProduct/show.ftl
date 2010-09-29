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
                ${product.serialNumber}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.rfidnumber"/>
            </td>
            <td class="value">
                ${product.rfidNumber!}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.producttype"/>
            </td>
            <td class="value">
                ${product.type.name}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.productstatus"/>
            </td>
            <td class="value">
                ${(product.productStatus.name)!}
            </td>
        </tr>
        <tr>
            <td>
                <@s.text name="label.identified"/>
            </td>
            <td class="value">
                ${action.formatDate(product.identified, false)}
            </td>
        </tr>
        <#if product.type.hasManufactureCertificate >
        <tr>
            <td>
                <@s.text name="label.manufacturecertificate"/>
            </td>
            <td class="value">
                <img src="<@s.url value="/images/pdf_small.gif"/>"/>
                <a href="<@s.url action="downloadSafetyNetworkManufacturerCert" namespace="/file" uniqueID="${product.uniqueID}" />" target="_blank" >
                    <@s.text name="label.downloadnow"/>
                </a>
            </td>
        </tr>
        </#if>
    </table>

	<#if !product.orderedInfoOptionList.isEmpty() >

        <div class="productInformationSection">
            ${product.type.name} <@s.text name="label.attributes"/>
        </div>

        <table class="productInformation">
            <#list product.orderedInfoOptionList as infoOption >
				<tr>
					<td>${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </td>
					<td class="value">${infoOption.name}</td>
				</tr>
			</#list>
        </table>
    </#if>
    </div>
    <#if product.type.imageName?exists >
        <div class="rightProduct">
            <img src="<@s.url action="downloadProductTypeImageSafetyNetwork" namespace="/file" uniqueID="${product.type.uniqueID}" networkId="${product.id}" />" alt="<@s.text name="label.productimage"/>" width="300"/>
        </div>
    </#if>

    <div style="clear: both;"></div>

    <#if product.type.instructions?exists && product.type.instructions?length gt 0 >
        <div class="productInformationSection">
            <@s.text name="label.warnings"/>
        </div>
        <div class="productInfoText">
            ${product.type.warnings!}
        </div>
    </#if>

    <#if product.type.instructions?exists && product.type.instructions?length gt 0 >
        <div class="productInformationSection">
            <@s.text name="label.instructions"/>
        </div>
        <div class="productInfoText">
            ${product.type.instructions!}
        </div>
    </#if>

	<#if (product.type.cautions?exists && product.type.cautions?length gt 0)
			|| (!product.type.attachments.isEmpty()) || (!productAttachments.isEmpty()) >

        <div class="productInformationSection">
            <@s.text name="label.additionalinformation"/>
        </div>

        <#if product.type.cautions?exists && product.type.cautions?length gt 0 >
            <p>
                <a href="${product.type.cautions}" target="_blank" ><@s.text name="label.morefromtheweb"/></a>
            </p>
        </#if>

        <#if !productAttachments.isEmpty() >
            <#assign downloadAction="downloadProductAttachedFileSafetyNetwork"/>
            <#assign attachments=productAttachments />
            <#assign attachmentID=product.uniqueID/>
            <#include "_attachedFilesDisplay.ftl"/>
        </#if>
    
        <#if !product.type.attachments.isEmpty() >
            <#assign downloadAction="downloadProductTypeAttachedFileSafetyNetwork"/>
            <#assign attachments=product.type.attachments />
            <#assign attachmentID=product.type.uniqueID/>
            <#include "_attachedFilesDisplay.ftl"/>
        </#if>

    </#if>


</div>