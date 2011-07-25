class DeleteOrdersFromNonIntergrationTenants < ActiveRecord::Migration

  def self.up
  
  	execute = "delete from orders where tenant_id in (select id from tenants where id not in(select distinct tenant_id from org_base where id in (select org_id from org_extendedfeatures where feature = 'Integration')));"
  	
  end
  
  
  def self.down
  end

end
