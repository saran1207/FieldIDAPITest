class LengthenCriteriaSectionName < ActiveRecord::Migration

  def self.up  
  	execute "alter table criteriasections modify title varchar(2000)"
  end
  
  def self.down    
	execute "alter table criteriasections modify title varchar(256)"
  end
  
end