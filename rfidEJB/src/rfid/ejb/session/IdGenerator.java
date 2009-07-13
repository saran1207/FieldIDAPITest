package rfid.ejb.session;

import javax.ejb.Local;

@Local 
public interface IdGenerator {
	public String getMaxNo();
}
