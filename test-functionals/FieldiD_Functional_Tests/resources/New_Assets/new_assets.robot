*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library         New_Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
Library         Setup.Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAddWithOrderPage
Library         Setup.Assets.view_asset_types_group_page.ViewAssetTypeGroupPage      WITH NAME       ViewAssetTypeGroupPage
Library         New_Assets.quick_event_page.QuickEventPage      WITH NAME       QuickEventPage
Library         New_Assets.asset_summary_page.AssetSummaryPage      WITH NAME       AssetSummaryPage
Library         New_Assets.asset_events_page.AssetEventsPage      WITH NAME       AssetEventsPage 
Library         New_Assets.perform_event_page.PerformEventPage      WITH NAME       PerformEventPage
Library         New_Assets.thing_event_summary_page.ThingEventSummaryPage      WITH NAME       ThingEventSummaryPage
Library         String



*** Variables ***

*** Keywords ***
Create An Asset
    [Arguments]     ${SERIAL_NUMBER}    ${RFID_NUMBER}=${EMPTY}  ${ASSET_TYPE}=${EMPTY}
    NewAddWithOrderPage.Click Add Tab
    The Current Page Should Be      CreateAssetPage
    Select Asset Type  ${ASSET_TYPE}
    Input Asset Serial Number       ${SERIAL_NUMBER}
    Input Rfid Number       ${RFID_NUMBER}
    Click Save Button
    
Start An Event
    [Arguments]  ${EVENT_NAME}
    Click Start Event Link
    The Current Page Should be  QuickEventPage
    Click Unscheduled Event Link  ${EVENT_NAME}
    
Perform An Event And Save
    [Arguments]   ${RESULT}   ${COMMENTS}   ${OWNER}
    The Current Page Should be  PerformEventPage
    Select Event Result  ${RESULT}
    Input Comments  ${COMMENTS}
    Input Owner Field  ${OWNER}
    Click Save Button
    
View Latest Completed Event
    Click Events Button
    Wait Until Page Contains      Show 
    The Current Page Should Be  AssetEventsPage
    Click View Button
    The Current Page Should Be  ThingEventSummaryPage
    
    

    