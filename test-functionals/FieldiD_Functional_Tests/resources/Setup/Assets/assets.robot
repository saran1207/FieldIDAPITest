*** Settings ***
Resource        ${CURDIR}/../../../resources/Common/common.robot
Resource        ${CURDIR}/../../../resources/Dashboard/dashboard.robot
Library         New_Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
Library         Setup.Assets.manage_asset_type_groups_page.ManageAssetTypeGroupsPage     WITH NAME       ManageAssetTypeGroupsPage
Library         Setup.Assets.create_asset_type_group_page.CreateAssetTypeGroupPage      WITH NAME       CreateAssetTypeGroupPage
Library         Setup.Assets.manage_asset_types_page.ManageAssetTypesPage      WITH NAME       ManageAssetTypesPage
Library         Setup.Assets.create_asset_type_page.CreateAssetTypePage      WITH NAME       CreateAssetTypePage
Library         Setup.Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAddWithOrderPage
Library         Setup.Assets.edit_asset_type_group_page.EditAssetTypeGroupPage      WITH NAME       EditAssetTypeGroupPage
Library         Setup.Assets.delete_asset_types_groups_page.DeleteAssetTypeGroupPage      WITH NAME       DeletedAssetTypeGroupPage
Library         Setup.Assets.view_asset_types_group_page.ViewAssetTypeGroupPage      WITH NAME       ViewAssetTypeGroupPage
Library         String



*** Variables ***

*** Keywords ***
Create An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}  ${ASSET_TYPE_GROUP}=${EMPTY}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Click Add Button
    The Current Page Should Be      CreateAssetTypePage
    Input Asset Type Name       ${ASSET_TYPE_NAME}
    Select Asset Group Dropdown  ${ASSET_TYPE_GROUP}
    Click Save Button
    
Create An Asset Type Group
    [Arguments]     ${ASSET_TYPE_GROUP_NAME}
    Go To Page  ManageAssetTypeGroupsPage
    The Current Page Should Be      ManageAssetTypeGroupsPage
    Click Add Button
    The Current Page Should Be      CreateAssetTypeGroupPage
    Input Asset Type Group Name     ${ASSET_TYPE_GROUP_NAME}
    Click Save Button
   
Go To Page Asset Type Group
    Go To Asset Type Groups From Dashboard
    The Current Page Should Be      ManageAssetTypeGroupsPage
    
Go To Page Edit Asset Type Group
    [Arguments]    ${assetTypeGroup}
    ${id}    Get Asset Group Id    ${assetTypeGroup}
    EditAssetTypeGroupPage.Set Page URL    ${id}
    Click Edit Asset Group Link    ${id}
    
Go To Page Delete Asset Type Group
    [Arguments]    ${assetTypeGroup}
    ${id}    Get Asset Group Id    ${assetTypeGroup}
    DeletedAssetTypeGroupPage.Set Page URL    ${id}
    Click Delete Asset Group Link    ${id}
    
Go To Page View Asset Type Group
    [Arguments]    ${assetTypeGroup}
    ${id}    Get Asset Group Id    ${assetTypeGroup}
    ViewAssetTypeGroupPage.Set Page URL    ${id}
    Click View Asset Group Link    ${id}
    
Edit Asset Type Group
    [Arguments]    ${assetTypeGroup}
    The Current Page Should Be    EditAssetTypeGroupPage
    Input Asset Type Group Name    ${assetTypeGroup}
    Click Save Button
    
Delete Asset Type Group
    The Current Page Should Be    DeleteAssetTypeGroupPage
    DeletedAssetTypeGroupPage.Click Delete Button
    
Add Asset Type From Asset Type Group
    [Arguments]    ${ASSET_TYPE_NAME}
    The Current Page Should Be     ViewAssetTypeGroupPage
    Click Add Asset Link
    The Current Page Should Be    CreateAssetTypePage
    Input Asset Type Name       ${ASSET_TYPE_NAME}
    Click Save Button
    
Get Asset Group Id
    [Arguments]    ${groupName}
    Go To Page    ManageAssetTypeGroupsPage
    The Current Page Should Be    ManageAssetTypeGroupsPage
    ${link}    Get Link From Name    ${groupName}
    ${url}    Get Element Attribute    ${link}    attribute=href
    ${groupId}    Fetch From Right    ${url}    =
    [return]    ${groupId}