alter table org_base modify contactemail varchar(255);
alter table org_base modify contactname varchar(255);
update org_base ob join org_customer oc on ob.id = oc.org_id set ob.contactemail = oc.contactemail, ob.contactname = oc.contactname;

