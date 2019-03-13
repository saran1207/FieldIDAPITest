*** Settings ***

Resource        ${CURDIR}/../../resources/Common/common.robot
Resource        ${CURDIR}/../../resources/AdminAccount/login_field_id_admin.robot
Resource        ${CURDIR}/../../resources/Users/create_user.robot
Resource        ${CURDIR}/../../resources/Dashboard/dashboard.robot
Library           String
Suite Teardown  Logout Of Field Id

*** Variables ***
${USERNAME}         dev@fieldid.com       
${PASSWORD}         R2d2>C3p0
${USERNAME1}        testauto
${PASSWORD1}        temp123
${OWNER}            Master Lock Field iD
${EMAIL}            saranya.mudaliar@ecompliance.com
${FIRSTNAME}        Test
${LASTNAME}         Automation

*** Keywords ***

*** Test Cases ***
Create User and verify
    Login to Field Id Admin Console  ${USERNAME}      ${PASSWORD}
    Sudo User  ${PASSWORD}
    Create A User  ${USERNAME1}  ${PASSWORD1}  ${OWNER}  ${EMAIL}  ${FIRSTNAME}  ${LASTNAME}
  