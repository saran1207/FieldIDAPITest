*** Settings ***
Documentation     This test suite verifies the functionailty of the Asset Type

Resource        ${CURDIR}/../../../resources/Login/Login.robot
Resource        ${CURDIR}/../../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../../resources/New_Assets/new_assets.robot
Library     Selenium2Library
Library           String
Library           DateTime
Library         Setup.Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAddWithOrderPage
Library         Setup.Assets.create_asset_type_page.CreateAssetTypePage      WITH NAME       CreateAssetTypePage
Library         New_Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         New_Assets.asset_summary_page.AssetSummaryPage        WITH NAME       AssetSummaryPage 
Library         Setup.Assets.event_type_assocation_page.EventTypeAssocationPage    WITH NAME       EventTypeAssocationPage
Library         Setup.Assets.manage_asset_types_page.ManageAssetTypesPage      WITH NAME       ManageAssetTypesPage
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto       
${PASSWORD}         temp123 
${USERFULLNAME}     Test Automation

*** Keywords ***
Create An Asset Type With All Attributes
    [Arguments]     ${ASSET_TYPE_NAME}
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Click Add Button
    The Current Page Should Be      CreateAssetTypePage
    Input Asset Type Name       ${ASSET_TYPE_NAME}
    Click Add Attribute Button
    Input Attribute Name    Text Field    1
    Select Attribute Datatype Dropdown    Text Field  1
    Click Add Attribute Button
    Input Attribute Name    Select Box   2
    Select Attribute Datatype Dropdown    Select Box   2
    Input Select Combo Box Values    select Option1    2
    Click Add Attribute Button
    Input Attribute Name    Combo Box   3
    Select Attribute Datatype Dropdown    Combo Box   3
    Input Select Combo Box Values    combo Option1    3
    Click Add Attribute Button
    Input Attribute Name    Unit Of Measure   4
    Select Attribute Datatype Dropdown    Unit Of Measure   4
    Select Unit Of Measure Dropdown    Inches    4
    Click Add Attribute Button
    Input Attribute Name    Date Field   5
    Select Attribute Datatype Dropdown    Date Field   5
    Click Save Button
    
Verify Asset Type With All Attributes
    ${result}=  Verify If Input Attribute Is Present   Text Field    1
    Should Be True  ${result}
    ${result}=  Verify If Select Attribute Datatype Dropdown Is Present    Text Field  1
    Should Be True  ${result}
    ${result}=  Verify If Input Attribute Is Present   Select Box   2
    Should Be True  ${result}
    ${result}=  Verify If Select Attribute Datatype Dropdown Is Present    Select Box   2
    Should Be True  ${result}
    ${result}=  Verify If Input Attribute Is Present   Combo Box   3
    Should Be True  ${result}
    ${result}=  Verify If Select Attribute Datatype Dropdown Is Present    Combo Box   3
    Should Be True  ${result}
    ${result}=  Verify If Input Attribute Is Present   Unit Of Measure   4
    Should Be True  ${result}
    ${result}=  Verify If Select Attribute Datatype Dropdown Is Present    Unit Of Measure   4
    Should Be True  ${result}
    ${result}=  Verify If Input Attribute Is Present   Date Field   5
    Should Be True  ${result}
    ${result}=  Verify If Select Attribute Datatype Dropdown Is Present    Date Field   5
    Should Be True  ${result}
    
Verify Copy Asset Type Values
    ${inputAttributeResult}=  Verify If Input Attribute Is Present   Text Field    1
    ${selectAttributeResult}=  Verify If Select Attribute Datatype Dropdown Is Present    Text Field  1
    ${assetGroup}  Get Selected Asset Group
    ${descriptionTemp}  Get Description Template
    Should Be True  ${inputAttributeResult}
    Should Be True  ${selectAttributeResult}
    Should Be Equal  ${assetGroup}  Fire Safety
    Should Be Equal  ${descriptionTemp}  Testing the description template with {Identifier} and {Text Field} field
    
