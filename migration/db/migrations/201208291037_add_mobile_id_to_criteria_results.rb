class AddMobileIdToCriteriaResults < ActiveRecord::Migration

  def self.up
    execute("ALTER TABLE criteriaresults ADD COLUMN mobileId VARCHAR(36)")
    execute("UPDATE criteriaresults SET mobileId = uuid()")
    execute("ALTER TABLE criteriaresults MODIFY COLUMN mobileId VARCHAR(36) NOT NULL UNIQUE")
  end

  def self.down
    execute("ALTER TABLE criteriaresults DROP COLUMN mobileId")
  end

end