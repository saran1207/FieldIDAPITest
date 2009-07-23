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
  gem 'activerecord-jdbcpostgresql-adapter', '= 0.9'
  require 'active_record/connection_adapters/jdbcpostgresql_adapter'
end

require 'active_record/connection_adapters/postgresql_adapter'

class ActiveRecord::Migration 
  extend MigrationHelpers
end

# monkey patch the postgres defaut types.
module ActiveRecord
  module ConnectionAdapters
    class PostgreSQLAdapter < AbstractAdapter
      def native_database_types()
        types = NATIVE_DATABASE_TYPES
        types[:primary_key] = "bigserial primary key"
        types[:string] = { :name => "character varying", :limit => 500 }
        types[:integer] = { :name => "integer", :limit => 8  }
        types
      end
      
      # Maps logical Rails types to PostgreSQL-specific data types.
      def type_to_sql(type, limit = nil, precision = nil, scale = nil)
        return super unless type.to_s == 'integer'
        

        case limit
          when 1..2;      'smallint'
          when 3..4;      'integer'
          when 5..8, nil;      'bigint'
          else raise(ActiveRecordError, "No integer type has byte size #{limit}. Use a numeric with precision 0 instead.")
        end
      end

    end     
    
  end
end


