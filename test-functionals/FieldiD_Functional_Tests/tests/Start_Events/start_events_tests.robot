*** Settings ***
Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/New_Assets/new_assets.robot
Library           String
Library           DateTime
Library         New_Assets.asset_summary_page.AssetSummaryPage    WITH NAME      AssetSummaryPage
Library         New_Assets.perform_event_page.PerformEventPage      WITH NAME       PerformEventPage
Library         New_Assets.thing_event_summary_page.ThingEventSummaryPage      WITH NAME       ThingEventSummaryPage

Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto          
${PASSWORD}         temp123
${EVENTRESULT}      Fail
${COMMENTS}        Test Comments
${OWNER}           Level1
${ASSET_TYPE}      Score Test
${EVENT_TYPE1}     Blank Event
${EVENT_TYPE2}     Score Event
${EVENT_TYPE3}     Observation Test
${SERIAL_NUMBER}   C1906


*** Keywords ***

Go To Asset And Start Event
    [Arguments]  ${SERIAL_NUMBER}  ${EVENT_TYPE}
    Go To Asset View Page      ${SERIAL_NUMBER}
    The Current Page Should Be    AssetSummaryPage
    Start An Event   ${EVENT_TYPE} 
    Wait Until Page Contains  Perform Event
    
*** Test Cases ***
Perform an Unscheduled Event and Verify 
    [Tags]    C1891  Smoke
    Go To Asset View Page      C1895Asset
    The Current Page Should Be    AssetSummaryPage
    Start An Event   ${EVENT_TYPE1}
    The Current Page Should be  PerformEventPage
    Select Event Result  ${EVENTRESULT}
    Input Comments  ${COMMENTS}
    Input Owner Field  ${OWNER}
    Click Save Button
    Wait Until Page Contains      Event Summary 
    The Current Page Should Be    ThingEventSummaryPage
    Page Should Contain  ${EVENTRESULT}
    Page Should Contain  ${COMMENTS}
    Page Should Contain  ${OWNER}
    
Perform an Event with Score settings and verify 
    [Tags]  C1906
    Go To Asset And Start Event  ${SERIAL_NUMBER}  ${EVENT_TYPE2}
    The Current Page Should be  PerformEventPage
    Input Owner Field  ${OWNER}
    #testdata 1
    Select Score  1
    Input Comments  ${COMMENTS}
    Click Save Button
    Wait Until Page Contains      Event Summary 
    The Current Page Should Be  ThingEventSummaryPage
    Page Should Contain  Fail
    Page Should Contain  ${COMMENTS}
    Page Should Contain  ${OWNER}
    Page Should Contain  (33%)
    Click Edit Button
    The Current Page Should be  PerformEventPage
    # testdata 2
    Select Score  2
    Input Comments  ${COMMENTS}
    Click Save Button
    Wait Until Page Contains      Event Summary
    The Current Page Should Be    ThingEventSummaryPage
    Page Should Contain  Pass
    Page Should Contain  (67%)
    Click Edit Button
    The Current Page Should be  PerformEventPage
    # testdata 3
    Select Score  3
    Input Comments  ${COMMENTS}
    Click Save Button
    Wait Until Page Contains      Event Summary
    The Current Page Should Be    ThingEventSummaryPage
     Page Should Contain  N/A
    Page Should Contain  (100%)
    
Perform an Event with Observations settings and verify  
    [Tags]  C1893
    Go To Asset And Start Event  ${SERIAL_NUMBER}  ${EVENT_TYPE3}
    The Current Page Should be  PerformEventPage
    Input Comments  ${COMMENTS}
    Input Owner Field  ${OWNER}
    #testdata 1
    Input Observation Score  Safe  61
    Input Observation Score  Unsafe  10
    Click Save Button 
    Wait Until Page Contains      Event Summary 
    The Current Page Should Be  ThingEventSummaryPage
    Page Should Contain  Pass
    Page Should Contain  ${COMMENTS}
    Page Should Contain  ${OWNER}
    Page Should Contain  (86%)
    Page Should Contain  (14%)
    Click Edit Button
    The Current Page Should be  PerformEventPage
    # testdata 2
    Input Observation Score  Safe  50
    Input Observation Score  Unsafe  10
    Click Save Button
    Wait Until Page Contains      Event Summary
    The Current Page Should Be    ThingEventSummaryPage
    Page Should Contain  N/A
    Page Should Contain  (83%)
    Page Should Contain  (17%)
    Click Edit Button
    The Current Page Should be  PerformEventPage
    # testdata 3
    Input Observation Score  Safe  50
    Input Observation Score  Unsafe  65
    Click Save Button
    Wait Until Page Contains      Event Summary
    The Current Page Should Be    ThingEventSummaryPage
    Page Should Contain  Fail
    Page Should Contain  (43%)
    Page Should Contain  (57%)
    Click Edit Button
    The Current Page Should be  PerformEventPage
    # testdata 4
    Input Observation Score  Safe  65
    Input Observation Score  Unsafe  65
    Click Save Button
    Wait Until Page Contains      Event Summary
    The Current Page Should Be    ThingEventSummaryPage
    Page Should Contain  Fail
    Page Should Contain  (50%)
    Page Should Contain  (50%)