*** Settings ***
Documentation     This test suite verifies the functionailty of the Asset Type

Resource        ${CURDIR}/../../../resources/Login/Login.robot
Resource        ${CURDIR}/../../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../../resources/New_Assets/new_assets.robot
Library           String
Library           DateTime
Library         Setup.Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAddWithOrderPage
Library         Setup.Assets.create_asset_type_page.CreateAssetTypePage      WITH NAME       CreateAssetTypePage
Library         New_Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto       
${PASSWORD}         temp123
${USERFULLNAME}     Test Automation

*** Keywords ***
Create An Asset Type With All Attributes
    [Arguments]     ${ASSET_TYPE_NAME}  ${ASSET_TYPE_GROUP}=${EMPTY}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Click Add Button
    The Current Page Should Be      CreateAssetTypePage
    Input Asset Type Name       ${ASSET_TYPE_NAME}
    Select Asset Group Dropdown  ${ASSET_TYPE_GROUP}
    Click Add Attribute Button
    Input Attribute Name    Text Field    1
    Select Attribute Datatype Dropdown    Text Field  1
    Click Add Attribute Button
    Input Attribute Name    Select Box   2
    Select Attribute Datatype Dropdown    Select Box   2
    Input Select Comb Box Values    select Option1    2
    Click Add Attribute Button
    Input Attribute Name    Combo Box   3
    Select Attribute Datatype Dropdown    Combo Box   3
    Input Select Comb Box Values    combo Option1    3
    Click Add Attribute Button
    Input Attribute Name    Unit Of Measure   4
    Select Attribute Datatype Dropdown    Unit Of Measure   4
    Select Unit Of Measure Dropdown    Inches    4
    Click Add Attribute Button
    Input Attribute Name    Date Field   5
    Select Attribute Datatype Dropdown    Date Field   5
    Click Save Button
    
Verify Creation Of An Asset Type
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Page Should Contain     ${ASSET_TYPE_NAME}
    
Enter Attributes Value For Asset
    Input Text Field Attribute    Text Field Value    1
    Select Select Box Value    select Option1    2
    Select Combo Box Value    combo Option1    3
    Input Unit Of Measure    5    4
    Input Date Field Attribute    05/01/19    5

*** Test Cases ***
Create Asset Type And Verify Creation
    [Tags]  Smoke
    Create An Asset Type  TestAssetType
    Verify Creation Of An Asset Type    TestAssetType
    [Teardown]  Delete Asset Type  TestAssetType
    
Create Asset Type With Attributes And Verify In New Asset
    [Tags]  C1731  C1732
    ${assetType}    Generate Random String  5
    Create An Asset Type With All Attributes    ${assetType}
    Verify Creation Of An Asset Type    ${assetType}
    ${SERIAL_NUMBER}    Generate Random String  5
    Go To New Asset From Dashboard
    The Current Page Should Be   NewAddWithOrderPage
    Click Add Tab
    The Current Page Should Be      CreateAssetPage
    Select Asset Type  ${assetType}
    Input Asset Serial Number       ${SERIAL_NUMBER}
    Enter Attributes Value For Asset
    Click Save Button
    Go To Asset View Page      ${SERIAL_NUMBER}
    Page should Contain  Text Field Value
    Page should Contain  select Option1
    Page should Contain   combo Option1
    Page should Contain   5 in
    Page should Contain   05/01/19
    [Teardown]  Delete Asset Type  ${assetType}