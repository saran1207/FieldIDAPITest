package rfid.ejb.session;

import javax.ejb.Local;

import com.n4systems.model.UnitOfMeasure;

@Local
public interface UnitOfMeasureManager {

	
	public UnitOfMeasure getUnitOfMeasureForInfoField(Long infoFieldId);
	
	
	
}
