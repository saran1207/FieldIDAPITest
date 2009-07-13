class TrimNames < ActiveRecord::Migration
  def self.up
    execute "update producttypes set name = TRIM(name), modified = now() where name like ' %' OR name like '% '"
    execute "update inspectiontypes set name = TRIM(name), modified = now() where name like ' %' OR name like '% '"
    execute "update customers set name = TRIM(name), modified = now() where name like ' %' OR name like '% '"
    execute "update divisions set name = TRIM(name), modified = now() where name like ' %' OR name like '% '"
    execute "update inspectionbooks set name = TRIM(name), modified = now() where name like ' %' OR name like '% '"
    execute "update users set userid = TRIM(userid) where userid like ' %' OR userid like '% '"
    execute "update users set firstname = TRIM(firstname) where firstname like ' %' OR firstname like '% '"
    execute "update users set lastname = TRIM(lastname) where lastname like ' %' OR lastname like '% '"
  end
end