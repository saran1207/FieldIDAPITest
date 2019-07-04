*** Settings ***
Documentation     This test suite verifies the functionailty of the Asset Type

Resource        ${CURDIR}/../../../resources/Login/Login.robot
Resource        ${CURDIR}/../../../resources/Setup/Events/events.robot
Library     Selenium2Library
Library           String
Library           DateTime
Library         Setup.Events.manage_event_types_page.ManageEventTypesPage  WITH NAME       ManageEventTypesPage
Library         Setup.Events.add_event_type_page.AddEventTypePage      WITH NAME       AddEventTypePage
Library         Setup.Events.view_event_type_page.ViewEventTypePage     WITH NAME       ViewEventTypePage
Library         Setup.Events.asset_type_assocation_page.AssetTypeAssocationPage    WITH NAME       AssetTypeAssocationPage
Library         Setup.Events.observation_groups_page.ObservationGroupsPage     WITH NAME       ObservationGroupsPage
Suite Setup      Perform Suite Setup
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto       
${PASSWORD}         temp123 
${USERFULLNAME}     Test Automation
${ASSETEVENT}  Asset Event
${PLACEEVENT}  Place Event
${PROCEDUREEVENT}  Procedure Audit
${ACTIONEVENT}    Action
${EVENTGROUP}   Visual Inspection
${ASSETTYPE}  Asset Type 1
${COPYEVENTTYPE}  Score Event
${SCORENAME}  Test Score


*** Keywords ***
Perform Suite Setup
   Login To Field Id Page  ${USERNAME}   ${PASSWORD} 
   ${ViewEventTypePage} =    Get Library Instance   ViewEventTypePage
    Set Suite Variable    ${ViewEventTypePage}
    
Verify Creation Of An Event Type
    [Arguments]     ${eventTypeName}  ${eventType}  ${eventTypeGroup}
    Search Event Type  ${eventTypeName}
    Page Should Contain     ${eventTypeName}
    Page Should Contain     ${eventType}
    Page Should Contain     ${eventTypeGroup}
    
Create Event Type and Verify Creation
    [Arguments]     ${eventType}  ${eventTypeGroup}
    ${eventTypeName}    Generate Random String  5
    Create An Event Type   ${eventType}  ${eventTypeName}   ${eventTypeGroup}
    Verify Creation Of An Event Type    ${eventTypeName}    ${eventType}   ${eventTypeGroup}
    Go To View Event Type  ${eventTypeName}
    [Return]   ${eventTypeName}
    
    
*** Test Cases ***
Create Asset Event Type And Verify Creation
    [Tags]   C1859  C2062
    ${eventTypeName}=   Create Event Type and Verify Creation   ${ASSETEVENT}  ${EVENTGROUP}
    The Current Page Should Be    ViewEventTypePage
    Element Should Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}
    
Create Place Event Type And Verify Creation
    [Tags]   C2017  C2062
    ${eventTypeName}=   Create Event Type and Verify Creation   ${PLACEEVENT}  ${EVENTGROUP}
    The Current Page Should Be    ViewEventTypePage
    Element Should Not Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}
     
Create Procedure Audit Event Type And Verify Creation
    [Tags]   C2019  C2062
    ${eventTypeName}=   Create Event Type and Verify Creation   ${PROCEDUREEVENT}  ${EVENTGROUP}
    The Current Page Should Be    ViewEventTypePage
    Element Should Not Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}
        
Create Action Event Type And Verify Creation
    [Tags]   C2018  C2062
   ${eventTypeName}=   Create Event Type and Verify Creation   ${ACTIONEVENT}  ${EVENTGROUP}
    The Current Page Should Be    ViewEventTypePage
    Element Should Not Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}  
    
Copy Event Type And Verify Creation
    [Tags]   C1861
    Copy Event Type  ${COPYEVENTTYPE}
    Verify Creation Of An Event Type    ${COPYEVENTTYPE} - 1   ${ASSETEVENT}   ${EVENTGROUP}
    Go To View Event Type  ${COPYEVENTTYPE} - 1
    The Current Page Should Be    ViewEventTypePage
    Page Should Contain    Section 1
    Page Should Contain    Criteria 1
    Page Should Contain    Score Group    
    Page Should Not Contain    No event form    
    [Teardown]  Delete Event Type   ${COPYEVENTTYPE} - 1

Verify Asset Type Association For An Event Type
    [Tags]  C1980
    ${eventTypeName}    Generate Random String  5
    Create An Event Type  ${ASSETEVENT}  ${eventTypeName}  ${EVENTGROUP}
    Go To Asset Type Assocaition Tab Of An Event Type  ${eventTypeName}
    The Current Page Should Be    AssetTypeAssocationPage
    Click Asset Type Checkbox    ${ASSETTYPE}
    Click Save Assettype Association Button
    Go To Asset Type Assocaition Tab Of An Event Type  ${eventTypeName}
    ${isChecked}  Verify If Asset Type Checkbox Is Checked   ${ASSETTYPE}
    Should Be True    ${isChecked}    
    [Teardown]  Delete Event Type  ${eventTypeName}  
    
Verify Add Observation Group
    [Tags]  C1872
     ${observationGroupName}    Generate Random String  5 
     Add Observation Group    ${observationGroupName}
     Add Score To Observation Group    ${observationGroupName}    ${SCORENAME}
     Click Save Score Button
     Handle Alert    ACCEPT
     Go To Page  ObservationGroupsPage
     ${isObservationGroupPresent}=  Verify If Observation Group Is Added    ${observationGroupName}
     Should Be True  ${isObservationGroupPresent}
     Select Observation Group    ${observationGroupName}
     ${isScorePresent}=  Verify If Score Is Added    ${SCORENAME}
     Should Be True  ${isScorePresent}
     [Teardown]  Delete Observation Group    ${observationGroupName}
     
Verify Edit Observation Group
    [Tags]  C1873
     ${observationGroupName}    Generate Random String  5 
     Add Observation Group    ${observationGroupName}
     Edit Observation Group    ${observationGroupName}   ${observationGroupName} + editted
     Go To Page  ObservationGroupsPage
     ${isObservationGroupPresent}=  Verify If Observation Group Is Added    ${observationGroupName} + editted
     Should Be True  ${isObservationGroupPresent}
     [Teardown]  Delete Observation Group    ${observationGroupName} + editted