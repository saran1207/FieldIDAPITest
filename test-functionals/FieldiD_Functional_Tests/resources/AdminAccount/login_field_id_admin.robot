*** Settings ***

Resource        ${CURDIR}/../../resources/Common/common.robot
Library         AdminAccount.login_field_id_admin_page.LoginFieldidAdminPage  WITH NAME  LoginFieldidAdminPage
Library         AdminAccount.tenants_page.TenantPage       WITH NAME       TenantsPage
Library         AdminAccount.tenant_edit_page.TenantEditPage      WITH NAME       TenantEditPage
Library         AdminAccount.tenent_users_page.TenantUserPage      WITH NAME       TenantUserPage

*** Variables ***
${LOGOUT_BUTTON}        xpath=//a[@href='/fieldid/logout.action']

*** Keywords ***
Login To Field Id Admin Page
    [Arguments]     ${USERNAME}     ${PASSWORD}
    LoginFieldidAdminPage.Input Username      ${USERNAME}
    LoginFieldidAdminPage.Input Password      ${PASSWORD}
    LoginFieldidAdminPage.Submit Credentials
    
    
Sudo User 
    [Arguments]   ${PASSWORD}
    TenantsPage.Click N4tenant
    TenantEditPage.Click View All Users
    #TenantUserPage.Click Sudo Any User
    TenantUserPage.Click Login
    select window  title=Field ID: Login
    LoginFieldidPage.Input Password  ${PASSWORD}
    LoginFieldidPage.Submit Credentials
    
    
