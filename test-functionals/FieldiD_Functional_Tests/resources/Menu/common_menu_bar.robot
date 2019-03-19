*** Settings ***

Library  Menu.common_menu_bar.CommonMenuBar  WITH NAME  CommonMenuBar


*** Variables ***


*** Keywords ***
Go To New Asset
    CommonMenuBar.Click New Asset
    
Go To Asset Type Groups
    CommonMenuBar.Click Asset Type Groups
     
Click Sign Out
    CommonMenuBar.Click Sign Out
