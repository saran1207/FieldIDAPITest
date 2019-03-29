*** Settings ***
Resource          ${CURDIR}/../Login/Login.robot
Resource          ${CURDIR}/../../resources/AdminAccount/login_field_id_admin.robot
Library     Selenium2Library
Library     PageObjectLibrary
Library     OperatingSystem
Library     Collections
Library     DatabaseLibrary

*** Variables ***
${SERVER_URL}        n4systems.fidstage.com
${ADMIN_SERVER_URL}  n4systems.fidstage.com
${BROWSER}           chrome
${LOGIN_URL}         http://${SERVER_URL}
${WELCOME_URL}       http://${SERVER_URL}/fieldid/w/dashboard
${SERVER_ADMIN_URL}  http://${ADMIN_SERVER_URL}/fieldid/admin/signIn.action
${DELAY}             0s
${TIMEOUT}           20s


*** Keywords ***
Setup Chrome Browser
   [Arguments]    ${headless}=${EMPTY}
   ${options}=    Evaluate    sys.modules['selenium.webdriver'].ChromeOptions()    sys
   Call Method    ${options}    add_argument    --disable-extensions
   Run keyword if    "${headless}" == "True"    Run Keywords
   ...    Call Method    ${options}    add_argument    --headless
   ...    AND    Call Method    ${options}    add_argument    --window-size\=1920,1080
   ${service_args}    Create List
        ...           --verbose
   Create WebDriver    Chrome    chrome_options=${options}  service_args=${service_args}
   Maximize Browser Window
   Set Selenium Speed    ${DELAY}
   Go To    ${LOGIN_URL}
   
 Open Browser To Login Page
   Run Keyword And Return if    '${BROWSER}' == 'chrome' or '${BROWSER}' == 'Chrome'     Setup Chrome Browser
   Run Keyword And Return if    '${BROWSER}' == 'chromeheadless' or '${BROWSER}' == 'Chromeheadless'     Setup Chrome Browser    True
   Run Keyword if     '${BROWSER}' == 'ie'    Setup IE Browser
   Open Browser    ${LOGIN_URL}    ${BROWSER}
   Maximize Browser Window
   Set Selenium Speed    ${DELAY}
   Set Selenium Timeout    ${TIMEOUT}
   Login Page Should Be Open  Field ID: Login
   
 Login Page Should Be Open
    [Arguments]    ${PAGE_TITLE}
    Log title
    Title Should Be     ${PAGE_TITLE}
    
 Login to Field Id System
    [Arguments]    ${USER_NAME}    ${PASSWORD}
    Set Server Variables    ${SERVER_URL}
    Open Browser To Login Page
    Login To Field Id Page    ${USER_NAME}    ${PASSWORD}
    
 Login to Field Id Admin Console
    [Arguments]    ${USER_NAME}    ${PASSWORD}
    Set Server Variables    ${SERVER_ADMIN_URL}
    Open Browser To Adminstrator Login Page
    Login To Field Id Admin Page   ${USER_NAME}    ${PASSWORD}
    Login To Field Id Admin Page   ${USER_NAME}    ${PASSWORD}
    
Open Browser To Adminstrator Login Page
   Run Keyword And Return if    '${BROWSER}' == 'chrome' or '${BROWSER}' == 'Chrome'     Setup Chrome Browser
   Run Keyword And Return if    '${BROWSER}' == 'chromeheadless' or '${BROWSER}' == 'Chromeheadless'     Setup Chrome Browser    True
   Run Keyword if     '${BROWSER}' == 'ie'    Setup IE Browser
   Open Browser   ${SERVER_ADMIN_URL}    ${BROWSER}
   Maximize Browser Window
   Set Selenium Speed    ${DELAY}
   Set Selenium Timeout    ${TIMEOUT}
   Login Page Should Be Open  Field ID Asdfgdministration
    
 Set Server Variables
    [Arguments]    ${SERVER_URL}

    Set Global Variable    ${LOGIN_URL}    ${SERVER_URL}
    Set Global Variable    ${WELCOME_URL}    ${SERVER_URL}/Dashboard
    Set Global Variable    ${ERROR_URL}    ${SERVER_URL}/error.html
    
Logout of Field Id System
    Logout Of Field Id
    
 Setup IE Browser
    ${ie default dc}=    Evaluate    sys.modules['selenium.webdriver'].DesiredCapabilities.INTERNETEXPLORER    sys, selenium.webdriver
    Set To Dictionary    ${ie default dc}    enablePersistentHover    ${False}    nativeEvents    ${False}
    
Setup Chrome Browser And Login to Field ID
    [Arguments]    ${USER_NAME}    ${PASSWORD}    ${headless}=${EMPTY}
    Setup Chrome Browser    ${headless}
    Maximize Browser Window
    Set Selenium Speed    ${DELAY}
    Login To Field Id Page    ${USER_NAME}    ${PASSWORD}

#Open Browser To Login Page
 #   Setup Chrome Browser
    #Open Browser        ${LOGIN_URL}        ${BROWSER}
 #   Wait Until Page Contains        Login