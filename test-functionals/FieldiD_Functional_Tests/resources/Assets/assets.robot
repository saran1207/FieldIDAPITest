*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library         Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
Library         Assets.manage_asset_type_groups_page.ManageAssetTypeGroupsPage      WITH NAME       ManageAssetTypeGroupsPage
Library         Assets.create_asset_type_group_page.CreateAssetTypeGroupPage      WITH NAME       CreateAssetTypeGroupPage
Library         Assets.manage_asset_types_page.ManageAssetTypesPage      WITH NAME       ManageAssetTypesPage
Library         Assets.create_asset_type_page.CreateAssetTypePage      WITH NAME       CreateAssetTypePage
Library         Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAddWithOrderPage
Library         Assets.edit_asset_type_group_page.EditAssetTypeGroupPage      WITH NAME       EditAssetTypeGroupPage
Library         Assets.delete_asset_types_groups_page.DeleteAssetTypeGroupPage      WITH NAME       DeletedAssetTypeGroupPage
Library         String



*** Variables ***

*** Keywords ***
Create An Asset
    [Arguments]     ${SERIAL_NUMBER}        ${RFID_NUMBER}
    NewAddWithOrderPage.Click Add Tab
    The Current Page Should Be      CreateAssetPage
    Input Asset Serial Number       ${SERIAL_NUMBER}
    Input Rfid Number       ${RFID_NUMBER}
    CreateAssetPage.Click Save Button

Create An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    ManageAssetTypesPage.Click Add Button
    The Current Page Should Be      CreateAssetTypePage
    Input Asset Type Name       ${ASSET_TYPE_NAME}
    CreateAssetTypePage.Click Save Button

    
Create An Asset Type Group
    [Arguments]     ${ASSET_TYPE_GROUP_NAME}
    Go To Page  ManageAssetTypeGroupsPage
    The Current Page Should Be      ManageAssetTypeGroupsPage
    ManageAssetTypeGroupsPage.Click Add Button
    The Current Page Should Be      CreateAssetTypeGroupPage
    CreateAssetTypeGroupPage.Input Asset Type Group Name     ${ASSET_TYPE_GROUP_NAME}
    CreateAssetTypeGroupPage.Click Save Button
   
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
    
Edit Asset Type Group
    [Arguments]    ${assetTypeGroup}
    EditAssetTypeGroupPage.Input Asset Type Group Name    ${assetTypeGroup}
    EditAssetTypeGroupPage.Click Save Button
    
Delete Asset Type Group
    DeletedAssetTypeGroupPage.Click Delete Button
    
Get Asset Group Id
    [Arguments]    ${groupName}
    Go To Page    ManageAssetTypeGroupsPage
    ${link}    ManageAssetTypeGroupsPage.Get Link From Name    ${groupName}
    ${url}    Get Element Attribute    ${link}    attribute=href
    ${groupId}    Fetch From Right    ${url}    =
    [return]    ${groupId}