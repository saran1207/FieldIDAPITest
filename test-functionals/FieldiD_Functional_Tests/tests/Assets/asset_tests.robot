*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/Assets/assets.robot
Library         Assets.create_asset_type_group_page.CreateAssetTypeGroupPage      WITH NAME       CreateAssetTypeGroupPage

Library           String
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto    
${PASSWORD}         temp123

*** Keywords ***
Verify Creation Of An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Page Should Contain     ${ASSET_TYPE_NAME}
    
Verify Creation Of An Asset
    [Arguments]     ${SERIAL_NUMBER}    ${RFID_NUMBER}
    Go To Page      SearchPage
    The Current Page Should Be      SearchPage
    Input Serial Number         ${SERIAL_NUMBER}
    Click Search Button
    Wait Until Page Contains        ${RFID_NUMBER}
   
*** Test Cases ***
Create Asset Type And Verify Creation
    Create An Asset Type    TestAssetType
    Verify Creation Of An Asset Type    TestAssetType

Create Asset And Verify Creation
    Go To New Asset From Dashboard
    Create An Asset     TestAsset   TEST123
    Verify Creation Of An Asset     TestAsset   TEST123    