package rfid.ejb.session;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import rfid.ejb.entity.CommentTempBean;

@Local
public interface CommentTemp {
	public List<CommentTempBean> findCommentTemplateByDate(Long tenantId, Date beginDate, Date endDate);
	
	
}
