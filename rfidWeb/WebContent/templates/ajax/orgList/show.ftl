var orgLists = new Object();
orgLists.orgList = ${action.json.toJson(orgs)};
orgLists.orgId = ${(owner.secondaryOrg.id)?default(primaryOrg.id)};
orgLists.customerList = ${action.json.toJson(customers)};
orgLists.customerId =  ${(owner.customerOrg.id)?default("null")};
orgLists.divisionList = ${action.json.toJson(divisions)};
orgLists.divisionId = ${(owner.divisionOrg.id)?default("null")};
updateOrgBrowser(orgLists);