*** Settings ***
Resource        ${CURDIR}/../../../resources/Common/common.robot
Resource        ${CURDIR}/../../../resources/Dashboard/dashboard.robot
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
Library         Setup.Events.manage_event_types_page.ManageEventTypesPage  WITH NAME       ManageEventTypesPage
Library         Setup.Events.add_event_type_page.AddEventTypePage      WITH NAME       AddEventTypePage
Library         Setup.Events.view_event_type_page.ViewEventTypePage     WITH NAME       ViewEventTypePage 
Library         Setup.Events.event_type_delete_confirm_page.EventTypeDeleteConfirmPage      WITH NAME       EventTypeDeleteConfirmPage
Library         Setup.Events.observation_groups_page.ObservationGroupsPage     WITH NAME       ObservationGroupsPage
Library         Setup.Events.observations_page.ObservationsPage   WITH NAME       ObservationsPage
Library         Setup.Events.score_groups_page.ScoreGroupsPage     WITH NAME       ScoreGroupsPage
Library         Setup.Events.scoring_page.ScoringPage   WITH NAME       ScoringPage
Library         String



*** Variables ***

*** Keywords ***
Create An Event Type
    [Arguments]     ${EVENT_TYPE}  ${EVENT_TYPE_NAME}  ${EVENT_TYPE_GROUP}
    Go To Page      ManageEventTypesPage
    The Current Page Should Be      ManageEventTypesPage
    Click Add Event Button
    Click Or Add Event Type Link  ${EVENT_TYPE}
    The Current Page Should Be      AddEventTypePage
    Input Event Type Name    ${EVENT_TYPE_NAME}
    Select Event Type Group    ${EVENT_TYPE_GROUP}
    Click Save Button
    
Search Event Type
    [Arguments]   ${EVENT_TYPE_NAME}
    Go To Page      ManageEventTypesPage
    The Current Page Should Be      ManageEventTypesPage
    Input Filter Name    ${EVENT_TYPE_NAME}
    Click Filter Button
    
Get Event Type Id
    [Arguments]    ${eventTypeName}
    Go To Page    ManageEventTypesPage
    The Current Page Should Be    ManageEventTypesPage
    ${link}    Get Link From Name    ${eventTypeName}
    ${url}    Get Element Attribute    ${link}    attribute=href
    ${eventTypeId}    Fetch From Right    ${url}    =
    [return]    ${eventTypeId}
    
Delete Event Type 
    [Arguments]    ${eventTypeName}
    Go To Page    ManageEventTypesPage
    The Current Page Should Be    ManageEventTypesPage
    ${id}  Get Event Type Id  ${eventTypeName}
    Click Edit Event Type Link    ${id}
    The Current Page Should Be      AddEventTypePage
    Click Delete Link
    The Current Page Should Be   EventTypeDeleteConfirmPage
    Click Delete Button
    
Go To View Event Type
    [Arguments]    ${eventTypeName}
    Go To Page    ManageEventTypesPage
    The Current Page Should Be    ManageEventTypesPage
    Click Or Add Event Type Link    ${eventTypeName}
    
Copy Event Type
    [Arguments]    ${eventTypeName}
    Go To Page    ManageEventTypesPage
    The Current Page Should Be    ManageEventTypesPage
    ${id}  Get Event Type Id  ${eventTypeName}
    Click Copy Event Type Link    ${id}
    
Go To Asset Type Assocaition Tab Of An Event Type
    [Arguments]    ${eventTypeName}
    Go To View Event Type  ${eventTypeName}
    The Current Page Should Be    ViewEventTypePage
    Click Asset Type Association Tab
    
Add Observation Group
    [Arguments]    ${observationGroupName}
    Go To Page  ObservationGroupsPage
    The Current Page Should Be  ObservationGroupsPage
    Input Observation Group Name    ${observationGroupName}
    Click Save Observation Group Button
    
