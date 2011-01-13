class ChangeAssetStatus < ActiveRecord::Migration

  def self.up
    execute("alter table assets drop foreign key assets_ibfk_1")
    execute("alter table events drop foreign key events_ibfk_3")    
    execute("alter table addassethistory drop foreign key fk_addproducthistory_productstatus")
    
  	execute("ALTER TABLE assetstatus CHANGE uniqueid id bigint(21) NOT NULL AUTO_INCREMENT")
  	
  	add_foreign_key(:assets, :assetstatus,  	     :source_column => :assetstatus_id,    :foreign_column => :id,     :name => "assets_ibfk_1")
  	add_foreign_key(:events, :assetstatus,      	 :source_column => :assetstatus_id,    :foreign_column => :id,     :name => "events_ibfk_3")
  	add_foreign_key(:addassethistory, :assetstatus,  :source_column => :r_productstatus,   :foreign_column => :id,     :name => "fk_addproducthistory_productstatus")
  	
  	rename_column(:assetstatus, :datecreated, :created)
  	rename_column(:assetstatus, :datemodified, :modified)
  	change_column(:assetstatus, :modifiedby, :integer, :null => true)
  	execute("update assetstatus set modifiedby = null")
  	add_column(:assetstatus, :createdby, :integer, :null => true)
  	execute("alter table assetstatus add foreign key fk_created_by_user (createdby) references users(id) on update no action on delete no action")
  end
  
  def self.down
    execute("alter table assets drop foreign key assets_ibfk_1")
    execute("alter table events drop foreign key events_ibfk_3")    
    execute("alter table addassethistory drop foreign key fk_addproducthistory_productstatus")
  	
  	execute("ALTER TABLE assetstatus CHANGE id uniqueid bigint(21) NOT NULL AUTO_INCREMENT") 

  	add_foreign_key(:assets, :assetstatus,  	     :source_column => :assetstatus_id,    :foreign_column => :uniqueid,     :name => "assets_ibfk_1")
  	add_foreign_key(:events, :assetstatus,      	 :source_column => :assetstatus_id,    :foreign_column => :uniqueid,     :name => "events_ibfk_3")
  	add_foreign_key(:addassethistory, :assetstatus,  :source_column => :r_productstatus,   :foreign_column => :uniqueid,     :name => "fk_addproducthistory_productstatus")

  	rename_column(:assetstatus, :created, :datecreated)
  	rename_column(:assetstatus, :modified, :datemodified)
  	change_column(:assetstatus, :modifiedby, :string, :null => true)
  	execute("update assetstatus set modifiedby = null")
  	execute("alter table assetstatus drop foreign key assetstatus_ibfk_1")
  	remove_column(:assetstatus, :createdby)
  end
end