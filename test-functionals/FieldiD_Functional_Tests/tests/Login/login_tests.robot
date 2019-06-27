*** Settings ***
Documentation     This test suite verifies the functionailty of the Login

Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Login/Login.robot
Library           String
Suite Teardown    Close All Browsers

*** Variables ***
${USERNAME}         testauto
${PASSWORD}         temp123

*** Keywords ***
Verify Successfull Login    
    Page Should Contain       Welcome
    
Verify Unsuccessfull Login   
   ${INVALID LOGIN MSG}=   LoginFieldidPage.Return Invalid Login Message
   Page Should Contain      ${INVALID LOGIN MSG}

*** Test Cases ***
Login And Logout Of Field Id
    [Tags]  Smoke  C1739
    Open Browser To Login Page
    Login To Field Id Page      ${USERNAME}      ${PASSWORD}
    Verify Successfull Login
    Logout Of Field Id
    
Test Invalid Login To Field Id
    [Tags]  Regression  C1740
    ${invalidPassword}    Generate Random String
    Login To Field Id Page      ${USERNAME}       ${invalidPassword}
    Verify Unsuccessfull Login