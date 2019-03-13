*** Settings ***
Resource        ${CURDIR}/../../resources/Common/common.robot
Library         AdminAccount.tenants_page.TenantPage       WITH NAME       TenantsPage
Library         AdminAccount.tenant_edit_page.TenantEditPage      WITH NAME       TenantEditPage
Library         AdminAccount.tenent_users_page.TenantUserPage      WITH NAME       TenantUserPage
Library         Dashboard.dashboard_page.DashboardPage        WITH NAME    DashboardPage
Library         Users.users_list_page.ManageUsersPage      WITH NAME       ManageUsersPage 
Library         Users.select_user_type.SelectUserTypePage     WITH NAME       SelectUserTypePage
Library         Users.add_user_page.AddUserPage      WITH NAME       AddUserPage


*** Variables ***

*** Keywords ***
Create A User
   [Arguments]    ${USERNAME}  ${PASSWORD}  ${OWNER}  ${EMAIL}  ${FIRSTNAME}  ${LASTNAME}
   
   Go To Page    ManageUsersPage   
   ManageUsersPage.Click Add Button
   SelectUserTypePage.Click Add Administration Userbutton
   AddUserPage.Input User Name Field    ${USERNAME}
   AddUserPage.Input Password Field   ${PASSWORD}
   AddUserPage.Input Verify Password Field     ${PASSWORD}
   AddUserPage.Input Owner Field    ${OWNER}
   AddUserPage.Input Email Field    ${EMAIL}
   AddUserPage.Input First Name Field   ${FIRSTNAME}
   AddUserPage.Input Last Name Field    ${LASTNAME}
   AddUserPage.Click All On Button
   AddUserPage.Click Save Button
