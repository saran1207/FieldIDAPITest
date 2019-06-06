*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library         New_Assets.create_asset_page.CreateAssetPage        WITH NAME       CreateAssetPage
Library         Search.search_page.SearchPage       WITH NAME       SearchPage
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