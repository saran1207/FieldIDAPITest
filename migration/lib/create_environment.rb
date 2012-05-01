$:.unshift "./model/"
$:.unshift "./lib/"

require 'thread'
require "rubygems"
require "bundler/setup"

require 'yaml'
require 'active_record'

#require 'active_record/connection_adapters/postgresql_adapter'

require 'migration_helper'
class ActiveRecord::Migration 
  extend MigrationHelpers
end


module ActiveRecord
  module ConnectionAdapters
    
    class JdbcAdapter < AbstractAdapter
      alias_method :base_native_database_types, :native_database_types
      def native_database_types()
        types = base_native_database_types()
        types[:primary_key] = "bigint(21) DEFAULT NULL auto_increment PRIMARY KEY"
        types[:integer] = { :name => "bigint", :limit => 21 }
        types
      end
      
        # Maps logical Rails types to MySQL-specific data types.
      def type_to_sql(type, limit = nil, precision = nil, scale = nil)
        return super unless type.to_s == 'integer'
        case limit
          when 1; 'tinyint'
          when 2; 'smallint'
          when 3; 'mediumint'
          when nil, 4..21; 'bigint'  # compatibility with MySQL default
          else raise(ActiveRecordError, "No integer type has byte size #{limit}")
        end
      end
      
    end
    
  end
end

module ::JdbcSpec
  module MySQL
    def create_table(name, options = {}) #:nodoc:
      super(name, {:options => "ENGINE=InnoDB"}.merge(options))
    end
  end
end

require "migration_pieces"