Add Score To Observation Group
    [Arguments]    ${observationGroupName}  ${scoreName}
    Go To Page  ObservationGroupsPage
    The Current Page Should Be  ObservationGroupsPage
    Select Observation Group  ${observationGroupName}
    Input Score Name   ${scoreName}
    Click Add Score Button
    
Delete Observation Group
    [Arguments]    ${observationGroupName}
    Go To Page  ObservationGroupsPage
    The Current Page Should Be  ObservationGroupsPage
    Click Delete Observation Group  ${observationGroupName}
    
Edit Observation Group
    [Arguments]   ${oldObservationGroupName}  ${newObservationGroupName}  
    Go To Page  ObservationGroupsPage
    The Current Page Should Be  ObservationGroupsPage
    Click Edit Observation Group Button  ${oldObservationGroupName}
    Input Edit Observation Group Name    ${newObservationGroupName}
    Click Save Edit Observation Group Button
    
Add Score Group
    [Arguments]    ${scoreGroupName}
    Go To Page  ScoreGroupsPage
    The Current Page Should Be  ScoreGroupsPage
    Input Score Group Name    ${scoreGroupName}
    Click Save Score Group Button
    
Add Score To Score Group
    [Arguments]    ${scoreGroupName}  ${scoreName}  ${scoreValue}
    Go To Page  ScoreGroupsPage
    The Current Page Should Be  ScoreGroupsPage
    Select Score Group  ${scoreGroupName}
    Input Score Name   ${scoreName}
    Input Score Value  ${scoreValue}
    Click Save Score Button
    
Delete Score Group
    [Arguments]    ${scoreGroupName}
    Go To Page  ScoreGroupsPage
    The Current Page Should Be  ScoreGroupsPage
    Click Delete Score Group  ${scoreGroupName}
    
Edit Score Group
    [Arguments]   ${oldScoreGroupName}  ${newScoreGroupName}  
    Go To Page  ScoreGroupsPage
    The Current Page Should Be  ScoreGroupsPage
    Click Edit Score Group Link  ${oldScoreGroupName}
    Input Edit Score Group Name    ${newScoreGroupName}
    Click Save Edit Score Group Link
    
Setup Observations To Event Type
    [Arguments]    ${eventType}   ${observationGroupName}  ${ObservationsCountFail}  ${ObservationsCountPass}  ${failValue1}  ${failValue2}  ${passValue1}  ${passValue2}
    Go To View Event Type  ${eventType}
    The Current Page Should Be   ViewEventTypePage
    Click Observations Link
    The Current Page Should Be  ObservationsPage
    Select Observation Group Dropdown    ${observationGroupName}
    Check Observation Count Result Checkbox
    Check Observation Percentage Checkbox
    Check Observation Section Totals Checkbox
    Select Observation Count Fail Dropdown    ${ObservationsCountFail}
    Select Observation Count Pass Dropdown    ${ObservationsCountPass}
    Input Fail Range Value1 Textbox    ${failValue1}
    Input Fail Range Value2 Textbox    ${failValue2}
    Input Pass Range Value1 Textbox    ${passValue1}
    Input Pass Range Value2 Textbox    ${passValue2}
    Click Save Observations Button  
    
Setup Scoring To Event Type
    [Arguments]    ${eventType}   ${ScoreName}  ${failValue1}  ${failValue2}  ${passValue1}  ${passValue2}
    Go To View Event Type  ${eventType}
    The Current Page Should Be   ViewEventTypePage
    Click Scoring Link
    The Current Page Should Be  ScoringPage
    Select Score Dropdown    ${ScoreName}
    Check Score Total Result Checkbox
    Check Score Percentage Checkbox
    Check Score Section Totals Checkbox
    Input Fail Range Value1 Textbox    ${failValue1}
    Input Fail Range Value2 Textbox    ${failValue2}
    Input Pass Range Value1 Textbox    ${passValue1}
    Input Pass Range Value2 Textbox    ${passValue2}
    Click Save Scoring Button  