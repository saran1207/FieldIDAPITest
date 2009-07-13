
class RemoveSerialNumberConstraint < ActiveRecord::Migration
  def self.up
    execute "drop index ps_serialnumber_unique"
    execute "drop index productserial_idx"
    execute "drop index productserial_rfidnumber_rorder_idx"
    add_index(:products, :serialnumber)
    add_index(:products, :rfidnumber)
    add_index(:products, :mobileguid)
  end
  
  def self.down
     raise ActiveRecord::IrreversibleMigration
  end
end
