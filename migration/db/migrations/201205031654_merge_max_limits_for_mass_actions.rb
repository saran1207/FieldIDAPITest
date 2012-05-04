class MergeMaxLimitsForMassActions < ActiveRecord::Migration
  def self.up
    execute("delete from configurations where identifier='MAX_SIZE_FOR_MASS_UPDATE' and tenantid is null");
    execute("delete from configurations where identifier='MAX_SIZE_FOR_PDF_PRINT_OUTS'");
    execute("update configurations set identifier='MASS_ACTIONS_LIMIT' where identifier='MAX_SIZE_FOR_MASS_UPDATE'");
  end

  def self.down
    execute("update configurations set identifier='MAX_SIZE_FOR_MASS_UPDATE' where identifier='MASS_ACTIONS_LIMIT'");
  end
end
