*** Settings ***
Documentation     This test suite verifies the functionailty of the Schedules

Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../resources/New_Assets/new_assets.robot
Library         Setup.Assets.create_asset_type_group_page.CreateAssetTypeGroupPage      WITH NAME       CreateAssetTypeGroupPage
Library         New_Assets.asset_summary_page.AssetSummaryPage      WITH NAME       AssetSummaryPage
Library         New_Assets.thing_event_summary_page.ThingEventSummaryPage      WITH NAME       ThingEventSummaryPage
Library         New_Assets.asset_events_page.AssetEventsPage      WITH NAME       AssetEventsPage
Library         Menu.common_menu_bar.CommonMenuBar  WITH NAME  CommonMenuBar
Library          DateTime
Library           String
Library     Selenium2Library
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto    
${PASSWORD}         temp123
${ASSETTYPE1}       Fire Extinguisher
${ASSET}            Event Trigger Asset
${ASSETTYPE2}       Weekly
${ASSETTYPE3}       Daily
${ASSETTYPE4}       Score Test
${ASSETTYPE5}       Asset Type 1
${ASSETTYPE6}       Asset Type 2
${OWNER1}           Test Automation
${OWNER2}           Level1

*** Keywords ***
Add Days To A Date
    [Arguments]  ${numOfDays}
    ${currentDate}  Get Current Date   
    ${newdatetime} =  Add Time To Date  ${currentDate}  ${numOfDays} days  
    [Return]  ${newdatetime}
    
Create Asset And Go To Asset Summary Page
    [Arguments]  ${ASSETTYPE}  ${OWNER}=${EMPTY}
    ${SERIAL_NUMBER}    Generate Random String  6
    Go To New Asset From Dashboard
    Create An Asset     ${SERIAL_NUMBER}   ${EMPTY}    ${ASSETTYPE}  ${OWNER}
    Go To Asset View Page      ${SERIAL_NUMBER}
    
Go To Asset Type And Schedule Recurring Event
    Go To Schedules Tab Of An Asset Type  ${ASSETTYPE6}
    Schedule Recurring Event  Blank Event
    
Remove All Schedules
    [Arguments]  ${ASSETTYPE}
    Go To Schedules Tab Of An Asset Type  ${ASSETTYPE}
    Click Remove Schedule Link Till Present
    
       
*** Test Cases ***
Verify Event Triggers On Asset Creation
    [Tags]  C2025
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE1}
    The Current Page Should Be    AssetSummaryPage
    ${currentDate}  Get Current Date    
    ${newdatetime1} =  Add Time To Date  ${currentDate}  365 days  result_format=%m/%d/%y
    ${newdatetime2} =  Add Time To Date  ${currentDate}  2190 days  result_format=%m/%d/%y
    ${newdatetime3} =  Add Time To Date  ${currentDate}  4380 days  result_format=%m/%d/%y
    ${schedule1}  Get Schedule    1
    ${schedule2}  Get Schedule    2
    ${schedule3}  Get Schedule    3
    Should Contain   ${schedule1}  Fire Extinguisher Annual Inspection
    Should Contain   ${schedule1}  In 365 Days on ${newdatetime1}
    Should Contain   ${schedule2}  Fire Extinguisher 6 Year Inspection
    Should Contain   ${schedule2}  In 2,190 Days on ${newdatetime2}
    Should Contain   ${schedule3}  Hydrostatic Test
    Should Contain   ${schedule3}  In 4,380 Days on ${newdatetime3}
    
Verify Event Triggers After Event Is Completed
    [Tags]  C1995
    Click Dashboard
    Wait Until Page Contains  Let's setup your dashboard
    Go To Asset View Page      ${ASSET}
    The Current Page Should Be    AssetSummaryPage
    Start Scheduled Event    1
    Wait Until Page Contains  Perform Event: Fire Extinguisher Annual Inspection
    The Current Page Should be  PerformEventPage
    Select Event Result  Pass
    Input Comments  Test Comments
    ${schedule1}  Get Schedule From Perform Event Page    1
    ${currentDate}  Get Current Date    
    ${newdatetime1} =  Add Time To Date  ${currentDate}  365 days  result_format=%m/%d/%y
    Should Contain   ${schedule1}  Fire Extinguisher Annual Inspection
    Should Contain   ${schedule1}  ${newdatetime1}
    Click Save Button
    Wait Until Page Contains      Event Summary 
    The Current Page Should Be    ThingEventSummaryPage
    Click Summary Link
    The Current Page Should Be    AssetSummaryPage
    ${schedule1}  Get Schedule    1
    Should Contain   ${schedule1}  Fire Extinguisher Annual Inspection
    Should Contain   ${schedule1}  In 365 Days on ${newdatetime1}
    
