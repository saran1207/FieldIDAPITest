*** Settings ***

Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Menu/common_menu_bar.robot
Library  Login.login_fieldid_page.LoginFieldidPage  WITH NAME  LoginFieldidPage

*** Variables ***
${LOGOUT_BUTTON}        xpath=//a[@href='/fieldid/logout.action']

*** Keywords ***
Login To Field Id Page
    [Arguments]     ${USERNAME}     ${PASSWORD}
    Open Browser To Login Page
    LoginFieldidPage.Input Username      ${USERNAME}
    LoginFieldidPage.Input Password      ${PASSWORD}
    LoginFieldidPage.Submit Credentials
    
Logout Of Field Id
    Click Sign Out
    Wait Until Page Contains        Login
    Close Browser
    
Verify Successfull Login    
    Page Should Contain       Welcome
    
Verify Unsuccessfull Login   
   ${INVALID LOGIN MSG}=   LoginFieldidPage.Return Invalid Login Message
   Page Should Contain      ${INVALID LOGIN MSG}
  