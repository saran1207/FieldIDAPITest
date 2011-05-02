<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
</head>

<#include '_vendorinfo.ftl'>

<div id="mainContent">

    <table class="list" id="assetTable">
        <tr>
            <th><@s.text name="label.assettype" /></th>
            <th><@s.text name="label.quantity_pre_assigned" /></th>
            <th></th>
        </tr>

        <#list bulkRegisterData.items as item>
        <tr>
            <td>
                ${item.assetType.name}
            </td>
            <td>
                ${item.count}
            </td>
            <td>
                <a href="<@s.url action="bulkReg" vendorId="${uniqueID}" networkAssetTypeId="${item.assetType.id}"/>">
                    <@s.text name="label.registerasset" />
                </a>
            </td>
        </tr>
        </#list>
    </table>

</div>