Verify Weekly Recurring Schedule On Asset Creation
    [Tags]  C1994
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE2}
    The Current Page Should Be    AssetSummaryPage
    Click Events Button
    Wait Until Page Contains      Show
    The Current Page Should Be    AssetEventsPage
    ${currentDate}  Get Current Date
    ${Day}=    Convert Date    ${currentDate}    result_format=%a
    ${newSchedule}=  Run Keyword IF  '${Day}' == 'Mon'   Add Days To A Date  1
    ...               ELSE IF  '${Day}' == 'Tue'   Add Days To A Date  0
    ...                  ELSE IF  '${Day}' == 'Wed'   Add Days To A Date  6
    ...                     ELSE IF  '${Day}' == 'Thu'   Add Days To A Date  5
    ...                        ELSE IF  '${Day}' == 'Fri'   Add Days To A Date  4
    ...                           ELSE IF  '${Day}' == 'Sat'   Add Days To A Date  3
    ${newSchedule1} =  Convert Date  ${newSchedule}  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule1}
    ${newSchedule2} =  Add Time To Date  ${newSchedule}  7 days  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule2}
    ${newSchedule3} =  Add Time To Date  ${newSchedule}  14 days  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule3}
    ${newSchedule4} =  Add Time To Date  ${newSchedule}  21 days  result_format=%m/%d/%y
    Page Should Contain   ${newSchedule4}
    ${newSchedule5} =  Add Time To Date  ${newSchedule}  28 days  result_format=%m/%d/%y
    Page Should Contain   ${newSchedule5}
    
Verify Daily Recurring Schedule On Asset Creation
    [Tags]  C2026
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE3}
    The Current Page Should Be    AssetSummaryPage
    Click Events Button
    Wait Until Page Contains      Show
    The Current Page Should Be    AssetEventsPage
    Click Due Link
    ${currentDate}  Get Current Date
    ${newSchedule1} =  Convert Date  ${currentDate}  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule1}
    ${newSchedule2} =  Add Time To Date  ${currentDate}  1 days  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule2}
    ${newSchedule3} =  Add Time To Date  ${currentDate}  2 days  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule3}
    ${newSchedule4} =  Add Time To Date  ${currentDate}  3 days  result_format=%m/%d/%y
    Page Should Contain   ${newSchedule4}
    ${newSchedule5} =  Add Time To Date  ${currentDate}  4 days  result_format=%m/%d/%y
    Page Should Contain   ${newSchedule5}
    ${newSchedule6} =  Add Time To Date  ${currentDate}  5 days  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule6}
    ${newSchedule7} =  Add Time To Date  ${currentDate}  6 days  result_format=%m/%d/%y
    Page Should Contain  ${newSchedule7}
    ${newSchedule8} =  Add Time To Date  ${currentDate}  7 days  result_format=%m/%d/%y
    Page Should Contain   ${newSchedule8}
    ${newSchedule9} =  Add Time To Date  ${currentDate}  8 days  result_format=%m/%d/%y
    Page Should Contain   ${newSchedule9}
    ${newSchedule10} =  Add Time To Date  ${currentDate}  9 days  result_format=%m/%d/%y
    Page Should Contain   ${newSchedule10} 
    
Verify Event Triggers For Only Selected Owners On Asset Creation
    [Tags]  C2048
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE4}  ${OWNER1}
    The Current Page Should Be    AssetSummaryPage
    Page Should Contain  No upcoming or overdue schedules
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE4}  ${OWNER2}
    The Current Page Should Be    AssetSummaryPage
    ${currentDate}  Get Current Date    
    ${newdatetime1} =  Add Time To Date  ${currentDate}  45 days  result_format=%m/%d/%y
    ${schedule1}  Get Schedule    1
    Should Contain   ${schedule1}  Blank Event
    Should Contain   ${schedule1}  In 45 Days on ${newdatetime1}
    
Verify Recurring Schedule For Only Selected Owners On Asset Creation
    [Tags]  C1991
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE5}  ${OWNER1}
    The Current Page Should Be    AssetSummaryPage
    Page Should Contain  No upcoming or overdue schedules
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE5}  ${OWNER2}
    The Current Page Should Be    AssetSummaryPage
     ${currentDate}  Get Current Date
    ${newSchedule1} =  Convert Date  ${currentDate}  result_format=%m/%d/%y
    ${schedule1}  Get Schedule    1
    Should Contain   ${schedule1}  Blank Event
    Should Contain   ${schedule1}  Today on ${newSchedule1}
    
Remove Recurring Schedules And verify That Schedules Are Deleted
    [Tags]  C2048
    [Setup]  Go To Asset Type And Schedule Recurring Event
    Create Asset And Go To Asset Summary Page  ${ASSETTYPE6}
    ${assetId} =  Get AssetId
    The Current Page Should Be    AssetSummaryPage
    ${schedule1}  Get Schedule    1
    Should Contain   ${schedule1}  Blank Event
    Go To Schedules Tab Of An Asset Type  ${ASSETTYPE6}
    Set Selenium Speed    0.5s
    Remove All Schedules  ${ASSETTYPE6}
    Set Selenium Speed    0s
    Go To Asset View Page      ${assetId}
    The Current Page Should Be    AssetSummaryPage
    Page Should Contain  No upcoming or overdue schedules
    [Teardown]  Remove All Schedules  ${ASSETTYPE6}   