
class UpdateCreateAndModifyTimes < ActiveRecord::Migration
  def self.up
    execute "update  commenttemplate set  datecreated = datecreated + interval '5 hours',    datemodified = datemodified + interval '5 hours'"
      
      
      execute "update  organization set      created = created + interval '5 hours',     modified = modified + interval '5 hours'"
      
      
      execute "update  productstatus set       datecreated = datecreated + interval '5 hours',     datemodified = datemodified + interval '5 hours'"
      
      
      execute "update  schedulerjob set     datecreated = datecreated + interval '5 hours',       datemodified = datemodified + interval '5 hours'"
      
      
      execute "update  addressinfo set     created = created + interval '5 hours',    modified = modified + interval '5 hours'"
      
      
      execute "update  autoattributecriteria set    created = created + interval '5 hours',     modified = modified + interval '5 hours'"
      
      execute "update  autoattributedefinition set    created = created + interval '5 hours',    modified = modified + interval '5 hours'"
      
      
      execute "update  catalogs set  created = created + interval '5 hours',   modified = modified + interval '5 hours'"
      
      
      execute "update  configurations set created =   created + interval '5 hours',modified = modified + interval '5 hours'"
      
      execute "update  criteria set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  criteriaresults set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  criteriasections set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  customers set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  divisions set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  fileattachments set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  findproductoption_manufacture set datecreated = datecreated + interval '5 hours', datemodified = datemodified + interval '5 hours'"
      
      execute "update  inspectionbooks set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  inspectiongroups set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  inspections set modified = modified + interval '5 hours'"
      
      
      execute "update  inspectionschedules set  created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  inspectiontypegroups set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  inspectiontypes set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  instructionalvideos set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  jobsites set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  lineitems set  created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  observations set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  orders set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  printouts set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  products set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  producttypegroups set  created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  producttypes set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  producttypeschedules set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  projects set modified = modified + interval '5 hours'"
      
      execute "update  requesttransactions set  created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  savedreports set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  states set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      execute "update  statesets set created = created + interval '5 hours', modified = modified + interval '5 hours'"
      
      
      execute "update  subproducts set  created = created + interval '5 hours',  modified = modified + interval '5 hours'" 
      
      
      execute "update  tagoptions set  created = created + interval '5 hours',    modified = modified + interval '5 hours'"
      
      
      execute "update  unitofmeasures set   created = created + interval '5 hours',   modified = modified + interval '5 hours'"
      
      
      execute "update  userrequest set   created = created + interval '5 hours',   modified = modified + interval '5 hours'"
      
      
      execute "update  users set   datecreated = datecreated + interval '5 hours',   datemodified = datemodified + interval '5 hours'"
      


  end
  
  def self.down
  end
end