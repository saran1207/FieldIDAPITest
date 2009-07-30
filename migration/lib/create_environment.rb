$:.unshift "./model/"
$:.unshift "./lib/"

gem 'activerecord', '= 2.3.2'
gem 'composite_primary_keys', '= 2.2.2'
gem 'tzinfo'

require 'yaml'
require 'active_record'
require 'migration_helper'
puts RUBY_PLATFORM
if RUBY_PLATFORM =~ /java/
  gem 'activerecord-jdbcmysql-adapter', "= 0.9.0"
  require 'active_record/connection_adapters/jdbcmysql_adapter'
end

require 'active_record/connection_adapters/mysql_adapter'

class ActiveRecord::Migration 
  extend MigrationHelpers
end


module ActiveRecord
  module ConnectionAdapters
    class MysqlAdapter < AbstractAdapter
      def native_database_types()
        types = NATIVE_DATABASE_TYPES
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


require "migration_pieces"

