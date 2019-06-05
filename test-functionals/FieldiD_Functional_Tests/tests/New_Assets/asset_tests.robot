*** Settings ***
Documentation     This test suite verifies the functionailty of the Assets

Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../resources/New_Assets/new_assets.robot
Resource        ${CURDIR}/../../resources/Search/search_assets.robot
Library         Setup.Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAssetWithOrderPage
Library         Setup.Assets.create_asset_type_group_page.CreateAssetTypeGroupPage      WITH NAME       CreateAssetTypeGroupPage
Library         New_Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         Menu.common_menu_bar.CommonMenuBar  WITH NAME  CommonMenuBar
Library         Setup.Assets.create_asset_type_page.CreateAssetTypePage      WITH NAME       CreateAssetTypePage
Library         New_Assets.asset_summary_page.AssetSummaryPage        WITH NAME       AssetSummaryPage 
Library          DateTime
Library           String
Library     Selenium2Library
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto    
${PASSWORD}         temp123
${ASSETTYPE}        Asset Type 3

*** Keywords ***
Enter Attributes Value For Asset
    Input Text Field Attribute    Text Field Value    1
    Select Select Box Value    select Option1    2
    Select Combo Box Value    combo Option1    3
    Input Unit Of Measure    5    4
    Input Date Field Attribute    05/01/19    5
    
Link New Asset To An Asset
    [Arguments]  ${assetToBeLinked}
    Click Link New Asset Link
    Set Selenium Speed    0.5s
    Input And Select Linked Asset Name    ${assetToBeLinked}
    Set Selenium Speed    0s
    Click Add Linked Asset Button
   
*** Test Cases ***
Create Asset And Verify Creation
    [Tags]  Smoke
    ${SERIAL_NUMBER}    Generate Random String  5
    Go To New Asset From Dashboard
    Create An Asset     ${SERIAL_NUMBER}   TEST123  Fire Extinguisher
    Verify Creation Of An Asset     ${SERIAL_NUMBER}  
    
Create Asset Type With Attributes And Verify
    [Tags]  C1732
    ${SERIAL_NUMBER}    Generate Random String  5
    Go To New Asset From Dashboard
    The Current Page Should Be   NewAssetWithOrderPage
    Click Add Tab
    The Current Page Should Be      CreateAssetPage
    Select Asset Type  ${ASSETTYPE}
    Input Asset Serial Number       ${SERIAL_NUMBER}
    Enter Attributes Value For Asset
    Click Save Button
    Go To Asset View Page      ${SERIAL_NUMBER}
    Page should Contain  Text Field Value
    Page should Contain  select Option1
    Page should Contain   combo Option1
    Page should Contain   5 in
    Page should Contain   05/01/19 
    
Verify Allow Asset Linking
    [Tags]  C1734
     ${assetType}    Generate Random String  5
     Create An Asset Type  ${assetType}
     Allow Asset Linking For Asset Type    ${assetType}
     ${SERIAL_NUMBER1}    Generate Random String  5
     ${SERIAL_NUMBER2}    Generate Random String  5
     Go To New Asset From Dashboard
     Create An Asset     ${SERIAL_NUMBER1}   ${EMPTY}  ${assetType}
     Go To New Asset From Dashboard
     Create An Asset     ${SERIAL_NUMBER2}   ${EMPTY}  ${assetType}
     Go To Asset View Page  ${SERIAL_NUMBER1}
     The Current Page Should Be    AssetSummaryPage
     Link New Asset To An Asset  ${SERIAL_NUMBER2}
     Click Dashboard
     Wait Until Page Contains  Let's setup your dashboard
     Go To Asset View Page  ${SERIAL_NUMBER1}
     The Current Page Should Be    AssetSummaryPage
     Page Should Contain  ${SERIAL_NUMBER2}
     [Teardown]  Delete Asset Type  ${assetType}  
     
Verify Description Template
    [Tags]  C1946
    ${assetType}    Generate Random String  5
    Create An Asset Type  ${assetType}
    The Current Page Should Be  ManageAssetTypesPage
    Click Asset Type Link    ${assetType}
    The Current Page Should Be    CreateAssetTypePage
    Click Add Attribute Button
    Input Attribute Name    Text Field    1
    Select Attribute Datatype Dropdown    Text Field  1
    Input Description Template    Testing the description template with {Identifier}, {RFID} and {Text Field} field
    Click Save Button
    ${SERIAL_NUMBER}    Generate Random String  5
    ${RFID_NUMBER}    Generate Random String  5
    Go To Page   CreateAssetPage
    The Current Page Should Be      CreateAssetPage
    Select Asset Type  ${assetType}
    Input Asset Serial Number       ${SERIAL_NUMBER}
    Input Rfid Number       ${RFID_NUMBER}
    Input Text Field Attribute    Custom Attribute    1
    Click Save Button
    Search For Asset Using Identifiers  ${SERIAL_NUMBER}
    Select Description Column
    Page Should Contain  Testing the description template with ${SERIAL_NUMBER}, ${RFID_NUMBER} and Custom Attribute field
    [Teardown]  Delete Asset Type  ${assetType}