class FileContentReplacer
  def self.replace_content(file_name, find, replace)
    if File.file? file_name
      file = File.new(file_name)
      lines = file.readlines
      file.close
  
      changes = false
      lines.each do |line|
        changes = true if line.gsub!(/#{find}/, replace)
      end
  
      # Don't write the file if there are no changes
      if changes
        file = File.new(file_name,'w')
        lines.each do |line|
          file.write(line)
        end
        file.close
      end
    end
  end
end