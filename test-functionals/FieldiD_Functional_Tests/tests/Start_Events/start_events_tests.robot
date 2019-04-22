*** Settings ***
Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Setup/Assets/assets.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/New_Assets/new_assets.robot
Library           String
Library           DateTime
Library         Assets.edit_asset_type_group_page.EditAssetTypeGroupPage      WITH NAME       EditAssetTypeGroupPage
Library         Assets.delete_asset_types_groups_page.DeleteAssetTypeGroupPage      WITH NAME       DeleteAssetTypeGroupPage
Library         Assets.manage_asset_type_groups_page.ManageAssetTypeGroupsPage    WITH NAME      ManageAssetTypeGroupsPage
Library         New_Assets.asset_summary_page.AssetSummaryPage    WITH NAME      AssetSummaryPage

Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto       
${PASSWORD}         temp123
${EVENTRESULT}      Fail
${COMMENTS}        Test Comments
${OWNER}           Level1

*** Keywords ***

*** Test Cases ***
Perform an Unscheduled Event and Verify 
    [Tags]    C1891
    Go To Asset View Page      C1895Asset
    The Current Page Should Be    AssetSummaryPage
    Start An Event   Blank Event
    Perform An Event And Save  ${EVENTRESULT}  ${COMMENTS}  ${OWNER}
    Go To Asset View Page      C1895Asset
    The Current Page Should Be    AssetSummaryPage
    View Latest Completed Event
    Page Should Contain  ${EVENTRESULT}
    Page Should Contain  ${COMMENTS}
    Page Should Contain  ${OWNER}
    
    
    