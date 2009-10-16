class FixUsers < ActiveRecord::Migration
  def self.up
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'Angela'  and r_tenant=   216044 and deleted = TRUE"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'BEAU'      and r_tenant= 10802351 and deleted = TRUE"
    execute "update users set userid=CONCAT(userid,'_1') where userid = 'peter'           and r_tenant= 15511453 and deleted = TRUE"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'Quyen'           and r_tenant=   216044 and deleted = TRUE"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'GOODRICH'        and r_tenant=   216044 and deleted = TRUE"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'Michelle'        and r_tenant= 15511480 and deleted = TRUE"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'TIM.REDLAND'     and r_tenant= 15511490 and deleted = TRUE"


    execute "update users set userid=CONCAT(userid, '_1') where userid = 'henry goodrich'           and r_tenant= 216044  Limit 1"

    execute "update users set userid=CONCAT(userid, '_1') where userid = 'steve'           and r_tenant= 15511453 Limit 1"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'TEMP'            and r_tenant=        4 Limit 1"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'KIM.LONEY'       and r_tenant= 15511490 Limit 1"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'Walter'          and r_tenant= 15511453 Limit 1"
    execute "update users set userid=CONCAT(userid, '_1') where userid = 'Endeavor Mine'   and r_tenant= 15511453 Limit 1"
    add_index "users", ["r_tenant", "userid"], :name => "uniqueuseridrtenant", :unique => true

  end
  
  def self.down
  end
end