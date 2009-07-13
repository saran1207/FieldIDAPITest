package rfid.dto;

import java.util.Collection;
import java.util.Date;

@Deprecated
public class GroupDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	private Collection col;

	public GroupDTO() {}

	@SuppressWarnings("unchecked")
	public GroupDTO(Long uniqueID, Date dateCreated, 
			Date dateModified, String modifiedBy, Collection col) {
		super(uniqueID, dateCreated, dateModified, modifiedBy);
		setCol(col);
	}

	@SuppressWarnings("unchecked")
	public void setCol(Collection col) {
		this.col = col;
	}

	@SuppressWarnings("unchecked")
	public Collection getCol() {
		return this.col;
	}

	public Long getRGroup() {
		return super.getUniqueID();
	}
}
