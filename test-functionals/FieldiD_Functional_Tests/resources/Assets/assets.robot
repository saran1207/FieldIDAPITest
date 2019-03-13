*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library         Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
Library         Assets.manage_asset_type_groups_page.ManageAssetTypeGroupsPage      WITH NAME       ManageAssetTypeGroupsPage
Library         Assets.add_asset_type_group_page.AddAssetTypeGroupPage      WITH NAME       AddAssetTypeGroupPage
Library         Assets.manage_asset_types_page.ManageAssetTypesPage      WITH NAME       ManageAssetTypesPage
Library         Assets.add_asset_type_page.AddAssetTypePage      WITH NAME       AddAssetTypePage
Library         Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAddWithOrderPage
Library         Assets.edit_asset_type_group_page.EditAssetTypeGroupPage      WITH NAME       EditAssetTypeGroupPage
Library         Assets.delete_asset_types_groups_page.DeleteAssetTypeGroupPage      WITH NAME       DeletedAssetTypeGroupPage
Library         String



*** Variables ***

*** Keywords ***
Create An Asset
    [Arguments]     ${SERIAL_NUMBER}        ${RFID_NUMBER}
   # Go To Page      CreateAssetPage
    NewAddWithOrderPage.Click Add Tab
    The Current Page Should Be      CreateAssetPage
    Input Asset Serial Number       ${SERIAL_NUMBER}
    Input Rfid Number       ${RFID_NUMBER}
    CreateAssetPage.Click Save Button

Verify Creation Of An Asset
    [Arguments]     ${SERIAL_NUMBER}    ${RFID_NUMBER}
    Go To Page      SearchPage
    The Current Page Should Be      SearchPage
    Input Serial Number         ${SERIAL_NUMBER}
    Click Search Button
    Wait Until Page Contains        ${RFID_NUMBER}
    Page Should Contain         ${RFID_NUMBER}

Create An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    ManageAssetTypesPage.Click Add Button
    The Current Page Should Be      AddAssetTypePage
    Input Asset Type Name       ${ASSET_TYPE_NAME}
    AddAssetTypePage.Click Save Button

Verify Creation Of An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Page Should Contain     ${ASSET_TYPE_NAME}
    
Create An Asset Type Group
    [Arguments]     ${ASSET_TYPE_GROUP_NAME}
    Go To Page  ManageAssetTypeGroupsPage
    The Current Page Should Be      ManageAssetTypeGroupsPage
    ManageAssetTypeGroupsPage.Click Add Button
    The Current Page Should Be      AddAssetTypeGroupPage
    AddAssetTypeGroupPage.Input Asset Type Group Name     ${ASSET_TYPE_GROUP_NAME}
    AddAssetTypeGroupPage.Click Save Button

Verify Creation Of An Asset Type Group
    [Arguments]     ${ASSET_TYPE_GROUP_NAME}
    Go To Page      ManageAssetTypeGroupsPage
    The Current Page Should Be      ManageAssetTypeGroupsPage
    Page Should Contain     ${ASSET_TYPE_GROUP_NAME}
    
Go To Asset Type Group Page
    Go To Asset Type Groups From Dashboard
    The Current Page Should Be      ManageAssetTypeGroupsPage
    
Go To Edit Asset Type Group
    [Arguments]    ${assetTypeGroup}
    ${id}    Get Asset Group Id    ${assetTypeGroup}
    EditAssetTypeGroupPage.Set Page URL    ${id}
    Click Edit Asset Group Link    ${id}
    
Go To Delete Asset Type Group
    [Arguments]    ${assetTypeGroup}
    ${id}    Get Asset Group Id    ${assetTypeGroup}
    DeletedAssetTypeGroupPage.Set Page URL    ${id}
    Click Delete Asset Group Link    ${id}
    
Edit Asset Type Group
    [Arguments]    ${assetTypeGroup}
    EditAssetTypeGroupPage.Input Asset Type Group Name    ${assetTypeGroup}
    EditAssetTypeGroupPage.Click Save Button

Verify Deletion Of An Asset Type Group
    [Arguments]     ${ASSET_TYPE_GROUP_NAME}
    Go To Page      ManageAssetTypeGroupsPage
    The Current Page Should Be      ManageAssetTypeGroupsPage
    Page Should Not Contain     ${ASSET_TYPE_GROUP_NAME}
    
Delete Asset Type Group
    DeletedAssetTypeGroupPage.Click Delete Button
    
Get Asset Group Id
    [Arguments]    ${groupName}
    Go To Page    ManageAssetTypeGroupsPage
    ${link}    ManageAssetTypeGroupsPage.Get Link From Name    ${groupName}
    ${url}    Get Element Attribute    ${link}    attribute=href
    ${groupId}    Fetch From Right    ${url}    =
    [return]    ${groupId}