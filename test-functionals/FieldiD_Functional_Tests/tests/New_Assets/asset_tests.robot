*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../resources/New_Assets/new_assets.robot
Library         Setup.Assets.create_asset_type_group_page.CreateAssetTypeGroupPage      WITH NAME       CreateAssetTypeGroupPage
Library          DateTime
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
    [Arguments]     ${SERIAL_NUMBER}
    Go To Page      SearchPage
    The Current Page Should Be      SearchPage
    Input Serial Number         ${SERIAL_NUMBER}
    Click Search Button
    ${currentDateTime}  Get Current Date    result_format=%m/%d/%y
    Wait Until Page Contains       ${currentDateTime}
   
*** Test Cases ***
Create Asset Type And Verify Creation
    [Tags]  Smoke
    Create An Asset Type    TestAssetType
    Verify Creation Of An Asset Type    TestAssetType

Create Asset And Verify Creation
    [Tags]  Smoke
    ${SERIAL_NUMBER}    Generate Random String  5
    Go To New Asset From Dashboard
    Create An Asset     ${SERIAL_NUMBER}   TEST123
    Verify Creation Of An Asset     ${SERIAL_NUMBER}    