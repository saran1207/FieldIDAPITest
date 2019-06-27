*** Settings ***
Resource        ${CURDIR}/../../../resources/Common/common.robot
Resource        ${CURDIR}/../../../resources/Dashboard/dashboard.robot
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
Library         Setup.Events.manage_event_types_page.ManageEventTypesPage  WITH NAME       ManageEventTypesPage
Library         Setup.Events.add_event_type_page.AddEventTypePage      WITH NAME       AddEventTypePage
Library         Setup.Events.view_event_type_page.ViewEventTypePage     WITH NAME       ViewEventTypePage 
Library         Setup.Events.event_type_delete_confirm_page.EventTypeDeleteConfirmPage      WITH NAME       EventTypeDeleteConfirmPage
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
    
To Go View Event Type
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
    To Go View Event Type  ${eventTypeName}
    The Current Page Should Be    ViewEventTypePage
    Click Asset Type Association Tab