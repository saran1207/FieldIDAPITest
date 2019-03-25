*** Settings ***

Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Assets/assets.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library           String
Library         Assets.edit_asset_type_group_page.EditAssetTypeGroupPage      WITH NAME       EditAssetTypeGroupPage
Library         Assets.delete_asset_types_groups_page.DeleteAssetTypeGroupPage      WITH NAME       DeleteAssetTypeGroupPage

Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto       
${PASSWORD}         temp123

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
    
Verify Delete Asset Type Group Page
     Page Should Contain   0 Asset Types being detached from this group
     Page Should Contain   0 Saved reports and searches to be deleted 
     
*** Test Cases ***
Create Asset Type Group With No Name Test
    [Tags]    C1843  Regression
    Go To Page Asset Type Group 
    Create An Asset Type Group      ${EMPTY}
    ${NAME_REQUIRED_ERROR_MSG}=   CreateAssetTypeGroupPage.Get Name Required Error Msg
    Page Should Contain      ${NAME_REQUIRED_ERROR_MSG}
    
Create Asset Type Group And Verify Creation Test
    [Tags]  C1703  Regression  Smoke
    ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    
Create Duplicate Asset Type Group Test
    [Tags]  C1844  Regression
     ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    Create An Asset Type Group      ${assetGroup}
    ${UNIQUE_NAME_ERROR_MSG}=   CreateAssetTypeGroupPage.Get Unique Name Error Msg
    Page Should Contain      ${UNIQUE_NAME_ERROR_MSG}
    
Edit Asset Type Group Test
    [Tags]  C1704  Regression
    ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    Go To Page Edit Asset Type Group     ${assetGroup}
    The Current Page Should Be    EditAssetTypeGroupPage
    Edit Asset Type Group     ${assetGroup}+editted
    Verify Creation Of An Asset Type Group       ${assetGroup}+editted
    
Deleted Asset Type Group Test  
    [Tags]  C1771  Regression  Smoke
    ${assetGroup}    Generate Random String  5
    Create An Asset Type Group      ${assetGroup}
    Verify Creation Of An Asset Type Group      ${assetGroup}
    Go To Page Delete Asset Type Group     ${assetGroup}
    The Current Page Should Be    DeleteAssetTypeGroupPage
    Delete Asset Type Group
    Verify Deletion Of An Asset Type Group       ${assetGroup}


    
    
    