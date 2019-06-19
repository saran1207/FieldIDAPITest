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
Library         Setup.Assets.delete_asset_type_page.DeleteAssetTypePage    WITH NAME       DeleteAssetTypePage
Library         Setup.Assets.asset_type_schedules_page.AssetTypeSchedulesPage    WITH NAME       AssetTypeSchedulesPage
Library         Setup.Assets.event_type_assocation_page.EventTypeAssocationPage    WITH NAME       EventTypeAssocationPage
Library         String



*** Variables ***

*** Keywords ***
Verify Creation Of An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Page Should Contain     ${ASSET_TYPE_NAME}
    
Go To An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Click Asset Type Link    ${ASSET_TYPE_NAME}

Delete Asset Type
    [Arguments]  ${ASSET_TYPE_NAME}
    Go To Page    ManageAssetTypesPage
    Click Asset Type Link    ${ASSET_TYPE_NAME}
    The Current Page Should Be    CreateAssetTypePage
    Click Delete Asset Type Button
    Wait Until Page Contains  Removal Details
    The Current Page Should Be    DeleteAssetTypePage
    Input Type Delete    DELETE
    Click Confirm Delete Asset Type Button 
    
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
    
Copy Asset Type 
    [Arguments]    ${assetType}
    Go To Page    ManageAssetTypesPage
    ${id}    Get Asset Type Id    ${assetType}
    Click Copy Asset Type Link    ${id}
    
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
    
Get Asset Type Id
    [Arguments]    ${assetTypeName}
    Go To Page    ManageAssetTypesPage
    The Current Page Should Be    ManageAssetTypesPage
    ${link}    Get Link From Name    ${assetTypeName}
    ${url}    Get Element Attribute    ${link}    attribute=href
    ${assetTypeId}    Fetch From Right    ${url}    =
    [return]    ${assetTypeId}
    
Schedule Recurring Event
    [Arguments]  ${eventType}
    Click Create New Rule Button
    Click Recurring Link
    Select Recurring Event Type Dropdown  ${eventType}
    Click Create Recurring Event Button
      
Go To Schedules Tab Of An Asset Type
    [Arguments]  ${assetTypeName}
    Go To Page  ManageAssetTypesPage
    Click Asset Type Link    ${assetTypeName}
    The Current Page Should Be    CreateAssetTypePage
    Click Schedules Link  
    The Current Page Should Be    AssetTypeSchedulesPage
    
Go To Events Tab Of An Asset Type
    [Arguments]  ${assetTypeName}
    Go To Page  ManageAssetTypesPage
    Click Asset Type Link    ${assetTypeName}
    The Current Page Should Be    CreateAssetTypePage
    Click Event Type Associations Link
    The Current Page Should Be    EventTypeAssocationPage
    
Allow Asset Linking For Asset Type
    [Arguments]  ${assetTypeName}
    Go To Page  ManageAssetTypesPage
    Click Asset Type Link    ${assetTypeName}
    The Current Page Should Be    CreateAssetTypePage
    Click More Information Link
    Click Allow Assset Linking Checkbox
    Click Save Button