class UpdateEventTypesDateInSetupDataLastModDates < ActiveRecord::Migration

    def self.up
        execute("update setupdatalastmoddates set eventtypes = now()")
    end

    def self.down
    end

end