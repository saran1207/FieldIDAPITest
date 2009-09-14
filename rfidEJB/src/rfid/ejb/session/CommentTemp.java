package rfid.ejb.session;

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Local;

import rfid.dto.CommentTempDTO;

@Local
public interface CommentTemp {
	public ArrayList<CommentTempDTO> findCommentTemplateByDate(Long tenantId, Date beginDate, Date endDate);
	
	
}
