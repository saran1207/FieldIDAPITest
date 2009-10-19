
class FixDivisions < ActiveRecord::Migration
  def self.up
    execute "update divisions set name=CONCAT(name, '_1') WHERE name='ZZZZ28178 HAYES ROSEVILLE, MI 48066' and customer_id = 319828  limit 1"
    execute "update divisions set name=CONCAT(name, '_1') WHERE name='2439 MCGILCHRIST ST SE SALEM_1'  and customer_id =     256124 limit 1"
    execute "update divisions set name=CONCAT(name, '_1') WHERE name='paul margolis SEATTLE'     and customer_id =     253891   limit 1"
    execute "update divisions set name=CONCAT(name, '_1') WHERE name='13340 SE 84TH Clackamas'    and customer_id =  282225   limit 1"
    execute "update divisions set name=CONCAT(name, '_1') WHERE name='dba DIAMOND TRAFFIC PRODUCTS OAKRIDGE'  and customer_id = 318945 limit 1 "
    execute "update divisions set name=CONCAT(name, '_1') WHERE name='3188 mesa avenue grand junction'  and customer_id = 282240 limit 1"


    execute "update divisions set name=CONCAT(name, '_1') WHERE name='2439 MCGILCHRIST ST SE SALEM'  and customer_id = 256124 limit 1"

    add_index "divisions", ["customer_id", "name"], :name => "division_name_enduser", :unique => true

  end
  
  def self.down
  end
end
