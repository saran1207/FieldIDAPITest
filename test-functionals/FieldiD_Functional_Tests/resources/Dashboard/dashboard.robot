*** Settings ***

Resource        ${CURDIR}/../../resources/Common/common.robot

Library  Dashboard.dashboard_page.DashboardPage  WITH NAME  DashboardPage
Library  Menu.common_menu_bar.CommonMenuBar  WITH NAME  CommonMenuBar


*** Variables ***
${LOGOUT_BUTTON}        xpath=//a[@href='/fieldid/logout.action']

*** Keywords ***
Go To New Asset From Dashboard
    CommonMenuBar.Click New Asset
    
Go To Asset Type Groups From Dashboard
    CommonMenuBar.Click Asset Type Groups
     

