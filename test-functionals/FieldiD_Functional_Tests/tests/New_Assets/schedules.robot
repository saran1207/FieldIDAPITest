*** Settings ***
Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../resources/New_Assets/new_assets.robot
Library         Setup.Assets.create_asset_type_group_page.CreateAssetTypeGroupPage      WITH NAME       CreateAssetTypeGroupPage
Library         New_Assets.asset_summary_page.AssetSummaryPage      WITH NAME       AssetSummaryPage
Library         New_Assets.thing_event_summary_page.ThingEventSummaryPage      WITH NAME       ThingEventSummaryPage
Library          DateTime
Library           String
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto    
${PASSWORD}         temp123
${ASSETTYPE}        Fire Extinguisher
${ASSET}            Event Trigger Asset

*** Keywords ***

   
*** Test Cases ***
Verify Event Triggers On Asset Creation
    [Tags]  C1993
    ${SERIAL_NUMBER}    Generate Random String  5
    Go To New Asset From Dashboard
    Create An Asset     ${SERIAL_NUMBER}   ${EMPTY}    ${ASSETTYPE}
    Go To Asset View Page      ${SERIAL_NUMBER}
    The Current Page Should Be    AssetSummaryPage
    ${currentDate}  Get Current Date    
    ${newdatetime1} =  Add Time To Date  ${currentDate}  365 days  result_format=%m/%d/%y
    ${newdatetime2} =  Add Time To Date  ${currentDate}  2190 days  result_format=%m/%d/%y
    ${newdatetime3} =  Add Time To Date  ${currentDate}  4380 days  result_format=%m/%d/%y
    ${schedule1}  Get Schedules    1
    ${schedule2}  Get Schedules    2
    ${schedule3}  Get Schedules    3
    Should Contain   ${schedule1}  Fire Extinguisher Annual Inspection
    Should Contain   ${schedule1}  In 365 Days on ${newdatetime1}
    Should Contain   ${schedule2}  Fire Extinguisher 6 Year Inspection
    Should Contain   ${schedule2}  In 2,190 Days on ${newdatetime2}
    Should Contain   ${schedule3}  Hydrostatic Test
    Should Contain   ${schedule3}  In 4,380 Days on ${newdatetime3}
    
Verify Event Triggers After Event Is Completed
    [Tags]  C1995
    Go To Asset View Page      ${ASSET}
    The Current Page Should Be    AssetSummaryPage
    Start Scheduled Event    1
    The Current Page Should be  PerformEventPage
    Select Event Result  Pass
    Input Comments  Test Comments
    ${schedule1}  Get Schedules    1
    ${currentDate}  Get Current Date    
    ${newdatetime1} =  Add Time To Date  ${currentDate}  365 days  result_format=%m/%d/%y
    Should Contain   ${schedule1}  Fire Extinguisher Annual Inspection
    Should Contain   ${schedule1}  ${newdatetime1}
    Click Save Button
    Wait Until Page Contains      Event Summary 
    The Current Page Should Be    ThingEventSummaryPage
    Click Summary Link
    The Current Page Should Be    AssetSummaryPage
    ${schedule1}  Get Schedules    1
    Should Contain   ${schedule1}  Fire Extinguisher Annual Inspection
    Should Contain   ${schedule1}  In 365 Days on ${newdatetime1}