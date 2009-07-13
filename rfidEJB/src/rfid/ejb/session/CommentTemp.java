package rfid.ejb.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import rfid.dto.CommentTempDTO;
import rfid.ejb.entity.CommentTempBean;

import com.n4systems.util.ListingPair;

@Local
public interface CommentTemp {
	public ArrayList<CommentTempDTO> findAllCommentTemps();
	public ArrayList<CommentTempDTO> findCommentTemplateByDate(Long tenantId, Date beginDate, Date endDate);
	
	@Deprecated
	public ArrayList<CommentTempDTO> findCommentTempsByRMan(Long tenantId);
	public ArrayList<CommentTempDTO> findCommentTempsForTenant(Long tenantId);
	
	/**
	 * @deprecated use CommentTemplateListableLoader
	 */
	public List<ListingPair> findCommentTemplatesLP(Long tenantId);
	public List<CommentTempBean> findCommentTemplates(Long tenantId);
	public CommentTempBean findCommentTemplate(Long uniqueID);
	public void updateCommentTemplate(CommentTempBean commentTempBean);
	public Long persistCommentTemplate(CommentTempBean commentTempBean);
	public void removeCommentTemplate(CommentTempBean commentTempBean);
}
