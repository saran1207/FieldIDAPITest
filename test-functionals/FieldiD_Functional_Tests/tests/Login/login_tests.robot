*** Settings ***

Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Login/Login.robot
Library           String
Suite Teardown    Close All Browsers

*** Variables ***
${USERNAME}         testauto
${PASSWORD}         temp123

*** Keywords ***

*** Test Cases ***
Login And Logout Of Field Id
    Open Browser To Login Page
    Login To Field Id Page      ${USERNAME}      ${PASSWORD}
    Verify Successfull Login
    Logout Of Field Id
    
Test Invalid Login To Field Id
    ${invalidPassword}    Generate Random String
    Login To Field Id Page      ${USERNAME}       ${invalidPassword}
    Verify Unsuccessfull Login
    