require 'organization'
require 'user'

class Configuration < ActiveRecord::Base
  set_table_name :configurations
  
  belongs_to  :tenant,      :foreign_key => 'tenantId',   :class_name => 'Organization'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby', :class_name => 'User'
  
  
  def self.fixConfigEntryName(name)
    case name
      when "app.root":                    return "GLOBAL_APPLICATION_ROOT"
      when "maxReportsPerPackage":        return "REPORTING_MAX_REPORTS_PER_FILE"
      when "mail.auth.user":              return "MAIL_AUTH_USER"
      when "mail.auth.pass":              return "MAIL_AUTH_PASS"
      when "smtpFromAddressDefault":      return "MAIL_FROM_ADDR"
      when "smtpReplyTo":                 return "MAIL_REPLY_TO"
      when "smtpHostDefault":             return "MAIL_HOST"
      when "mailSubjectPrefix":           return "MAIL_SUBJECT_PREFIX"
      when "mailBodyPlainHeader":         return "MAIL_BODY_PLAIN_HEADER"
      when "mailBodyPlainFooter":         return "MAIL_BODY_PLAIN_FOOTER"
      when "mailBodyHtmlHeader":          return "MAIL_BODY_HTML_HEADER"
      when "mailBodyHtmlFooter":          return "MAIL_BODY_HTML_FOOTER"
      when "mailAttachmentList":          return "MAIL_ATTACHMENT_LIST"
      when "chart.size.x":                return "GRAPHING_CHART_SIZE_X"
      when "chart.size.y":                return "GRAPHING_CHART_SIZE_Y"
      when "chart.config.peakMarkers":    return "GRAPHING_CHART_PEAK_MARKERS"
      when "chart.config.peakArrows":     return "GRAPHING_CHART_PEAK_ARROWS"
      when "chart.config.peakDots":       return "GRAPHING_CHART_PEAK_DOTS"
      when "ps.per.mobile.page":          return "MOBILE_PAGESIZE_PRODUCTS"
      when "i.per.mobile.page":           return "MOBILE_PAGESIZE_INSPECTIONS"
      when "setup.data.per.mobile.page":  return "MOBLIE_PAGESIZE_SETUPDATA"
      when "total.inspection.buttons":    return "WEB_TOTAL_INSPECTION_BUTTONS"
      when "file.upload.max":             return "WEB_FILE_UPLOAD_MAX"
      else raise "Unknown ConfigEntry [" + name + "]"
    end
  end
  
  def displayString
    tenantString="global"
    if !tenantId.nil?
      tenantString = tenantId.to_s
    end
    
    "#{key} (" + tenantString + ") = #{value}"
  end
end