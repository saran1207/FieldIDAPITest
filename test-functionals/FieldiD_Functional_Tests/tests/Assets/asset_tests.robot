*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/Login/Login.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Resource        ${CURDIR}/../../resources/Assets/assets.robot
Library         Assets.add_asset_type_group_page.AddAssetTypeGroupPage      WITH NAME       AddAssetTypeGroupPage

Library           String
Suite Setup     Login To Field Id Page      ${USERNAME}      ${PASSWORD}
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         testauto    
${PASSWORD}         temp123

*** Keywords ***
   
*** Test Cases ***
Create Asset Type And Verify Creation
    Create An Asset Type    TestAssetType
    Verify Creation Of An Asset Type    TestAssetType

Create Asset And Verify Creation
    Go To New Asset From Dashboard
    Create An Asset     TestAsset   TEST123
    Verify Creation Of An Asset     TestAsset   TEST123
    
    
    