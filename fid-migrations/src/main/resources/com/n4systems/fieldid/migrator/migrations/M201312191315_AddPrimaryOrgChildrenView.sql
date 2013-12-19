create view primary_org_children as
  (select 'SECONDARY' as type,org.tenant_id, org_id, org_id as id,primaryorg_id as parent_id from org_secondary sec join org_base org on org.id = sec.org_id)
  union all
  (select 'CUSTOMER' as type, org.tenant_id, org_id, org_id as id,parent_id as parent_id from org_customer cust join org_base org on org.id = cust.org_id);

