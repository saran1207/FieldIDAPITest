*** Settings ***

Library  Menu.common_menu_bar.CommonMenuBar  WITH NAME  CommonMenuBar


*** Variables ***


*** Keywords ***
Go To New Asset
    Click New Asset
    
Go To Asset Type Groups
    Click Asset Type Groups
     
Sign Out
    Click Sign Out
    
Go To Asset View Page
    [Arguments]  ${asset_details}
    Search Asset  ${asset_details}
    Wait Until Page Contains      Details    
    
    
