*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library         New_Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
Library         Setup.Assets.new_asset_with_order_page.NewAssetWithOrderPage     WITH NAME       NewAddWithOrderPage
Library         Setup.Assets.view_asset_types_group_page.ViewAssetTypeGroupPage      WITH NAME       ViewAssetTypeGroupPage
Library         Setup.Assets.manage_asset_types_page.ManageAssetTypesPage      WITH NAME       ManageAssetTypesPage
Library         Setup.Assets.asset_type_schedules_page.AssetTypeSchedulesPage      WITH NAME       AssetTypeSchedulesPage
Library         New_Assets.quick_event_page.QuickEventPage      WITH NAME       QuickEventPage
Library         New_Assets.asset_summary_page.AssetSummaryPage      WITH NAME       AssetSummaryPage
Library         New_Assets.asset_events_page.AssetEventsPage      WITH NAME       AssetEventsPage 
Library         New_Assets.perform_event_page.PerformEventPage      WITH NAME       PerformEventPage
Library         New_Assets.thing_event_summary_page.ThingEventSummaryPage      WITH NAME       ThingEventSummaryPage
Library         String



*** Variables ***

*** Keywords ***
Search For Asset Using Identifiers
    [Arguments]  ${SERIAL_NUMBER}=${EMPTY}  ${RFID_NUM}=${EMPTY}  ${REF_NUM}=${EMPTY}
    Go To Page      SearchPage
    The Current Page Should Be      SearchPage
    Input Serial Number         ${SERIAL_NUMBER}
    Input Rfid Number    ${RFID_NUM}
    Input Ref Number    ${REF_NUM}
    Click Search Button
    
Select Description Column
    Click Display Column Button
    Select Description Column Checkbox
    Click Search Button
    