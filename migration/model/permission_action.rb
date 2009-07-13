class PermissionAction < ActiveRecord::Base
  set_table_name :permissionaction
  set_primary_key :uniqueid
  
  def bitMask
    case uniqueid
      when 1
        return (1 << 0)
      when 2
        return (1 << 4)
      when 3
        return (1 << 2)
      when 4
        return (1 << 3)
      when 6
        return (1 << 5)
      when 7
        return (1 << 1)
      when 8
        return (1 << 6)
    end
  end
end