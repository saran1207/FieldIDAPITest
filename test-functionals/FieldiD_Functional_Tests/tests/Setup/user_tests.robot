*** Settings ***

Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/AdminAccount/login_field_id_admin.robot
Resource        ${CURDIR}/../../resources/Users/user.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library           String
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         dev@fieldid.com       
${PASSWORD}         eHplwc!LaHVy
${USERNAME1}        testauto
${PASSWORD1}        temp123
${OWNER}            N4 Systems Inc.
${EMAIL}            saranya.mudaliar@ecompliance.com
${FIRSTNAME}        Test
${LASTNAME}         Automation

*** Keywords ***

*** Test Cases ***
Create User If Does Not Exist and Verify
    [Tags]  Preconditions
    Login to Field Id Admin Console  ${USERNAME}      ${PASSWORD}
    Sudo User  ${PASSWORD}
    ${user_exists}  Run Keyword And Return Status    Verify If User Exist    ${USERNAME1}        
    Run Keyword if   not ${user_exists}  Create A User  ${USERNAME1}  ${PASSWORD1}  ${OWNER}  ${EMAIL}  ${FIRSTNAME}  ${LASTNAME}
    Verify If User Exist    ${USERNAME1}
  