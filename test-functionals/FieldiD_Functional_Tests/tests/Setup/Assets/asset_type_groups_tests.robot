*** Settings ***

Resource        ${CURDIR}/../../../resources/Login/Login.robot
Resource        ${CURDIR}/../../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../../resources/Dashboard/dashboard.robot
Library           String
Library           DateTime
Library         Setup.Assets.edit_asset_type_group_page.EditAssetTypeGroupPage      WITH NAME       EditAssetTypeGroupPage
Library         Setup.Assets.delete_asset_types_groups_page.DeleteAssetTypeGroupPage      WITH NAME       DeleteAssetTypeGroupPage
Library         Setup.Assets.manage_asset_type_groups_page.ManageAssetTypeGroupsPage    WITH NAME      ManageAssetTypeGroupsPage

Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto       
${PASSWORD}         temp123
${USERFULLNAME}     Test Automation

*** Keywords ***

Verify Creation Of An Asset
    [Arguments]     ${SERIAL_NUMBER}    ${RFID_NUMBER}
    Go To Page      SearchPage
    The Current Page Should Be      SearchPage
    Input Serial Number         ${SERIAL_NUMBER}
    Click Search Button
    Wait Until Page Contains        ${RFID_NUMBER}
    Page Should Contain         ${RFID_NUMBER}

Verify Creation Of An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Page Should Contain     ${ASSET_TYPE_NAME}
    
Verify Creation Of An Asset Type Group
    [Arguments]     ${ASSET_TYPE_GROUP_NAME}
    Go To Page      ManageAssetTypeGroupsPage
    The Current Page Should Be      ManageAssetTypeGroupsPage
    Page Should Contain     ${ASSET_TYPE_GROUP_NAME}
    
Verify Deletion Of An Asset Type Group
    [Arguments]     ${ASSET_TYPE_GROUP_NAME}
    Go To Page      ManageAssetTypeGroupsPage
    The Current Page Should Be      ManageAssetTypeGroupsPage
    Page Should Not Contain     ${ASSET_TYPE_GROUP_NAME}   
     
*** Test Cases ***
Create Asset Type Group With No Name Test
    [Tags]    C1843  C1709
    Go To Page Asset Type Group
    Create An Asset Type Group      ${EMPTY}
    ${NAME_REQUIRED_ERROR_MSG}=   CreateAssetTypeGroupPage.Get Name Required Error Msg
    Page Should Contain      ${NAME_REQUIRED_ERROR_MSG}
    
Create Asset Type Group And Verify Creation Test
    [Tags]  C1703  Smoke
    ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    
Create Duplicate Asset Type Group Test
    [Tags]  C1844
     ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    Create An Asset Type Group      ${assetGroup}
    ${UNIQUE_NAME_ERROR_MSG}=   CreateAssetTypeGroupPage.Get Unique Name Error Msg
    Page Should Contain      ${UNIQUE_NAME_ERROR_MSG}
    
Edit Asset Type Group Test
    [Tags]  C1704  Smoke
    ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    Go To Page Edit Asset Type Group     ${assetGroup}
    Edit Asset Type Group     ${assetGroup}+editted
    Verify Creation Of An Asset Type Group       ${assetGroup}+editted
    
Deleted Asset Type Group With 1 Asset Type Test
    [Tags]  C1771  
   ${assetGroup}    Generate Random String  5
   ${assetType}     Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    Create An Asset Type  ${assetType}   ${assetGroup}
    Go To Page Delete Asset Type Group     ${assetGroup}
    The Current Page Should Be    DeleteAssetTypeGroupPage
    ${num_of_asset_types}  Get Num Of Asset Types Attached
    Should Be Equal   ${num_of_asset_types}    1 Asset Types being detached from this group
    Page Should Contain   Saved reports and searches to be deleted
    Delete Asset Type Group
    Verify Deletion Of An Asset Type Group       ${assetGroup}
    
Create Asset Type From Asset Type Group Test
    [Tags]  C1723  	C1724  
   ${assetGroup}    Generate Random String  5
   ${assetType}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    Go To Page View Asset Type Group  ${assetGroup}
    Add Asset Type From Asset Type Group  ${assetType} 
    Verify Creation Of An Asset Type   ${assetType} 
    Go To Page      ManageAssetTypeGroupsPage
    Go To Page View Asset Type Group  ${assetGroup}
    Page Should Contain     ${assetType}
    
Asset Type Group List In Asset Type Page Test
    [Tags]  C1705  
    Go to Page  ManageAssetTypeGroupsPage
    The Current Page Should Be  ManageAssetTypeGroupsPage 
    ${ASSET_TYPE_GROUP_LIST}  Get Asset Group List  
    Go to Page  CreateAssetTypePage  
    The Current Page Should Be  CreateAssetTypePage 
    ${ASSET_TYPE_GROUP_LIST_FROM_ASSET_TYPE_PAGE}  Get Asset Group Dropdown List
    Should Be Equal    ${ASSET_TYPE_GROUP_LIST}   ${ASSET_TYPE_GROUP_LIST_FROM_ASSET_TYPE_PAGE}
    
List View for Asset Types Groups Test
    [Tags]  C1708  
    ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup} 
    ${currentDateTime}  Get Current Date    result_format=%m/%d/%y
    ${assetGroupId}  Get Asset Group Id  ${assetGroup}
    ${createdByUsername}  Get Create By Username  ${assetGroupId}
    ${createdOnDate}  Get Create On Date  ${assetGroupId}
    ${modifiedByUsername}  Get Modified By Username  ${assetGroupId}
    ${modifiedOnDate}  Get Last Modified Date  ${assetGroupId}
    Should Be Equal   ${createdByUsername}   ${USERFULLNAME} 
    Should Contain   ${createdOnDate}  ${currentDateTime}  
    Should Be Equal   ${modifiedByUsername}   ${USERFULLNAME} 
    Should Contain    ${modifiedOnDate}  ${currentDateTime}