*** Test Cases ***
Create Asset Type And Verify Creation
    [Tags]  Smoke  C1707
    Create An Asset Type  TestAssetType
    Verify Creation Of An Asset Type    TestAssetType
    [Teardown]  Delete Asset Type  TestAssetType
    
Create Asset Type With Attributes And Verify
    [Tags]  C1731
    ${assetType}    Generate Random String  5
    Create An Asset Type With All Attributes    ${assetType}
    Verify Creation Of An Asset Type    ${assetType}
    Click Asset Type Link    ${assetType}
    The Current Page Should Be    CreateAssetTypePage
    Verify Asset Type With All Attributes
    [Teardown]  Delete Asset Type  ${assetType}
    
Verify Event Type Association For An Asset Type
    [Tags]  C1981
    ${assetType}    Generate Random String  5
    Create An Asset Type  ${assetType}
    Go To Events Tab Of An Asset Type  ${assetType}
    The Current Page Should Be    EventTypeAssocationPage
    Click Event Type Checkbox    Blank Event
    Click Save Eventtype Association Button
    Go To Events Tab Of An Asset Type  ${assetType}
    ${isChecked}  Verify If Event Type Checkbox Is Checked    Blank Event
    Should Be True    ${isChecked}    
    [Teardown]  Delete Asset Type  ${assetType}
    
Copy Asset Type And Verify
    [Tags]  C1715
     ${assetType1}    Generate Random String  6
     ${assetType2}    Generate Random String  6
    Create An Asset Type  ${assetType1}
    Go To An Asset Type    ${assetType1}
    The Current Page Should Be    CreateAssetTypePage
    Select Asset Group Dropdown    Fire Safety
    Click Add Attribute Button
    Input Attribute Name    Text Field    1
    Select Attribute Datatype Dropdown    Text Field  1
    Input Description Template    Testing the description template with {Identifier} and {Text Field} field
    Click Save Button
    Verify Creation Of An Asset Type    ${assetType1}
    Copy Asset Type     ${assetType1}
    The Current Page Should Be    CreateAssetTypePage
    Verify Copy Asset Type Values
    Input Asset Type Name       ${assetType2}
    Click Save Button
    Verify Creation Of An Asset Type    ${assetType2}
    [Teardown]  Run Keywords  
                ...    Delete Asset Type  ${assetType1}
                ...    AND  Delete Asset Type  ${assetType2} 
                   
Delete Asset Type And Verify
    [Tags]  C1737
    ${assetType}    Generate Random String  6
    Create An Asset Type   ${assetType}
    Verify Creation Of An Asset Type     ${assetType}
    Delete Asset Type   ${assetType} 
    Go To Page      ManageAssetTypesPage
    The Current Page Should Be      ManageAssetTypesPage
    Page Should Not Contain     ${assetType}  
    
Upload File To Asset Type And Verify
    [Tags]  C1978
     ${assetType}    Generate Random String  6
    Create An Asset Type  ${assetType} 
    Verify Creation Of An Asset Type    ${assetType} 
    Click Asset Type Link    ${assetType}
    The Current Page Should Be    CreateAssetTypePage
    Click More Information Link
    Input File Name  Asset Report.xlsx
    Click Save Button
    The Current Page Should Be      ManageAssetTypesPage
    Click Asset Type Link    ${assetType}
    The Current Page Should Be    CreateAssetTypePage
    Click More Information Link
    Set Selenium Speed    0.5s
    Page Should Contain  Asset Report.xlsx
    Set Selenium Speed    0s
    [Teardown]  Delete Asset Type  ${assetType}   
    
Verify Caution URL
    [Tags]  C1979
     ${assetType}    Generate Random String  6
    Create An Asset Type  ${assetType} 
    Verify Creation Of An Asset Type    ${assetType} 
    Click Asset Type Link    ${assetType}
    The Current Page Should Be    CreateAssetTypePage
    Click More Information Link
    Input Caution Url    Plain Text
    Click Save Button
    Page Should Contain  'Plain Text' is not a valid URL.
    Input Caution Url    https://google.com
    Click Save Button 
    The Current Page Should Be      ManageAssetTypesPage
    [Teardown]  Delete Asset Type  ${assetType}   
    