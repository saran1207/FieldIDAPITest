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
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto       
${PASSWORD}         temp123 
${USERFULLNAME}     Test Automation
${AssetEvent}  Asset Event
${PlaceEvent}  Place Event
${ProcedureAudit}  Procedure Audit
${Action}    Action
${EventGroup}   Visual Inspection


*** Keywords ***
Verify Creation Of An Event Type
    [Arguments]     ${eventTypeName}  ${eventType}  ${eventTypeGroup}
    Search Event Type  ${eventTypeName}
    Page Should Contain     ${eventTypeName}
    Page Should Contain     ${eventType}
    Page Should Contain     ${eventTypeGroup}
    
*** Test Cases ***
Create Asset Event Type And Verify Creation
    [Tags]   C1859
    ${ViewEventTypePage}=    Get Library Instance    ViewEventTypePage
    Set Suite Variable    ${ViewEventTypePage}
     ${eventTypeName}    Generate Random String  5
    Create An Event Type  ${AssetEvent}  ${eventTypeName}  ${EventGroup}
    Verify Creation Of An Event Type    ${eventTypeName}   ${AssetEvent}   ${EventGroup}
    To Go View Event Type  ${eventTypeName}
    The Current Page Should Be    ViewEventTypePage
    Element Should Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}
    
Create Place Event Type And Verify Creation
    [Tags]   C2017
    ${ViewEventTypePage}=    Get Library Instance    ViewEventTypePage
    Set Suite Variable    ${ViewEventTypePage}
     ${eventTypeName}    Generate Random String  5
    Create An Event Type  ${PlaceEvent}  ${eventTypeName}  ${EventGroup}
    Verify Creation Of An Event Type    ${eventTypeName}   ${PlaceEvent}   ${EventGroup}
    To Go View Event Type  ${eventTypeName}
    The Current Page Should Be    ViewEventTypePage
    Element Should Not Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}
     
Create Procedure Audit Event Type And Verify Creation
    [Tags]   C2019
    ${ViewEventTypePage}=    Get Library Instance    ViewEventTypePage
    Set Suite Variable    ${ViewEventTypePage}
     ${eventTypeName}    Generate Random String  5
    Create An Event Type  ${ProcedureAudit}  ${eventTypeName}  ${EventGroup}
    Verify Creation Of An Event Type    ${eventTypeName}   ${ProcedureAudit}   ${EventGroup}
    To Go View Event Type  ${eventTypeName}
    The Current Page Should Be    ViewEventTypePage
    Element Should Not Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}
        
Create Action Event Type And Verify Creation
    [Tags]   C2018
    ${ViewEventTypePage}=    Get Library Instance    ViewEventTypePage
    Set Suite Variable    ${ViewEventTypePage}
     ${eventTypeName}    Generate Random String  5
    Create An Event Type  ${Action}  ${eventTypeName}  ${EventGroup}
    Verify Creation Of An Event Type    ${eventTypeName}   ${Action}   ${EventGroup}
    To Go View Event Type  ${eventTypeName}
    The Current Page Should Be    ViewEventTypePage
    Element Should Not Be Visible    ${ViewEventTypePage._locators['asset_type_associations']}  
    [Teardown]  Delete Event Type  ${eventTypeName}  